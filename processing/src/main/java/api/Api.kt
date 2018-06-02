package api

import application.Sync
import application.domain.MatchControl
import application.domain.SummonerControl
import com.github.salomonbrys.kodein.instance
import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import com.google.appengine.api.taskqueue.QueueFactory
import db.match.TimelineDAO
import di.KodeinManager
import model.beans.Response
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 */
@Api(name = "api",
        version = "v1",
        namespace = ApiNamespace(ownerDomain = "com.analyticsforleague",
                ownerName = "com.analyticsforleague",
                packagePath = "")
)
class Api  {

    private val km = KodeinManager()
    private val sync = km.kodein.instance<Sync>()
    private val summonerControl = km.kodein.instance<SummonerControl>()

    @ApiMethod(name = "syncUser",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "syncUser/{summonerId}")
    fun syncUser(@Named("summonerId") summonerId : Long): Response {
        val responseCode = sync.syncMatches(summonerId)
        return Response(responseCode, "")
    }

    @ApiMethod(name = "createNewUser",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "createNewUser/{name}")
    fun createNewUser(@Named("name") name : String): Response {
        val summonerExists = summonerControl.checkSummonerExists(name)
        if (summonerExists) {
            return Response(349, "")
        }

        val registerSummoner = summonerControl.registerSummoner(name)
        if (!registerSummoner) {
            return Response(404, "Not Found")
        }

        return Response(200, "ok")
    }

    @ApiMethod(name = "syncMatchList",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "syncMatchList/{summonerId}")
    fun syncMatchList(@Named("summonerId") summonerId: Long) : Response {
        val responseCode = sync.syncMatchSummaries(summonerId)
        return Response(responseCode, "")
    }

    @ApiMethod(name = "syncMatch",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "syncMatch/{gameId}/{summonerId}")
    fun syncMatch(@Named("gameId") gameId : Long, @Named("summonerId") summonerId: Long) : Response {
        Logger.getLogger(this::class.java.name).log(Level.INFO, "requested match sync for $gameId", "")
        val result = sync.matchControl.fetchAndSaveMatch(gameId, summonerId)
        if (result) {
            return Response(200, "")
        }

        return Response(500, "")
    }

    @ApiMethod(name = "syncProgress",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "syncProgress/{summonerId}")
    fun syncProgress(@Named("summonerId") summonerId: Long) : Response {
        Logger.getLogger(this::class.java.name).log(Level.INFO, "requested sync progress update for $summonerId", "")
        val result = sync.fetchSyncProgress(summonerId)
        return Response(200, result.toString())
    }

    @ApiMethod(name = "refineStats",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "refineStats/{summonerId}")
    fun refineStats(@Named("summonerId") summonerId: Long) : Response {
        Logger.getLogger(this::class.java.name).log(Level.INFO, "requested stat refinement for $summonerId", "")
        val result = sync.refineStats(summonerId)
        return Response(result, result.toString())
    }


}