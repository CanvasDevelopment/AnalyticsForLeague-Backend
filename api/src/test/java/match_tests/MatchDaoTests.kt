package match_tests

import database.DbHelper
import database.match.MatchDao
import database.match.MatchSummaryDao
import database.match.model.MatchSummary
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

class MatchDaoTests {

    val dbHelper = DbHelper()
    val matchSummaryDao = MatchSummaryDao(dbHelper)
    val matchDao = MatchDao(dbHelper)
    val random = Random()

    val heroSummonerId = random.nextLong()
    val heroChampId = random.nextInt()
    val villanChampId  = random.nextInt()
    val heroTeamId = random.nextInt()
    val villanTeamId = random.nextInt()
    val heroWin = random.nextBoolean()
    val villanWin = random.nextBoolean()
    val heroTeamTowerKills = random.nextInt()
    val villanTeamTowerKills = random.nextInt()
    val heroTeamDragonKills = random.nextInt()
    val villanTeamDragonKills = random.nextInt()
    val heroTeamRiftHeraldKills = random.nextInt()
    val villanTeamRiftHeraldKills = random.nextInt()
    val heroTeamBaronKills = random.nextInt()
    val villanTeamBaronKills = random.nextInt()
    val heroKills = random.nextInt()
    val villanKills = random.nextInt()
    val heroDeaths = random.nextInt()
    val villanDeaths = random.nextInt()
    val heroAssists = random.nextInt()
    val villanAssists = random.nextInt()
    val heroWardsPlaced =random.nextInt()
    val villanWardsPlaced = random.nextInt()
    val heroWardsKilled = random.nextInt()
    val villanWardsKilled = random.nextInt()
    val heroGoldEarlyGame = random.nextFloat()
    val villanGoldEarlyGame = random.nextFloat()
    val heroGoldMidGame = random.nextFloat()
    val villanGoldMidGame =random.nextFloat()
    val heroGoldLateGame = random.nextFloat()
    val villanGoldLateGame = random.nextFloat()
    val heroCreepsEarlyGame = random.nextFloat()
    val villanCreepsEarlyGame = random.nextFloat()
    val heroCreepsMidGame = random.nextFloat()
    val villanCreepsMidGame = random.nextFloat()
    val heroCreepsLateGame = random.nextFloat()
    val villanCreepsLateGame = random.nextFloat()
    val heroDamageEarlyGame = random.nextFloat()
    val villanDamageEarlyGame = random.nextFloat()
    val heroDamageMidGame = random.nextFloat()
    val villanDamageMidGame = random.nextFloat()
    val heroDamageLateGame = random.nextFloat()
    val villanDamageLateGame = random.nextFloat()
    val heroXpEarlyGame = random.nextFloat()
    val villanXpEarlyGame = random.nextFloat()
    val heroXpMidGame = random.nextFloat()
    val villanXpMidGame = random.nextFloat()
    val heroXpLateGame = random.nextFloat()
    val villanXpLateGame = random.nextFloat()

    @Before
    fun setUp() {
        val sql = "Delete from matchSummary"
        val sql2 = "Delete from top_summarystats"
        dbHelper.executeSQLScript(sql)
        dbHelper.executeSQLScript(sql2)
    }

