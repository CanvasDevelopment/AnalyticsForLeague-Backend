package network_tests

import db.requests.EndpointRateLimitStatusDao
import db.requests.RequestDao
import model.Summoner
import model.match.Match
import network.RateLimiter
import network.RequestHandler
import network.riotapi.MatchServiceApi
import network.riotapi.header.RateLimitBucket
import network.riotapi.header.RateLimitsWrapper
import network.riotapi.header.RiotApiResponseHeaderParser
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import util.RIOT_API_KEY
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * @author Josiah Kendall
 */
class RequestHandlerTests {

    private val requestDao = mock(RequestDao::class.java)
    private val endpointRateLimitDao = mock(EndpointRateLimitStatusDao::class.java)
    private val rateLimiter = mock(RateLimiter::class.java)
    private val parser = mock(RiotApiResponseHeaderParser::class.java)
    private val requestHandler = RequestHandler(requestDao, endpointRateLimitDao, rateLimiter, parser)
    private val matchService = mock(MatchServiceApi::class.java)

    @Test
    fun `ensure we stop at rate limit and wait out the minute`() {
        val timeLeftToWait = 5000
        val startTime = System.currentTimeMillis()
        // We are allowed 12 requests per minute
        `when`(rateLimiter.fetchWaitTime(ArgumentMatchers.anyInt())).thenReturn(timeLeftToWait)
        `when`(endpointRateLimitDao.doesEndpointExistInDb(1)).thenReturn(true)
        requestHandler.requestDataWithRateLimiting(
                { matchService.getMatchByMatchId("212", -1) },
                1)
        val totalTime = System.currentTimeMillis()
        Assert.assertTrue(totalTime > (startTime + timeLeftToWait))
    }

    @Test
    fun `Ensure that the wait time is accurate (100ms accuracy)`() {
        // We are allowed 12 requests per minute

        val startTime = System.currentTimeMillis()
        `when`(rateLimiter.fetchWaitTime(ArgumentMatchers.anyInt())).thenReturn(1000)
        `when`(endpointRateLimitDao.doesEndpointExistInDb(1)).thenReturn(true)
        requestHandler.requestDataWithRateLimiting(
                { matchService.getMatchByMatchId("212", -1) },
                1)
        val totalTime = System.currentTimeMillis()
        val upperLimit = 1100
        val lowerLimit = 1000
        Assert.assertTrue(totalTime-startTime > lowerLimit)
        Assert.assertTrue(totalTime-startTime < upperLimit)
    }

    @Test
    fun `Make sure that we can handle the first request in a long time properly`() {
        requestHandler.requestDataWithRateLimiting(
                { matchService.getMatchByMatchId("212", -1) },
                1)
        verify(matchService, times(1)).getMatchByMatchId("212",-1)
    }

    @Test
    fun `ensure we fetch data if we are below rate limit`() {
        val timeLeftToWait = 100
        val startTime = System.currentTimeMillis()
        `when`(rateLimiter.fetchWaitTime(ArgumentMatchers.anyInt())).thenReturn(0)
        `when`(endpointRateLimitDao.doesEndpointExistInDb(1)).thenReturn(true)
        requestHandler.requestDataWithRateLimiting(
                { matchService.getMatchByMatchId("212", -1) },
                1)
        val totalTime = System.currentTimeMillis()
        Assert.assertTrue(totalTime < (startTime + timeLeftToWait))
    }

    @Test
    fun `ensure that the higher order function we pass returns the data that we expect`() {
        `when`(requestDao.getRateLimit()).thenReturn(24)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(3)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(40000)
        val match = Match(-1,
                "fdfd",
                1234,
                34343,
                1,
                1,
                1,
                "fdsfd",
                "rerere",
                "fdfd",
                ArrayList(),
                ArrayList(),
                ArrayList())
        `when`(matchService.getMatchByMatchId(anyString(), anyLong())).thenReturn(match)
        val matchRecieved = requestHandler.requestDataWithRateLimiting(
                { matchService.getMatchByMatchId("212", -1) },
                1)
        Assert.assertTrue(match == matchRecieved)
    }

    @Test
    fun `ensure that we can send a request and handle the response`() {
        `when`(requestDao.getRateLimit()).thenReturn(24)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(3)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(40000)
        val url = URL("https://oc1.api.riotgames.com/lol/summoner/v3/summoners/by-name/kloin?api_key=$RIOT_API_KEY")
        val networkResult = requestHandler.requestDataWithRateLimiting (
                { requestHandler.sendHttpGetRequest(Summoner::class.java,url,1) },
                1)
        Assert.assertEquals(networkResult.code, 200)
        val summoner = networkResult.data
        if (summoner == null) {
            Assert.fail()
        }
        Assert.assertEquals(summoner?.accountId,200774483.toLong())
        Assert.assertEquals(summoner?.name,"kloin")
    }

    @Test
    fun `ensure that we can handle a 404`() {
        `when`(requestDao.getRateLimit()).thenReturn(24)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(3)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(40000)
        val url = URL("https://oc1.api.riotgames.com/lol/summoner/v3/summoners/by-name/kloinfdsfdsfdsfds?api_key=$RIOT_API_KEY")
        val conn = mock(HttpURLConnection::class.java)
        `when`(parser.parseRateLimitsForAppAndEndpoint(conn, 1)).thenReturn(mock(RateLimitsWrapper::class.java))

        `when`(url.openConnection()).thenReturn(conn)
        val networkResult = requestHandler.requestDataWithRateLimiting (
                { requestHandler.sendHttpGetRequest(Summoner::class.java,url,1) },
                1)
        Assert.assertEquals(networkResult.code, 404)
        val summoner = networkResult.data
        Assert.assertEquals(summoner, null)
    }
}