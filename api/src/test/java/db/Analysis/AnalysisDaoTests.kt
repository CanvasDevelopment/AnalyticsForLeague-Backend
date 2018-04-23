package db.Analysis

import api.stat.analysis.AnalysisDao
import database.DbHelper
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import util.Constant.StatTypes.CREEPS
import util.Constant.GameStage.EARLY_GAME
import util.Constant.GameStage.LATE_GAME
import util.Constant.GameStage.MID_GAME
import util.Constant.LANE.JUNGLE
import util.Constant.LANE.MID
import util.Constant.LANE.TOP
import util.Constant.StatAccumulators.AVG
import util.Constant.StatAccumulators.MAX
import util.Constant.StatTypes.DAMAGE_DEALT
import util.Constant.StatTypes.RIFT_HERALD_KILLS
import util.Constant.StatTypes.TEAM_BARON_KILLS
import util.Constant.StatTypes.TEAM_DRAGON_KILLS
import util.Constant.StatTypes.WARDS_KILLED
import util.Constant.StatTypes.WARDS_PLACED
import kotlin.test.assertEquals

@Suppress("RemoveRedundantBackticks")
/**
 * @author Josiah Kendall
 */

class AnalysisDaoTests {
    private val dbHelper = DbHelper()
    private val ad = AnalysisDao(dbHelper)
    private val summonerId : Long = 1542360

    @Before
    fun setUp () {
        dbHelper.connect()
    }

    @Test
    fun `Make sure that we can get average villan creeps per minute for mid`() {
        val deltas = ad.fetchAvgDeltas(1542360, 20, MID, CREEPS)
        assert(deltas.earlyGame == 62f)
        assert(deltas.midGame == 65.33333587646484f)
        assert(deltas.lateGame == 75.99999f)
    }

    @Test
    fun `Make sure that we can get the max creeps per minute for mid`() {
        val deltas = ad.fetchMaxDeltas(1542360, 20, MID, CREEPS)
        assert(deltas.earlyGame == 65f)
        assertEquals(deltas.midGame , 81.00001f)
        assert(deltas.lateGame == 88.99999f)
    }

    @Test
    fun `Make sure that we can get the min damage per minute deltas for mid`() {
        val deltas = ad.fetchMinDeltas(1542360, 20, MID, DAMAGE_DEALT)
        assert(deltas.earlyGame == 2232f)
        assertEquals(deltas.midGame , 3601f)
        assert(deltas.lateGame == 4090.0f)
    }

    @Test
    fun `Make sure that we can fetch average head to head stats for a summoner with no specific champion when playing top during the early game`() {
        val deltas = ad.fetchHeadToHeadStat(summonerId,20,"TOP",AVG,EARLY_GAME, CREEPS)
        Assert.assertTrue(deltas.heroStatValue == 50.75f)
        Assert.assertTrue(deltas.enemyStatValue == 53.63636432994496f)
    }

    @Test
    fun `Make sure that we can fetch average head to head stats for a summoner with a specific champion`() {
        val deltas = ad.fetchHeadToHeadStat(summonerId,20,TOP,AVG, EARLY_GAME, CREEPS,58)
        assert(deltas.heroStatValue == 55f)
        assert(deltas.enemyStatValue == 47f)
    }

    @Test
    fun `Make sure that we can fetch average head to head stats for a summoner with no specific champion when playing top during the mid game`() {
        val deltas = ad.fetchHeadToHeadStat(summonerId,20,"TOP",AVG, MID_GAME, CREEPS)
        Assert.assertTrue(deltas.heroStatValue == 68.41666603088379f)
        Assert.assertTrue(deltas.enemyStatValue == 61.72727272727273f)
    }

    @Test
    fun `Make sure that we can fetch average head to head stats for a summoner with a specific champion during mid game`() {
        val deltas = ad.fetchHeadToHeadStat(summonerId,20,TOP,AVG, MID_GAME, CREEPS,58)
        assert(deltas.heroStatValue == 66.33333079020183f)
        assert(deltas.enemyStatValue == 52.333333333333336f)
    }

