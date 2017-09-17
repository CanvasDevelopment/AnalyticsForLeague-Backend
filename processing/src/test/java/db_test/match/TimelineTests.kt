package db_test.match

import db.requests.DBHelper
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
        Assert.assertEquals(timeline.creepsPerMinDeltas.tenToTwenty , timeline2.creepsPerMinDeltas.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.creepsPerMinDeltas.zeroToTen , timeline2.creepsPerMinDeltas.zeroToTen, 0.05)
        Assert.assertEquals(timeline.creepsPerMinDeltas.twentyToThirty , timeline2.creepsPerMinDeltas.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.damageTakenDiffPerMinDeltas.zeroToTen, timeline2.damageTakenDiffPerMinDeltas.zeroToTen, 0.05)
        Assert.assertEquals(timeline.damageTakenDiffPerMinDeltas.tenToTwenty, timeline2.damageTakenDiffPerMinDeltas.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.damageTakenDiffPerMinDeltas.twentyToThirty, timeline2.damageTakenDiffPerMinDeltas.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.damageTakenPerMinDeltas.zeroToTen, timeline2.damageTakenPerMinDeltas.zeroToTen, 0.05)
        Assert.assertEquals(timeline.damageTakenPerMinDeltas.tenToTwenty, timeline2.damageTakenPerMinDeltas.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.damageTakenPerMinDeltas.twentyToThirty , timeline2.damageTakenPerMinDeltas.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.goldPerMinDeltas.zeroToTen , timeline2.goldPerMinDeltas.zeroToTen, 0.05)
        Assert.assertEquals(timeline.goldPerMinDeltas.tenToTwenty , timeline2.goldPerMinDeltas.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.goldPerMinDeltas.twentyToThirty , timeline2.goldPerMinDeltas.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.csDiffPerMinDeltas.zeroToTen , timeline2.csDiffPerMinDeltas.zeroToTen, 0.05)
        Assert.assertEquals(timeline.csDiffPerMinDeltas.tenToTwenty, timeline2.csDiffPerMinDeltas.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.csDiffPerMinDeltas.twentyToThirty, timeline2.csDiffPerMinDeltas.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.xpDiffPerMinDeltas.zeroToTen ,timeline2.xpDiffPerMinDeltas.zeroToTen, 0.05)
        Assert.assertEquals(timeline.xpDiffPerMinDeltas.tenToTwenty, timeline2.xpDiffPerMinDeltas.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.xpDiffPerMinDeltas.twentyToThirty, timeline2.xpDiffPerMinDeltas.twentyToThirty, 0.05)
        Assert.assertEquals(timeline.xpPerMinDeltas.zeroToTen ,timeline2.xpPerMinDeltas.zeroToTen, 0.05)
        Assert.assertEquals(timeline.xpPerMinDeltas.tenToTwenty, timeline2.xpPerMinDeltas.tenToTwenty, 0.05)
        Assert.assertEquals(timeline.xpPerMinDeltas.twentyToThirty, timeline2.xpPerMinDeltas.twentyToThirty, 0.05)
        Assert.assertTrue(timeline.role == timeline2.role)
        Assert.assertTrue(timeline.role == timeline2.role)
    }
}