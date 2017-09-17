package db_test.stats

import db.requests.DBHelper
import db.stats.GameStageDeltaDAOImpl
import model.stats.GameStageDelta
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import util.Tables

/**
 * @author Josiah Kendall
 */
class GameStageDeltaStatTests {

    private var gameStageDeltaDAO: GameStageDeltaDAOImpl? = null
    private var dbHelper: DBHelper? = null
    val tables = Tables()

    @Before
    fun setUp() {
        dbHelper = DBHelper()
        dbHelper!!.connect()
        gameStageDeltaDAO = GameStageDeltaDAOImpl(dbHelper!!)
    }

    @After
    fun tearDown() {
        dbHelper!!.disconnect()
    }

    @Test
    fun TestThatWeCanSaveAndFetchCsDiffPerMin() {
        val creepsPerMin = GameStageDelta()
        creepsPerMin.tenToTwenty = 12.3
        creepsPerMin.zeroToTen = 6.45
        creepsPerMin.twentyToThirty = 5.67
        creepsPerMin.thirtyToEnd = 7.3333332
        val id = gameStageDeltaDAO!!.saveDeltas(creepsPerMin, tables.CS_DIFF_PER_MIN, -1)
        val second = gameStageDeltaDAO!!.getGameStageDeltas(id, tables.CS_DIFF_PER_MIN)
        Assert.assertEquals(creepsPerMin.zeroToTen, second!!.zeroToTen, 0.005)
        Assert.assertEquals(creepsPerMin.tenToTwenty, second.tenToTwenty, 0.005)
        Assert.assertEquals(creepsPerMin.twentyToThirty, second.twentyToThirty, 0.005)
        Assert.assertEquals(creepsPerMin.thirtyToEnd, second.thirtyToEnd, 0.005)
    }

    @Test
    fun TestThatWeCanSaveAndFetchGoldPerMin() {
        val creepsPerMin = GameStageDelta()
        creepsPerMin.tenToTwenty = 12.3
        creepsPerMin.zeroToTen = 6.45
        creepsPerMin.twentyToThirty = 5.67
        creepsPerMin.thirtyToEnd = 7.3333332
        val id = gameStageDeltaDAO!!.saveDeltas(creepsPerMin, tables.GOLD_PER_MIN, -1)
        val second = gameStageDeltaDAO!!.getGameStageDeltas(id, tables.GOLD_PER_MIN)
        Assert.assertEquals(creepsPerMin.zeroToTen, second!!.zeroToTen, 0.005)
        Assert.assertEquals(creepsPerMin.tenToTwenty, second.tenToTwenty, 0.005)
        Assert.assertEquals(creepsPerMin.twentyToThirty, second.twentyToThirty, 0.005)
        Assert.assertEquals(creepsPerMin.thirtyToEnd, second.thirtyToEnd, 0.005)
    }

    @Test
    fun TestThatWeCanSaveAndFetchDamageTakenPeMin() {
        val creepsPerMin = GameStageDelta()
        creepsPerMin.tenToTwenty = 12.3
        creepsPerMin.zeroToTen = 6.45
        creepsPerMin.twentyToThirty = 5.67
        creepsPerMin.thirtyToEnd = 7.3333332
        val id = gameStageDeltaDAO!!.saveDeltas(creepsPerMin, tables.DAMAGE_TAKEN_PER_MIN, -1)
        val second = gameStageDeltaDAO!!.getGameStageDeltas(id, tables.DAMAGE_TAKEN_PER_MIN)
        Assert.assertEquals(creepsPerMin.zeroToTen, second!!.zeroToTen, 0.005)
        Assert.assertEquals(creepsPerMin.tenToTwenty, second.tenToTwenty, 0.005)
        Assert.assertEquals(creepsPerMin.twentyToThirty, second.twentyToThirty, 0.005)
        Assert.assertEquals(creepsPerMin.thirtyToEnd, second.thirtyToEnd, 0.005)
    }

    @Test
    fun TestThatWeCanSaveAndFetchDamageTakenDiffPerMin() {
        val creepsPerMin = GameStageDelta()
        creepsPerMin.tenToTwenty = 12.3
        creepsPerMin.zeroToTen = 6.45
        creepsPerMin.twentyToThirty = 5.67
        creepsPerMin.thirtyToEnd = 7.3333332
        val id = gameStageDeltaDAO!!.saveDeltas(creepsPerMin, tables.DAMAGE_TAKEN_DIFF_PER_MIN, -1)
        val second = gameStageDeltaDAO!!.getGameStageDeltas(id, tables.DAMAGE_TAKEN_DIFF_PER_MIN)
        Assert.assertEquals(creepsPerMin.zeroToTen, second!!.zeroToTen, 0.005)
        Assert.assertEquals(creepsPerMin.tenToTwenty, second.tenToTwenty, 0.005)
        Assert.assertEquals(creepsPerMin.twentyToThirty, second.twentyToThirty, 0.005)
        Assert.assertEquals(creepsPerMin.thirtyToEnd, second.thirtyToEnd, 0.005)
    }

    @Test
    fun TestThatWeCanSaveAndFetchXpDiffPerMin() {
        val creepsPerMin = GameStageDelta()
        creepsPerMin.setTenToTwenty(12.3)
        creepsPerMin.setZeroToTen(12.3)
        creepsPerMin.setTwentyToThirty(5.67)
        creepsPerMin.setThirtyToEnd(7.3333332)
        val id = gameStageDeltaDAO!!.saveDeltas(creepsPerMin, tables.XP_DIFF_PER_MIN, -1)
        val second = gameStageDeltaDAO!!.getGameStageDeltas(id, tables.XP_DIFF_PER_MIN)
        Assert.assertEquals(creepsPerMin.zeroToTen, second!!.zeroToTen, 0.005)
        Assert.assertEquals(creepsPerMin.tenToTwenty, second.tenToTwenty, 0.005)
        Assert.assertEquals(creepsPerMin.twentyToThirty, second.twentyToThirty, 0.005)
        Assert.assertEquals(creepsPerMin.thirtyToEnd, second.thirtyToEnd, 0.005)
    }

    @Test
    fun TestThatWeCanSaveAndFetchXpPerMin() {
        val creepsPerMin = GameStageDelta()
        creepsPerMin.tenToTwenty = 12.3
        creepsPerMin.zeroToTen = 6.45
        creepsPerMin.twentyToThirty = 5.67
        creepsPerMin.thirtyToEnd = 7.3333332
        val id = gameStageDeltaDAO!!.saveDeltas(creepsPerMin, tables.XP_PER_MIN, -1)
        val second = gameStageDeltaDAO!!.getGameStageDeltas(id, tables.XP_PER_MIN)
        Assert.assertEquals(creepsPerMin.zeroToTen, second!!.zeroToTen, 0.005)
        Assert.assertEquals(creepsPerMin.tenToTwenty, second.tenToTwenty, 0.005)
        Assert.assertEquals(creepsPerMin.twentyToThirty, second.twentyToThirty, 0.005)
        Assert.assertEquals(creepsPerMin.thirtyToEnd, second.thirtyToEnd, 0.005)
    }
}
