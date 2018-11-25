package api.user

import com.github.salomonbrys.kodein.instance
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.Named
import di.KodeinManager_api
import model.Response

/**
 * @author Josiah Kendall
 */
//@Api(name = "summoner",
//        version = "v1",
//        namespace = ApiNamespace(ownerDomain = "com.analyticsforleague",
//        ownerName = "com.analyticsforleague",
//        packagePath = "")
//)
class UserApi {

    private val km = KodeinManager_api()
    private val presenter: UserPresenter = km.kodein.instance()

    @ApiMethod(name = "doesUserExist",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "doesUserExist/{userName}")
    fun doesUserExist(@Named("userName") userName : String) : String {
        if (presenter.checkIfUserExists(userName)) {
            return "200"
        }

        return "404"
    }

    fun registerUser(userName: String) : String {
        return ""
    }

    @ApiMethod(name = "sayHello",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "sayHello/{name}")
    fun sayHello(@Named("name") name : String) : Response<String> {
        return Response(200, "Hello $name")
    }
}