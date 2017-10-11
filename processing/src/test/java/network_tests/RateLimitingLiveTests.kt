package network_tests

import application.domain.MatchControl
import application.domain.SummonerControl
import com.github.salomonbrys.kodein.instance
import di.KodeinManager
import network.riotapi.SummonerService
import org.junit.Before
import org.junit.Test
import util.RIOT_API_KEY

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

    @Before
    fun setUp() {
        matchControl = km.kodein.instance()
        summonerControl = km.kodein.instance()
    }

    /**
     * Fetches the match summary list for a user, and then fetches each of the matches.
     */
    @Test
    fun `FetchAndSaveMatchsForAMatchSummaryList`() {
        summonerControl.registerSummoner("kloin")

        // fetch match summaries for me
        matchControl.downloadAndSaveMatchSummaries(1542360)
        val preFetchMatchTime = System.currentTimeMillis()
        matchControl.fetchAndSaveMatchesForASummoner(1542360, 10)
        assert(preFetchMatchTime < System.currentTimeMillis() -2000)

        // by the time we finish, we should have all matches saved and it should have taken us
    }

}