package service_contracts

import com.github.salomonbrys.kodein.instance
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.QueueFactory
import com.google.appengine.api.taskqueue.TaskOptions
import com.google.appengine.api.utils.SystemProperty
import com.google.gson.Gson
import database.match.MatchSummaryDao
import database.match.model.MatchSummary
import di.KodeinManager
import network.NetworkResult
import network.Response
import util.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 */
class ProcessingImpl : ProcessingContract {

    val km = KodeinManager()
    private val gson = Gson()
    private val PRODUCTION_URL = "https://processing-dot-analytics-for-league.appspot.com/"
    private val LOCAL_URL = "http://192.168.21.41:65070"

    private val matchSummaryDao : MatchSummaryDao = km.kodein.instance()
    private val syncQueue = QueueFactory.getDefaultQueue()

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
        conn.connectTimeout = 10000
        conn.readTimeout = 10000

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
        val syncUrl = url + "/_ah/processing/api/v1/syncMatchList/$summonerId"
        val result : NetworkResult<Response> = sendHttpGetRequest(Response::class.java, URL(syncUrl))

        // send request to the database
        if (result.code == 200) {

            // fetch the match list from the db, and dispatch a bunch of tasks to the task queue.

            queueTasksForMatches(summonerId, syncQueue)
        }

        return result.code == 200
    }

    /**
     * Creates a bunch of tasks. Each task is a url request to fetch and store each match.
     * This queues a task that will run asynchronously.
     */
    private fun queueTasksForMatches(summonerId: Long, queue: Queue) {

        val topMatchSummaries : ArrayList<MatchSummary> = matchSummaryDao.getRecentMatchesBySummonerIdForRole(summonerId, 500, SOLO, TOP)
        val midMatchSummaries = matchSummaryDao.getRecentMatchesBySummonerIdForRole(summonerId, 500, SOLO, MID)
        val jungleMatchSummaries = matchSummaryDao.getRecentMatchesBySummonerIdForRole(summonerId, 500, NONE, JUNGLE)
        val adcMatchSummaries = matchSummaryDao.getRecentMatchesBySummonerIdForRole(summonerId, 500, DUO_CARRY, BOT)
        val supMatchSummaries = matchSummaryDao.getRecentMatchesBySummonerIdForRole(summonerId, 500, DUO_SUPPORT, BOT)

        val listOfSummaries = ArrayList<MatchSummary>()
        listOfSummaries.addAll(topMatchSummaries)
        listOfSummaries.addAll(midMatchSummaries)
        listOfSummaries.addAll(jungleMatchSummaries)
        listOfSummaries.addAll(adcMatchSummaries)
        listOfSummaries.addAll(supMatchSummaries)

        val syncMatchUrl = "/_ah/api/match/v1/syncMatch"

        listOfSummaries.forEach {
            Logger.getLogger(this::class.java.name).log(Level.SEVERE, "Dispatching task for ${it.gameId}", "")
            val task = TaskOptions.Builder.withDefaults()
            task.method(TaskOptions.Method.GET)
            task.url("$syncMatchUrl/${it.gameId}/$summonerId")
            queue.add(task)
        }
    }

    fun syncMatchProxy(matchId : Long, summonerId: Long) {
        val syncUrl = "$url/_ah/processing/api/v1/syncMatch/$matchId/$summonerId"
        val result : NetworkResult<Response> = sendHttpGetRequest(Response::class.java, URL(syncUrl))
    }

    override fun refineUserStats(summonerId: Long): Boolean {
        val refineUrl = "$url/_ah/processing/api/v1/refineStats/$summonerId"
        val result :NetworkResult<Response> = sendHttpGetRequest(Response::class.java, URL(refineUrl))
        return result.code in 200..299
    }

}