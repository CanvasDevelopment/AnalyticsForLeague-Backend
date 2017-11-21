package network

import com.google.gson.Gson
import db.requests.EndpointRateLimitStatusDao
import db.requests.RateLimitDao
import db.requests.RequestDAOContract
import model.networking.Endpoints
import model.networking.NetworkResult
import network.riotapi.header.RateLimitBucket
import network.riotapi.header.RiotApiResponseHeaderParser
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
                     private val endpointRateLimitDao: EndpointRateLimitStatusDao,
                     private val rateLimiter: RateLimiter,
                     private val riotApiResponseHeaderParser: RiotApiResponseHeaderParser) {

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
        val endpointExists = endpointRateLimitDao.doesEndpointExistInDb(endpointId)

        if (endpointExists) {
            val timeToWait = rateLimiter.fetchWaitTime(endpointId)
            Thread.sleep(timeToWait.toLong())
        }

        return apiCall()
    }

    /**
     * Sends an http GET request to a specified url. Returns a pojo as part of a [NetworkResult] object, with the accompanying
     * response code. If the code is anything other than a 200, the data will be null.
     * @param type The class type we want our data response to be
     * @param url The url to send our GET request to
     */
    fun <T> sendHttpGetRequest(type : Class<T>, url : URL, endpointId: Int) : NetworkResult<T> {
        val conn = url.openConnection() as HttpURLConnection
        conn.doOutput = true
        conn.requestMethod = "GET"

        val rateLimitsFromHeaders = riotApiResponseHeaderParser.parseRateLimitsForAppAndEndpoint(conn, endpointId)

        // save method endpoint
        endpointRateLimitDao.saveEndpointRateLimitStatus(rateLimitsFromHeaders.endpointRateLimit)
        endpointRateLimitDao.saveEndpointRateLimitStatus(rateLimitsFromHeaders.appKeyRateLimit)

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
}