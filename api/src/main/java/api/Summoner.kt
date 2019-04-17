package api

import api.controllers.SummonerController
import com.github.salomonbrys.kodein.instance

import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named

import database.DbHelper
import db.summoner.SummonerDao
import di.KodeinManager_api
import model.Response
import model.response_beans.SummonerDetails
import model.response_beans.SyncProgress
import service_contracts.ProcessingImpl

/**
 * @author Josiah Kendall
 */
@Api(name = "summoner",
        version = "v1",
        namespace = ApiNamespace(ownerDomain = "com.analyticsforleague",
                ownerName = "com.analyticsforleague",
                packagePath = "")
)
class Summoner {

    private val km = KodeinManager_api()
    private val dbHelper = km.kodein.instance<DbHelper>()
    private val summonerDao = SummonerDao(dbHelper)
    private val processingInterface = ProcessingImpl()
    private val summonerController = SummonerController(summonerDao,processingInterface)

    @ApiMethod(name = "sayHello",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "sayHello/{name}")
    fun sayHello(@Named("name") name : String) : Response<String> = Response(200, "Hello $name, dbhelper is null : ${dbHelper == null}")

    /**
     * Find out if a summoner is registered with our database.
     *
     * @param summonerName  The name of the summoner we are checking for
     * @return              True if the summoner exists, false if not.
     */
    @ApiMethod(name = "isSummonerRegistered",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "isRegistered/{name}")
    fun isSummonerRegistered(@Named("name") summonerName: String) : Response<Boolean> =
            summonerController.isSummonerRegistered(summonerName)

    /**
     * Register a summoner with our service. This triggers a fetch of the summoner from the riot server, and saves their
     * details in the database.
     *
     * @param summonerName  The name of the summoner who we are registering.
     * @return              The summoner id, or -1 if the registration process was unsuccessful.
     */
    @ApiMethod(name = "registerSummoner",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "register/{name}")
    fun registerSummoner(@Named("name") summonerName: String) : Response<SummonerDetails> =
            summonerController.registerSummoner(summonerName)

    /**
     * Fetch the details for a summoner
     * @param summonerId    The id of the summoner for whom we want the details
     * @return              A [Response] object with the relevant info for our summoner. The data is a json string of
     *                      a [SummonerDetails] object
     */
    @ApiMethod(name = "summonerDetails",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "summonerDetails/{summonerId}")
    fun getSummonerDetails(@Named("summonerId") summonerId : String) : Response<SummonerDetails> =
            summonerController.fetchSummonerDetails(summonerId)

    @ApiMethod(name = "sync",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "sync/{summonerId}")
    fun sync(@Named("summonerId") summonerId : String) : Response<String> = summonerController.syncSummoner(summonerId)
    @ApiMethod(name = "refineUserStats",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "refineUserStats/{summonerId}")
    fun refineUserStats(@Named("summonerId") summonerId: String) : Response<String> = summonerController.refineUserStats(summonerId)

    @ApiMethod(name = "syncProgress",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "syncProgress/{summonerId}")
    fun syncProgress(@Named("summonerId") summonerId: String) : Response<SyncProgress> {
        return summonerController.syncProgress(summonerId)
    }
}