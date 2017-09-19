package network.riotapi.header

import db.requests.EndpointRateLimitStatusDao
import model.networking.EndpointRateLimit
import model.networking.Endpoints
import java.net.HttpURLConnection
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 */
class RiotApiResponseHeaderParser(private val rateLimitStatusDao: EndpointRateLimitStatusDao) {

    private val log = Logger.getLogger(this::class.java.name)

    private val headers = Headers()

    /**
     * Produces the rate limits specified in the header for a request to an endpoint. The rate limits are for the
     * endpoint and the overall app key rates.
     * @param connection The httpUrl connection that contains the headers.
     * @param endpointId The id of the endpoint that this was sent to.
     * @return A [RateLimitsWrapper] with the relevant rates on it.
     */
    fun parseRateLimitsForAppAndEndpoint(connection : HttpURLConnection, endpointId : Int) : RateLimitsWrapper {
        val appRateLimitString = connection.getHeaderField(headers.APP_RATE_LIMIT)
        val appRateLimitCountString = connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)

        val methodRateLimitString = connection.getHeaderField(headers.METHOD_RATE_LIMIT)
        val methodRateLimitCountString = connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)
        val retryAfter = connection.getHeaderField(headers.RETRY_AFTER)

        // if retry after is not an int, we need to log an error here
        val appKeyRates = EndpointRateLimit(
                Endpoints().COMPLETE_APP,
                retryAfter.toInt(),
                parseRateLimit(appRateLimitString, appRateLimitCountString))

        val endpointRateLimit = EndpointRateLimit(
                endpointId,
                retryAfter.toInt(),
                parseRateLimit(methodRateLimitString,methodRateLimitCountString))

        return RateLimitsWrapper(appKeyRates, endpointRateLimit, endpointId)
    }

    /**
     * Parse the rate limits and their current usage.
     *
     * @param rateLimitBucketsString            This is the rate limit buckets. They reflect the different rates that
     *                                          are currently set by riot.
     * @param currentStateOfTheBucketsString    This is the current usage amount by our apikey of the different rates.
     * @return An array list of [RateLimitBucket]'s representing the different limits and our current usage count
     */
    fun parseRateLimit(rateLimitBucketsString: String?,
                       currentStateOfTheBucketsString: String?) : ArrayList<RateLimitBucket> {

        if (rateLimitBucketsString == null
                || currentStateOfTheBucketsString == null
                || rateLimitBucketsString.isEmpty()
                || currentStateOfTheBucketsString.isEmpty()) {
            return ArrayList()
        }

        val bucketsArrayList = ArrayList<RateLimitBucket>()
        val currentTime = System.currentTimeMillis()

        // split along the commas
        val bucketStringArray = rateLimitBucketsString.split(",")
        val currentStateBucketStringArray = currentStateOfTheBucketsString.split(",")
        val bucketStringIterator = bucketStringArray.iterator()
        val currentRequestCountBucketStringIterator = currentStateBucketStringArray.iterator()

        while (bucketStringIterator.hasNext() && currentRequestCountBucketStringIterator.hasNext()) {
            val bucketLimitParts = bucketStringIterator.next().split(":")
            val bucketCountParts = currentRequestCountBucketStringIterator.next().split(":")

            val maxRequests = bucketLimitParts[0].toInt()
            val requestCount =  bucketCountParts[0].toInt()

            var requestTime : Long = -1
            if (requestCount == 1) {
                requestTime = currentTime
            }

            val rateDuration = bucketCountParts[1].toInt()

            val bucket = RateLimitBucket(
                    maxRequests,
                    requestCount,
                    requestTime,
                    rateDuration)
            bucketsArrayList.add(bucket)
        }

        return bucketsArrayList
    }
}