package api.stat.analysis

import api.stat.analysis.model.AnalysisStatCardSkeleton
import api.stat.analysis.model.HeadToHeadStat
import api.stat.analysis.model.StatDetails
import com.github.salomonbrys.kodein.instance
import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import di.KodeinManager_api
import model.Response
import util.Constant.StatAccumulators.AVG
import util.Constant.StatTypes

/**
 * @author Josiah Kendall
 *
 * The api for the stats in the analysis tab. Each cardUrl should have a cardUrl and a detailUrl method listed in here.
 */
@Api(name = "analysis",
        version = "v1",
        namespace = ApiNamespace(ownerDomain = "com.analyticsforleague",
                ownerName = "com.analyticsforleague",
                packagePath = "")
)
class Analysis {

    private val km = KodeinManager_api()
    private val presenter: AnalysisPresenter = km.kodein.instance()

    // Just a simple response test for the api
    @ApiMethod(name = "sayHi",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "sayHi/{name}")
    fun sayHi(@Named("name") name : String) : Response<String> = Response(200, name)

    /**
     * @param summonerId    The users summoner id.
     * @param lane          The role to filter the results to.
     * @param champId       The champion to filter the results to
     * @param games         The number of limit
     */
    @ApiMethod(name = "getStatListWithChamp",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "getStatList/{summonerId}/{lane}/{champ}/{limit}")
    fun getStatListWithChamp(@Named("summonerId") summonerId : String,
                    @Named("lane") lane : String,
                    @Named("champ")champId: Int,
                    @Named("limit")games: Int) : Response<ArrayList<AnalysisStatCardSkeleton>> {
        val data = presenter.fetchStatList(summonerId,lane,champId)
        return Response(200, data)
    }

    @ApiMethod(name = "getStatList",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "getStatList/{summonerId}/{lane}/{limit}")
    fun getStatList(@Named("summonerId") summonerId : String,
                    @Named("lane") lane : String,
                    @Named("limit") games: Int ) : Response<ArrayList<AnalysisStatCardSkeleton>> {

        val data = presenter.fetchStatList(summonerId,lane)
        return Response(200, data)
    }

    /**
     * Fetch a [AnalysisStatCardSkeleton] array for loading data on a card.
     *
     * @param summonerId    The summoner id of the user
     * @param numberOfGames The number of limit a user wants to filter the results to. E.g, for the most recent twenty limit,
     *                      this will be 20. The most recent 30, it will be 30 etc.
     * @param lane          The lane that the user wants to filter the results to.
     * @param champId       The id of the champion that the user wants to filter the results to.
     * @param statName      The name of the statistics that are required. See [StatTypes].
     */
    @ApiMethod(name = "fetchFullStatCardWithChamp",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "fullStatCard/{summonerId}/{limit}/{lane}/{statName}/{champ}")
    fun fetchFullStatCardValuesWithChamp(
                                @Named("summonerId") summonerId : String,
                                @Named("limit") numberOfGames : Int,
                                @Named("lane") lane: String,
                                @Named("champ") champId : Int,
                                @Named("statName") statName : String) : Response<ArrayList<HeadToHeadStat>> {
        val fullStatCard = presenter.fetchStatsForFullCard(summonerId,numberOfGames,lane,champId,statName,AVG)
        return Response(200, fullStatCard)
    }

    /**
     * Fetch a [AnalysisStatCardSkeleton] array for loading data on a card.
     * @param summonerId    The summoner id of the user
     * @param numberOfGames The number of limit a user wants to filter the results to. E.g, for the most recent twenty limit,
     *                      this will be 20. The most recent 30, it will be 30 etc.
     * @param lane          The lane that the user wants to filter the results to.
     * @param statName      The name of the statistics that are required. See [StatTypes].
     */
    @ApiMethod(name = "fetchFullStatCardValues",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "fullStatCard/{summonerId}/{limit}/{lane}/{statName}")
    fun fetchFullStatCardValues(@Named("summonerId") summonerId : String,
                                @Named("limit") numberOfGames : Int,
                                @Named("lane") lane: String,
                                @Named("statName") statName : String) : Response<ArrayList<HeadToHeadStat>> {
        val fullStatCard = presenter.fetchStatsForFullCard(summonerId,numberOfGames,lane,statName,AVG)
        return Response(200, fullStatCard)
    }

    /**
     * Fetch a half select - a select that covers only half the screen. This usually represents a single select - such as wards
     * killed, or wards placed, or barons killed etc..
     */
    @ApiMethod(name = "fetchHalfStatCardWithChamp",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "halfStatCard/{summonerId}/{limit}/{lane}/{champ}/{statName}")
    fun fetchHalfStatCardWithChamp( @Named("summonerId") summonerId : String,
                                    @Named("limit") numberOfGames : Int,
                                    @Named("lane") lane: String,
                                    @Named("champ") champId : Int,
                                    @Named("statName") statName : String) : Response<HeadToHeadStat> {

        val halfStatCard = presenter.fetchStatsForHalfCard(summonerId, numberOfGames, lane, champId, statName)
        return Response(200, halfStatCard)
    }

    /**
     * Fetch a half select - a select that covers only half the screen. This usually represents a single select - such as wards
     * killed, or wards placed, or barons killed etc..
     */
    @ApiMethod(name = "fetchHalfStatCard",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "halfStatCard/{summonerId}/{limit}/{lane}/{statName}")
    fun fetchHalfStatCard( @Named("summonerId") summonerId : String,
                           @Named("limit") numberOfGames : Int,
                           @Named("lane") lane: String,
                           @Named("statName") statName : String) : Response<HeadToHeadStat> {
        val response = presenter.fetchStatsForHalfCard(summonerId, numberOfGames, lane, statName)
        return Response(200, response)
    }

    @ApiMethod(name = "fetchStatDetails",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "statDetails/{summonerId}/{games}/{lane}/{statName}")
    fun fetchStatDetails(@Named("summonerId") summonerId : String,
                         @Named("games") games : Int,
                         @Named("lane") lane: String,
                         @Named("statName") statName: String) : Response<StatDetails>{
        return Response(200, presenter.fetchStatDetails(summonerId, games, lane, statName))
    }

    @ApiMethod(name = "fetchStatDetailsWithChamp",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "statDetails/{summonerId}/{games}/{lane}/{statName}/{champKey}")
    fun fetchStatDetailsWithChamp(@Named("summonerId") summonerId : String,
                         @Named("games") games : Int,
                         @Named("lane") lane: String,
                         @Named("statName") statName: String,
                         @Named("champKey") champKey : String) : Response<StatDetails>{
        return Response(200, presenter.fetchStatDetails(summonerId, games, lane, statName, champKey))
    }

    @ApiMethod(name = "fetchSummonerWinRate",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "fetchSummonerWinRate/{summonerId}/{lane}")
    fun fetchSummonerWinRate(@Named("summonerId") summonerId: String, @Named("lane") lane : String) : Response<HeadToHeadStat> {
        return Response(200, presenter.fetchSummonerWinRate(summonerId, lane))
    }

    @ApiMethod(name = "fetchSummonerWinRateWithChamp",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "fetchSummonerWinRate/{summonerId}/{lane}/{champKey}")
    fun fetchSummonerWinRateWithChamp(@Named("summonerId") summonerId: String,
                                      @Named("lane") lane : String,
                                      @Named("champKey") champKey : String) : Response<HeadToHeadStat> {
        return return Response(200, presenter.fetchSummonerWinRate(summonerId, lane, champKey))
    }
}