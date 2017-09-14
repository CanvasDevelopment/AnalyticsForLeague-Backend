package network_tests

import com.github.salomonbrys.kodein.instance
import com.google.gson.GsonBuilder
import db.DBHelper
import di.KodeinManager
import network.riotapi.ChampionService
import org.junit.BeforeClass
import org.junit.Test
import retrofit.RestAdapter
import retrofit.appengine.UrlFetchClient
import retrofit.client.UrlConnectionClient
import retrofit.converter.GsonConverter
import util.RIOT_API_KEY
import util.Tables
import util.testing.MockClient
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * @author Josiah Kendall
 */
class ChampServiceTests {


    lateinit var dbHelper: DBHelper
    private val km = KodeinManager()
    lateinit var champService : ChampionService

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

    private fun getChampData() : String {
        val inputs = FileInputStream("C:\\Users\\jek40\\Desktop\\analyticsforleague.com\\afl_backend\\processing\\src\\main\\resources\\json\\champ.json")
        val buf = BufferedReader(InputStreamReader(inputs))
        var line = buf.readLine()
        val sb = StringBuilder()
        while (line != null) {
            sb.append(line).append("\n")
            line = buf.readLine()
        }

        return sb.toString()
    }

    @Test
    fun `Test that we can fetch champ list`() {
        val mockClient = MockClient({ -> getListData() })
        val adapterBuilder: RestAdapter.Builder = RestAdapter.Builder()
                .setEndpoint("https://oc1.api.riotgames.com")
                .setConverter(GsonConverter(GsonBuilder().create()))
                .setClient(mockClient)
        val serviceAdapter = adapterBuilder.build()!!
        champService = serviceAdapter.create(ChampionService::class.java)
        val champList = champService.fetchChampList("")
        assert(champList.champions[0].id == 266 )
    }

    @Test
    fun `Test that we can fetch champ by id`() {
        val mockClient = MockClient({ -> getChampData() })
        val adapterBuilder: RestAdapter.Builder = RestAdapter.Builder()
                .setEndpoint("https://oc1.api.riotgames.com")
                .setConverter(GsonConverter(GsonBuilder().create()))
                .setClient(mockClient)
        val serviceAdapter = adapterBuilder.build()!!
        champService = serviceAdapter.create(ChampionService::class.java)
        val champ = champService.getChampById(62, "image", "key")
        assert(champ.id == 62)
        assert(champ.name == "Wukong")
        assert(champ.title == "the Monkey King")
        assert(champ.key == "MonkeyKing")
        assert(champ.image.full == "MonkeyKing.png")
        assert(champ.image.sprite == "champion2.png")
        assert(champ.image.group == "champion")
        assert(champ.image.x == 48)
        assert(champ.image.y == 48)
        assert(champ.image.w == 48)
        assert(champ.image.h == 48)
    }

    @Test
    fun `Test that we can fetch champ by id over network (This relies on valid API_KEY)`() {
        val adapterBuilder: RestAdapter.Builder = RestAdapter.Builder()
                .setEndpoint("https://oc1.api.riotgames.com")
                .setConverter(GsonConverter(GsonBuilder().create()))
                .setClient(UrlConnectionClient())
        val serviceAdapter = adapterBuilder.build()!!
        champService = serviceAdapter.create(ChampionService::class.java)
        val champ = champService.getChampById(62, "image", RIOT_API_KEY)
        assert(champ.id == 62)
        assert(champ.name == "Wukong")
        assert(champ.title == "the Monkey King")
        assert(champ.key == "MonkeyKing")
        assert(champ.image.full == "MonkeyKing.png")
        assert(champ.image.sprite == "champion2.png")
        assert(champ.image.group == "champion")
        assert(champ.image.x == 48)
        assert(champ.image.y == 48)
        assert(champ.image.w == 48)
        assert(champ.image.h == 48)
    }

    @Test
    fun `Test that we can fetch champ list from riot Api`() {
        val adapterBuilder: RestAdapter.Builder = RestAdapter.Builder()
                .setEndpoint("https://oc1.api.riotgames.com")
                .setConverter(GsonConverter(GsonBuilder().create()))
                .setClient(UrlConnectionClient())
        val serviceAdapter = adapterBuilder.build()!!
        champService = serviceAdapter.create(ChampionService::class.java)
        val champList = champService.fetchChampList(RIOT_API_KEY)
        assert(champList.champions[0].id == 266 )
    }



}