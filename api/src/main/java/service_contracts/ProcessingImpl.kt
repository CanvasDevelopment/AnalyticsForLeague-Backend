package service_contracts

import com.github.salomonbrys.kodein.instance
import com.google.appengine.api.taskqueue.QueueFactory
import com.google.appengine.api.taskqueue.TaskOptions
import com.google.appengine.api.utils.SystemProperty
import com.google.gson.Gson
import database.match.MatchSummaryDAO
import database.match.model.MatchSummary
import di.KodeinManager
import network.NetworkResult
import network.Response
import util.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Josiah Kendall
 */
class ProcessingImpl : ProcessingContract {

    val km = KodeinManager()
    private val gson = Gson()
    private val PRODUCTION_URL = "https://processing-dot-analytics-for-league.appspot.com/"
    private val LOCAL_URL = "http://192.168.20.210:65070"

    private val matchSummaryDao : MatchSummaryDAO = km.kodein.instance()
    private val syncQueue = QueueFactory.getQueue("sync")

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
        val syncUrl = url + "/_ah/processing/api/v1/syncUser/$summonerId"
        val result : NetworkResult<Response> = sendHttpGetRequest(Response::class.java, URL(syncUrl))

        // send request to the database
        if (result.code == 200) {
            // fetch the match list from the db, and dispatch a bunch of tasks to the task queue.
            queueTasksForMatches(summonerId)
        }

        return result.code == 200
    }

    /**
     * Creates a bunch of tasks. Each task is a url request to fetch and store each match.
     * This queues a task that will run asynchronously.
     */
    private fun queueTasksForMatches(summonerId: Long) {

        val topMatchSummaries : ArrayList<MatchSummary> = matchSummaryDao.getRecentMatchesBySummonerIdForRole(summonerId, 20, SOLO, TOP)
        val midMatchSummaries = matchSummaryDao.getRecentMatchesBySummonerIdForRole(summonerId, 20, SOLO, MID)
        val jungleMatchSummaries = matchSummaryDao.getRecentMatchesBySummonerIdForRole(summonerId, 20, NONE, JUNGLE)
        val adcMatchSummaries = matchSummaryDao.getRecentMatchesBySummonerIdForRole(summonerId, 20, DUO_CARRY, BOT)
        val supMatchSummaries = matchSummaryDao.getRecentMatchesBySummonerIdForRole(summonerId, 20, DUO_SUPPORT, BOT)

        val listOfSummaries = ArrayList<MatchSummary>()
        listOfSummaries.addAll(topMatchSummaries)
        listOfSummaries.addAll(midMatchSummaries)
        listOfSummaries.addAll(jungleMatchSummaries)
        listOfSummaries.addAll(adcMatchSummaries)
        listOfSummaries.addAll(supMatchSummaries)

        val syncMatchUrl = "/syncMatch"

        listOfSummaries.forEach {
            syncQueue.add(TaskOptions.Builder.withUrl("$syncMatchUrl/${it.gameId}"))
        }
    }

}