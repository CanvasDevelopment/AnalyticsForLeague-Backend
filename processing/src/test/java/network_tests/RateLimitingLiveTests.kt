package network_tests

import application.domain.MatchControl
import application.domain.SummonerControl
import com.github.salomonbrys.kodein.instance
import db.requests.DBHelper
import di.KodeinManager
import org.junit.Before
import org.junit.Test
import util.*

/**
 * @author Josiah Kendall
 *
 * Tests to make sure that our rate limiting works. Note that these tests are live so will take some time.
 *
 * Make sure that these tests are run with a developer key. Make sure that the correct rate limits are set.
 */
class RateLimitingLiveTests {

    val km = KodeinManager()
    lateinit var matchControl : MatchControl
    lateinit var summonerControl : SummonerControl
    val dbHelper = km.kodein.instance<DBHelper>()

    @Before
    fun setUp() {
        matchControl = km.kodein.instance()
        summonerControl = km.kodein.instance()
        val tables = Tables()
        dbHelper.connect()
        dbHelper.executeSQLScript("DELETE FROM matchtable")
        dbHelper.executeSQLScript("DELETE FROM matchsummary")

        dbHelper.executeSQLScript("DELETE FROM jungle_summarystats")
        dbHelper.executeSQLScript("DELETE FROM top_summarystats")
        dbHelper.executeSQLScript("DELETE FROM mid_summarystats")
        dbHelper.executeSQLScript("DELETE FROM adc_summarystats")
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

    /**
     * Fetches the match summary list for a user, and then fetches each of the matches.
     */
    @Test
    fun `FetchAndSaveMatchsForAMatchSummaryList`() {
        summonerControl.registerSummoner("kloin")
        val summonerId : Long = 1542360
        // fetch match summaries for me
        matchControl.downloadAndSaveMatchSummaries(summonerId)
        val preFetchMatchTime = System.currentTimeMillis()
        matchControl.fetchAndSaveMatchesForASummoner(summonerId, 30)

        matchControl.refineMatchData(summonerId, SOLO, MID)
        // by the time we finish, we should have all matches saved and it should have taken us
    }

}