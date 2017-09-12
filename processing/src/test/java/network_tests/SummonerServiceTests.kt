package network_tests

import application.region.RegionController
import com.google.gson.GsonBuilder
import db_test.refined_stats.RefinedStatsTests
import network.NetworkInterface
import network.riotapi.MatchServiceApi
import network.riotapi.SummonerService
import org.junit.Before
import org.junit.Test
import retrofit.RestAdapter
import retrofit.converter.GsonConverter
import util.testing.MockClient

/**
 * @author Josiah Kendall
 */
class SummonerServiceTests {

    lateinit var networkInterface : NetworkInterface
    lateinit var summonerServiceApi: SummonerService

    @Before
    fun setUp() {
        networkInterface = NetworkInterface(RegionController())
        summonerServiceApi = networkInterface.getSummonerService()
    }

    fun getMock404Data() : String {
        return "{\n"+
            "\"status\": {\n"+
            "\"status_code\": 404,\n"+
            "\"message\": \"Data not found - summoner not found\"\n"+
        "}"
    }
}
