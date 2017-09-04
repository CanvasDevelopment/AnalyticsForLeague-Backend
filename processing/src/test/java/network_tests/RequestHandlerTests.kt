package network_tests

import db.RequestDAOContract
import model.match.Match
import network.RequestHandler
import network.riotapi.MatchServiceApi
import org.junit.Assert
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*

/**
 * @author Josiah Kendall
 */
class RequestHandlerTests {

    private val requestDao = mock(RequestDAOContract::class.java)
    private val requestHandler = RequestHandler(requestDao)
    private val matchService = mock(MatchServiceApi::class.java)

    @Test
    fun `ensure we stop at rate limit and wait out the minute`() {
        val timeLeftToWait = 5000
        val startTime = System.currentTimeMillis()
        // We are allowed 12 requests per minute
        `when`(requestDao.getRateLimitPerMinute()).thenReturn(12)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(55)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(12)
        requestHandler.requestData { matchService.getMatchByMatchId("212", -1) }
        val totalTime = System.currentTimeMillis()
        Assert.assertTrue(totalTime > (startTime + timeLeftToWait))
    }

    @Test
    fun `ensure we fetch data if we are below rate limit`() {
        val timeLeftToWait = 1000
        val startTime = System.currentTimeMillis()
        // We are allowed 12 requests per minute
        `when`(requestDao.getRateLimitPerMinute()).thenReturn(12)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(40)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(11)
        requestHandler.requestData { matchService.getMatchByMatchId("212", -1) }
        val totalTime = System.currentTimeMillis()
        Assert.assertTrue(totalTime < (startTime + timeLeftToWait))
    }

    @Test
    fun `ensure that we record quest in dao once we have sent it`() {
        `when`(requestDao.getRateLimitPerMinute()).thenReturn(24)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(3)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(40)
        requestHandler.requestData { matchService.getMatchByMatchId("212", -1) }
        verify(requestDao, times(1)).recordRequest(Matchers.anyLong())
    }

    @Test
    fun `ensure that we record request even if we delay the request`() {
        val timeLeftToWait = 5000
        val startTime = System.currentTimeMillis()
        // We are allowed 12 requests per minute
        `when`(requestDao.getRateLimitPerMinute()).thenReturn(12)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(55)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(12)
        requestHandler.requestData { matchService.getMatchByMatchId("212", -1) }
        val totalTime = System.currentTimeMillis()
        Assert.assertTrue(totalTime > (startTime + timeLeftToWait))
        verify(requestDao, times(1)).recordRequest(Matchers.anyLong())
    }

    @Test
    fun `ensure that we successfully wipe requests table once duration exceeded`() {

        // We are allowed 12 requests per minute
        `when`(requestDao.getRateLimitPerMinute()).thenReturn(12)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(60)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(11)
        requestHandler.requestData { matchService.getMatchByMatchId("212", -1) }
        verify(requestDao, times(1)).clearRateLimitRequests()
    }

    @Test
    fun `ensure that the higher order function we pass returns the data that we expect`() {
        `when`(requestDao.getRateLimitPerMinute()).thenReturn(24)
        `when`(requestDao.requestsSinceLastClearedRates()).thenReturn(3)
        `when`(requestDao.timeSinceLastClearedRates()).thenReturn(40)
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
        val matchRecieved = requestHandler.requestData { matchService.getMatchByMatchId("212", -1) }
        Assert.assertTrue(match == matchRecieved)
    }

}