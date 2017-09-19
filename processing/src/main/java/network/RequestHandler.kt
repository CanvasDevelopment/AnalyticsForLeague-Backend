package network

import com.google.gson.Gson
import db.requests.EndpointRateLimitStatusDao
import db.requests.RequestDAOContract
import model.networking.Endpoints
import model.networking.NetworkResult
import network.riotapi.header.RateLimitBucket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Josiah Kendall
 *
 * Handles network requests to the riot api.
 */
class RequestHandler(private val requestDAOContract: RequestDAOContract,
                     private val rateLimitDao: EndpointRateLimitStatusDao) {

    private val gson = Gson()

    /**
     * This is the duration on which we limit to a certain number of requests
     */
    private var timeFrameInSeconds = 60 // default to a minute

    /**
     * Requests data from the server. Allows us to pass an Api/network call as a higher order function and return its result.
     * This method handles the rate limiting of our requests
     * @param apiCall The api method to call.
     */
    fun <T> requestDataWithRateLimiting(apiCall: () -> T, endpointId : Int) : T {

        val requestsMadeThisMinute = requestDAOContract.requestsSinceLastClearedRates()
        val requestsAllowedInAMinute = requestDAOContract.getRateLimit()
        val timeToWait : Long = (timeFrameInSeconds *1000 - requestDAOContract.timeSinceLastClearedRates()).toLong()

        // If time to wait is not above 0, this must be the first request in some time
        if (timeToWait > 0) {
            if (requestsMadeThisMinute >= requestsAllowedInAMinute) {

                // Wait until we have exceeded the rate limit time.
                Thread.sleep(timeToWait)
            }
        }


        // Record our request
        requestDAOContract.recordRequest(System.currentTimeMillis())

        // Wipe out the saved requests if it has been over the given time period
        if (requestDAOContract.timeSinceLastClearedRates() >= timeFrameInSeconds) {
            requestDAOContract.clearRateLimitRequests()
        }

        return apiCall()
    }

    /**
     * Sends an http GET request to a specified url. Returns a pojo as part of a [NetworkResult] object, with the accompanying
     * response code. If the code is anything other than a 200, the data will be null.
     * @param type The class type we want our data response to be
     * @param url The url to send our GET request to
     */
    fun <T> sendHttpGetRequest(type : Class<T>, url : URL) : NetworkResult<T> {
        val conn = url.openConnection() as HttpURLConnection
        conn.doOutput = true
        conn.requestMethod = "GET"

        val respCode = conn.responseCode

        if (respCode == HttpURLConnection.HTTP_OK) {
            val response = StringBuffer()
            val reader = BufferedReader(InputStreamReader(conn.inputStream))

            // Parse our response
            val lines = reader.readLines()
            for(line in lines) {
                response.append(line)
            }
            reader.close()
            val result = gson.fromJson(response.toString(), type)
            return NetworkResult(result, 200)
        }
        // return our error
        return NetworkResult(null, respCode)
    }

    /**
     * This fetches the wait time for a request.
     * @return returns 0 if we have no time to wait, else it returns the time to wait in milliseconds
     */
    fun fetchWaitTime(endpointId: Int) : Int {
        val endpointRateLimit = rateLimitDao.getEndPointRateLimitStatus(endpointId)
        val appRateLimit = rateLimitDao.getEndPointRateLimitStatus(Endpoints().COMPLETE_APP)
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
     *
     */
    private fun produceLongestWait(endPointlist: ArrayList<RateLimitBucket>, appList: ArrayList<RateLimitBucket>): Int {
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
    private fun produceLongestWaitTimeForRateLimitArray(rateLimits : Iterator<RateLimitBucket>) : Int {
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
    private fun produceWaitTimeForIndividualRateLimitBucket(rateLimitBucket: RateLimitBucket) : Int {
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
    /**
     * Set the rate limit for our requests
     * @param requests The number of requests in a given time frame
     * @param timeFrameInSeconds The given time frame in seconds.
     */
    fun setApiKeyRate(requests : Int, timeFrameInSeconds : Int) {
        // set the time frame
        // set the number of requests
        this.timeFrameInSeconds = timeFrameInSeconds
        requestDAOContract.setRequestRateLimit(requests)

    }
}