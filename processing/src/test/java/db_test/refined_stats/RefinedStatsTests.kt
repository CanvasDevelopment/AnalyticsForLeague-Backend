package db_test.refined_stats

import com.github.salomonbrys.kodein.instance
import com.google.gson.GsonBuilder
import db.DBHelper
import db.match.MatchDAO
import db.refined_stats.RefinedStatsDAO
import di.KodeinManager
import network.riotapi.MatchService
import org.junit.Assert
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
        private val km = KodeinManager()

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

            return sb.toString()
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

            return sb.toString()
        }
    }
    private val tables = Tables()

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
    fun ensureThatWeCanFetchCreepsDiffPerMinRefinedStatListForTop() {
        val stats = refinedStatsDAO.fetchGameStageStatListForHero(tables.CS_DIFF_PER_MIN,
                1542360,
                "SOLO",
                "TOP")

        Assert.assertEquals(stats.size, 2)
        Assert.assertEquals(stats[0].gameId, 161346846)
        Assert.assertEquals(stats[0].earlyGame, -17.999999523162842f, 0.05f)
        Assert.assertEquals(stats[0].midGame, -12.999999523162842f, 0.05f)
        Assert.assertEquals(stats[0].lateGame, 20f, 0.05f)
        Assert.assertEquals(stats[1].gameId, 161345363)
        Assert.assertEquals(stats[1].earlyGame, 6.9999998807907104f, 0.05f)
        Assert.assertEquals(stats[1].midGame, 25f, 0.05f)
        Assert.assertEquals(stats[1].lateGame, 11.000000238418579f, 0.05f)
    }

    @Test
    fun ensureThatWeCanFetchCreepsPerMinRefinedStatListForTop() {
        val stats = refinedStatsDAO.fetchGameStageStatListForHero(tables.CREEPS_PER_MIN,
                1542360,
                "SOLO",
                "TOP")

        Assert.assertEquals(stats.size, 2)
        Assert.assertEquals(stats[0].gameId, 161346846)
        Assert.assertEquals(stats[0].earlyGame, 45.999999046325684f, 0.05f)
        Assert.assertEquals(stats[0].midGame, 74.00000095367432f, 0.05f)
        Assert.assertEquals(stats[0].lateGame, 86.99999809265137f, 0.05f)
        Assert.assertEquals(stats[1].gameId, 161345363)
        Assert.assertEquals(stats[1].earlyGame, 53.00000190734863f, 0.05f)
        Assert.assertEquals(stats[1].midGame, 86.00000381469727f, 0.05f)
        Assert.assertEquals(stats[1].lateGame, 40f, 0.05f)
    }

    @Test
    fun ensureThatWeCanFetchGoldPerMinRefinedStatListForTop() {
        val stats = refinedStatsDAO.fetchGameStageStatListForHero(tables.GOLD_PER_MIN,
                1542360,
                "SOLO",
                "TOP")

        Assert.assertEquals(stats.size, 2)
        Assert.assertEquals(stats[0].gameId, 161346846)
        Assert.assertEquals(stats[0].earlyGame, 1916.9999694824219f, 0.05f)
        Assert.assertEquals(stats[0].midGame, 3008.9999389648438f, 0.05f)
        Assert.assertEquals(stats[0].lateGame, 4037.0001220703125f, 0.05f)
        Assert.assertEquals(stats[1].gameId, 161345363)
        Assert.assertEquals(stats[1].earlyGame, 2341.999969482422f, 0.05f)
        Assert.assertEquals(stats[1].midGame, 4247.0001220703125f, 0.05f)
        Assert.assertEquals(stats[1].lateGame, 3753.9999389648438f, 0.05f)
    }

    @Test
    fun ensureThatWeCanFetchDamageTakenRefinedStatListForTop() {
        val stats = refinedStatsDAO.fetchGameStageStatListForHero(
                tables.DAMAGE_TAKEN_PER_MIN,
                1542360,
                "SOLO",
                "TOP")

        Assert.assertEquals(stats.size, 2)
        Assert.assertEquals(stats[0].gameId, 161346846)
        Assert.assertEquals(stats[0].earlyGame, 5085f, 0.05f)
        Assert.assertEquals(stats[0].midGame, 9730f, 0.05f)
        Assert.assertEquals(stats[0].lateGame, 16000.999755859375f, 0.05f)
        Assert.assertEquals(stats[1].gameId, 161345363)
        Assert.assertEquals(stats[1].earlyGame, 5529.000244140625f, 0.05f)
        Assert.assertEquals(stats[1].midGame, 11333.00048828125f, 0.05f)
        Assert.assertEquals(stats[1].lateGame, 15704.000244140625f, 0.05f)
    }

    @Test
    fun ensureThatWeCanFetchGoldScoreForEnemyGameStages() {
        val stats = refinedStatsDAO.fetchGameStageStatListForVillian(
                tables.GOLD_PER_MIN,
                1542360,
                "SOLO",
                "TOP")

        Assert.assertEquals(stats.size, 2)
        Assert.assertEquals(stats[0].gameId, 161346846)
        Assert.assertEquals(stats[0].earlyGame, 2285f, 0.05f)
        Assert.assertEquals(stats[0].midGame, 4177.9998779296875f, 0.05f)
        Assert.assertEquals(stats[0].lateGame, 4612.9998779296875f, 0.05f)
        Assert.assertEquals(stats[1].gameId, 161345363)
        Assert.assertEquals(stats[1].earlyGame, 2283.000030517578f, 0.05f)
        Assert.assertEquals(stats[1].midGame, 3881.0000610351562f, 0.05f)
        Assert.assertEquals(stats[1].lateGame, 2518.9999389648438f, 0.05f)
    }

    @Test
    fun ensureThatWeCanGetStatsTableStatsForHeroInRoleTOP() {
        val stats = refinedStatsDAO.fetchPlayerStatisticsForHero(1542360,
                "SOLO",
                "TOP")

        Assert.assertEquals(stats.size, 2)
        Assert.assertEquals(stats[0].gameId, 161346846)
        Assert.assertEquals(stats[0].kills, 0)
        Assert.assertEquals(stats[0].deaths, 5)
        Assert.assertEquals(stats[0].assists, 2)
        Assert.assertEquals(stats[0].wardsPlaced, 11)
        Assert.assertEquals(stats[0].wardsKilled, 1)
        Assert.assertEquals(stats[1].gameId, 161345363)
        Assert.assertEquals(stats[1].kills, 2)
        Assert.assertEquals(stats[1].deaths, 5)
        Assert.assertEquals(stats[1].assists, 7)
        Assert.assertEquals(stats[1].wardsPlaced, 12)
        Assert.assertEquals(stats[1].wardsKilled, 0)
    }

    @Test
    fun ensureThatWeCanGetStatsTableStatsForHeroInRoleJUNGLE() {
        val stats = refinedStatsDAO.fetchPlayerStatisticsForHero(1542360,
                "NONE",
                "JUNGLE")

        Assert.assertEquals(stats.size, 13)
        Assert.assertEquals(stats[0].gameId, 161736353)
        Assert.assertEquals(stats[0].kills, 13)
        Assert.assertEquals(stats[0].deaths, 5)
        Assert.assertEquals(stats[0].assists, 10)
        Assert.assertEquals(stats[0].wardsPlaced, 14)
        Assert.assertEquals(stats[0].wardsKilled, 1)
    }

    @Test
    fun ensureThatWeCanGetStatsTableStatsForEnemyWhenWePlayingTopAndTheyPlayingTop() {
        val stats = refinedStatsDAO.fetchPlayerStatisticsForVillian(1542360,
                "SOLO",
                "TOP")

        Assert.assertEquals(stats.size, 2)
        Assert.assertEquals(stats[0].gameId, 161346846)
        Assert.assertEquals(stats[0].kills, 6)
        Assert.assertEquals(stats[0].deaths, 2)
        Assert.assertEquals(stats[0].assists, 1)
        Assert.assertEquals(stats[0].wardsPlaced, 10)
        Assert.assertEquals(stats[0].wardsKilled, 2)
    }

    @Test
    fun ensureThatWeCanGetStatsTableStatsForEnemyWhenWePlayingJungleAndTheyPlayingJungle() {
        val stats = refinedStatsDAO.fetchPlayerStatisticsForVillian(1542360,
                "NONE",
                "JUNGLE")

        Assert.assertEquals(stats.size, 13)
        Assert.assertEquals(stats[0].gameId, 161736353)
        Assert.assertEquals(stats[0].kills, 4)
        Assert.assertEquals(stats[0].deaths, 14)
        Assert.assertEquals(stats[0].assists, 4)
        Assert.assertEquals(stats[0].wardsPlaced, 12)
        Assert.assertEquals(stats[0].wardsKilled, 1)
    }
}