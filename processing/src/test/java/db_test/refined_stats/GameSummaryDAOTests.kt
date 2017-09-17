package db_test.refined_stats

import db.requests.DBHelper
import db.refined_stats.GameSummaryDAO
import model.refined_stats.FullGameStat
import model.refined_stats.GameStageStat
import model.refined_stats.RefinedGeneralGameStageColumnNames
import model.refined_stats.HeroTeamSummaryStat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import util.columnnames.StaticColumnNames
import java.sql.ResultSet
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Josiah Kendall
 */
class GameSummaryDAOTests {

    private val GAME_SUMMARY_TABLE = "top_summaryStats"
    private val tableName = "top"
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
        val refinedSummaryStat = HeroTeamSummaryStat(
                random.nextLong(),
                random.nextInt(),
                random.nextLong(),
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val result = gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat, tableName)
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
                "From $GAME_SUMMARY_TABLE " +
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
    fun gameSummaryCanSaveSummaryStatForVillan() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val refinedSummaryStat2 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val heroResult = gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat2, tableName)
        Assert.assertTrue(heroResult!= (-1).toLong())
        val result = gameSummaryDAO.saveVillanTeamSummaryStat(summonerId, refinedSummaryStat,tableName)
        Assert.assertTrue(result != (-1).toLong())

        // we dont need a save to produce the summary stat so lets just make it happen with the dbHelper
        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "villanChampId,\n" +
                "villanTeamId,\n" +
                "villanWin,\n" +
                "villanTeamTowerKills,\n" +
                "villanTeamDragonKills,\n" +
                "villanTeamRiftHeraldKills,\n" +
                "villanTeamBaronKills " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = ${refinedSummaryStat.summonerId}"
        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForVillanGameStageStat()
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
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                random.nextLong(),
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())
        val refinedSummaryStat2 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                random.nextLong(),
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())
        val refinedSummaryStat3 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                random.nextLong(),
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val arrayOfStats = ArrayList<HeroTeamSummaryStat>()
        arrayOfStats.add(refinedSummaryStat1)
        arrayOfStats.add(refinedSummaryStat2)
        arrayOfStats.add(refinedSummaryStat3)

        gameSummaryDAO.saveHeroTeamSummaryStats(summonerId, arrayOfStats,tableName)

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
                "From $GAME_SUMMARY_TABLE " +
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
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val creepsStats = GameStageStat(random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                gameId)

        val staticColumnNames = StaticColumnNames()
        val statNames = RefinedGeneralGameStageColumnNames(
                staticColumnNames.HERO_CREEPS_EARLY_GAME,
                staticColumnNames.HERO_CREEPS_MID_GAME,
                staticColumnNames.HERO_CREEPS_LATE_GAME)

        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.saveGameStageStat(summonerId,creepsStats,statNames,tableName)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills," +
                "${staticColumnNames.HERO_CREEPS_EARLY_GAME}," +
                "${staticColumnNames.HERO_CREEPS_MID_GAME}," +
                "${staticColumnNames.HERO_CREEPS_LATE_GAME} " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = $summonerId"

        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {

            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForGameStageStat(statNames)
            Assert.assertEquals(creepsStats.gameId, retrievedSummaryStat.gameId)
            Assert.assertEquals(creepsStats.earlyGame, retrievedSummaryStat.earlyGame, 0.005f)
            Assert.assertEquals(creepsStats.midGame, retrievedSummaryStat.midGame, 0.005f)
            Assert.assertEquals(creepsStats.lateGame, retrievedSummaryStat.lateGame, 0.005f)

        }

        Assert.assertEquals(count, 1)
    }
    @Test
    fun ensureWeCanSaveDamageScoreStageForHero() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val creepsStats = GameStageStat(random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                gameId)

        val staticColumnNames = StaticColumnNames()
        val statNames = RefinedGeneralGameStageColumnNames( staticColumnNames.HERO_DAMAGE_EARLY_GAME,
                staticColumnNames.HERO_DAMAGE_MID_GAME,
               staticColumnNames.HERO_DAMAGE_LATE_GAME)


        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.saveGameStageStat(summonerId,creepsStats,statNames,tableName)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills," +
                "${statNames.earlyGame}," +
                "${statNames.midGame}," +
                "${statNames.lateGame} " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = $summonerId"

        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForGameStageStat(statNames)
            Assert.assertEquals(creepsStats.gameId, retrievedSummaryStat.gameId)
            Assert.assertEquals(creepsStats.earlyGame, retrievedSummaryStat.earlyGame, 0.005f)
            Assert.assertEquals(creepsStats.midGame, retrievedSummaryStat.midGame, 0.005f)
            Assert.assertEquals(creepsStats.lateGame, retrievedSummaryStat.lateGame, 0.005f)
        }

        Assert.assertEquals(count, 1)
    }

    @Test
    fun ensureWeCanSaveGoldScoreStageForHero() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val creepsStats = GameStageStat(random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                gameId)

        val staticColumnNames = StaticColumnNames()
        val statNames = RefinedGeneralGameStageColumnNames(staticColumnNames.HERO_GOLD_EARLY_GAME,
               staticColumnNames.HERO_GOLD_MID_GAME,
                staticColumnNames.HERO_GOLD_LATE_GAME)

        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.saveGameStageStat(summonerId,creepsStats,statNames,tableName)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills," +
                "${statNames.earlyGame}," +
                "${statNames.midGame}," +
                "${statNames.lateGame} " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = $summonerId"

        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForGameStageStat(statNames)
            Assert.assertEquals(creepsStats.gameId, retrievedSummaryStat.gameId)
            Assert.assertEquals(creepsStats.earlyGame, retrievedSummaryStat.earlyGame, 0.005f)
            Assert.assertEquals(creepsStats.midGame, retrievedSummaryStat.midGame, 0.005f)
            Assert.assertEquals(creepsStats.lateGame, retrievedSummaryStat.lateGame, 0.005f)
        }

        Assert.assertEquals(count, 1)
    }

    @Test
    fun ensureWeCanSaveXPScoreStageForHero() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val creepsStats = GameStageStat(random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                gameId)

        val staticColumnNames = StaticColumnNames()
        val statNames = RefinedGeneralGameStageColumnNames(
                staticColumnNames.HERO_XP_EARLY_GAME,
                staticColumnNames.HERO_XP_MID_GAME,
                staticColumnNames.HERO_XP_LATE_GAME)

        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.saveGameStageStat(summonerId,creepsStats,statNames,tableName)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills," +
                "${statNames.earlyGame}," +
                "${statNames.midGame}," +
                "${statNames.lateGame} " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = $summonerId"

        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForGameStageStat(statNames)
            Assert.assertEquals(creepsStats.gameId, retrievedSummaryStat.gameId)
            Assert.assertEquals(creepsStats.earlyGame, retrievedSummaryStat.earlyGame, 0.005f)
            Assert.assertEquals(creepsStats.midGame, retrievedSummaryStat.midGame, 0.005f)
            Assert.assertEquals(creepsStats.lateGame, retrievedSummaryStat.lateGame, 0.005f)
        }

        Assert.assertEquals(count, 1)
    }

    @Test
    fun ensureWeCanSaveCreepsScoreStageForEnemy() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val creepsStats = GameStageStat(random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                gameId)

        val staticColumnNames = StaticColumnNames()
        val statNames = RefinedGeneralGameStageColumnNames(
                staticColumnNames.VILLAN_CREEPS_EARLY_GAME,
             staticColumnNames.VILLAN_CREEPS_MID_GAME,
               staticColumnNames.VILLAN_CREEPS_LATE_GAME)


        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.saveGameStageStat(summonerId,creepsStats,statNames,tableName)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills," +
                "${statNames.earlyGame}," +
                "${statNames.midGame}," +
                "${statNames.lateGame} " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = $summonerId"

        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {

            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForGameStageStat(statNames)
            Assert.assertEquals(creepsStats.gameId, retrievedSummaryStat.gameId)
            Assert.assertEquals(creepsStats.earlyGame, retrievedSummaryStat.earlyGame, 0.005f)
            Assert.assertEquals(creepsStats.midGame, retrievedSummaryStat.midGame, 0.005f)
            Assert.assertEquals(creepsStats.lateGame, retrievedSummaryStat.lateGame, 0.005f)

        }

        Assert.assertEquals(count, 1)
    }

    @Test
    fun ensureWeCanSaveDamageScoreStageForEnemy() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val creepsStats = GameStageStat(random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                gameId)

        val staticColumnNames = StaticColumnNames()
        val statNames = RefinedGeneralGameStageColumnNames(
                staticColumnNames.VILLAN_DAMAGE_EARLY_GAME,
                staticColumnNames.VILLAN_DAMAGE_MID_GAME,
                staticColumnNames.VILLAN_DAMAGE_LATE_GAME)


        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.saveGameStageStat(summonerId,creepsStats,statNames,tableName)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills," +
                "${statNames.earlyGame}," +
                "${statNames.midGame}," +
                "${statNames.lateGame} " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = $summonerId"

        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForGameStageStat(statNames)
            Assert.assertEquals(creepsStats.gameId, retrievedSummaryStat.gameId)
            Assert.assertEquals(creepsStats.earlyGame, retrievedSummaryStat.earlyGame, 0.005f)
            Assert.assertEquals(creepsStats.midGame, retrievedSummaryStat.midGame, 0.005f)
            Assert.assertEquals(creepsStats.lateGame, retrievedSummaryStat.lateGame, 0.005f)
        }

        Assert.assertEquals(count, 1)
    }

    @Test
    fun ensureWeCanSaveGoldScoreStageForEnemy() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val creepsStats = GameStageStat(random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                gameId)

        val staticColumnNames = StaticColumnNames()
        val statNames = RefinedGeneralGameStageColumnNames(
                staticColumnNames.VILLAN_GOLD_EARLY_GAME,
                staticColumnNames.VILLAN_GOLD_MID_GAME,
                staticColumnNames.VILLAN_GOLD_LATE_GAME)


        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.saveGameStageStat(summonerId,creepsStats,statNames,tableName)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills," +
                "${statNames.earlyGame}," +
                "${statNames.midGame}," +
                "${statNames.lateGame} " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = $summonerId"

        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForGameStageStat(statNames)
            Assert.assertEquals(creepsStats.gameId, retrievedSummaryStat.gameId)
            Assert.assertEquals(creepsStats.earlyGame, retrievedSummaryStat.earlyGame, 0.005f)
            Assert.assertEquals(creepsStats.midGame, retrievedSummaryStat.midGame, 0.005f)
            Assert.assertEquals(creepsStats.lateGame, retrievedSummaryStat.lateGame, 0.005f)
        }

        Assert.assertEquals(count, 1)
    }

    @Test
    fun ensureWeCanSaveArrayOfStatges() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val game2 = random.nextLong()

        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val refinedSummaryStat2 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                game2,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val creepsStats = GameStageStat(random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                gameId)
        val creepsStats2 = GameStageStat(random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                game2)

        val staticColumnNames = StaticColumnNames()
        val statNames = RefinedGeneralGameStageColumnNames(staticColumnNames.VILLAN_XP_EARLY_GAME,
                staticColumnNames.VILLAN_XP_MID_GAME,
                staticColumnNames.VILLAN_XP_LATE_GAME)


        val summarystatarray = ArrayList<HeroTeamSummaryStat>()
        summarystatarray.add(refinedSummaryStat1)
        summarystatarray.add(refinedSummaryStat2)

        val gameStageStatArray = ArrayList<GameStageStat>()
        gameStageStatArray.add(creepsStats)
        gameStageStatArray.add(creepsStats2)

        gameSummaryDAO.saveHeroTeamSummaryStats(summonerId, summarystatarray,tableName)
        gameSummaryDAO.saveGameStageStatList(summonerId,gameStageStatArray,statNames,tableName)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills," +
                "${statNames.earlyGame}," +
                "${statNames.midGame}," +
                "${statNames.lateGame} " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = $summonerId"

        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            val statToCheck = gameStageStatArray[count]
            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForGameStageStat(statNames)
            Assert.assertEquals(statToCheck.gameId, retrievedSummaryStat.gameId)
            Assert.assertEquals(statToCheck.earlyGame, retrievedSummaryStat.earlyGame, 0.005f)
            Assert.assertEquals(statToCheck.midGame, retrievedSummaryStat.midGame, 0.005f)
            Assert.assertEquals(statToCheck.lateGame, retrievedSummaryStat.lateGame, 0.005f)
        }

        Assert.assertEquals(count, 2)
    }

    @Test
    fun ensureWeCanSaveXPScoreStageForEnemy() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val creepsStats = GameStageStat(random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                gameId)

        val staticColumnNames = StaticColumnNames()
        val statNames = RefinedGeneralGameStageColumnNames(
                staticColumnNames.VILLAN_XP_EARLY_GAME,
               staticColumnNames.VILLAN_XP_MID_GAME,
                staticColumnNames.VILLAN_XP_LATE_GAME)


        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.saveGameStageStat(summonerId,creepsStats,statNames,tableName)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills," +
                "${statNames.earlyGame}," +
                "${statNames.midGame}," +
                "${statNames.lateGame} " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = $summonerId"

        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            count += 1
            val retrievedSummaryStat = retrievedSummaryStats.produceTestResultForGameStageStat(statNames)
            Assert.assertEquals(creepsStats.gameId, retrievedSummaryStat.gameId)
            Assert.assertEquals(creepsStats.earlyGame, retrievedSummaryStat.earlyGame, 0.005f)
            Assert.assertEquals(creepsStats.midGame, retrievedSummaryStat.midGame, 0.005f)
            Assert.assertEquals(creepsStats.lateGame, retrievedSummaryStat.lateGame, 0.005f)
        }

        Assert.assertEquals(count, 1)
    }

    @Test
    fun ensureThatWeCanSaveAndLoadFullGameStat() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val fullgameStat = FullGameStat(
                gameId,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.savePlayerGameSummaryStatsItemForHero(summonerId,fullgameStat,tableName)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills," +
                "heroKills," +
                "heroDeaths," +
                "heroAssists," +
                "heroWardsPlaced," +
                "heroWardsKilled " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = $summonerId"

        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            count += 1
            val retrievedStat = retrievedSummaryStats.produceTestResultForHeroFullGameStat()
            Assert.assertEquals(fullgameStat.gameId, retrievedStat.gameId)
            Assert.assertEquals(fullgameStat.kills, retrievedStat.kills)
            Assert.assertEquals(fullgameStat.deaths, retrievedStat.deaths)
            Assert.assertEquals(fullgameStat.assists, retrievedStat.assists)
            Assert.assertEquals(fullgameStat.wardsKilled, retrievedStat.wardsKilled)
            Assert.assertEquals(fullgameStat.wardsPlaced, retrievedStat.wardsPlaced)
        }

        Assert.assertEquals(count, 1)
    }

    @Test
    fun ensureThatWeCanSaveAndLoadFullGameStatForVillan() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val fullgameStat = FullGameStat(
                gameId,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.savePlayerGameSummaryStatsItemForVillan(summonerId,fullgameStat,tableName)

        val sql = "SELECT " +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills," +
                "villanKills," +
                "villanDeaths," +
                "villanAssists," +
                "villanWardsPlaced," +
                "villanWardsKilled " +
                "From $GAME_SUMMARY_TABLE " +
                "Where heroSummonerId = $summonerId"

        val retrievedSummaryStats = dbHelper.executeSqlQuery(sql)
//        Assert.assertEquals(retrievedSummaryStats.fetchSize, 1)
        var count = 0
        while (retrievedSummaryStats.next()) {
            count += 1
            val retrievedStat = retrievedSummaryStats.produceTestResultForVillanFullGameStat()
            Assert.assertEquals(fullgameStat.gameId, retrievedStat.gameId)
            Assert.assertEquals(fullgameStat.kills, retrievedStat.kills)
            Assert.assertEquals(fullgameStat.deaths, retrievedStat.deaths)
            Assert.assertEquals(fullgameStat.assists, retrievedStat.assists)
            Assert.assertEquals(fullgameStat.wardsKilled, retrievedStat.wardsKilled)
            Assert.assertEquals(fullgameStat.wardsPlaced, retrievedStat.wardsPlaced)
        }

        Assert.assertEquals(count, 1)
    }

    @Test
    fun `Make sure the we find no previous result when there is none`() {
        val haveAlreadySaved = gameSummaryDAO.doesGameSummaryForSummonerExist(-1, -1)
        Assert.assertTrue(!haveAlreadySaved)
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val fullgameStat = FullGameStat(
                gameId,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.savePlayerGameSummaryStatsItemForVillan(summonerId,fullgameStat,tableName)
        val stillHaveAlreadySaved = gameSummaryDAO.doesGameSummaryForSummonerExist(-1, -1)
        Assert.assertTrue(!stillHaveAlreadySaved)
    }

    @Test
    fun `Make sure that we can properly check if we have previously saved data about this game for this summoner`() {
        val haveAlreadySaved = gameSummaryDAO.doesGameSummaryForSummonerExist(-1, -1)
        Assert.assertTrue(!haveAlreadySaved)
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val fullgameStat = FullGameStat(
                gameId,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.savePlayerGameSummaryStatsItemForVillan(summonerId,fullgameStat,tableName)
        val stillHaveAlreadySaved = gameSummaryDAO.doesGameSummaryForSummonerExist(gameId, summonerId)
        Assert.assertTrue(stillHaveAlreadySaved)
    }

    @Test
    fun `GetCreepsEarlyGame() Make sure we return -1 if we find nothing`() {
        val creeps = gameSummaryDAO.fetchCreepsEarlyGameForMatch(1,1,tableName)
        Assert.assertTrue(creeps == -1f)
    }

    @Test
    fun `GetCreepsEarlyGame() Make sure we return the correct result when there is one`() {
        val summonerId = random.nextLong()
        val gameId = random.nextLong()
        val refinedSummaryStat1 = HeroTeamSummaryStat(
                summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val creepsStats = GameStageStat(
                random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                gameId)

        val staticColumnNames = StaticColumnNames()
        val statNames = RefinedGeneralGameStageColumnNames(
                staticColumnNames.HERO_CREEPS_EARLY_GAME,
                staticColumnNames.HERO_CREEPS_MID_GAME,
                staticColumnNames.HERO_CREEPS_LATE_GAME)


        gameSummaryDAO.insertHeroTeamSummaryStat(refinedSummaryStat1,tableName)
        gameSummaryDAO.saveGameStageStat(summonerId,creepsStats,statNames,tableName)

        val creeps = gameSummaryDAO.fetchCreepsEarlyGameForMatch(gameId,summonerId,tableName)
        Assert.assertTrue(creeps == creepsStats.earlyGame)
    }

    private fun ResultSet.produceTestResultForVillanGameStageStat() : HeroTeamSummaryStat {
        return HeroTeamSummaryStat(
                getLong("heroSummonerId"),
                getInt("villanChampId"),
                getLong("gameId"),
                getInt("villanTeamId"),
                getBoolean("villanWin"),
                getInt("villanTeamTowerKills"),
                getInt("villanTeamDragonKills"),
                getInt("villanTeamRiftHeraldKills"),
                getInt("villanTeamBaronKills"))
    }

    private fun ResultSet.produceTestResultForVillanFullGameStat() : FullGameStat {
        return FullGameStat(getLong("gameId"),
                getInt("villanKills"),
                getInt("villanDeaths"),
                getInt("villanAssists"),
                getInt("villanWardsPlaced"),
                getInt("villanWardsKilled"))
    }
    private fun ResultSet.produceTestResultForHeroFullGameStat() : FullGameStat{
        return FullGameStat(getLong("gameId"),
                getInt("heroKills"),
                getInt("heroDeaths"),
                getInt("heroAssists"),
                getInt("heroWardsPlaced"),
                getInt("heroWardsKilled"))
    }

    private fun ResultSet.produceTestResultForGameStageStat(names : RefinedGeneralGameStageColumnNames) : GameStageStat {
        return GameStageStat(getFloat(names.earlyGame),
                getFloat(names.midGame),
                getFloat(names.lateGame),
                getLong("gameId"))
    }

    private fun ResultSet.produceTestResultForHeroSummaryStat(): HeroTeamSummaryStat {
        return HeroTeamSummaryStat(
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

