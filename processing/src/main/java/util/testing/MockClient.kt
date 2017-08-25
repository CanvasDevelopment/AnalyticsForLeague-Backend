package util.testing

import retrofit.client.Client
import retrofit.client.Request
import retrofit.client.Response
import retrofit.mime.TypedByteArray
import java.io.FileInputStream
import java.util.*

/**
 * @author Josiah Kendall
 *
 * A Mock [Client] class, used to handle requests to the server, parse them and return mock responses. Common use case
 * is to load data from disk (pre saved JSON files), and then return them. This allows us to constantly test against
 * the same numbers, with real world data. Use [body] to produce the mock JSON response that you want to use for your test.
 */
class MockClient(val body: () -> String) : Client {

    override fun execute(request: Request?): Response {
        val stringResponse : String = body()


        return Response(request?.url,
                200,
                "nothing",
                Collections.emptyList(),
                TypedByteArray("application/json", stringResponse.toByteArray(charset("UTF-8"))))

    }

}