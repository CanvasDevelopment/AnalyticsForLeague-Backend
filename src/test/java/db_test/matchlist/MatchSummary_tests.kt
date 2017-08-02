package db_test.matchlist

import db.DBHelper
import db.matchlist.MatchSummaryDAO
import db.stats.CreepsPerMinDAOImpl
import model.matchlist.MatchSummary
import model.stats.CreepsPerMin
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Josiah Kendall
 */
class MatchSummary_tests {


    @Test
    fun EnsureThatWeCanSaveAMatchSummaryThenFetchItById() {

        val dbHelper: DBHelper = DBHelper()
        val matchSummaryDAO: MatchSummaryDAO = MatchSummaryDAO(dbHelper)
        dbHelper.Connect()
        val matchSummary = MatchSummary()
        matchSummary.lane = "TOP"
        matchSummary.timestamp = 1234567
        matchSummary.season = 9
        matchSummary.queue = 7
        matchSummary.champion = 5
        matchSummary.gameId = 1234567
        matchSummary.platformId = "OCE1"
        matchSummary.role = "SOLO"
        matchSummary.summonerId = 5

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
        dbHelper.Disconnect()
    }

    @Test
    fun EnsureThatWeCanSaveAndFetchAMatchSummaryByGameId() {
        val dbHelper: DBHelper = DBHelper()
        val matchSummaryDAO: MatchSummaryDAO = MatchSummaryDAO(dbHelper)
        dbHelper.Connect()
        val matchSummary = MatchSummary()
        matchSummary.lane = "TOP"
        matchSummary.timestamp = 1234567
        matchSummary.season = 9
        matchSummary.queue = 7
        matchSummary.champion = 5
        matchSummary.gameId = 123456789 // if test fails try changing this :)
        matchSummary.platformId = "OCE1"
        matchSummary.role = "SOLO"
        matchSummary.summonerId = 4

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

        dbHelper.Disconnect()
    }

    @Test
    fun ensure_WeCanGetAllMatchSummariesBySummonerId() {
        val summonerId :Long = 12345
        val dbHelper: DBHelper = DBHelper()
        val matchSummaryDAO: MatchSummaryDAO = MatchSummaryDAO(dbHelper)
        dbHelper.Connect()
        val matchSummary = MatchSummary()
        matchSummary.lane = "TOP"
        matchSummary.timestamp = 1234567
        matchSummary.season = 9
        matchSummary.queue = 7
        matchSummary.champion = 5
        matchSummary.gameId = 123456789 // if test fails try changing this :)
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
        matchSummary2.gameId = 123456749 // if test fails try changing this :)
        matchSummary2.platformId = "OCE1"
        matchSummary2.role = "SOLO"
        matchSummary2.summonerId = summonerId

        matchSummaryDAO.saveMatchSummary(matchSummary2)

        val matchSummaries = matchSummaryDAO.getAllMatchesBySummonerId(summonerId.toInt())
        Assert.assertTrue(matchSummaries.size == 2)
        dbHelper.Disconnect()
    }
}