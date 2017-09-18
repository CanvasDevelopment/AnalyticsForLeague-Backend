package network.riotapi.header

import db.requests.EndpointRateLimitStatusDao
import model.networking.EndpointRateLimitStatus
import java.net.HttpURLConnection

/**
 * @author Josiah Kendall
 */
class RiotApiResponseHeaderParser(private val rateLimitStatusDao: EndpointRateLimitStatusDao) {

    private val headers = Headers()

    fun parseResponse(connection : HttpURLConnection, endpointId : Int) : EndpointRateLimitStatus {
        val appRateLimitString = connection.getHeaderField(headers.APP_RATE_LIMIT)
        val appRateLimitCountString = connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)
        val date = connection.getHeaderField(headers.DATE)
        val methodRateLimitString = connection.getHeaderField(headers.METHOD_RATE_LIMIT)
        val methodRateLimitCountString = connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)
        val retryAfter = connection.getHeaderField(headers.RETRY_AFTER)

        // parse the header strings into objects

        // if our request is not the first, we dont want to update the entire object, just a few fields.
        // if our request is the first, we do want to update the field
        // with all the values that are not null, we want to add to the already exising field

    }

    fun parseRateLimit(rateLimitBucketsString: String,
                       currentStateBucketsString: String) : ArrayList<RateLimitBucket> {
        val bucketsArrayList = ArrayList<RateLimitBucket>()
        val currentTime = System.currentTimeMillis()

        // split along the commas
        val bucketStringArray = rateLimitBucketsString.split(",")
        val currentStateBucketStringArray = currentStateBucketsString.split(",")
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