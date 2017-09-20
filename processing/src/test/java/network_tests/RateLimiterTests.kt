package network_tests

import db.requests.EndpointRateLimitStatusDao
import network.RateLimiter
import network.riotapi.header.RateLimitBucket
import org.junit.Test
import org.mockito.Mockito.mock
/**
 * @author Josiah Kendall
 */
class RateLimiterTests {

    private val rateLimiter = RateLimiter(mock(EndpointRateLimitStatusDao::class.java))

    @Test
    fun `Make sure that we can calculate longest wait time correctly for a single rate limit`() {
        val limit = RateLimitBucket(10, 10,System.currentTimeMillis(),10)
        Thread.sleep(100)
        val timeToWait = rateLimiter.produceWaitTimeForIndividualRateLimitBucket(limit)
        assert(timeToWait in 9801..9999)
    }

    @Test
    fun `Make sure that we can return 0 as longest wait time correctly when we have not exceeded any rates`() {
        val limit = RateLimitBucket(10, 8,System.currentTimeMillis(),10)
        Thread.sleep(100)
        val timeToWait = rateLimiter.produceWaitTimeForIndividualRateLimitBucket(limit)
        assert(timeToWait == 0)
    }

    @Test
    fun `Make sure that we can calculate the longest wait time correctly for a group of rate limits`() {
        val rateLimit = RateLimitBucket(10, 10, System.currentTimeMillis(), 20)
        val rateLimit1 = RateLimitBucket(100, 10, System.currentTimeMillis(), 60)
        val rateLimit2 = RateLimitBucket(1000, 1003, System.currentTimeMillis(), 180)
        val rateList = ArrayList<RateLimitBucket>()
        rateList.add(rateLimit)
        rateList.add(rateLimit1)
        rateList.add(rateLimit2)

        val timeToWait = rateLimiter.produceLongestWaitTimeForRateLimitArray(rateList.iterator())
        assert(timeToWait in 177*1000..180*1000)
    }

    @Test
    fun `Make sure that we can calculate the longest wait time correctly for a group of rate limits v2`() {
        val rateLimit = RateLimitBucket(10, 10, System.currentTimeMillis(), 20)
        val rateLimit1 = RateLimitBucket(100, 10, System.currentTimeMillis(), 60)
        val rateLimit2 = RateLimitBucket(1000, 999, System.currentTimeMillis(), 180)
        val rateList = ArrayList<RateLimitBucket>()
        rateList.add(rateLimit)
        rateList.add(rateLimit1)
        rateList.add(rateLimit2)

        val timeToWait = rateLimiter.produceLongestWaitTimeForRateLimitArray(rateList.iterator())
        assert(timeToWait in 19*1000..20*1000)
    }

    @Test
    fun `Make sure that we can calculate the longest wait time correctly for a group of rate limits when we have not exceeded any`() {
        val rateLimit = RateLimitBucket(10, 8, System.currentTimeMillis(), 20)
        val rateLimit1 = RateLimitBucket(100, 10, System.currentTimeMillis(), 60)
        val rateLimit2 = RateLimitBucket(1000, 999, System.currentTimeMillis(), 180)
        val rateList = ArrayList<RateLimitBucket>()
        rateList.add(rateLimit)
        rateList.add(rateLimit1)
        rateList.add(rateLimit2)

        val timeToWait = rateLimiter.produceLongestWaitTimeForRateLimitArray(rateList.iterator())
        assert(timeToWait == 0)
    }

    @Test
    fun `Make sure that we can calculate wait time correctly using app limits and method endpoint limits`() {
        val appList = ArrayList<RateLimitBucket>()
        val endpointList = ArrayList<RateLimitBucket>()
        val rateLimit = RateLimitBucket(10, 10, System.currentTimeMillis(), 20)
        val rateLimit1 = RateLimitBucket(100, 100, System.currentTimeMillis(), 60)
        val rateLimit2 = RateLimitBucket(1000, 999, System.currentTimeMillis(), 180)
        val rateLimit3 = RateLimitBucket(10, 9, System.currentTimeMillis(), 20)
        val rateLimit4 = RateLimitBucket(100, 102, System.currentTimeMillis(), 80)
        val rateLimit5 = RateLimitBucket(1000, 999, System.currentTimeMillis(), 180)
        appList.add(rateLimit)
        appList.add(rateLimit1)
        appList.add(rateLimit2)
        endpointList.add(rateLimit3)
        endpointList.add(rateLimit4)
        endpointList.add(rateLimit5)
        val timeToWait = rateLimiter.produceLongestWait(endpointList, appList)
        assert(timeToWait in 79*1000..80*1000)
    }
}