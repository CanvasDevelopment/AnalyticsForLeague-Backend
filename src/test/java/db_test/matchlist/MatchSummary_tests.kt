package db_test.matchlist

import db.DBHelper
import db.matchlist.MatchSummaryDAOImpl
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
        val matchSummaryDAO: MatchSummaryDAOImpl = MatchSummaryDAOImpl(dbHelper)
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
        dbHelper.Disconnect()
    }

    @Test
    fun EnsureThatWeCanSaveAndFetchAMatchSummaryByGameId() {
        val dbHelper: DBHelper = DBHelper()
        val matchSummaryDAO: MatchSummaryDAOImpl = MatchSummaryDAOImpl(dbHelper)
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
        dbHelper.Disconnect()
    }
}