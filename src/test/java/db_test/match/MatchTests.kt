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
class MatchTests {

    lateinit var dbHelper : DBHelper
    lateinit var matchDAO : MatchDAO

    // participantIdentity DAO
    lateinit var piDAO : ParticipantIdentityDAO
    lateinit var playerDAO : PlayerDAO

    // Participant DAO
    lateinit var timelineDAO: TimelineDAO
    lateinit var runeDAO: RuneDAO
    lateinit var masteryDAO: MasteryDAO
    lateinit var statDAO: StatDAO
    lateinit var gameStageDelta : GameStageDeltaDAOImpl
    lateinit var participantDAO : ParticipantDAO

    // Team DAO
    lateinit var banDAO : BanDAO
    lateinit var teamDAO : TeamDAO

    @Before
    fun setUp() {
        dbHelper = DBHelper()
        dbHelper.connect()

        playerDAO = PlayerDAO(dbHelper)
        piDAO = ParticipantIdentityDAO(dbHelper, playerDAO)

        gameStageDelta = GameStageDeltaDAOImpl(dbHelper)
        timelineDAO = TimelineDAO(dbHelper, gameStageDelta)
        runeDAO = RuneDAO(dbHelper)
        masteryDAO = MasteryDAO(dbHelper)
        statDAO = StatDAO(dbHelper)
        participantDAO = ParticipantDAO(dbHelper,timelineDAO,runeDAO,masteryDAO,statDAO)

        banDAO = BanDAO(dbHelper)
        teamDAO = TeamDAO(dbHelper, banDAO)

        matchDAO = MatchDAO(dbHelper,
                teamDAO,
                participantDAO,
                piDAO)
    }

    @After
    fun cleanUp() {
        val tables = Tables()
        dbHelper.executeSQLScript("DELETE FROM matchtable")
        dbHelper.executeSQLScript("DELETE FROM mastery")
        dbHelper.executeSQLScript("DELETE FROM participantIdentity")
        dbHelper.executeSQLScript("DELETE FROM participant")
        dbHelper.executeSQLScript("DELETE FROM ban")
        dbHelper.executeSQLScript("DELETE FROM team")
        dbHelper.executeSQLScript("DELETE FROM ${tables.CS_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.XP_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.XP_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.DAMAGE_TAKEN_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.DAMAGE_TAKEN_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.CREEPS_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.GOLD_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM player")
        dbHelper.executeSQLScript("DELETE FROM rune")
        dbHelper.executeSQLScript("DELETE FROM stats")
        dbHelper.executeSQLScript("DELETE FROM timeline")
    }

