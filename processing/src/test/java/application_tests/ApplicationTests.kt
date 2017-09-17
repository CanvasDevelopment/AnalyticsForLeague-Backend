package application_tests

import application.region.RegionController
import db.requests.DBHelper
import db.match.*
import db.matchlist.MatchSummaryDAO
import db.stats.GameStageDeltaDAOImpl
import network.NetworkInterface
import org.junit.After
import org.junit.Before


/**
 * @author Josiah Kendall
 */
class ApplicationTests {
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

    lateinit var matchSummaryDAO : MatchSummaryDAO

    lateinit var networkInterface : NetworkInterface

    val regionController : RegionController = RegionController()

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

        networkInterface = NetworkInterface(regionController)

        matchSummaryDAO = MatchSummaryDAO(dbHelper)
    }

    @After
    fun cleanUp() {
//        val tables = Tables()
//        dbHelper.executeSQLScript("DELETE FROM matchtable")
//        dbHelper.executeSQLScript("DELETE FROM mastery")
//        dbHelper.executeSQLScript("DELETE FROM participantIdentity")
//        dbHelper.executeSQLScript("DELETE FROM participant")
//        dbHelper.executeSQLScript("DELETE FROM ban")
//        dbHelper.executeSQLScript("DELETE FROM team")
//        dbHelper.executeSQLScript("DELETE FROM ${tables.CS_DIFF_PER_MIN}")
//        dbHelper.executeSQLScript("DELETE FROM ${tables.XP_PER_MIN}")
//        dbHelper.executeSQLScript("DELETE FROM ${tables.XP_DIFF_PER_MIN}")
//        dbHelper.executeSQLScript("DELETE FROM ${tables.DAMAGE_TAKEN_PER_MIN}")
//        dbHelper.executeSQLScript("DELETE FROM ${tables.DAMAGE_TAKEN_DIFF_PER_MIN}")
//        dbHelper.executeSQLScript("DELETE FROM ${tables.CREEPS_PER_MIN}")
//        dbHelper.executeSQLScript("DELETE FROM ${tables.GOLD_PER_MIN}")
//        dbHelper.executeSQLScript("DELETE FROM player")
//        dbHelper.executeSQLScript("DELETE FROM rune")
//        dbHelper.executeSQLScript("DELETE FROM stats")
//        dbHelper.executeSQLScript("DELETE FROM timeline")
    }
}