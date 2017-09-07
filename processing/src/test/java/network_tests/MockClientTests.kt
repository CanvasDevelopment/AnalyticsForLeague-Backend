package network_tests

import com.google.gson.GsonBuilder
import network.riotapi.MatchServiceApi
import org.junit.Assert
import org.junit.Test
import retrofit.RestAdapter
import retrofit.converter.GsonConverter
import util.testing.MockClient
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * @author Josiah Kendall
 */
class MockClientTests {
    val testString = "dsfdsfdsfdsfds"
    @Test
    fun ensureMockClientReturnsCorrectly() {
        val mockClient = MockClient({-> getData()})
        val adapterBuilder : RestAdapter.Builder = RestAdapter.Builder()
                .setEndpoint("https://oc1.api.riotgames.com")
                .setConverter(GsonConverter(GsonBuilder().create()))
                .setClient(mockClient)
        val serviceAdapter = adapterBuilder.build()!!
        val matchService = serviceAdapter.create<MatchServiceApi>(MatchServiceApi::class.java)
        val result = matchService.getMatchByMatchId("",1)
        Assert.assertEquals(result.gameId,161340788 )
    }

    fun getData() : String {

        val inputs = FileInputStream("C:\\Users\\jek40\\Desktop\\analyticsforleague.com\\afl_backend\\processing\\src\\main\\resources\\json\\161340788.json")
        val buf = BufferedReader(InputStreamReader(inputs))
        var line = buf.readLine()
        val sb = StringBuilder()
        while(line != null){
            sb.append(line).append("\n")
            line = buf.readLine()
        }
        val fileAsString = sb.toString()


        return fileAsString
    }
}