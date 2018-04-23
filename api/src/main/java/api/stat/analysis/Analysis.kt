package api.stat.analysis

import api.stat.analysis.model.AnalysisStatCardSkeleton
import api.stat.analysis.model.FullStatCard
import api.stat.analysis.model.CreepsPerMinuteDeltasDetail
import api.stat.analysis.model.HeadToHeadStat
import com.github.salomonbrys.kodein.instance
import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import di.KodeinManager
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

    private val km = KodeinManager()
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
     * @param games         The number of games
     */
    @ApiMethod(name = "getStatList",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "getStatList/{summonerId}/{lane}/{champ}/{games}")
    fun getStatList(@Named("summonerId") summonerId: Long,
                    @Named("lane") lane : String,
                    @Named("champ")champId: Int,
                    @Named("games")games: Int) : ArrayList<AnalysisStatCardSkeleton> {
        return presenter.fetchStatList(summonerId,lane,champId)
    }

    @ApiMethod(name = "getStatList",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "getStatList/{summonerId}/{lane}/{games}")
    fun getStatList(@Named("summonerId") summonerId: Long,
                    @Named("lane") lane : String,
                    @Named("games")games: Int) : ArrayList<AnalysisStatCardSkeleton> {

        return presenter.fetchStatList(summonerId,lane)
    }

    @ApiMethod(name = "creepsPerMinuteDeltasCard",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "creepsPerMinute/cardUrl/{summonerId}/{games}/{lane}")
    fun creepsPerMinuteDeltasCard(@Named("summonerId")summonerId : Long,
                                  @Named("games") games : Int,
                                  @Named("lane") lane : String) : FullStatCard {
        return presenter.creepsPerMinuteDeltasCard(summonerId, games, lane)
    }

    /**
     * The fetch for getting the information for the creeps per minute cardUrl detailUrl page.
     *
     * @param summonerId    The hero summoner id.
     * @param games         The number of recent games to limit our filter to.
     * @param lane          The lane we want the info for.
     * @return A [CreepsPerMinuteDeltasDetail] object with the stats on it.
     */
    @ApiMethod(name = "creepsPerMinuteDeltasDetail",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "creepsPerMinute/details/{summonerId}/{games}/{lane}")
    fun creepsPerMinuteDeltasDetail(@Named("summonerId")summonerId : Long,
                                    @Named("games") games : Int,
                                    @Named("lane") lane : String) : CreepsPerMinuteDeltasDetail {

        return presenter.creepsPerMinuteDeltasDetail(summonerId,games, lane)
    }

    /**
     * The fetch for getting the information for the damage per minute cardUrl detailUrl page.
     *
     * @param summonerId    The hero summoner id.
     * @param games         The number of recent games to limit our filter to.
     * @param lane          The lane we want the info for.
     * @return A [CreepsPerMinuteDeltasDetail] object with the stats on it.
     */
    @ApiMethod(name = "damagePerMinuteDeltasDetail",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "damagePerMinute/details/{summonerId}/{games}/{lane}")
    fun damagePerMinuteDeltasDetail(@Named("summonerId")summonerId : Long,
                                    @Named("games") games : Int,
                                    @Named("lane") lane : String) : CreepsPerMinuteDeltasDetail {
        return presenter.damagePerMinuteDeltasDetail(summonerId,games, lane)
    }

    @ApiMethod(name = "damagePerMinuteDeltasCard",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "damagePerMinute/cardUrl/{summonerId}/{games}/{lane}")
    fun damagePerMinuteDeltasCard(@Named("summonerId")summonerId : Long,
                                  @Named("games") games : Int,
                                  @Named("lane") lane : String) : FullStatCard {

        return presenter.creepsPerMinuteDeltasCard(summonerId, games, lane)
    }

    /**
     * Fetch a [AnalysisStatCardSkeleton] array for loading data on a card.
     *
     * @param summonerId    The summoner id of the user
     * @param numberOfGames The number of games a user wants to filter the results to. E.g, for the most recent twenty games,
     *                      this will be 20. The most recent 30, it will be 30 etc.
     * @param lane          The lane that the user wants to filter the results to.
     * @param champId       The id of the champion that the user wants to filter the results to.
     * @param statName      The name of the statistics that are required. See [StatTypes].
     */
    @ApiMethod(name = "fetchFullStatCardValues",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "fullStatCard/{summonerId}/{games}/{lane}/{champ}/{statName}")
    fun fetchFullStatCardValues(@Named("summonerId") summonerId: Long,
                                @Named("games") numberOfGames : Int,
                                @Named("lane") lane: String,
                                @Named("champ") champId : Int,
                                @Named("statName") statName : String) : ArrayList<HeadToHeadStat> {
        return presenter.fetchStatsForFullCard(summonerId,numberOfGames,lane,champId,statName,AVG)
    }

    /**
     * Fetch a [AnalysisStatCardSkeleton] array for loading data on a card.
     * @param summonerId    The summoner id of the user
     * @param numberOfGames The number of games a user wants to filter the results to. E.g, for the most recent twenty games,
     *                      this will be 20. The most recent 30, it will be 30 etc.
     * @param lane          The lane that the user wants to filter the results to.
     * @param statName      The name of the statistics that are required. See [StatTypes].
     */
    @ApiMethod(name = "fetchFullStatCardValues",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "fullStatCard/{summonerId}/{games}/{lane}/{champ}/{statName}")
    fun fetchFullStatCardValues(@Named("summonerId") summonerId: Long,
                                @Named("games") numberOfGames : Int,
                                @Named("lane") lane: String,
                                @Named("statName") statName : String) : ArrayList<HeadToHeadStat> {
        return presenter.fetchStatsForFullCard(summonerId,numberOfGames,lane,statName,AVG)
    }


    /**
     * Fetch a half stat - a stat that covers only half the screen. This usually represents a single stat - such as wards
     * killed, or wards placed, or barons killed etc..
     */
    fun fetchHalfStatCard(@Named("summonerId") summonerId: Long,
                          @Named("games") numberOfGames : Int,
                          @Named("lane") lane: String,
                          @Named("champ") champId : Int,
                          @Named("statName") statName : String) : HeadToHeadStat {
        return presenter.fetchStatsForHalfCard(summonerId, numberOfGames, lane, champId, statName)
    }




}