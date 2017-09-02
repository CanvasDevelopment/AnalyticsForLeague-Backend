package db_test.refined_stats

import db.DBHelper
import db.refined_stats.GameSummaryDAO
import model.refined_stats.GameStageStat
import model.refined_stats.RefinedGeneralGameStageColumnNames
import model.refined_stats.RefinedStatSummary
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.sql.ResultSet
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Josiah Kendall
 */
class GameSummaryDAOTests {

    private val GAME_SUMMARY_TABLE = "gamesummarystats"
    lateinit var dbHelper: DBHelper
    private lateinit var gameSummaryDAO: GameSummaryDAO
    private val random = Random()

    @Before
    fun setUp() {
        dbHelper = DBHelper()
        dbHelper.connect()

        gameSummaryDAO = GameSummaryDAO(dbHelper)
    }

    @After
    fun cleanUp() {
        dbHelper.run {
            executeSQLScript("Delete from $GAME_SUMMARY_TABLE")
            disconnect()
        }
    }

    @Test
    fun gameSummaryCanSaveSummaryStat() {
        val refinedSummaryStat = RefinedStatSummary(
                random.nextLong(),
                random.nextInt(),
                random.nextLong(),
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val result = gameSummaryDAO.insertHeroSummaryStat(refinedSummaryStat)
        Assert.assertTrue(result != (-1).toLong())

        // we dont need a save to produce the summary stat so lets just make it happen with the dbHelper
        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills " +
                "From gamesummarystats " +
                "Where heroSummonerId = ${refinedSummaryStat.summonerId}"
        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForHeroSummaryStat()
            Assert.assertEquals(refinedSummaryStat.summonerId, retrievedSummaryStat.summonerId)
            Assert.assertEquals(refinedSummaryStat.champId, retrievedSummaryStat.champId)
            Assert.assertEquals(refinedSummaryStat.teamBaronKills, retrievedSummaryStat.teamBaronKills)
            Assert.assertEquals(refinedSummaryStat.teamDragonKills, retrievedSummaryStat.teamDragonKills)
            Assert.assertEquals(refinedSummaryStat.teamTowerKills, retrievedSummaryStat.teamTowerKills)
            Assert.assertEquals(refinedSummaryStat.teamRiftHeraldKills, retrievedSummaryStat.teamRiftHeraldKills)
            Assert.assertEquals(refinedSummaryStat.win, retrievedSummaryStat.win)
            Assert.assertEquals(refinedSummaryStat.gameId, retrievedSummaryStat.gameId)
        }
        Assert.assertEquals(count, 1)
    }

    @Test
    fun ensureThatWeCanSaveAnArrayOfHeroSummaryStats() {
        val summonerId = random.nextLong()
        val refinedSummaryStat1 = RefinedStatSummary(
                summonerId,
                random.nextInt(),
                random.nextLong(),
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())
        val refinedSummaryStat2 = RefinedStatSummary(
                summonerId,
                random.nextInt(),
                random.nextLong(),
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())
        val refinedSummaryStat3 = RefinedStatSummary(
                summonerId,
                random.nextInt(),
                random.nextLong(),
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val arrayOfStats = ArrayList<RefinedStatSummary>()
        arrayOfStats.add(refinedSummaryStat1)
        arrayOfStats.add(refinedSummaryStat2)
        arrayOfStats.add(refinedSummaryStat3)

        gameSummaryDAO.saveHeroSummaryStats(summonerId, arrayOfStats)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills " +
                "From gamesummarystats " +
                "Where heroSummonerId = $summonerId"
        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            val refinedSummaryStat = arrayOfStats[count]
            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForHeroSummaryStat()
            Assert.assertEquals(refinedSummaryStat.summonerId, retrievedSummaryStat.summonerId)
            Assert.assertEquals(refinedSummaryStat.champId, retrievedSummaryStat.champId)
            Assert.assertEquals(refinedSummaryStat.teamBaronKills, retrievedSummaryStat.teamBaronKills)
            Assert.assertEquals(refinedSummaryStat.teamDragonKills, retrievedSummaryStat.teamDragonKills)
            Assert.assertEquals(refinedSummaryStat.teamTowerKills, retrievedSummaryStat.teamTowerKills)
            Assert.assertEquals(refinedSummaryStat.teamRiftHeraldKills, retrievedSummaryStat.teamRiftHeraldKills)
            Assert.assertEquals(refinedSummaryStat.win, retrievedSummaryStat.win)
            Assert.assertEquals(refinedSummaryStat.gameId, retrievedSummaryStat.gameId)
        }

        Assert.assertEquals(count, 3)

    }

    @Test
    fun ensureWeCanSaveCreepsScoreStageForHero() {
        val creepsStats = GameStageStat(random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                random.nextLong())

        val statNames = 
    }

    private fun ResultSet.produceTestResultForHeroSummaryStat(): RefinedStatSummary {
        return RefinedStatSummary(
                getLong("heroSummonerId"),
                getInt("heroChampId"),
                getLong("gameId"),
                getInt("heroTeamId"),
                getBoolean("heroWin"),
                getInt("heroTeamTowerKills"),
                getInt("HeroTeamDragonKills"),
                getInt("heroTeamRiftHeraldKills"),
                getInt("heroTeamBaronKills"))
    }

}

