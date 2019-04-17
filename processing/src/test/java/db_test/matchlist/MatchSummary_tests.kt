package db_test.matchlist

import db.requests.DBHelper
import db.matchlist.MatchSummaryDAO
import model.matchlist.MatchSummary
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * @author Josiah Kendall
 */
class MatchSummary_tests {
    private val random = Random()

    lateinit var dbHelper : DBHelper
    lateinit var matchSummaryDAO : MatchSummaryDAO
    val gameId : Long = -1
    val MATCH_SUMMARY = "matchsummary"
    val summonerId : String = "-1"
    @Before
    fun setUp() {
        dbHelper = DBHelper()
        dbHelper.connect()
        matchSummaryDAO = MatchSummaryDAO(dbHelper)
    }

    @After
    fun cleanUp() {
        dbHelper.executeSQLScript("DELETE FROM $MATCH_SUMMARY where gameId = -1")
        dbHelper.executeSQLScript("DELETE FROM $MATCH_SUMMARY where summonerId = -1")
    }

    @Test
    fun EnsureThatWeCanSaveAMatchSummaryThenFetchItById() {

        val matchSummary = MatchSummary()
        matchSummary.lane = "TOP"
        matchSummary.timestamp = 1234567
        matchSummary.season = 9
        matchSummary.queue = 7
        matchSummary.champion = 5
        matchSummary.gameId = gameId
        matchSummary.platformId = "OCE1"
        matchSummary.role = "SOLO"
        matchSummary.summonerId = "5"

        val id = matchSummaryDAO.saveMatchSummary(matchSummary)
        val ms2 = matchSummaryDAO.getMatchSummary(id)
        Assert.assertTrue(ms2.role == matchSummary.role)
        Assert.assertTrue(ms2.lane == matchSummary.lane)
        Assert.assertTrue(ms2.season == matchSummary.season)
        Assert.assertTrue(ms2.timestamp == matchSummary.timestamp)
        Assert.assertTrue(ms2.queue == matchSummary.queue)
        Assert.assertTrue(ms2.champion == matchSummary.champion)
        Assert.assertTrue(ms2.gameId == matchSummary.gameId)
        Assert.assertTrue(ms2.platformId == matchSummary.platformId)
        Assert.assertTrue(ms2.summonerId == matchSummary.summonerId)
    }

    @Test
    fun EnsureThatWeCanSaveAndFetchAMatchSummaryByGameId() {
        val matchSummary = MatchSummary()
        matchSummary.lane = "TOP"
        matchSummary.timestamp = 1234567
        matchSummary.season = 9
        matchSummary.queue = 7
        matchSummary.champion = 5
        matchSummary.gameId = gameId
        matchSummary.platformId = "OCE1"
        matchSummary.role = "SOLO"
        matchSummary.summonerId = "4"

        matchSummaryDAO.saveMatchSummary(matchSummary)
        val ms2 = matchSummaryDAO.getMatchSummaryByGameId(matchSummary.gameId)
        Assert.assertTrue(ms2.role == matchSummary.role)
        Assert.assertTrue(ms2.lane == matchSummary.lane)
        Assert.assertTrue(ms2.season == matchSummary.season)
        Assert.assertTrue(ms2.timestamp == matchSummary.timestamp)
        Assert.assertTrue(ms2.queue == matchSummary.queue)
        Assert.assertTrue(ms2.champion == matchSummary.champion)
        Assert.assertTrue(ms2.gameId == matchSummary.gameId)
        Assert.assertTrue(ms2.platformId == matchSummary.platformId)
        Assert.assertTrue(ms2.summonerId == matchSummary.summonerId)
    }

    @Test
    fun ensure_WeCanGetAllMatchSummariesBySummonerId() {
        val matchSummary = MatchSummary()
        matchSummary.lane = "TOP"
        matchSummary.timestamp = 1234567
        matchSummary.season = 9
        matchSummary.queue = 7
        matchSummary.champion = 5
        matchSummary.gameId = gameId // if test fails try changing this :)
        matchSummary.platformId = "OCE1"
        matchSummary.role = "SOLO"
        matchSummary.summonerId = summonerId
        matchSummaryDAO.saveMatchSummary(matchSummary)

        val matchSummary2 = MatchSummary()
        matchSummary2.lane = "TOP"
        matchSummary2.timestamp = 1234567
        matchSummary2.season = 9
        matchSummary2.queue = 7
        matchSummary2.champion = 5
        matchSummary2.gameId = gameId // if test fails try changing this :)
        matchSummary2.platformId = "OCE1"
        matchSummary2.role = "SOLO"
        matchSummary2.summonerId = summonerId

        matchSummaryDAO.saveMatchSummary(matchSummary2)

        val matchSummaries = matchSummaryDAO.getAllMatchesBySummonerId(summonerId)
        Assert.assertTrue(matchSummaries.size == 2)
    }

    @Test
    fun ensureThatWeReturnFalseIfMatchSummaryDoesNotExist() {
        val exists = matchSummaryDAO.exists(-12345678)
        Assert.assertTrue(!exists)
    }

    @Test
    fun ensureThatWeReturnTrueIfMatchSummaryDoesExist() {
        val matchSummary = MatchSummary()
        matchSummary.lane = "TOP"
        matchSummary.timestamp = 1234567
        matchSummary.season = 9
        matchSummary.queue = 7
        matchSummary.champion = 5
        matchSummary.gameId = gameId
        matchSummary.platformId = "OCE1"
        matchSummary.role = "SOLO"
        matchSummary.summonerId = summonerId
        matchSummaryDAO.saveMatchSummary(matchSummary)
        val exists = matchSummaryDAO.exists(gameId)
        Assert.assertTrue(exists)
    }

