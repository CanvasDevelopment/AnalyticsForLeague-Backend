package api.match

import com.github.salomonbrys.kodein.instance
import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import database.match.model.MatchIdentifier
import di.KodeinManager_api
import model.MatchPerformanceDetails
import model.MatchSummary
import model.Response
import service_contracts.ProcessingImpl

/**
 * @author Josiah Kendall
 */
@Api(name = "match",
        version = "v1",
        namespace = ApiNamespace(ownerDomain = "com.analyticsforleague",
                ownerName = "com.analyticsforleague",
                packagePath = ""))
class Match {

    private val km = KodeinManager_api()
    private val matchController = km.kodein.instance<MatchController>()
    private val processingImpl = km.kodein.instance<ProcessingImpl>()

    @ApiMethod(name = "loadTwentyMatchIdsWithRole",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "loadTwentyMatchIds/{role}/{startingPoint}/{summonerId}")
    fun loadTwentyMatchIdsWithRole(@Named("role") role : Int,
                           @Named("startingPoint") startingPoint : Int,
                           @Named("summonerId") summonerId : String) : Response<ArrayList<MatchIdentifier>> {
        return Response(200, matchController.loadTwentyMatchIds(role,startingPoint,summonerId))
    }

    @ApiMethod(name = "loadTwentyMatchIdsWithRoleAndChamp",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "loadTwentyMatchIds/{role}/{startingPoint}/{summonerId}/{champId}")
    fun loadTwentyMatchIdsWithRoleAndChamp(@Named("role") role : Int,
                           @Named("startingPoint") startingPoint : Int,
                           @Named("summonerId") summonerId : String,
                           @Named("champId") champId : Int) : Response<ArrayList<MatchIdentifier>> {
        return Response(200, matchController.loadTwentyMatchIds(role,startingPoint,summonerId, champId))
    }

    @ApiMethod(name = "loadTwentyMatchIds",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "loadTwentyMatchIds/{startingPoint}/{summonerId}")
    fun loadTwentyMatchIds(@Named("startingPoint") startingPoint : Int,
                           @Named("summonerId") summonerId : String) : Response<ArrayList<MatchIdentifier>> {
        return Response(200, matchController.loadTwentyMatchIds(startingPoint,summonerId))
    }

    @ApiMethod(name = "loadMatchSummary",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "loadMatchSummary/{role}/{matchId}/{summonerId}")
    fun loadMatchSummary(@Named("role") role : Int,
                         @Named("matchId") matchId : Long,
                         @Named("summonerId") summonerId : String) : Response<MatchSummary> {
        return Response(200, matchController.loadMatchSummary(role,matchId, summonerId))
    }

    @ApiMethod(name = "loadMatchDetails",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "loadMatchDetails/{role}/{matchId}/{summonerId}")
    fun loadMatchDetails(@Named("role") role: Int,
                         @Named("matchId") matchId: Long,
                         @Named("summonerId") summonerId : String) : Response<MatchPerformanceDetails> {
        return Response(200, matchController.loadMatchPerformanceSummary(role,matchId,summonerId))
    }

    @ApiMethod(name = "syncMatch",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "syncMatch/{matchId}/{summonerId}")
    fun syncMatch(@Named("matchId") gameId : Long,
                         @Named("summonerId") summonerId: String) : Response<String> {
        processingImpl.syncMatchProxy(gameId,summonerId)
        return Response(200, "")
    }
}