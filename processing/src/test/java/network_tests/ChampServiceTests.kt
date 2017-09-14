package network_tests

import com.github.salomonbrys.kodein.instance
import com.google.gson.GsonBuilder
import db.DBHelper
import db.match.MatchDAO
import db.refined_stats.RefinedStatsDAO
import di.KodeinManager
import model.champion.ChampList
import network.riotapi.ChampionService
import network.riotapi.MatchServiceApi
import org.junit.BeforeClass
import org.junit.Test
import retrofit.RestAdapter
import retrofit.converter.GsonConverter
import util.Tables
import util.testing.MockClient
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * @author Josiah Kendall
 */
class ChampServiceTests {

    companion object {
        lateinit var dbHelper: DBHelper
        private val km = KodeinManager()
        lateinit var champService : ChampionService

        @BeforeClass
        @JvmStatic
        fun doSetUp() {
            dbHelper = km.kodein.instance()
            dbHelper.connect()
            // wipe our database.
            val tables = Tables()

            val mockClient = MockClient({ -> getListData() })
            val adapterBuilder: RestAdapter.Builder = RestAdapter.Builder()
                    .setEndpoint("https://oc1.api.riotgames.com")
                    .setConverter(GsonConverter(GsonBuilder().create()))
                    .setClient(mockClient)
            val serviceAdapter = adapterBuilder.build()!!
            champService = serviceAdapter.create(ChampionService::class.java)

        }


        @JvmStatic
        private fun getListData(): String {
            val inputs = FileInputStream("C:\\Users\\jek40\\Desktop\\analyticsforleague.com\\afl_backend\\processing\\src\\main\\resources\\json\\champList.json")
            val buf = BufferedReader(InputStreamReader(inputs))
            var line = buf.readLine()
            val sb = StringBuilder()
            while (line != null) {
                sb.append(line).append("\n")
                line = buf.readLine()
            }

            return sb.toString()
        }
    }

    @Test
    fun `Test that we can fetch champ list`() {
        val champList = champService.fetchChampList("")
        assert(champList.champions[0].id == 266 )
    }


}