    @Test
    fun `Make sure that we can fetch average head to head stats for a summoner with no specific champion when playing top during the late game`() {
        val deltas = ad.fetchHeadToHeadStat(summonerId,20,"TOP",AVG, LATE_GAME, CREEPS)
        assert(deltas.heroStatValue == 50.125f)
        assert(deltas.enemyStatValue == 53f)
    }

    @Test
    fun `Make sure that we can fetch average head to head stats for a summoner with a specific champion during late game`() {
        val deltas = ad.fetchHeadToHeadStat(summonerId,20,TOP,AVG, LATE_GAME, CREEPS,58)
        assert(deltas.heroStatValue == 41.5f)
        assert(deltas.enemyStatValue == 62f)
    }

    @Test
    fun `Make sure that we can fetch average head to head stats of wards killed`() {
        val deltas = ad.fetchHeadToHeadStat(summonerId, 20, JUNGLE, AVG, WARDS_KILLED)
        assert(deltas.heroStatValue == 1.1f)
        assert(deltas.enemyStatValue == 1.45f)
    }

    @Test
    fun `Make sure that we can fetch average head to head stats of wards placed`() {
        val deltas = ad.fetchHeadToHeadStat(summonerId, 20, JUNGLE, AVG, WARDS_PLACED)
        assert(deltas.heroStatValue == 11.3f)
        assert(deltas.enemyStatValue == 9.05f)
    }

    @Test
    fun `Make sure that we can fetch average head to head stats of barons killed`() {
        val deltas = ad.fetchHeadToHeadStat(summonerId, 20, JUNGLE, AVG, TEAM_BARON_KILLS)
        assert(deltas.heroStatValue == 0.2f)
        assert(deltas.enemyStatValue == 0.35f)
    }

    @Test
    fun `Make sure that we can fetch average head to head stats of dragons killed`() {
        val deltas = ad.fetchHeadToHeadStat(summonerId, 20, JUNGLE, AVG, TEAM_DRAGON_KILLS)
        assert(deltas.heroStatValue == 1.7f)
        assert(deltas.enemyStatValue == 1.050f)
    }

    @Test
    fun `Make sure that we can fetch average head to head stats of rift heralds killed`() {
        val deltas = ad.fetchHeadToHeadStat(summonerId, 20, JUNGLE, AVG, RIFT_HERALD_KILLS)
        assert(deltas.heroStatValue == 0.05f)
        assert(deltas.enemyStatValue == 0f)
    }


    @Test
    fun `Make sure that we can fetch list of creeps per minute early game for a given role with a specific champ`() {
        val list = ad.fetchPerformanceHistory(summonerId, 20,TOP, CREEPS,58, EARLY_GAME)
        assert(list.size == 3)
        assert(list[0].heroStatValue == 62f)
        assert(list[0].enemyStatValue == 35f)
        assert(list[1].heroStatValue == 56f)
        assert(list[1].enemyStatValue == 54f)
        assert(list[2].heroStatValue == 47f)
        assert(list[2].enemyStatValue == 52f)
    }

    @Test
    fun `Make sure that we can fetch list of creeps per minute mid game for a given role with a specific champ`() {
        val list = ad.fetchPerformanceHistory(summonerId, 20, TOP, CREEPS,58, MID_GAME)
        assert(list.size == 3)
        assert(list[0].heroStatValue == 30f)
        assert(list[0].enemyStatValue == 54f)
        assert(list[1].heroStatValue == 84f)
        assert(list[1].enemyStatValue == 50f)
        assert(list[2].heroStatValue == 85f)
        assert(list[2].enemyStatValue == 53f)
    }

