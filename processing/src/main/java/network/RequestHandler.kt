package network

import db.RequestDAOContract

/**
 * @author Josiah Kendall
 */
class RequestHandler(val requestDAOContract: RequestDAOContract) {

    /**
     * This is the duration on which we limit to a certain number of requests
     */
    public val REQUEST_TIME_FRAME_MEADSUREMENT = 60

    /**
     * Requests data from the server. Allows us to pass an Api call as a higher order function and return its result.
     * This method handles the rate limiting of our requests
     * @param apiCall The api method to call.
     */
    fun <T> requestData(apiCall: () -> T) : T {

        val requestsMadeThisMinute = requestDAOContract.requestsSinceLastClearedRates()
        val requestsAllowedInAMinute = requestDAOContract.getRateLimitPerMinute()
        val timeToWait : Long = (REQUEST_TIME_FRAME_MEADSUREMENT - requestDAOContract.timeSinceLastClearedRates()) * 1000.toLong()

        if (requestsMadeThisMinute >= requestsAllowedInAMinute) {

            // Wait until we have exceeded the rate limit time.
            Thread.sleep(timeToWait)
        }

        // Record our request
        requestDAOContract.recordRequest(System.currentTimeMillis())

        // Wipe out the saved requests if it has been over the given time period
        if (requestDAOContract.timeSinceLastClearedRates() >= REQUEST_TIME_FRAME_MEADSUREMENT) {
            requestDAOContract.clearRateLimitRequests()
        }

        return apiCall()
    }

}