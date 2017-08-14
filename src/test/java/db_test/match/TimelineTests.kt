package db_test.match

import db.DBHelper
import db.match.TimelineDAO
import db.stats.GameStageDeltaDAOImpl
import model.match.Timeline
import model.stats.GameStageDelta
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * @author Josiah Kendall
 */
class TimelineTests {

    lateinit var dbHelper : DBHelper
    lateinit var timelineDAO : TimelineDAO
    lateinit var gameStageDeltaDAO : GameStageDeltaDAOImpl

    @Before
    fun setUp () {
        dbHelper = DBHelper()
        dbHelper.connect()
        gameStageDeltaDAO = GameStageDeltaDAOImpl(dbHelper)
        timelineDAO = TimelineDAO(dbHelper, gameStageDeltaDAO)
    }

    @After
    fun cleanUp() {
        dbHelper.executeSQLScript("delete from timeline where participantRowId = -1")
    }

    @Test
    fun ensureThatWeCanFetchByParticipantRowId() {
        val random = Random()

        val cpm = GameStageDelta()
        cpm.zeroToTen = random.nextDouble()
        cpm.tenToTwenty = random.nextDouble()
        cpm.twentyToThirty = random.nextDouble()
        cpm.thirtyToEnd= random.nextDouble()

        val xpm = GameStageDelta()
        xpm.zeroToTen = random.nextDouble()
        xpm.tenToTwenty = random.nextDouble()
        xpm.twentyToThirty = random.nextDouble()
        xpm.thirtyToEnd= random.nextDouble()

        val gpm = GameStageDelta()
        gpm.zeroToTen = random.nextDouble()
        gpm.tenToTwenty = random.nextDouble()
        gpm.twentyToThirty = random.nextDouble()
        gpm.thirtyToEnd= random.nextDouble()

        val dpm = GameStageDelta()
        dpm.zeroToTen = random.nextDouble()
        dpm.tenToTwenty = random.nextDouble()
        dpm.twentyToThirty = random.nextDouble()
        dpm.thirtyToEnd= random.nextDouble()

        val csdpm = GameStageDelta()
        csdpm.zeroToTen = random.nextDouble()
        csdpm.tenToTwenty = random.nextDouble()
        csdpm.twentyToThirty = random.nextDouble()
        csdpm.thirtyToEnd= random.nextDouble()

        val xpdpm = GameStageDelta()
        xpdpm.zeroToTen = random.nextDouble()
        xpdpm.tenToTwenty = random.nextDouble()
        xpdpm.twentyToThirty = random.nextDouble()
        xpdpm.thirtyToEnd= random.nextDouble()

        val dtdpm = GameStageDelta()
        dtdpm.zeroToTen = random.nextDouble()
        dtdpm.tenToTwenty = random.nextDouble()
        dtdpm.twentyToThirty = random.nextDouble()
        dtdpm.thirtyToEnd= random.nextDouble()

        val timeline = Timeline(-1,cpm,xpm,gpm,csdpm,xpdpm,dpm,dtdpm,"SOLO", "TOP")
        val timelineId =  timelineDAO.saveTimeline(timeline, -1)
        if (timelineId == -1) {
            Assert.fail()
        }

        val timeline2 = timelineDAO.getTimelineByParticipantRowId(-1)
        Assert.assertTrue(timeline.lane == timeline2.lane)
        Assert.assertTrue(timeline.role == timeline2.role)
        Assert.assertEquals(timeline.creepsPerMin.tenToTwenty , timeline2.creepsPerMin.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.creepsPerMin.zeroToTen , timeline2.creepsPerMin.zeroToTen, 0.05)
        Assert.assertEquals(timeline.creepsPerMin.twentyToThirty , timeline2.creepsPerMin.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.creepsPerMin.thirtyToEnd ,timeline2.creepsPerMin.thirtyToEnd, 0.05)
        Assert.assertEquals(timeline.damageTakenDiffPerMin.zeroToTen, timeline2.damageTakenDiffPerMin.zeroToTen, 0.05)
        Assert.assertEquals(timeline.damageTakenDiffPerMin.tenToTwenty, timeline2.damageTakenDiffPerMin.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.damageTakenDiffPerMin.twentyToThirty, timeline2.damageTakenDiffPerMin.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.damageTakenDiffPerMin.thirtyToEnd, timeline2.damageTakenDiffPerMin.thirtyToEnd, 0.05)
        Assert.assertEquals(timeline.damageTakenPerMin.zeroToTen, timeline2.damageTakenPerMin.zeroToTen, 0.05)
        Assert.assertEquals(timeline.damageTakenPerMin.tenToTwenty, timeline2.damageTakenPerMin.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.damageTakenPerMin.twentyToThirty , timeline2.damageTakenPerMin.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.damageTakenPerMin.thirtyToEnd ,timeline2.damageTakenPerMin.thirtyToEnd, 0.05)
        Assert.assertEquals(timeline.goldPerMin.zeroToTen , timeline2.goldPerMin.zeroToTen, 0.05)
        Assert.assertEquals(timeline.goldPerMin.tenToTwenty , timeline2.goldPerMin.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.goldPerMin.twentyToThirty , timeline2.goldPerMin.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.goldPerMin.thirtyToEnd ,timeline2.goldPerMin.thirtyToEnd, 0.05)
        Assert.assertEquals(timeline.csDiffPerMin.zeroToTen , timeline2.csDiffPerMin.zeroToTen, 0.05)
        Assert.assertEquals(timeline.csDiffPerMin.tenToTwenty, timeline2.csDiffPerMin.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.csDiffPerMin.twentyToThirty, timeline2.csDiffPerMin.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.csDiffPerMin.thirtyToEnd, timeline2.csDiffPerMin.thirtyToEnd, 0.05)
        Assert.assertEquals(timeline.xpDiffPerMin.zeroToTen ,timeline2.xpDiffPerMin.zeroToTen, 0.05)
        Assert.assertEquals(timeline.xpDiffPerMin.tenToTwenty, timeline2.xpDiffPerMin.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.xpDiffPerMin.twentyToThirty, timeline2.xpDiffPerMin.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.xpDiffPerMin.thirtyToEnd, timeline2.xpDiffPerMin.thirtyToEnd, 0.05)
        Assert.assertEquals(timeline.xpPerMin.zeroToTen ,timeline2.xpPerMin.zeroToTen, 0.05)
        Assert.assertEquals(timeline.xpPerMin.tenToTwenty, timeline2.xpPerMin.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.xpPerMin.twentyToThirty, timeline2.xpPerMin.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.xpPerMin.thirtyToEnd, timeline2.xpPerMin.thirtyToEnd, 0.05)
        Assert.assertTrue(timeline.role == timeline2.role)
        Assert.assertTrue(timeline.role == timeline2.role)
    }
}