    @Test
    fun `Make sure that we can fetch match list of creeps per minutes mid game for a given role with any champ`() {
        val list = ad.fetchPerformanceHistory(summonerId, 20, TOP, CREEPS, MID_GAME)
        assert(list.size == 11)
        assert(list[0].heroStatValue == 74f)
        assert(list[0].enemyStatValue == 87f)
        assert(list[1].heroStatValue == 86f)
        assert(list[1].enemyStatValue == 61f)
        assert(list[2].heroStatValue == 70f)
        assert(list[2].enemyStatValue == 76f)
    }


    @Test
    fun `Make sure that we can fetch list of creeps per minute late game for a given role with a specific champ`() {
        val list = ad.fetchPerformanceHistory(summonerId, 20, TOP, CREEPS,58, LATE_GAME)
        assert(list.size == 2)
        assert(list[0].heroStatValue == 48f)
        assert(list[0].enemyStatValue == 75f)
        assert(list[1].heroStatValue == 35f)
        assert(list[1].enemyStatValue == 49f)
    }

    @Test
    fun `Make sure that we can fetch list of dragon kills for a given role`() {
        val list = ad.fetchPerformanceHistory(summonerId, 20, JUNGLE, TEAM_DRAGON_KILLS,254)
        assert(list.size == 13)
        assert(list[0].heroStatValue == 2f)
        assert(list[0].enemyStatValue == 1f)
        assert(list[1].heroStatValue == 1f)
        assert(list[12].heroStatValue == 3f)
        assert(list[12].enemyStatValue == 0f)
    }

    @Test
    fun `Make sure that we can fetch list of dragon kills for a given role with any champion`() {
        val list = ad.fetchPerformanceHistory(summonerId, 20, JUNGLE, TEAM_DRAGON_KILLS)
        assert(list.size == 20)
        assert(list[0].heroStatValue == 2f)
        assert(list[0].enemyStatValue == 1f)
        assert(list[1].heroStatValue == 1f)
        assert(list[1].enemyStatValue == 0f)
        assert(list[19].enemyStatValue == 3f)
    }

    @Test
    fun `Make sure that we can fetch the max dragons a summoner has killed with any champion when playing jungle`() {
        val result = ad.fetchStat(20, TEAM_DRAGON_KILLS,JUNGLE,summonerId, MAX)
        assert(result == 4f)
    }

    @Test
    fun `Make sure that we can fetch the max dragons a summoner has killed with 76 when playing jungle`() {
        val result = ad.fetchStat(20, TEAM_DRAGON_KILLS,JUNGLE,summonerId, MAX, 76)
        assert(result == 3f)
    }

    @Test
    fun `Check max creeps for early game top with any champ`() {
        val result = ad.fetchStat(100, CREEPS, TOP, summonerId, EARLY_GAME, MAX)
        assert(result ==62f )
    }

    @Test
    fun `Test that we can fetch Creep deltas for top lane with no specified champ`() {
        val results = ad.fetchDeltas(summonerId, 20, TOP, CREEPS, AVG)
        assert(results[0].heroStatValue == 50.75f)
        assert(results[1].heroStatValue == 68.41666603088379f)
        assert(results[2].heroStatValue == 33.416666666666664f)

        assert(results[0].enemyStatValue == 53.63636432994496f)
        assert(results[1].enemyStatValue == 61.72727272727273f)
        assert(results[2].enemyStatValue == 38.54545454545455f)
    }

    @Test
    fun `Test that we can fetch Creep deltas for top lane with a specified champ`() {
        val results = ad.fetchDeltas(summonerId, 20, TOP, CREEPS, AVG,58)
        assert(results[0].heroStatValue == 55f)
        assert(results[1].heroStatValue == 66.33333079020183f)
        assert(results[2].heroStatValue == 27.666666666666668f)

        assert(results[0].enemyStatValue == 47f)
        assert(results[1].enemyStatValue == 52.333333333333336f)
        assert(results[2].enemyStatValue == 41.333333333333336f)
    }
}