    @Test
    fun `Test We Can List 20 matches for any role`() {
        val summonerId = random.nextLong()
        var index = 0
        while (index < 25) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId))
            index += 1
        }

        val summaries = matchDao.loadTwentyIds(0,summonerId)
        assert(summaries.size == 20)
    }

    @Test
    fun `Test We Can List 20 matches for any role with offset`() {
        val summonerId = random.nextLong()
        var index = 0
        while (index < 25) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId))
            index += 1
        }

        val summaries = matchDao.loadTwentyIds(10,summonerId)
        assert(summaries.size == 15)
    }

    @Test
    fun `Test We Can List 20 matches for any role with a certain champ`() {
        val summonerId = random.nextLong()
        val champId = random.nextInt()
        var index = 0
        while (index < 15) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId, champId))
            index += 1
        }

        while (index < 25) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId))
            index += 1
        }

        val summaries = matchDao.loadTwentyIds(10,summonerId, champId)
        assert(summaries.size == 5)
    }

    @Test
    fun `Test We Can List 20 matches for a single role with a certain champ`() {
        val summonerId = random.nextLong()
        val champId = random.nextInt()
        val lane = random.nextInt().toString()
        var index = 0
        while (index < 15) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId, champId,lane))
            index += 1
        }

        while (index < 25) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId))
            index += 1
        }

        val summaries = matchDao.loadTwentyIds(5,summonerId, champId, lane)
        assert(summaries.size == 10)
    }

    @Test
    fun `Test We Can List 20 matches for a single role with any champ`() {
        val summonerId = random.nextLong()
        val lane = random.nextInt().toString()
        val champId = random.nextInt()
        var index = 0
        while (index < 15) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId,lane))
            index += 1
        }

        while (index < 25) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId,champId))
            index += 1
        }

        val summaries = matchDao.loadTwentyIds(10,summonerId,lane)
        assert(summaries.size == 5)
    }

    @Test
    fun `Test That We Can Fetch Correct Game Result And Champ Ids`() {
        val summonerId = 1542360L
        val tableName = "top_summarystats"
        val gameId = 161346846L

        val resultSet = matchDao.fetchWinAndChampIds(gameId,summonerId,tableName)
        assert(resultSet.first())
        assert(resultSet.getBoolean("heroWin"))
        assert(resultSet.getInt("heroChampId") == 27)
        assert(resultSet.getInt("villanChampId") == 13)
    }

    @Test
    fun `Test that we can fetch the correct stats for a match`() {
        // create some matches.
        // save them to the database
        // fetch them and make sure that they are accurate
        val gameId = random.nextLong()

        val sql = produceSqlToSaveMatch(gameId)

        dbHelper.executeSQLScript(sql)
        val columnNames = ArrayList<String>()
        columnNames.add("heroCreepsEarlyGame")
        columnNames.add("heroCreepsMidGame")
        columnNames.add("heroCreepsLateGame")
        columnNames.add("villanCreepsEarlyGame")
        columnNames.add("villanCreepsMidGame")
        columnNames.add("villanCreepsLateGame")
        val resultSet = matchDao.fetchMatchDetails(heroSummonerId,gameId,columnNames,"top_summarystats")
        assert(resultSet.first())

        Assert.assertEquals(resultSet.getFloat("heroCreepsEarlyGame"), heroCreepsEarlyGame, 0.05f)
        Assert.assertEquals(resultSet.getFloat("heroCreepsMidGame"), heroCreepsMidGame, 0.05f)
        Assert.assertEquals(resultSet.getFloat("heroCreepsLateGame"), heroCreepsLateGame, 0.05f)
        Assert.assertEquals(resultSet.getFloat("villanCreepsEarlyGame"), villanCreepsEarlyGame, 0.05f)
        Assert.assertEquals(resultSet.getFloat("villanCreepsMidGame"), villanCreepsMidGame, 0.05f)
        Assert.assertEquals(resultSet.getFloat("villanCreepsLateGame"), villanCreepsLateGame, 0.05f)
    }

    @Test
    fun makeSureThatWeCanSaveMultipleMatchesButStillFetchTheCorrectOne() {
        val gameId1 = random.nextLong()
        val gameId2 = random.nextLong()
        val sqlGame1 = produceSqlToSaveMatch(gameId1)
        val sqlGame2 = produceSqlToSaveMatch(gameId2)
        dbHelper.executeSQLScript(sqlGame1)
        dbHelper.executeSQLScript(sqlGame2)
        val columnNames = ArrayList<String>()
        columnNames.add("heroCreepsEarlyGame")
        columnNames.add("heroCreepsMidGame")
        columnNames.add("heroCreepsLateGame")
        columnNames.add("villanCreepsEarlyGame")
        columnNames.add("villanCreepsMidGame")
        columnNames.add("villanCreepsLateGame")
        val resultSet = matchDao.fetchMatchDetails(heroSummonerId, gameId2,columnNames,"top_summarystats")
        assert(resultSet.first())
        Assert.assertEquals(resultSet.getFloat("heroCreepsEarlyGame"), heroCreepsEarlyGame, 0.05f)
        Assert.assertEquals(resultSet.getFloat("heroCreepsMidGame"), heroCreepsMidGame, 0.05f)
        Assert.assertEquals(resultSet.getFloat("heroCreepsLateGame"), heroCreepsLateGame, 0.05f)
        Assert.assertEquals(resultSet.getFloat("villanCreepsEarlyGame"), villanCreepsEarlyGame, 0.05f)
        Assert.assertEquals(resultSet.getFloat("villanCreepsMidGame"), villanCreepsMidGame, 0.05f)
        Assert.assertEquals(resultSet.getFloat("villanCreepsLateGame"), villanCreepsLateGame, 0.05f)
        assert(!resultSet.next())
    }

    private fun produceSqlToSaveMatch(gameId : Long) :String {
        // sql to save all these stats
        return "insert INTO top_summarystats(" +
                "gameId, " +
                "heroSummonerId, " +
                "heroChampId, " +
                "villanChampId, " +
                "heroTeamId, " +
                "villanTeamId, " +
                "heroWin, " +
                "villanWin, " +
                "heroTeamTowerKills, " +
                "villanTeamTowerKills, " +
                "heroTeamDragonKills, " +
                "villanTeamDragonKills, " +
                "heroTeamRiftHeraldKills, " +
                "villanTeamRiftHeraldKills, " +
                "heroTeamBaronKills, " +
                "villanTeamBaronKills, " +
                "heroKills, " +
                "villanKills, " +
                "heroDeaths, " +
                "villanDeaths, " +
                "heroAssists, " +
                "villanAssists, " +
                "heroWardsPlaced, " +
                "villanWardsPlaced, " +
                "heroWardsKilled, " +
                "villanWardsKilled, " +
                "heroGoldEarlyGame, " +
                "villanGoldEarlyGame, " +
                "heroGoldMidGame, " +
                "villanGoldMidGame, " +
                "heroGoldLateGame, " +
                "villanGoldLateGame, " +
                "heroCreepsEarlyGame, " +
                "villanCreepsEarlyGame, " +
                "heroCreepsMidGame, " +
                "villanCreepsMidGame, " +
                "heroCreepsLateGame, " +
                "villanCreepsLateGame, " +
                "heroDamageEarlyGame, " +
                "villanDamageEarlyGame, " +
                "heroDamageMidGame, " +
                "villanDamageMidGame, " +
                "heroDamageLateGame, " +
                "villanDamageLateGame, " +
                "heroXpEarlyGame, " +
                "villanXpEarlyGame, " +
                "heroXpMidGame, " +
                "villanXpMidGame, " +
                "heroXpLateGame, " +
                "villanXpLateGame) VALUES (" +
                "$gameId, " +
                "$heroSummonerId, " +
                "$heroChampId, " +
                "$villanChampId, " +
                "$heroTeamId, " +
                "$villanTeamId, " +
                "$heroWin, " +
                "$villanWin, " +
                "$heroTeamTowerKills, " +
                "$villanTeamTowerKills, " +
                "$heroTeamDragonKills, " +
                "$villanTeamDragonKills, " +
                "$heroTeamRiftHeraldKills, " +
                "$villanTeamRiftHeraldKills, " +
                "$heroTeamBaronKills, " +
                "$villanTeamBaronKills, " +
                "$heroKills, " +
                "$villanKills, " +
                "$heroDeaths, " +
                "$villanDeaths, " +
                "$heroAssists, " +
                "$villanAssists, " +
                "$heroWardsPlaced, " +
                "$villanWardsPlaced, " +
                "$heroWardsKilled, " +
                "$villanWardsKilled, " +
                "$heroGoldEarlyGame, " +
                "$villanGoldEarlyGame, " +
                "$heroGoldMidGame, " +
                "$villanGoldMidGame, " +
                "$heroGoldLateGame, " +
                "$villanGoldLateGame, " +
                "$heroCreepsEarlyGame, " +
                "$villanCreepsEarlyGame, " +
                "$heroCreepsMidGame, " +
                "$villanCreepsMidGame, " +
                "$heroCreepsLateGame, " +
                "$villanCreepsLateGame, " +
                "$heroDamageEarlyGame, " +
                "$villanDamageEarlyGame, " +
                "$heroDamageMidGame, " +
                "$villanDamageMidGame, " +
                "$heroDamageLateGame, " +
                "$villanDamageLateGame, " +
                "$heroXpEarlyGame, " +
                "$villanXpEarlyGame, " +
                "$heroXpMidGame, " +
                "$villanXpMidGame, " +
                "$heroXpLateGame, " +
                "$villanXpLateGame)"
    }

    fun produceRandomMatchSummary(summonerId : Long) : MatchSummary {
        return MatchSummary(
                random.nextInt(),
                random.nextFloat().toString(),
                random.nextLong(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                random.nextFloat().toString(),
                random.nextFloat().toString(),
                summonerId)
    }

    fun produceRandomMatchSummary(summonerId : Long, champId :Int) : MatchSummary {
        return MatchSummary(
                random.nextInt(),
                random.nextFloat().toString(),
                random.nextLong(),
                champId,
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                random.nextFloat().toString(),
                random.nextFloat().toString(),
                summonerId)
    }

    fun produceRandomMatchSummary(summonerId : Long, champId :Int, lane : String) : MatchSummary {
        return MatchSummary(
                random.nextInt(),
                random.nextFloat().toString(),
                random.nextLong(),
                champId,
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                random.nextFloat().toString(),
                lane,
                summonerId)
    }

    fun produceRandomMatchSummary(summonerId : Long, lane: String) : MatchSummary {
        return MatchSummary(
                random.nextInt(),
                random.nextFloat().toString(),
                random.nextLong(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                random.nextFloat().toString(),
                lane,
                summonerId)
    }


}