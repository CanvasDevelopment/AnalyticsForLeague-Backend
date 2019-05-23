package service_contracts

import com.github.salomonbrys.kodein.instance
import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.QueueFactory
import com.google.appengine.api.taskqueue.TaskOptions
import com.google.appengine.api.utils.SystemProperty
import com.google.gson.Gson
import database.match.MatchSummaryDao
import database.match.model.MatchSummary
import di.KodeinManager_api
import model.response_beans.SyncProgress
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

    val km = KodeinManager_api()
    private val gson = Gson()
    private val PRODUCTION_URL = "https://processing-dot-analytics-gg.appspot.com"
    private val LOCAL_URL = "http://192.168.0.103:65070"

    private val matchSummaryDao : MatchSummaryDao = km.kodein.instance()
    private val syncQueue = QueueFactory.getDefaultQueue()
    private val log = Logger.getLogger(this::class.java.name)

    private val url : String

    init {
        url = if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            PRODUCTION_URL
        } else {
            LOCAL_URL
        }
    }

    override fun syncUser(summonerId : String) : Boolean {
        val syncUrl = "$url/_ah/processing/api/v1/syncUser/$summonerId"
        val result = sendHttpGetRequest(Response::class.java, URL(syncUrl))
        return result.code == 200
    }

    override fun createNewUser(accountName : String): Int {
        // send request to
        val accountNameNoSpaces = accountName.replace(" ", "%20")
        val fetchUserUrl = "$url/_ah/processing/api/v1/createNewUser/$accountNameNoSpaces"
        val result = sendHttpGetRequest(Response::class.java, URL(fetchUserUrl))
        return result.code
    }

    private fun <T> sendHttpGetRequest(type : Class<T>, url : URL) : NetworkResult<T> {
        log.info("Sending request to url: $url")
        val conn = url.openConnection() as HttpURLConnection
        conn.doOutput = true
        conn.requestMethod = "GET"
        conn.connectTimeout = 250000
        conn.readTimeout = 250000

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
            // more logging
            log.info("received successful response : $response")

            return NetworkResult(result, 200)
        }
        // return our error
        return NetworkResult(null, respCode)
    }

    override fun syncUserMatchList(summonerId: String): Boolean {
        val syncUrl = "$url/_ah/processing/api/v1/syncMatchList/$summonerId"
        val result : NetworkResult<Response> = sendHttpGetRequest(Response::class.java, URL(syncUrl))

        // send request to the database
        if (result.code == 200) {

            // fetch the match list from the db, and dispatch a bunch of tasks to the task queue.
            // todo rather than fetching all the matches from the server, the processing should dispatch all the requests itself.
//            queueTasksForMatches(summonerId, syncQueue)
        }

        return result.code == 200
    }

    /**
     * Creates a bunch of tasks. Each task is a url request to fetch and store each match.
     * This queues a task that will run asynchronously.
     */
    private fun queueTasksForMatches(summonerId: String, queue: Queue) {

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
            // if production, do it different?
            Logger.getLogger(this::class.java.name).log(Level.SEVERE, "Dispatching task for ${it.gameId}", "")
            val task = TaskOptions.Builder.withDefaults()
            task.method(TaskOptions.Method.GET)
            task.url("$syncMatchUrl/${it.gameId}/$summonerId")
            queue.add(task)
        }
    }

    fun syncMatchProxy(matchId : Long, summonerId: String) {
        val syncUrl = "$url/_ah/processing/api/v1/syncMatch/$matchId/$summonerId"
        val result : NetworkResult<Response> = sendHttpGetRequest(Response::class.java, URL(syncUrl))
    }

    override fun refineUserStats(summonerId : String) : Boolean {
        val refineUrl = "$url/_ah/processing/api/v1/refineStats/$summonerId"
        val result :NetworkResult<Response> = sendHttpGetRequest(Response::class.java, URL(refineUrl))
        return result.code in 200..299
    }

    override fun getSyncProgress(summonerId : String): SyncProgress {
        val syncProgressUrl = "$url/_ah/processing/api/v1/syncProgress/$summonerId"
        val result : NetworkResult<Response> = sendHttpGetRequest(Response::class.java, URL(syncProgressUrl))
        if (result.data == null) {
            return SyncProgress()
        }
        return gson.fromJson(result.data!!.data, SyncProgress::class.java)
    }

}