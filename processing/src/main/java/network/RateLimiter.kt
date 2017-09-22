package network

import db.requests.EndpointRateLimitStatusDao
import model.networking.Endpoints
import network.riotapi.header.RateLimitBucket

/**
 * @author Josiah Kendall
 */
class RateLimiter(val endpointRateLimitStatusDao: EndpointRateLimitStatusDao) {

    /**
     * This fetches the wait time for a request.
     * @return returns 0 if we have no time to wait, else it returns the time to wait in milliseconds
     */
    fun fetchWaitTime(endpointId: Int) : Int {
        val endpointRateLimit = endpointRateLimitStatusDao.getEndPointRateLimitStatus(endpointId)
        val appRateLimit = endpointRateLimitStatusDao.getEndPointRateLimitStatus(Endpoints().COMPLETE_APP)
        if (endpointRateLimit.retryAfter != 0 || appRateLimit.retryAfter != 0) {
            return if (endpointRateLimit.retryAfter > appRateLimit.retryAfter) {
                endpointRateLimit.retryAfter * 1000
            } else {
                appRateLimit.retryAfter * 1000
            }
        }

        return produceLongestWait(endpointRateLimit.rateLimitBuckets, appRateLimit.rateLimitBuckets)
    }

    /**
     * Produce the time that we need to wait for the api to be usable again. For the vast majority of the time, this will
     * be 0
     * @param endPointlist  The list of [RateLimitBucket]'s that hold the data for the different rate limits for
     *                      the endpoint we are sending our request to.
     * @param appList       The list of [RateLimitBucket]'s that hold the data for the different rate limits for
     *                      our app.
     * @return The length of time in milliseconds. Can be zero.
     */
    fun produceLongestWait(endPointlist: ArrayList<RateLimitBucket>, appList: ArrayList<RateLimitBucket>): Int {
        var longestWait = 0
        val appRateLimitIterator = appList.iterator()
        val endpointRateLimitIterator = endPointlist.iterator()
        val longestEndpointWaitTime = produceLongestWaitTimeForRateLimitArray(endpointRateLimitIterator)
        val longestAppRateLimitWaitTime = produceLongestWaitTimeForRateLimitArray(appRateLimitIterator)

        if (longestEndpointWaitTime > longestWait) {
            longestWait = longestEndpointWaitTime
        }

        if (longestAppRateLimitWaitTime > longestWait) {
            longestWait = longestAppRateLimitWaitTime
        }

        return longestWait
    }

    /**
     * Produce the longest time that we have in an array of rate limits.
     * @return 0 if there is no wait time, else the longest wait time that we found.
     */
    fun produceLongestWaitTimeForRateLimitArray(rateLimits : Iterator<RateLimitBucket>) : Int {
        var waitTime = 0
        while (rateLimits.hasNext()) {
            val endpointRateLimitBucket = rateLimits.next()
            val wait = produceWaitTimeForIndividualRateLimitBucket(endpointRateLimitBucket)
            if (wait > waitTime) {
                waitTime = wait
            }
        }
        return waitTime
    }

    /**
     * Produce the wait time for a single rate limit.
     * @return 0 if we have not reached rate limit, or the wait time until the end of the limit period in milliseconds
     */
    fun produceWaitTimeForIndividualRateLimitBucket(rateLimitBucket: RateLimitBucket) : Int {
        if (rateLimitBucket.requestCount >= rateLimitBucket.maxRequests) {
            // calculate duration left to end of request period
            val currentTime = System.currentTimeMillis()
            val endOfRateLimitPeriod = rateLimitBucket.firstRequestTime + (rateLimitBucket.rateDuration*1000)
            if (currentTime < endOfRateLimitPeriod) {
                return (endOfRateLimitPeriod - currentTime).toInt()
            }
        }

        return 0
    }
}