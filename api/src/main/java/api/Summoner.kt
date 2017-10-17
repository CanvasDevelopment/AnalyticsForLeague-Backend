package api

import api.controllers.SummonerController
import com.google.api.server.spi.config.*
import database.DbHelper
import db.summoner.SummonerDao
import model.Response
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

    private val dbHelper = DbHelper()
    private val summonerDao = SummonerDao(dbHelper)
    private val processingInterface = ProcessingImpl()
    private val summonerController = SummonerController(summonerDao,processingInterface)

    @ApiMethod(name = "sayHello",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "sayHello/{name}")
    fun sayHello(@Named("name") name : String) : Response {
        return Response(200, "Hello $name")
    }

    /**
     * Find out if a summoner is registered with our database.
     *
     * @param summonerName  The name of the summoner we are checking for
     * @return              True if the summoner exists, false if not.
     */
    @ApiMethod(name = "isSummonerRegistered",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "exists/{name}")
    fun isSummonerRegistered(@Named("name") summonerName: String) : Response {
        return summonerController.isSummonerRegistered(summonerName)
    }

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
    fun registerSummoner(@Named("name") summonerName: String) : Response {
        return summonerController.registerSummoner(summonerName)
    }

    /**
     * Fetch the details for a summoner
     * @param summonerId    The id of the summoner for whom we want the details
     * @return              A [Response] object with the relevant info for our summoner. The data is a json string of
     *                      a [SummonerDetails] object
     */
    @ApiMethod(name = "summonerDetails",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "summonerDetails/{summonerId}")
    fun getSummonerDetails(@Named("summonerId") summonerId : Long) : Response {
        return summonerController.fetchSummonerDetails(summonerId)
    }
}