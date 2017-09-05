package network

import com.google.gson.Gson
import db.RequestDAOContract
import model.NetworkResult
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Josiah Kendall
 *
 * Handles network requests to the riot api.
 */
class RequestHandler(private val requestDAOContract: RequestDAOContract) {
    private val gson = Gson()
    /**
     * This is the duration on which we limit to a certain number of requests
     */
    private val REQUEST_TIME_FRAME_MEASUREMENT = 60

    /**
     * Requests data from the server. Allows us to pass an Api/netork call as a higher order function and return its result.
     * This method handles the rate limiting of our requests
     * @param apiCall The api method to call.
     */
    fun <T> requestDataWithRateLimiting(apiCall: () -> T) : T {

        val requestsMadeThisMinute = requestDAOContract.requestsSinceLastClearedRates()
        val requestsAllowedInAMinute = requestDAOContract.getRateLimitPerMinute()
        val timeToWait : Long = (REQUEST_TIME_FRAME_MEASUREMENT - requestDAOContract.timeSinceLastClearedRates()) * 1000.toLong()

        if (requestsMadeThisMinute >= requestsAllowedInAMinute) {

            // Wait until we have exceeded the rate limit time.
            Thread.sleep(timeToWait)
        }

        // Record our request
        requestDAOContract.recordRequest(System.currentTimeMillis())

        // Wipe out the saved requests if it has been over the given time period
        if (requestDAOContract.timeSinceLastClearedRates() >= REQUEST_TIME_FRAME_MEASUREMENT) {
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

}