package domain_tests

import application.domain.MatchControl
import db.match.MatchDAO
import db.matchlist.MatchSummaryDAO
import db.summoner.SummonerDAO
import network.NetworkInterface
import network.riotapi.MatchServiceApi
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*

/**
 * @author Josiah Kendall
 */
class MatchControlTests {

    lateinit var matchContol : MatchControl
    lateinit var matchDao : MatchDAO
    lateinit var matchSummaryDao : MatchSummaryDAO
    lateinit var matchServiceApi : MatchServiceApi
    lateinit var summonerDao : SummonerDAO

    @Before
    fun setUp() {
        // mock dependencies
        matchServiceApi = mock(MatchServiceApi::class.java)
        matchDao = mock(MatchDAO::class.java)
        matchSummaryDao = mock(MatchSummaryDAO::class.java)
        summonerDao = mock(SummonerDAO::class.java)

        // match control
        matchContol = MatchControl(matchDao,
                matchSummaryDao,
                matchServiceApi,
                summonerDao)
    }

    @Test
    fun `make sure we exit if we dont find summoner`() {
        `when`(summonerDao.getSummoner(Matchers.anyLong())).thenReturn(null)
        matchContol.sync(1)
        verify(matchServiceApi, times(1)).getMatchListForAccount(Matchers.anyString(), Matchers.anyLong())
    }
}