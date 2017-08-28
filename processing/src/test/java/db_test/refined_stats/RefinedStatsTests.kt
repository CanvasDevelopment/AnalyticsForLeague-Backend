package db_test.refined_stats

import com.github.salomonbrys.kodein.instance
import com.google.gson.GsonBuilder
import db.DBHelper
import db.match.MatchDAO
import db.refined_stats.RefinedStatsDAO
import di.KodeinManager
import model.positions.*
import model.refined_stats.RefinedStatSummary
import network.riotapi.MatchService
import org.junit.Assert
import org.junit.Before
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
class RefinedStatsTests {

    companion object {
        lateinit var dbHelper: DBHelper
        lateinit var refinedStatsDAO: RefinedStatsDAO
        lateinit var matchDAO: MatchDAO
        val km = KodeinManager()
        init {

        }

        @BeforeClass
        @JvmStatic
        fun doSetUp() {
            dbHelper = km.kodein.instance()
            dbHelper.connect()
            matchDAO = km.kodein.instance()
            refinedStatsDAO = km.kodein.instance()
            // wipe our database.
            val tables = Tables()
            dbHelper.executeSQLScript("DELETE FROM matchtable")
            dbHelper.executeSQLScript("DELETE FROM mastery")
            dbHelper.executeSQLScript("DELETE FROM participantIdentity")
            dbHelper.executeSQLScript("DELETE FROM participant")
            dbHelper.executeSQLScript("DELETE FROM ban")
            dbHelper.executeSQLScript("DELETE FROM team")
            dbHelper.executeSQLScript("DELETE FROM ${tables.CS_DIFF_PER_MIN}")
            dbHelper.executeSQLScript("DELETE FROM ${tables.XP_PER_MIN}")
            dbHelper.executeSQLScript("DELETE FROM ${tables.XP_DIFF_PER_MIN}")
            dbHelper.executeSQLScript("DELETE FROM ${tables.DAMAGE_TAKEN_PER_MIN}")
            dbHelper.executeSQLScript("DELETE FROM ${tables.DAMAGE_TAKEN_DIFF_PER_MIN}")
            dbHelper.executeSQLScript("DELETE FROM ${tables.CREEPS_PER_MIN}")
            dbHelper.executeSQLScript("DELETE FROM ${tables.GOLD_PER_MIN}")
            dbHelper.executeSQLScript("DELETE FROM player")
            dbHelper.executeSQLScript("DELETE FROM rune")
            dbHelper.executeSQLScript("DELETE FROM stats")
            dbHelper.executeSQLScript("DELETE FROM timeline")

            var mockClient = MockClient({ -> getListData() })
            val adapterBuilder: RestAdapter.Builder = RestAdapter.Builder()
                    .setEndpoint("https://oc1.api.riotgames.com")
                    .setConverter(GsonConverter(GsonBuilder().create()))
                    .setClient(mockClient)
            var serviceAdapter = adapterBuilder.build()!!
            val matchService = serviceAdapter.create<MatchService>(MatchService::class.java)
            val result = matchService.getMatchListForAccount("", 1)
            for (match in result.matches) {
                mockClient = MockClient { getMatchData(match.gameId) }
                adapterBuilder.setClient(mockClient)
                serviceAdapter = adapterBuilder.build()
                val matchService2 = serviceAdapter.create<MatchService>(MatchService::class.java)
                val match2 = matchService2.getMatchByMatchId("ds", -1)

                matchDAO.saveMatch(match2) // todo implement some sort of version of this into the code

            }
        }

        @JvmStatic
        private fun getMatchData(gameId: Long): String {

            val inputs = FileInputStream("C:\\Users\\jek40\\Desktop\\analyticsforleague.com\\afl_backend\\processing\\src\\main\\resources\\json\\$gameId.json")
            val buf = BufferedReader(InputStreamReader(inputs))
            var line = buf.readLine()
            val sb = StringBuilder()
            while (line != null) {
                sb.append(line).append("\n")
                line = buf.readLine()
            }
            val fileAsString = sb.toString()

            return fileAsString
        }

        @JvmStatic
        private fun getListData(): String {
            val inputs = FileInputStream("C:\\Users\\jek40\\Desktop\\analyticsforleague.com\\afl_backend\\processing\\src\\main\\resources\\json\\recent.json")
            val buf = BufferedReader(InputStreamReader(inputs))
            var line = buf.readLine()
            val sb = StringBuilder()
            while (line != null) {
                sb.append(line).append("\n")
                line = buf.readLine()
            }
            val fileAsString = sb.toString()

            return fileAsString
        }
    }


    val tables = Tables()
    val jungle = Jungle()
    val top = Top()
    val mid = Middle()
    val adc = Marksman()
    val sup = Support()

    @Test
    fun ensureThatWeCanGetCreepsPerMinEarlyMidAndLateGameCorrectlyForJungle() {
        // trigger 'fetch refined stats'
        val gameStageRefinedStats = refinedStatsDAO.fetchGameStageStatAverageForHero(tables.CREEPS_PER_MIN,
                1542360,
                jungle.role,
                jungle.lane)
        Assert.assertEquals(gameStageRefinedStats.earlyGame, 2.1538461515536675f, 0.005f)
        Assert.assertEquals(gameStageRefinedStats.midGame, 16.38461553133451f, 0.005f)
        Assert.assertEquals(gameStageRefinedStats.lateGame, 32.69230824250442f, 0.005f)
    }

