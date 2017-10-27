package db.Analysis

import api.stat.analysis.AnalysisDao
import database.DbHelper
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Josiah Kendall
 */

class AnalysisDaoTests {
    val dbHelper = DbHelper()
    val ad = AnalysisDao(dbHelper)

    @Before
    fun setUp () {
        dbHelper.connect()
    }

    @Test
    fun `Test That We Can Get Creeps Card Correctly`() {
        val card =  ad.fetchAvgCreepsPerMinStatCard(1542360,20,"MID")
        assert(card.heroCreepsEarlyGame == 62f)
        assert(card.heroCreepsMidGame == 65.33333587646484f)
        assert(card.heroCreepsLateGame == 50.666664123535156f)
        assert(card.villanCreepsEarlyGame == 36.5f)
        assert(card.villanCreepsMidGame == 29f) // TODO make this fail. It should not have nulls saved.
        assert(card.villanCreepsLateGame == 21f)
    }

    @Test
    fun `Make sure that we can get average villan creeps per minute for mid`() {
        val deltas = ad.fetchAvgDeltas(1542360, 20, "mid","heroCreeps")
        assert(deltas.earlyGame == 62f)
        assert(deltas.midGame == 65.33333587646484f)
        assert(deltas.lateGame == 50.666664123535156f)
    }

    @Test
    fun `Make sure that we can get the max creeps per minute for mid`() {
        val deltas = ad.fetchMaxDeltas(1542360, 20, "mid","heroCreeps")
        assert(deltas.earlyGame == 65f)
        assertEquals(deltas.midGame , 81.00001f)
        assert(deltas.lateGame == 88.99999f)
    }

    @Test
    fun `Make sure that we can get the min damage per minute deltas for mid`() {
        val deltas = ad.fetchMinDeltas(1542360, 20, "mid","heroDamage")
        assert(deltas.earlyGame == 2232f)
        assertEquals(deltas.midGame , 3601f)
        assert(deltas.lateGame == 0f)
    }
}