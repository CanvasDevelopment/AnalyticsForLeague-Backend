package db_test.match

import db.DBHelper
import db.match.*
import db.stats.GameStageDeltaDAOImpl
import model.match.*
import model.stats.GameStageDelta
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import util.Tables
import java.util.*

/**
 * @author Josiah Kendall
 */
class ParticipantTests {

    lateinit var dbHelper : DBHelper
    lateinit var timelineDAO: TimelineDAO
    lateinit var runeDAO: RuneDAO
    lateinit var masteryDAO: MasteryDAO
    lateinit var statDAO: StatDAO
    lateinit var gameStageDelta : GameStageDeltaDAOImpl
    lateinit var participantDAO : ParticipantDAO
    val tables = Tables()

    @Before
    fun setUp() {
        dbHelper = DBHelper()
        dbHelper.connect()
        gameStageDelta = GameStageDeltaDAOImpl(dbHelper)
        timelineDAO = TimelineDAO(dbHelper, gameStageDelta)
        runeDAO = RuneDAO(dbHelper)
        masteryDAO = MasteryDAO(dbHelper)
        statDAO = StatDAO(dbHelper)
        participantDAO = ParticipantDAO(dbHelper,timelineDAO,runeDAO,masteryDAO,statDAO)
    }

    @After
    fun cleanUp() {
        dbHelper.executeSQLScript("Delete from ${tables.CREEPS_PER_MIN}")
        dbHelper.executeSQLScript("Delete from ${tables.CS_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("Delete from ${tables.DAMAGE_TAKEN_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("Delete from ${tables.DAMAGE_TAKEN_PER_MIN}")
        dbHelper.executeSQLScript("Delete from ${tables.GOLD_PER_MIN}")
        dbHelper.executeSQLScript("Delete from ${tables.XP_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("Delete from ${tables.XP_PER_MIN}")
    }

    @Test
    fun ensureWeCanSaveAndFetchAParticipant() {
        val masteries = ArrayList<Mastery>()
        val runes = ArrayList<Rune>()
        val mastery1 = Mastery(1, 5)
        val mastery2 = Mastery(2, 5)
        val mastery3 = Mastery(3, 5)
        val mastery4 = Mastery(4, 5)
        val mastery5 = Mastery(5, 5)
        masteries.add(mastery1)
        masteries.add(mastery2)
        masteries.add(mastery3)
        masteries.add(mastery4)
        masteries.add(mastery5)

        val rune1 = Rune(1, 5)
        val rune2 = Rune(2, 5)
        val rune3 = Rune(3, 5)
        val rune4 = Rune(4, 5)
        val rune5 = Rune(5, 5)

        runes.add(rune1)
        runes.add(rune2)
        runes.add(rune3)
        runes.add(rune4)
        runes.add(rune5)
        val cpm = GameStageDelta()
        val random = Random()
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

        // our Participant
        val participant = Participant(
                -1,
                -1,
                -1,
                -1,
                -2,
                masteries,
                runes,
                "BRONZE",
                stat,
                timeline)

        val savedId = participantDAO.saveParticipant(participant, -1, -1)
        val participantRecovered = participantDAO.getParticipantByRowId(savedId)
        Assert.assertTrue(participant.highestAchievedSeasonTier == participantRecovered.highestAchievedSeasonTier)
        Assert.assertTrue(participant.championId == participantRecovered.championId)
        Assert.assertTrue(participant.teamId == participantRecovered.teamId)
        Assert.assertTrue(participant.masteries[0].masteryId == participantRecovered.masteries[0].masteryId)
        Assert.assertTrue(participant.masteries[0].rank == participantRecovered.masteries[0].rank)
        Assert.assertTrue(participant.runes[0].rank == participantRecovered.runes[0].rank)
        Assert.assertTrue(participant.runes[0].runeId == participantRecovered.runes[0].runeId)
        Assert.assertTrue(participant.stats.win == participantRecovered.stats.win)
        Assert.assertTrue(participant.stats.firstBloodKill == participantRecovered.stats.firstBloodKill)
        Assert.assertTrue(participant.stats.firstInhibitorKill == participantRecovered.stats.firstInhibitorKill)
        Assert.assertTrue(participant.stats.firstTowerKill== participantRecovered.stats.firstTowerKill)
        Assert.assertTrue(participant.stats.firstTowerAssist== participantRecovered.stats.firstTowerAssist)
    }
}