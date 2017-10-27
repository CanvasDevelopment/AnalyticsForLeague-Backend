package api

import application.Sync
import application.domain.MatchControl
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

    val sync = km.kodein.instance<Sync>()

    @ApiMethod(name = "damagePerMinuteDeltasDetail",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "syncUser/{summonerId}")
    fun syncUser(@Named("summonerId")summonerId : Long): Int {
        return sync.syncMatches(summonerId)
    }

    fun createNewUser(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    @ApiMethod(name = "sayHi")
    fun sayHi() : Response {
        return Response(200, "HI!")
    }

}