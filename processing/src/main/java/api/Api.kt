package api

import application.Sync
import application.domain.MatchControl
import application.domain.SummonerControl
import com.github.salomonbrys.kodein.instance
import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import di.KodeinManager
import model.beans.Response

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
            path = "syncMatch/{gameId}")
    fun syncMatch(@Named("gameId") gameId : Long) : Response {
        val result = sync.matchControl.fetchAndSaveMatch(gameId);
        if (result) {
            return Response(200, "")
        }

        return Response(500, "")
    }
}