    @Test
    fun testWeCanSaveAndFetchMatchById() {

        // set up teams
        val team1 = getTeam()
        val team2 = getTeam()
        val teams = arrayListOf<Team>()
        teams.add(team1)
        teams.add(team2)

        val participant1 = getParticipant()
        val participant2 = getParticipant()
        val participant3 = getParticipant()
        val participant4 = getParticipant()
        val participant5 = getParticipant()
        val participant6 = getParticipant()
        val participant7 = getParticipant()
        val participant8 = getParticipant()
        val participant9 = getParticipant()
        val participant10 = getParticipant()

        val participants = arrayListOf<Participant>()
        participants.add(participant1)
        participants.add(participant2)
        participants.add(participant3)
        participants.add(participant4)
        participants.add(participant5)
        participants.add(participant6)
        participants.add(participant7)
        participants.add(participant8)
        participants.add(participant9)
        participants.add(participant10)

        val participantIdentity1 = getParticipantIdentity()
        val participantIdentity2 = getParticipantIdentity()
        val participantIdentity3 = getParticipantIdentity()
        val participantIdentity4 = getParticipantIdentity()
        val participantIdentity5 = getParticipantIdentity()
        val participantIdentity6 = getParticipantIdentity()
        val participantIdentity7 = getParticipantIdentity()
        val participantIdentity8 = getParticipantIdentity()
        val participantIdentity9 = getParticipantIdentity()
        val participantIdentity10 = getParticipantIdentity()

        val participantIdentities = arrayListOf<ParticipantIdentity>()
        participantIdentities.add(participantIdentity1)
        participantIdentities.add(participantIdentity2)
        participantIdentities.add(participantIdentity3)
        participantIdentities.add(participantIdentity4)
        participantIdentities.add(participantIdentity5)
        participantIdentities.add(participantIdentity6)
        participantIdentities.add(participantIdentity7)
        participantIdentities.add(participantIdentity8)
        participantIdentities.add(participantIdentity9)
        participantIdentities.add(participantIdentity10)

        val match = Match(
                -1,
                "oce",
                123456,
                123456,
                1,
                1,
                6,
                "test",
                "test",
                "test",
                teams,
                participants,
                participantIdentities
        )

        matchDAO.saveMatch(match)
        val matchRecovered = matchDAO.getMatch(-1)
        Assert.assertTrue(match.gameType == matchRecovered.gameType)
        Assert.assertTrue(match.gameMode == matchRecovered.gameMode)
        Assert.assertTrue(match.gameVersion== matchRecovered.gameVersion)
        Assert.assertTrue(match.platformId== matchRecovered.platformId)
        Assert.assertTrue(match.gameCreation== matchRecovered.gameCreation)
        Assert.assertTrue(match.gameDuration== matchRecovered.gameDuration)
        Assert.assertTrue(match.participantIdentities.size== matchRecovered.participantIdentities.size)
        Assert.assertTrue(match.participants.size == matchRecovered.participants.size)
    }

    private fun getParticipant() : Participant {
        val random = Random()
        val masteries = ArrayList<Mastery>()
        val runes = ArrayList<Rune>()
        val mastery1 = Mastery(random.nextInt(), random.nextInt())
        val mastery2 = Mastery(random.nextInt(), random.nextInt())
        val mastery3 = Mastery(random.nextInt(), random.nextInt())
        val mastery4 = Mastery(random.nextInt(), random.nextInt())
        val mastery5 = Mastery(random.nextInt(), random.nextInt())
        masteries.add(mastery1)
        masteries.add(mastery2)
        masteries.add(mastery3)
        masteries.add(mastery4)
        masteries.add(mastery5)

        val rune1 = Rune(random.nextInt(), 5)
        val rune2 = Rune(random.nextInt(), 5)
        val rune3 = Rune(random.nextInt(), 5)
        val rune4 = Rune(random.nextInt(), random.nextInt())
        val rune5 = Rune(random.nextInt(), random.nextInt())

        runes.add(rune1)
        runes.add(rune2)
        runes.add(rune3)
        runes.add(rune4)
        runes.add(rune5)
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
        return participant
    }
    private fun getParticipantIdentity() : ParticipantIdentity {
        val player1 = produceRandomPlayer()
        val player2 = produceRandomPlayer()
        val player3 = produceRandomPlayer()

        val pi1 = ParticipantIdentity(1, -1, player1)
        return pi1
    }
    private fun getTeam() : Team {
        val random = Random()
        val ban1 = Ban(random.nextInt(), random.nextInt())
        val ban2 = Ban(random.nextInt(), random.nextInt())
        val ban3 = Ban(random.nextInt(), random.nextInt())
        val ban4 = Ban(random.nextInt(), random.nextInt())
        val ban5 = Ban(random.nextInt(), random.nextInt())
        val bans = ArrayList<Ban>()
        bans.add(ban1)
        bans.add(ban2)
        bans.add(ban3)
        bans.add(ban4)
        bans.add(ban5)

        val team = Team(-1,
                "fail",
                false,
                false,
                false,
                false,
                false,
                false,
                1,
                2,
                3,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                0,
                bans)

        return team
    }

    fun produceRandomPlayer() : Player {
        val summonerId : Long = Random().nextLong()
        return Player(
                "OCE", // doesnt matter
                556, // not used in current tests
                "Joe", // not used in current tests its just info
                summonerId, // used
                "OCE", // not used
                "BLAH", // not used
                1) // not used
    }
}