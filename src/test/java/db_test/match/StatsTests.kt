package db_test.match

import db.DBHelper
import db.match.StatDAO
import model.match.Stats
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Josiah Kendall
 */
class StatsTests {
    lateinit var dbHelper :DBHelper
    lateinit var statDAO : StatDAO

    @Before
    fun setUp() {
        dbHelper = DBHelper()
        dbHelper.connect()
        statDAO = StatDAO(dbHelper)
    }

    @Test
    fun ensureWeCanGetStatObjectByParticipantRowId() {
        val stat = Stats(1,
                true,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                true,
                true,
                true,
                false,
                false,
                true,
                1,
                1,
                1,
                1)

        statDAO.saveStats(stat, -1)
        val stats2 = statDAO.getStatForParticipant(-1)
        Assert.assertTrue(stats2.firstInhibitorAssist == stat.firstInhibitorAssist)
        Assert.assertTrue(stats2.firstInhibitorKill == stat.firstInhibitorKill)
        Assert.assertTrue(stats2.firstBloodKill == stat.firstBloodKill)
        Assert.assertTrue(stats2.win == stat.win)
        Assert.assertTrue(stats2.physicalDamageTaken == stat.physicalDamageTaken)
        Assert.assertTrue(stats2.firstInhibitorAssist == stat.firstInhibitorAssist)
        Assert.assertTrue(stats2.firstInhibitorAssist == stat.firstInhibitorAssist)
        Assert.assertTrue(stats2.firstInhibitorAssist == stat.firstInhibitorAssist)
        Assert.assertTrue(stats2.firstInhibitorAssist == stat.firstInhibitorAssist)
        Assert.assertTrue(stats2.firstInhibitorAssist == stat.firstInhibitorAssist)
        Assert.assertTrue(stats2.firstInhibitorAssist == stat.firstInhibitorAssist)
        // todo finsh this.
    }
    // todo consider writing other tests that might be relavent
}