package domain_tests

import application.domain.MatchControl
import db.match.MatchDAOContracts
import db.matchlist.MatchSummaryDaoContract
import db.refined_stats.GameSummaryDaoContract
import db.refined_stats.RefinedStatDAOContract
import db.summoner.SummonerDAOContract
import model.Summoner
import model.match.Match
import model.matchlist.MatchList
import model.matchlist.MatchSummary
import network.riotapi.MatchServiceApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*
import util.RIOT_API_KEY
import util.Tables
import util.columnnames.StaticColumnNames

/**
 * @author Josiah Kendall
 */
class MatchControlTests {

    lateinit var matchContol: MatchControl
    lateinit var matchDao: MatchDAOContracts.MatchDAOContract
    lateinit var matchSummaryDao: MatchSummaryDaoContract
    lateinit var matchServiceApi: MatchServiceApi
    lateinit var summonerDaoContract: SummonerDAOContract
    lateinit var refinedStatDaoContract: RefinedStatDAOContract
    lateinit var gameSummaryDaoContract: GameSummaryDaoContract

    @Before
    fun setUp() {

        // mock dependencies
        gameSummaryDaoContract = mock(GameSummaryDaoContract::class.java)
        matchServiceApi = mock(MatchServiceApi::class.java)
        matchDao = mock(MatchDAOContracts.MatchDAOContract::class.java)
        matchSummaryDao = mock(MatchSummaryDaoContract::class.java)
        summonerDaoContract = mock(SummonerDAOContract::class.java)
        refinedStatDaoContract = mock(RefinedStatDAOContract::class.java)

        // match control
        matchContol = MatchControl(matchDao,
                matchServiceApi,
                summonerDaoContract,
                refinedStatDaoContract,
                gameSummaryDaoContract)
    }

    @Test
    fun `Make sure we exit if we don't find summoner`() {
        `when`(summonerDaoContract.getSummoner(Matchers.anyLong())).thenReturn(null)
        matchContol.sync(1)
        verify(summonerDaoContract, times(1)).getSummoner(1)
        verify(matchServiceApi, times(0)).getMatchListForAccount(Matchers.anyString(), Matchers.anyLong())
    }

    @Test
    fun `Make sure we do fetch match list if we do have the summoner saved`() {
        val summoner = Summoner()
        summoner.accountId = 1
        `when`(summonerDaoContract.getSummoner(Matchers.anyLong())).thenReturn(summoner)
        `when`(summonerDaoContract.getSummoner(Matchers.anyLong())).thenReturn(summoner)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(Matchers.anyLong(), Matchers.anyLong())).thenReturn(false)
        val matchSummaries = ArrayList<MatchSummary>()
        val matchSummary1 = MatchSummary(1,
                "oce",
                1,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)