    @Test
    fun `Make sure that we can fetch a limited amount of match summaries for a summoner`() {
        val summonerId = random.nextLong().toString()
        val matchSummary1 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP")
        val matchSummary2 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP")
        val matchSummary3 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP")
        val matchSummary4 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP")
        val matchSummary5 = generateMatchSummaryForSummoner(summonerId, "SOLO", "MID")
        val matchSummary6 = generateMatchSummaryForSummoner(summonerId, "SOLO", "MID")
        val matchSummary7 = generateMatchSummaryForSummoner("-1", "SOLO", "MID")
        val matchSummary8 = generateMatchSummaryForSummoner("-1", "SOLO", "MID")

        matchSummaryDAO.saveMatchSummary(matchSummary1)
        matchSummaryDAO.saveMatchSummary(matchSummary2)
        matchSummaryDAO.saveMatchSummary(matchSummary3)
        matchSummaryDAO.saveMatchSummary(matchSummary4)
        matchSummaryDAO.saveMatchSummary(matchSummary5)
        matchSummaryDAO.saveMatchSummary(matchSummary6)
        matchSummaryDAO.saveMatchSummary(matchSummary7)
        matchSummaryDAO.saveMatchSummary(matchSummary8)

        val summaries = matchSummaryDAO.getRecentMatchesBySummonerIdForRole(summonerId,2,"SOLO", "TOP")
        assert(summaries.size == 2)
        assert(summaries[0].gameId == matchSummary4.gameId)
        assert(summaries[1].gameId == matchSummary3.gameId)
    }

    @Test
    fun `Make sure that we can fetch the correct amount of matches for a summoner`() {
        val summonerId = random.nextLong().toString()

        assert(summonerId != "-1")
        val matchSummary1 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP")
        val matchSummary2 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP")
        val matchSummary3 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP")
        val matchSummary4 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP")
        val matchSummary5 = generateMatchSummaryForSummoner(summonerId, "SOLO", "MID")
        val matchSummary6 = generateMatchSummaryForSummoner(summonerId, "SOLO", "MID")
        // 6 matches for our summoner
        val matchSummary7 = generateMatchSummaryForSummoner("-1", "SOLO", "MID")
        val matchSummary8 = generateMatchSummaryForSummoner("-1", "SOLO", "MID")

        matchSummaryDAO.saveMatchSummary(matchSummary1)
        matchSummaryDAO.saveMatchSummary(matchSummary2)
        matchSummaryDAO.saveMatchSummary(matchSummary3)
        matchSummaryDAO.saveMatchSummary(matchSummary4)
        matchSummaryDAO.saveMatchSummary(matchSummary5)
        matchSummaryDAO.saveMatchSummary(matchSummary6)
        matchSummaryDAO.saveMatchSummary(matchSummary7)
        matchSummaryDAO.saveMatchSummary(matchSummary8)

        val numberOfMatches = matchSummaryDAO.loadNumberOfMatchSummariesForASummoner(summonerId)
        assert(numberOfMatches == 6)
    }

    @Test
    fun `Make sure that we can fetch the correct amount of matches less than a given game id for a summoner`() {
        val summonerId = random.nextLong().toString()

        assert(summonerId != "-1")
        val matchSummary1 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP", 1)
        val matchSummary2 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP", 2)
        val matchSummary3 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP", 3)
        val matchSummary4 = generateMatchSummaryForSummoner(summonerId, "SOLO", "TOP", 4)
        val matchSummary5 = generateMatchSummaryForSummoner(summonerId, "SOLO", "MID", 5)
        val matchSummary6 = generateMatchSummaryForSummoner(summonerId, "SOLO", "MID", 6)
        // 6 matches for our summoner
        val matchSummary7 = generateMatchSummaryForSummoner("-1", "SOLO", "MID")
        val matchSummary8 = generateMatchSummaryForSummoner("-1", "SOLO", "MID")

        matchSummaryDAO.saveMatchSummary(matchSummary1)
        matchSummaryDAO.saveMatchSummary(matchSummary2)
        matchSummaryDAO.saveMatchSummary(matchSummary3)
        matchSummaryDAO.saveMatchSummary(matchSummary4)
        matchSummaryDAO.saveMatchSummary(matchSummary5)
        matchSummaryDAO.saveMatchSummary(matchSummary6)
        matchSummaryDAO.saveMatchSummary(matchSummary7)
        matchSummaryDAO.saveMatchSummary(matchSummary8)

        val numberOfMatches = matchSummaryDAO.loadNumberOfMatchSummariesUpToAndIncludingGivenMatchId(summonerId,4)
        assert(numberOfMatches == 4)
    }

    private fun generateMatchSummaryForSummoner(summonerId: String, role: String, lane: String) : MatchSummary {
        return MatchSummary(random.nextInt(),
                "oce",
                -1L,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                role,
                lane,
                summonerId)
    }

    private fun generateMatchSummaryForSummoner(summonerId: String, role: String, lane: String, gameId: Long) : MatchSummary {
        return MatchSummary(random.nextInt(),
                "oce",
                gameId,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                role,
                lane,
                summonerId)
    }
}