    @Test
    fun ensureThatWeCanGetCreepsPerMinEarlyMidAndLateGameCorrectlyForMid() {
        // trigger 'fetch refined stats'
        val gameStageRefinedStats = refinedStatsDAO.fetchGameStageStatAverageForHero(tables.CREEPS_PER_MIN,
                1542360,
                jungle.role,
                jungle.lane)
        Assert.assertEquals(gameStageRefinedStats.earlyGame, 2.1538461515536675f, 0.005f)
        Assert.assertEquals(gameStageRefinedStats.midGame, 16.38461553133451f, 0.005f)
        Assert.assertEquals(gameStageRefinedStats.lateGame, 32.69230824250442f, 0.005f)
    }

    @Test
    fun ensureThatWeCanGetCreepsPerMinEarlyMidAndLateGameCorrectlyForSupport() {
        // trigger 'fetch refined stats'
        val gameStageRefinedStats = refinedStatsDAO.fetchGameStageStatAverageForHero(tables.CREEPS_PER_MIN,
                1542360,
                jungle.role,
                jungle.lane)
        Assert.assertEquals(gameStageRefinedStats.earlyGame, 2.1538461515536675f, 0.005f)
        Assert.assertEquals(gameStageRefinedStats.midGame, 16.38461553133451f, 0.005f)
        Assert.assertEquals(gameStageRefinedStats.lateGame, 32.69230824250442f, 0.005f)
    }

    @Test
    fun ensureThatWeCanGetCreepsPerMinEarlyMidAndLateGameCorrectlyForADC() {
        // trigger 'fetch refined stats'
        val gameStageRefinedStats = refinedStatsDAO.fetchGameStageStatAverageForHero(tables.CREEPS_PER_MIN,
                1542360,
                jungle.role,
                jungle.lane)
        Assert.assertEquals(gameStageRefinedStats.earlyGame, 2.1538461515536675f, 0.005f)
        Assert.assertEquals(gameStageRefinedStats.midGame, 16.38461553133451f, 0.005f)
        Assert.assertEquals(gameStageRefinedStats.lateGame, 32.69230824250442f, 0.005f)
    }

    @Test
    fun ensureThatWeCanGetCreepsDiffPerMinEarlyMidAndLateGameCorrectlyForJungle() {
        // trigger 'fetch refined stats'
        val gameStageRefinedStats = refinedStatsDAO.fetchGameStageStatAverageForHero(tables.CS_DIFF_PER_MIN,
                1542360,
                jungle.role,
                jungle.lane)
        Assert.assertEquals(gameStageRefinedStats.earlyGame, -2.91666670391957f, 0.005f)
        Assert.assertEquals(gameStageRefinedStats.midGame, 5.166666656732559f, 0.005f)
        Assert.assertEquals(gameStageRefinedStats.lateGame, 10.999999940395355f, 0.005f)
    }

    @Test
    fun ensureThatWeFetchSummaryStatsCorrectlyForHero() {
        val summaryStats = refinedStatsDAO.fetchGameSummaryStatsForHero(
                1542360,
                "SOLO",
                "TOP")

        Assert.assertEquals(summaryStats.size, 2)
        Assert.assertEquals(summaryStats[0].champId, 27)
        Assert.assertEquals(summaryStats[1].champId, 27)
        Assert.assertEquals(summaryStats[0].gameId, 161346846)
        Assert.assertEquals(summaryStats[1].gameId, 161345363)
        Assert.assertEquals(summaryStats[0].teamId, 100)
        Assert.assertEquals(summaryStats[1].teamId, 200)
        Assert.assertEquals(summaryStats[0].win, true)
        Assert.assertEquals(summaryStats[1].win, true)
        Assert.assertEquals(summaryStats[0].teamTowerKills, 7)
        Assert.assertEquals(summaryStats[1].teamTowerKills, 11)
        Assert.assertEquals(summaryStats[0].teamDragonKills, 3)
        Assert.assertEquals(summaryStats[1].teamDragonKills, 3)
        Assert.assertEquals(summaryStats[0].teamRiftHeraldKills, 0)
        Assert.assertEquals(summaryStats[1].teamRiftHeraldKills, 0)
    }

    @Test
    fun ensureThatWeFetchSummaryStatsCorrectlyForEnemy() {
        val summaryStats = refinedStatsDAO.fetchGameSummaryStatsForVillan(
                1542360,
                "SOLO",
                "TOP")

        Assert.assertEquals(summaryStats.size, 2)
        Assert.assertEquals(summaryStats[0].champId, 13)
        Assert.assertEquals(summaryStats[1].champId, 35)
        Assert.assertEquals(summaryStats[0].gameId, 161346846)
        Assert.assertEquals(summaryStats[1].gameId, 161345363)
        Assert.assertEquals(summaryStats[0].teamId, 200)
        Assert.assertEquals(summaryStats[1].teamId, 100)
        Assert.assertEquals(summaryStats[0].win, false)
        Assert.assertEquals(summaryStats[1].win, false)
        Assert.assertEquals(summaryStats[0].teamTowerKills, 3)
        Assert.assertEquals(summaryStats[1].teamTowerKills, 0)
        Assert.assertEquals(summaryStats[0].teamDragonKills, 1)
        Assert.assertEquals(summaryStats[1].teamDragonKills, 0)
        Assert.assertEquals(summaryStats[0].teamRiftHeraldKills, 0)
        Assert.assertEquals(summaryStats[1].teamRiftHeraldKills, 0)
    }

    @Test
    fun ensureThatWeCanFetch

    @Test
    fun ensureThatWeFetchTheCorrectAmountOfRows_ForTopSolo_With_AnyChamp() {

    }
}