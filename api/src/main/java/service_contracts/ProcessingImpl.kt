package service_contracts

import com.google.appengine.api.utils.SystemProperty
import com.google.gson.Gson
import network.NetworkResult
import network.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Josiah Kendall
 */
class ProcessingImpl : ProcessingContract {

    private val gson = Gson()
    private val PRODUCTION_URL = "https://processing-dot-analytics-for-league.appspot.com/"
    private val LOCAL_URL = "http://localhost:43210"
    private val url : String

    constructor() {

        url = if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            PRODUCTION_URL
        }else {
            LOCAL_URL
        }
    }
    override fun syncUser(accountId: Long): Boolean {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createNewUser(accountName: String): Int {
        // send request to
        val fetchUserUrl = url + "/_ah/processing/api/v1/createNewUser/$accountName"
        val result = sendHttpGetRequest(Response::class.java, URL(fetchUserUrl))
        return result.code
    }

    private fun <T> sendHttpGetRequest(type : Class<T>, url : URL) : NetworkResult<T> {
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