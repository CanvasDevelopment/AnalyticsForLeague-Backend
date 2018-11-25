package api

import com.github.salomonbrys.kodein.instance
import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import di.KodeinManager_api
import model.Response
import service_contracts.ProcessingImpl

/**
 * @author Josiah Kendall
 */
@Api(name = "match",
        version = "v1",
        namespace = ApiNamespace(ownerDomain = "com.analyticsforleague",
                ownerName = "com.analyticsforleague",
                packagePath = "")
)
class MatchApi {
    private val km = KodeinManager_api()
    private val processingApi = km.kodein.instance<ProcessingImpl>()

    @ApiMethod(name = "isSummonerRegistered",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "syncMatch/{matchId}/{summonerId}")
    fun syncMatch(@Named("matchId")matchId : Long, @Named("summonerId") summonerId : Long) : Response<Boolean> {
        processingApi.syncMatchProxy(matchId, summonerId)
        return Response(200, true)
    }

}
