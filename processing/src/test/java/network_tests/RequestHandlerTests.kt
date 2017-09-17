package network_tests

import db.requests.RequestDao
import model.Summoner
import model.match.Match
import network.RequestHandler
import network.riotapi.MatchServiceApi
import org.junit.Assert
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*
import util.RIOT_API_KEY
import java.net.URL

/**
 * @author Josiah Kendall
 */
class RequestHandlerTests {

    private val requestDao = mock(RequestDao::class.java)
    private val requestHandler = RequestHandler(requestDao)
    private val matchService = mock(MatchServiceApi::class.java)

    @Test
    fun `ensure we stop at rate limit and wait out the minute`() {
        val timeLeftToWait = 5000
        val startTime = System.currentTimeMillis()
        // We are allowed 12 requests per minute
        `when`(requestDao.getRateLimit()).thenReturn(12)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(55000)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(12)
        requestHandler.requestDataWithRateLimiting { matchService.getMatchByMatchId("212", -1) }
        val totalTime = System.currentTimeMillis()
        Assert.assertTrue(totalTime > (startTime + timeLeftToWait))
    }

    @Test
    fun `Ensure that the wait time is accurate (100ms accuracy)`() {
        // We are allowed 12 requests per minute
        `when`(requestDao.getRateLimit()).thenReturn(12)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(59000)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(12)
        val startTime = System.currentTimeMillis()
        requestHandler.requestDataWithRateLimiting { matchService.getMatchByMatchId("212", -1) }
        val totalTime = System.currentTimeMillis()
        val upperLimit = 1100
        val lowerLimit = 1000
        Assert.assertTrue(totalTime-startTime > lowerLimit)
        Assert.assertTrue(totalTime-startTime < upperLimit)
    }

    @Test
    fun `Make sure that we can handle the first request in a long time properly`() {
        `when`(requestDao.getRateLimit()).thenReturn(12)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(500000)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(13)
        requestHandler.requestDataWithRateLimiting { matchService.getMatchByMatchId("212", -1) }
        verify(matchService, times(1)).getMatchByMatchId("212",-1)
    }

    @Test
    fun `Test running more items that a time frame can handle`() {
        `when`(requestDao.getRateLimit()).thenReturn(10)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(50000)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(13)
    }

    @Test
    fun `Make sure that we clear request list if we have exceeded the waiting period`() {

    }

    @Test
    fun `ensure we fetch data if we are below rate limit`() {
        val timeLeftToWait = 1000
        val startTime = System.currentTimeMillis()
        // We are allowed 12 requests per minute
        `when`(requestDao.getRateLimit()).thenReturn(12)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(40000)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(11)
        requestHandler.requestDataWithRateLimiting { matchService.getMatchByMatchId("212", -1) }
        val totalTime = System.currentTimeMillis()
        Assert.assertTrue(totalTime < (startTime + timeLeftToWait))
    }

    @Test
    fun `ensure that we record quest in dao once we have sent it`() {
        `when`(requestDao.getRateLimit()).thenReturn(24)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(3)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(40000)
        requestHandler.requestDataWithRateLimiting { matchService.getMatchByMatchId("212", -1) }
        verify(requestDao, times(1)).recordRequest(Matchers.anyLong())
    }

    @Test
    fun `ensure that we record request even if we delay the request`() {
        val timeLeftToWait = 5000
        val startTime = System.currentTimeMillis()
        // We are allowed 12 requests per minute
        `when`(requestDao.getRateLimit()).thenReturn(12)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(55000)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(12)
        requestHandler.requestDataWithRateLimiting { matchService.getMatchByMatchId("212", -1) }
        val totalTime = System.currentTimeMillis()
        Assert.assertTrue(totalTime > (startTime + timeLeftToWait))
        verify(requestDao, times(1)).recordRequest(Matchers.anyLong())
    }

    @Test
    fun `ensure that we successfully wipe requests table once duration exceeded`() {

        // We are allowed 12 requests per minute
        `when`(requestDao.getRateLimit()).thenReturn(12)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(60000)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(11)
        requestHandler.requestDataWithRateLimiting { matchService.getMatchByMatchId("212", -1) }
        verify(requestDao, times(1)).clearRateLimitRequests()
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
        `when`(matchService.getMatchByMatchId(Matchers.anyString(), Matchers.anyLong())).thenReturn(match)
        val matchRecieved = requestHandler.requestDataWithRateLimiting { matchService.getMatchByMatchId("212", -1) }
        Assert.assertTrue(match == matchRecieved)
    }

    // note that this test will need a working api key to succeed
    @Test
    fun `ensure that we can send a request and handle the response`() {
        `when`(requestDao.getRateLimit()).thenReturn(24)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(3)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(40000)
        val url = URL("https://oc1.api.riotgames.com/lol/summoner/v3/summoners/by-name/kloin?api_key=$RIOT_API_KEY")
        val networkResult = requestHandler.requestDataWithRateLimiting { requestHandler.sendHttpGetRequest(Summoner::class.java,url) }
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
        val networkResult = requestHandler.requestDataWithRateLimiting { requestHandler.sendHttpGetRequest(Summoner::class.java,url) }
        Assert.assertEquals(networkResult.code, 404)
        val summoner = networkResult.data
        Assert.assertEquals(summoner, null)
    }

}