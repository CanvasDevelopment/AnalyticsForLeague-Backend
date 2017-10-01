package api

import com.google.api.server.spi.config.*
import com.google.gson.Gson
import database.DbHelper
import db.summoner.SummonerDao
import model.Response
import model.response_beans.SummonerDetails
import model.response_beans.SummonerExistence
import service_contracts.ProcessingContract

/**
 * @author Josiah Kendall
 */
@Api(name = "summoner",
        version = "v1",
        namespace = ApiNamespace(ownerDomain = "com.analyticsforleague",
                ownerName = "com.analyticsforleague",
                packagePath = "")
)
class Summoner(val dbHelper : DbHelper,
               val summonerDao: SummonerDao,
               val processingInterface : ProcessingContract) {

    val gson = Gson()

    @ApiMethod(name = "sayHello", path = "sayHello/{name}")
    fun sayHello(@Named("name") name : String) : Response {
        return Response(200, "Hello $name")
    }

    /**
     * Find out if a summoner is registered with our database.
     *
     * @param summonerName  The name of the summoner we are checking for
     * @return              True if the summoner exists, false if not.
     */
    @ApiMethod(name = "isSummonerRegistered", path = "exists/{name}")
    fun isSummonerRegistered(@Named("name") summonerName: String) : Response {
        val existence = SummonerExistence(summonerDao.getSummoner(summonerName) != null)
        return Response(200, gson.toJson(existence))
    }

    /**
     * Register a summoner with our service. This triggers a fetch of the summoner from the riot server, and saves their
     * details in the database.
     *
     * @param summonerName  The name of the summoner who we are registering.
     * @return              The summoner id, or -1 if the registration process was unsuccessful.
     */
    @ApiMethod(name = "registerSummoner", path = "register/{name}")
    fun registerSummoner(@Named("name") summonerName: String) : Response {
        val creationResultCode = processingInterface.createNewUser(summonerName)
        if (creationResultCode == 200) {
            val summonerDetails = summonerDao.getSummoner(summonerName)
            if (summonerDetails != null) {
                return Response(200, gson.toJson(summonerDetails))
            }
        }

        // failed
        return Response(creationResultCode, "")

    }

//    /**
//     * Fetch the details for a summoner
//     * @param summonerId The id of the summoner for whom we want the details
//     * @return A [SummonerDetails] object with the relevant info for our summoner.
//     */
////    fun fetchSummonerDetails(summonerId : Long) : SummonerDetails {
////        TODO()
////    }
}