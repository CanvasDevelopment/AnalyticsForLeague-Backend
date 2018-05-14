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
    private val LOCAL_URL = "http://192.168.1.3:43210"
    private val url : String

    init {
        url = if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            PRODUCTION_URL
        } else {
            LOCAL_URL
        }
    }

    override fun syncUser(summonerId: Long): Boolean {
        val syncUrl = url + "/_ah/processing/api/v1/syncUser/$summonerId"
        val result = sendHttpGetRequest(Response::class.java, URL(syncUrl))
        return result.code == 200
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
    override fun syncUserMatchList(summonerId: Long): Boolean {
        // todo add the correct url here once it has been added to the processing module.
        val syncUrl = url + "/_ah/processing/api/v1/syncUser/$summonerId"k
        val result = sendHttpGetRequest(Response::class.java, URL(syncUrl))
        return result.code == 200

    }

}