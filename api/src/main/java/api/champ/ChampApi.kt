package api.champ

import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import di.KodeinManager_api
import com.github.salomonbrys.kodein.instance
import model.Response

/**
 * @author Josiah Kendall
 *
 * The API class for the champion data
 */
@Api(name = "champ",
        version = "v1",
        namespace = ApiNamespace(ownerDomain = "com.analyticsforleague",
                ownerName = "com.analyticsforleague",
                packagePath = "")
)
class ChampApi {
    private val km = KodeinManager_api()
    private val champDataProcessing  : ChampDataProcessing = km.kodein.instance()
    private val champDataDAO : ChampDataDAO = km.kodein.instance()

    /**
     * Fetch and sync the latest data on champs from the server. This calls
     * a method in the processing module that downloads a json file, parses it
     * and saves all the objects.
     *
     * @param version This is the version that we fetch the champs for. For
     * instance, if the game is updated and a new champ is added, we simply
     * call this method with the version code (e.g 6.14.2) and it will update
     * the database with the newest changes to the champion list.
     */
    @ApiMethod(name = "syncChamps",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "syncChamps/{version}")
    fun syncChamps(@Named("version") version : String) {
        champDataProcessing.start(version)
    }

    /**
     * Fetch and download all the champs that we have in our database.
     */
    @ApiMethod(name = "fetchAllChamps",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "fetchAllChamps/{summonerId}")

    fun fetchAllChampsForSummoner(@Named("summonerId") summonerId : String) : Response<ArrayList<ChampData>> {
//        val
        return Response(200, champDataDAO.loadChampDataForAllChamps(summonerId))
    }

    /**
     * Simple method to retrieve info for all the champs
     */
    @ApiMethod(name = "fetchAllChamps",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "fetchAllChamps")
    fun fetchSimpleChampInfo() : Response<ArrayList<SimpleChamp>> {
        return Response(200, champDataDAO.loadChampDataForAllChamps())

    }

}
