package network.riotapi.header

import db.requests.EndpointRateLimitStatusDao
import model.networking.EndpointRateLimitStatus
import java.net.HttpURLConnection

/**
 * @author Josiah Kendall
 */
class RiotApiResponseHeaderParser(private val rateLimitStatusDao: EndpointRateLimitStatusDao) {

    private val headers = Headers()

//    fun parseResponse(connection : HttpURLConnection, endpointId : Int) : EndpointRateLimitStatus {
//        val appRateLimitString = connection.getHeaderField(headers.APP_RATE_LIMIT)
//        val appRateLimitCountString = connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)
//        val date = connection.getHeaderField(headers.DATE)
//        val methodRateLimitString = connection.getHeaderField(headers.METHOD_RATE_LIMIT)
//        val methodRateLimitCountString = connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)
//        val retryAfter = connection.getHeaderField(headers.RETRY_AFTER)
//
//        // parse the header strings into objects
//
//        // if our request is not the first, we dont want to update the entire object, just a few fields.
//        // if our request is the first, we do want to update the field
//        // with all the values that are not null, we want to add to the already exising field
//        val previousStatus = rateLimitStatusDao.getEndPointRateLimitStatus(endpointId)
//    }
//
//    fun parseRateLimit(bucketsString: String) : RateLimitBuckets {
//        val bucketsArray = ArrayList<RateLimitBucket>()
//        // split along the commas
//        val bucketStringArray = bucketsString.split(",")
//        for (bucketString in bucketStringArray) {
//
//        }
//        // for each object, split along the colon, so that we have the length.
//        // if we only have 2, just do two
//
//    }
}