        matchSummaries.add(matchSummary1)
        matchSummaries.add(matchSummary2)
        matchSummaries.add(matchSummary3)
        val matchList = MatchList()
        matchList.matches = matchSummaries
        `when`(matchServiceApi.getMatchListForAccount(RIOT_API_KEY, 1)).thenReturn(matchList)
        matchContol.sync(1)
        verify(summonerDaoContract, times(1)).getSummoner(1)
        verify(matchServiceApi, times(1)).getMatchListForAccount(RIOT_API_KEY, 1)
    }

    @Test
    fun `Make sure we test to see if a match exists before fetching it`() {
        val summoner = Summoner()
        summoner.accountId = -1
        `when`(summonerDaoContract.getSummoner(Matchers.anyLong())).thenReturn(summoner)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(Matchers.anyLong(), Matchers.anyLong())).thenReturn(false)
        val matchSummaries = ArrayList<MatchSummary>()
        val matchSummary1 = MatchSummary(1,
                "oce",
                1,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)

        matchSummaries.add(matchSummary1)
        matchSummaries.add(matchSummary2)
        matchSummaries.add(matchSummary3)
        val matchList = MatchList()
        matchList.matches = matchSummaries
        `when`(matchServiceApi.getMatchListForAccount(RIOT_API_KEY, -1)).thenReturn(matchList)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(1, -1)).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(2, -1)).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(3, -1)).thenReturn(false)
        matchContol.sync(-1)
        verify(gameSummaryDaoContract, times(1)).doesGameSummaryForSummonerExist(1, -1)
        verify(gameSummaryDaoContract, times(1)).doesGameSummaryForSummonerExist(2, -1)
        verify(gameSummaryDaoContract, times(1)).doesGameSummaryForSummonerExist(3, -1)
    }

    @Test
    fun `Make sure we fetch a match if it does not exist in our refined db`() {
        val summoner = Summoner()
        summoner.accountId = -1
        `when`(summonerDaoContract.getSummoner(Matchers.anyLong())).thenReturn(summoner)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(Matchers.anyLong(), Matchers.anyLong())).thenReturn(false)
        val matchSummaries = ArrayList<MatchSummary>()
        val matchSummary1 = MatchSummary(1,
                "oce",
                1,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)

        matchSummaries.add(matchSummary1)
        matchSummaries.add(matchSummary2)
        matchSummaries.add(matchSummary3)
        val matchList = MatchList()
        matchList.matches = matchSummaries
        `when`(matchServiceApi.getMatchListForAccount(RIOT_API_KEY, -1)).thenReturn(matchList)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(1, -1)).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(2, -1)).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(3, -1)).thenReturn(false)
        matchContol.sync(-1)
        verify(matchServiceApi, times(1)).getMatchByMatchId(RIOT_API_KEY, 1)
        verify(matchServiceApi, times(1)).getMatchByMatchId(RIOT_API_KEY, 2)
        verify(matchServiceApi, times(1)).getMatchByMatchId(RIOT_API_KEY, 3)
    }

    @Test
    fun `Make sure that we don't try to fetch any matches that we already have saved for that summoner`() {
        val summoner = Summoner()
        summoner.accountId = -1
        `when`(summonerDaoContract.getSummoner(Matchers.anyLong())).thenReturn(summoner)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(Matchers.anyLong(), Matchers.anyLong())).thenReturn(false)
        val matchSummaries = ArrayList<MatchSummary>()
        val matchSummary1 = MatchSummary(1,
                "oce",
                1,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)

        matchSummaries.add(matchSummary1)
        matchSummaries.add(matchSummary2)
        matchSummaries.add(matchSummary3)
        val matchList = MatchList()
        matchList.matches = matchSummaries
        `when`(matchServiceApi.getMatchListForAccount(RIOT_API_KEY, -1)).thenReturn(matchList)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(1, -1)).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(2, -1)).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(3, -1)).thenReturn(true)
        matchContol.sync(-1)
        verify(matchServiceApi, times(1)).getMatchByMatchId(RIOT_API_KEY, 1)
        verify(matchServiceApi, times(1)).getMatchByMatchId(RIOT_API_KEY, 2)
        verify(matchServiceApi, times(0)).getMatchByMatchId(RIOT_API_KEY, 3)
    }

    @Test
    fun `Make sure that we save a match when we retrieve it`() {
        val summoner = Summoner()
        summoner.accountId = -1
        `when`(summonerDaoContract.getSummoner(Matchers.anyLong())).thenReturn(summoner)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(Matchers.anyLong(), Matchers.anyLong())).thenReturn(false)
        val matchSummaries = ArrayList<MatchSummary>()
        val matchSummary1 = MatchSummary(1,
                "oce",
                1,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                "SOLO",
                "TOP",
                -1)

        matchSummaries.add(matchSummary1)
        matchSummaries.add(matchSummary2)
        matchSummaries.add(matchSummary3)
        val matchList = MatchList()
        matchList.matches = matchSummaries
        `when`(matchServiceApi.getMatchListForAccount(RIOT_API_KEY, -1)).thenReturn(matchList)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(1, -1)).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(2, -1)).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(3, -1)).thenReturn(true)
        val matchDetail = Match(1,
                "oce",
                1234,
                1234,
                1,
                1,
                1,
                "fdfd",
                "",
                "",
                ArrayList(),
                ArrayList(),
                ArrayList())

        val matchDetail2 = Match(2,
                "oce",
                1234,
                1234,
                1,
                1,
                1,
                "fdfd",
                "",
                "",
                ArrayList(),
                ArrayList(),
                ArrayList())
        `when`(matchServiceApi.getMatchByMatchId(RIOT_API_KEY, 1)).thenReturn(matchDetail)
        `when`(matchServiceApi.getMatchByMatchId(RIOT_API_KEY, 2)).thenReturn(matchDetail2)
        matchContol.sync(-1)
        verify(matchDao, times(1)).saveMatch(matchDetail)
        verify(matchDao, times(1)).saveMatch(matchDetail2)
        // test commit
    }

    @Test
    fun `Make sure that villan gold per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchContol.getColumns(tables.GOLD_PER_MIN, false)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.VILLAN_GOLD_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.VILLAN_GOLD_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.VILLAN_GOLD_LATE_GAME)
    }

    @Test
    fun `Make sure that villan xp per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchContol.getColumns(tables.XP_PER_MIN, false)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.VILLAN_XP_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.VILLAN_XP_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.VILLAN_XP_LATE_GAME)
    }

    @Test
    fun `Make sure that villan cs per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchContol.getColumns(tables.CREEPS_PER_MIN, false)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.VILLAN_CREEPS_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.VILLAN_CREEPS_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.VILLAN_CREEPS_LATE_GAME)
    }

    @Test
    fun `Make sure that villan damage per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchContol.getColumns(tables.DAMAGE_TAKEN_PER_MIN, false)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.VILLAN_DAMAGE_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.VILLAN_DAMAGE_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.VILLAN_DAMAGE_LATE_GAME)
    }

    @Test
    fun `Make sure that hero gold per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchContol.getColumns(tables.GOLD_PER_MIN, true)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.HERO_GOLD_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.HERO_GOLD_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.HERO_GOLD_LATE_GAME)
    }

    @Test
    fun `Make sure that hero xp per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchContol.getColumns(tables.XP_PER_MIN, true)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.HERO_XP_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.HERO_XP_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.HERO_XP_LATE_GAME)
    }

    @Test
    fun `Make sure that hero cs per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchContol.getColumns(tables.CREEPS_PER_MIN, true)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.HERO_CREEPS_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.HERO_CREEPS_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.HERO_CREEPS_LATE_GAME)
    }

    @Test
    fun `Make sure that hero damage per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchContol.getColumns(tables.DAMAGE_TAKEN_PER_MIN, true)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.HERO_DAMAGE_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.HERO_DAMAGE_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.HERO_DAMAGE_LATE_GAME)
    }


}