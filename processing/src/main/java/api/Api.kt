package api

import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
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
class Api() {

    @ApiMethod(name = "sayHi")
    fun sayHi() : Response {
        return Response(200, "HI!")
    }
}