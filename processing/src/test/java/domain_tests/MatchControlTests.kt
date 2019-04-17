package domain_tests

import application.domain.MatchControl
import db.match.MatchDAO
import db.matchlist.MatchSummaryDAO
import db.refined_stats.GameSummaryDAO
import db.refined_stats.RefinedStatDAOContract
import db.summoner.SummonerDAOContract
import model.Summoner
import model.match.Match
import model.matchlist.MatchList
import model.matchlist.MatchSummary
import model.networking.NetworkResult
import model.refined_stats.FullGameStat
import model.refined_stats.GameStageStat
import model.refined_stats.TeamSummaryStat
import network.riotapi.MatchServiceApiImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*
import util.*
import util.columnnames.StaticColumnNames
import java.lang.IllegalStateException
import java.util.*

/**
 * @author Josiah Kendall
 */
class MatchControlTests {

    private val random = Random()

    private val tables = Tables()
    private val role = SOLO
    private val lane = TOP

    private lateinit var matchControl: MatchControl
    lateinit var matchDao: MatchDAO
    lateinit var matchSummaryDao: MatchSummaryDAO
    lateinit var matchServiceApi: MatchServiceApiImpl
    lateinit var summonerDaoContract: SummonerDAOContract
    lateinit var refinedStatDaoContract: RefinedStatDAOContract
    lateinit var gameSummaryDaoContract: GameSummaryDAO

    @Before
    fun setUp() {

        // mock dependencies
        gameSummaryDaoContract = mock(GameSummaryDAO::class.java)
        matchServiceApi = mock(MatchServiceApiImpl::class.java)
        matchDao = mock(MatchDAO::class.java)
        matchSummaryDao = mock(MatchSummaryDAO::class.java)
        summonerDaoContract = mock(SummonerDAOContract::class.java)
        refinedStatDaoContract = mock(RefinedStatDAOContract::class.java)

        // match control
        matchControl = MatchControl(matchDao,
                matchServiceApi,
                matchSummaryDao,
                summonerDaoContract,
                refinedStatDaoContract,
                gameSummaryDaoContract)
    }

    @Test
    fun `Make sure we exit if we don't find summoner`() {
        `when`(summonerDaoContract.getSummoner(Matchers.anyString())).thenReturn(null)
        matchControl.downloadAndSaveMatchSummaries("§")
        verify(summonerDaoContract, times(1)).getSummoner("1")
        verify(matchServiceApi, times(0)).getMatchListForAccount(Matchers.anyString(), Matchers.anyString())
    }

    @Test
    fun `Make sure we do fetch match list if we do have the summoner saved`() {
        val summoner = Summoner()
        summoner.accountId = "1"
        `when`(summonerDaoContract.getSummoner(Matchers.anyString())).thenReturn(summoner)
        `when`(summonerDaoContract.getSummoner(Matchers.anyString())).thenReturn(summoner)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(Matchers.anyLong(), Matchers.anyString())).thenReturn(false)
        val matchSummaries = ArrayList<MatchSummary>()
        val matchSummary1 = MatchSummary(1,
                "oce",
                1,
                34,
                1,
                9,
                123456,
                role,
                lane,
                "1")
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                role,
                lane,
                "1")
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                role,
                lane,
                "1")

        matchSummaries.add(matchSummary1)
        matchSummaries.add(matchSummary2)
        matchSummaries.add(matchSummary3)
        val matchList = MatchList(matchSummaries, 0, 10, 10)
        `when`(matchServiceApi.getMatchListForAccount(RIOT_API_KEY, "1")).thenReturn(NetworkResult(matchList, 200))
        matchControl.downloadAndSaveMatchSummaries("1")
        verify(summonerDaoContract, times(1)).getSummoner("1")
        verify(matchServiceApi, times(1)).getMatchListForAccount(RIOT_API_KEY, "1")
    }

    @Test
    fun `Make sure we test to see if a match exists before fetching it`() {
        val summoner = Summoner()
        summoner.accountId = "1"
        `when`(summonerDaoContract.getSummoner(Matchers.anyString())).thenReturn(summoner)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(Matchers.anyLong(), Matchers.anyString())).thenReturn(false)
        val matchSummaries = ArrayList<MatchSummary>()
        val matchSummary1 = MatchSummary(1,
                "oce",
                1,
                34,
                1,
                9,
                123456,
                role,
                lane,
                "1")
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                role,
                "TOP",
                "1")
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                role,
                lane,
                "1")

        matchSummaries.add(matchSummary1)
        matchSummaries.add(matchSummary2)
        matchSummaries.add(matchSummary3)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(1, "1")).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(2, "1")).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(3, "1")).thenReturn(false)
        matchControl.fetchAndSaveAllMatchesInAMatchSummaryList("1", matchSummaries)
        verify(gameSummaryDaoContract, times(1)).doesGameSummaryForSummonerExist(1, "1")
        verify(gameSummaryDaoContract, times(1)).doesGameSummaryForSummonerExist(2, "1")
        verify(gameSummaryDaoContract, times(1)).doesGameSummaryForSummonerExist(3, "1")
    }

    @Test
    fun `Make sure we fetch a match if it does not exist in our refined db`() {
        val summoner = Summoner()
        summoner.accountId = "1"
        `when`(summonerDaoContract.getSummoner(Matchers.anyString())).thenReturn(summoner)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(Matchers.anyLong(), Matchers.anyString())).thenReturn(false)
        val matchSummaries = ArrayList<MatchSummary>()
        val matchSummary1 = MatchSummary(1,
                "oce",
                1,
                34,
                1,
                9,
                123456,
                role,
                lane,
                "1")
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                role,
                "TOP",
                "1")
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                role,
                lane,
                "1")

        matchSummaries.add(matchSummary1)
        matchSummaries.add(matchSummary2)
        matchSummaries.add(matchSummary3)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(1, "1")).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(2, "1")).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(3, "1")).thenReturn(true)
        matchControl.fetchAndSaveAllMatchesInAMatchSummaryList("1", matchSummaries)
        verify(matchServiceApi, times(1)).getMatchByMatchId(RIOT_API_KEY, 1)
        verify(matchServiceApi, times(1)).getMatchByMatchId(RIOT_API_KEY, 2)
        verify(matchServiceApi, times(0)).getMatchByMatchId(RIOT_API_KEY, 3)
    }

    @Test
    fun `Make sure that we don't try to fetch any match summaries that we already have saved for that summoner`() {
        val summoner = Summoner()
        summoner.accountId = "1"
        `when`(summonerDaoContract.getSummoner(Matchers.anyString())).thenReturn(summoner)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(Matchers.anyLong(), Matchers.anyString())).thenReturn(false)
        val matchSummaries = ArrayList<MatchSummary>()
        val matchSummary1 = MatchSummary(1,
                "oce",
                1,
                34,
                1,
                9,
                123456,
                role,
                lane,
                "1")
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                role,
                lane,
                "1")
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                role,
                lane,
                "1")

        matchSummaries.add(matchSummary1)
        matchSummaries.add(matchSummary2)
        matchSummaries.add(matchSummary3)
        val matchList = MatchList(matchSummaries, 0, 10, 10)
        `when`(matchServiceApi.getMatchListForAccount(RIOT_API_KEY, "1")).thenReturn(NetworkResult(matchList, 200))
        `when`(matchSummaryDao.exists(1)).thenReturn(false)
        `when`(matchSummaryDao.exists(2)).thenReturn(false)
        `when`(matchSummaryDao.exists(3)).thenReturn(true)
        matchControl.downloadAndSaveMatchSummaries("1")
        verify(matchSummaryDao, times(1)).saveMatchSummary(matchSummary1)
        verify(matchSummaryDao, times(1)).saveMatchSummary(matchSummary2)
        verify(matchSummaryDao, times(0)).saveMatchSummary(matchSummary3)
    }

    @Test
    fun `Make sure that villan gold per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchControl.getColumns(tables.GOLD_PER_MIN, false)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.VILLAN_GOLD_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.VILLAN_GOLD_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.VILLAN_GOLD_LATE_GAME)
    }

    @Test
    fun `Make sure that villan xp per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchControl.getColumns(tables.XP_PER_MIN, false)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.VILLAN_XP_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.VILLAN_XP_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.VILLAN_XP_LATE_GAME)
    }

    @Test
    fun `Make sure that villan cs per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchControl.getColumns(tables.CREEPS_PER_MIN, false)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.VILLAN_CREEPS_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.VILLAN_CREEPS_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.VILLAN_CREEPS_LATE_GAME)
    }

    @Test
    fun `Make sure that villan damage per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchControl.getColumns(tables.DAMAGE_TAKEN_PER_MIN, false)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.VILLAN_DAMAGE_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.VILLAN_DAMAGE_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.VILLAN_DAMAGE_LATE_GAME)
    }

    @Test
    fun `Make sure that hero gold per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchControl.getColumns(tables.GOLD_PER_MIN, true)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.HERO_GOLD_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.HERO_GOLD_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.HERO_GOLD_LATE_GAME)
    }

    @Test
    fun `Make sure that hero xp per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchControl.getColumns(tables.XP_PER_MIN, true)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.HERO_XP_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.HERO_XP_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.HERO_XP_LATE_GAME)
    }

    @Test
    fun `Make sure that hero cs per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchControl.getColumns(tables.CREEPS_PER_MIN, true)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.HERO_CREEPS_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.HERO_CREEPS_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.HERO_CREEPS_LATE_GAME)
    }

    @Test
    fun `Make sure that hero damage per min generates the correct column names`() {
        val tables = Tables()
        val columnNames = StaticColumnNames()
        val generatedNames = matchControl.getColumns(tables.DAMAGE_TAKEN_PER_MIN, true)
        Assert.assertTrue(generatedNames.earlyGame == columnNames.HERO_DAMAGE_EARLY_GAME)
        Assert.assertTrue(generatedNames.midGame == columnNames.HERO_DAMAGE_MID_GAME)
        Assert.assertTrue(generatedNames.lateGame == columnNames.HERO_DAMAGE_LATE_GAME)
    }

    @Test
    fun `Make sure that we save hero summary stats`() {
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role, lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, "top")).thenReturn(true)
        matchControl.refineMatchData("1",role, lane)
        verify(gameSummaryDaoContract, times(1)).saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save villan summary stats`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(true)
        matchControl.refineMatchData("1", role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats("1", villanSummaryList, matchControl.getTableName(lane,role))
    }

    @Test(expected = IllegalStateException::class)
    fun `Make sure that we exit if we fail to save initial summary stats`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(false)
        matchControl.refineMatchData("1", role, lane)
        verify(gameSummaryDaoContract, times(0)).saveVillanTeamSummaryStats("1", villanSummaryList, matchControl.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save hero creep stats`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForHero(tables.CREEPS_PER_MIN,"1", role, lane)).thenReturn(heroCreeps)
        matchControl.refineMatchData("1", role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats("1", villanSummaryList, matchControl.getTableName(lane,role))
        val columns = matchControl.getColumns(tables.CREEPS_PER_MIN,true)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList("1",heroCreeps,columns, matchControl.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save hero gold stats correctly`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForHero(tables.GOLD_PER_MIN,"1", role, lane)).thenReturn(heroCreeps)
        matchControl.refineMatchData("1", role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats("1", villanSummaryList, matchControl.getTableName(lane,role))
        val columns = matchControl.getColumns(tables.GOLD_PER_MIN,true)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList("1",heroCreeps,columns, matchControl.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save hero xp stats correctly`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForHero(tables.XP_PER_MIN,"1", role, lane)).thenReturn(heroCreeps)
        matchControl.refineMatchData("1", role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats("1", villanSummaryList, matchControl.getTableName(lane,role))
        val columns = matchControl.getColumns(tables.XP_PER_MIN,true)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList("1",heroCreeps,columns, matchControl.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save hero damage stats correctly`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForHero(tables.DAMAGE_TAKEN_PER_MIN,"1", role, lane)).thenReturn(heroCreeps)
        matchControl.refineMatchData("1", role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats("1", villanSummaryList, matchControl.getTableName(lane,role))
        val columns = matchControl.getColumns(tables.DAMAGE_TAKEN_PER_MIN,true)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList("1",heroCreeps,columns, matchControl.getTableName(lane,role))
    }
    @Test
    fun `Make sure that we save villan creep stats`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForVillian(tables.CREEPS_PER_MIN,"1", role, lane)).thenReturn(heroCreeps)
        matchControl.refineMatchData("1", role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats("1", villanSummaryList, matchControl.getTableName(lane,role))
        val columns = matchControl.getColumns(tables.CREEPS_PER_MIN,false)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList("1",heroCreeps,columns, matchControl.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save villan gold stats correctly`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForVillian(tables.GOLD_PER_MIN,"1", role, lane)).thenReturn(heroCreeps)
        matchControl.refineMatchData("1", role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats("1", villanSummaryList, matchControl.getTableName(lane,role))
        val columns = matchControl.getColumns(tables.GOLD_PER_MIN,false)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList("1",heroCreeps,columns, matchControl.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save villan xp stats correctly`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForVillian(tables.XP_PER_MIN,"1", role, lane)).thenReturn(heroCreeps)
        matchControl.refineMatchData("1", role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats("1", villanSummaryList, matchControl.getTableName(lane,role))
        val columns = matchControl.getColumns(tables.XP_PER_MIN,false)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList("1",heroCreeps,columns, matchControl.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save villan damage stats correctly`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForVillian(tables.DAMAGE_TAKEN_PER_MIN,"1", role, lane)).thenReturn(heroCreeps)
        matchControl.refineMatchData("1", role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats("1", villanSummaryList, matchControl.getTableName(lane,role))
        val columns = matchControl.getColumns(tables.DAMAGE_TAKEN_PER_MIN,false)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList("1",heroCreeps,columns, matchControl.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save hero player stats correctly`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(true)
        val heroFullGameStatList = ArrayList<FullGameStat>()
        heroFullGameStatList.add(mock(FullGameStat::class.java))
        `when`(refinedStatDaoContract.fetchPlayerStatisticsForHero("1", role, lane)).thenReturn(heroFullGameStatList)
        matchControl.refineMatchData("1", role, lane)

        verify(gameSummaryDaoContract, times(1)).savePlayerGameSummaryStatsListForHero("1",heroFullGameStatList, matchControl.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save villan player stats correctly`() {
        val villanSummaryList = ArrayList<TeamSummaryStat>()
        villanSummaryList.add(mock(TeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<TeamSummaryStat>()
        heroSummaryList.add(mock(TeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan("1", role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero("1", role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats("1", heroSummaryList, matchControl.getTableName(lane,role))).thenReturn(true)
        val villanFullGameStatList = ArrayList<FullGameStat>()
        villanFullGameStatList.add(mock(FullGameStat::class.java))
        `when`(refinedStatDaoContract.fetchPlayerStatisticsForVillian("1", role, lane)).thenReturn(villanFullGameStatList)
        matchControl.refineMatchData("1", role, lane)

        verify(gameSummaryDaoContract, times(1)).savePlayerGameSummaryStatsListForVillan("1",villanFullGameStatList, matchControl.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we fetch all matches if we set 0 or negative`() {
        matchControl.fetchAndSaveMatchesForASummoner("1", 0)
        verify(matchSummaryDao, times(1)).getAllMatchesBySummonerId("1")
    }

    @Test
    fun `Make sure that we combine all matches`() {
        val topSumamries = ArrayList<MatchSummary>()
        topSumamries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        topSumamries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        topSumamries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        topSumamries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))

        val midSumamries = ArrayList<MatchSummary>()
        midSumamries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        midSumamries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        midSumamries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        midSumamries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))


        val jungleSummaries = ArrayList<MatchSummary>()
        jungleSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        jungleSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        jungleSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        jungleSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))

        val adcSummaries = ArrayList<MatchSummary>()
        adcSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        adcSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        adcSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        adcSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))

        val supSummaries = ArrayList<MatchSummary>()
        supSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        supSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        supSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))
        supSummaries.add(generateMatchSummaryForSummoner("1", "SHIT", "SHIT"))

        `when`(matchSummaryDao.getRecentMatchesBySummonerIdForRole("1", 4, SOLO, TOP)).thenReturn(topSumamries)
        `when`(matchSummaryDao.getRecentMatchesBySummonerIdForRole("1", 4, SOLO, MID)).thenReturn(midSumamries)
        `when`(matchSummaryDao.getRecentMatchesBySummonerIdForRole("1", 4, NONE, JUNGLE)).thenReturn(jungleSummaries)
        `when`(matchSummaryDao.getRecentMatchesBySummonerIdForRole("1", 4, DUO_CARRY, BOT)).thenReturn(adcSummaries)
        `when`(matchSummaryDao.getRecentMatchesBySummonerIdForRole("1", 4, DUO_SUPPORT, BOT)).thenReturn(supSummaries)
        matchControl.fetchAndSaveMatchesForASummoner("1", 4)
        verify(gameSummaryDaoContract, times(20)).doesGameSummaryForSummonerExist(1, "1")
    }

    @Test
    fun `Make sure that we can generate table name for solo top`() {
        Assert.assertTrue(matchControl.getTableName(TOP,SOLO) == TOP)
    }

    @Test
    fun `Make sure that we can generate table name for solo mid`() {
        Assert.assertTrue(matchControl.getTableName(MID,SOLO) == MID)
    }

    @Test
    fun `Make sure that we can generate table name for jungle`() {
        Assert.assertTrue(matchControl.getTableName(JUNGLE, NONE) == JUNGLE)
    }

    @Test
    fun `Make sure that we can generate table name for duo carry`() {
        Assert.assertTrue(matchControl.getTableName(BOT, DUO_CARRY) == ADC)
    }

    @Test
    fun `Make sure that we can generate table name for duo support`() {
        Assert.assertTrue(matchControl.getTableName(BOT, DUO_SUPPORT) == SUPPORT)
    }

    @Test(expected = IllegalStateException::class)
    fun `Make sure that we can fail if we try to generate using incorrect string`() {
        Assert.assertTrue(matchControl.getTableName("middle",role) == "")
    }

    @Test
    fun `Make sure that we do not fetch a match that already exists`() {
        val gameId = 123456L
        val summonerID = "1234567"
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(gameId, summonerID)).thenReturn(false)
        `when`(matchDao.exists(gameId)).thenReturn(true)
        matchControl.fetchAndSaveMatch(gameId, summonerID)
        verify(matchServiceApi, times(0)).getMatchByMatchId(RIOT_API_KEY, gameId)
    }

    @Test
    fun `Test that we do not fetch a match that already exists in refined table`() {
        val gameId = 123456L
        val summonerID = "1234567"
        `when`(matchDao.exists(gameId)).thenReturn(false)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(gameId, summonerID)).thenReturn(true)
        matchControl.fetchAndSaveMatch(gameId, summonerID)
        verify(matchServiceApi, times(0)).getMatchByMatchId(RIOT_API_KEY, gameId)
    }

    @Test
    fun `Test that we do fetch a match that does not exist in either`() {
        val gameId = 123456L
        val summonerID = "1234567"
        val matchDetails = NetworkResult<Match>(mock(Match::class.java), 200)
        `when`(matchServiceApi.getMatchByMatchId(RIOT_API_KEY, gameId)).thenReturn(matchDetails)
        `when`(gameSummaryDaoContract.doesGameSummaryForSummonerExist(gameId, summonerID)).thenReturn(false)
        `when`(matchDao.exists(gameId)).thenReturn(false)
        matchControl.fetchAndSaveMatch(gameId, summonerID)
        verify(matchServiceApi, times(1)).getMatchByMatchId(RIOT_API_KEY, gameId)

    }



//    @Test
//    fun `Make sure that we can do a full load for a specific role`() {
//        val summonerId : Long = 12345678
//
//        val fullGameStatsForHero = ArrayList<FullGameStat>()
//        fullGameStatsForHero.add(produceFullGameStat(1))
//        fullGameStatsForHero.add(produceFullGameStat(1))
//        val heroSummaryList = ArrayList<TeamSummaryStat>()
//
//        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(summonerId, SOLO, TOP )).thenReturn(heroSummaryList)
//
//        `when`(refinedStatDaoContract.fetchPlayerStatisticsForHero(
//                summonerId,
//                "SOLO",
//                "TOP")).thenReturn(fullGameStatsForHero)
//        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(summonerId, heroSummaryList, anyString())).thenReturn(true)
//
//        matchControl.refineMatchData()
//    }

    private fun produceHeroTeamSummary(summonerId: String, gameId: Long) : TeamSummaryStat {
        return TeamSummaryStat(summonerId,
                random.nextInt(),
                gameId,
                random.nextInt(),
                random.nextBoolean(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())
    }

    private fun produceFullGameStat(gameId: Long) : FullGameStat {
        return FullGameStat(gameId,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())
    }

    private fun generateMatchSummaryForSummoner(summonerId: String, role: String, lane: String) : MatchSummary {
        return MatchSummary(random.nextInt(),
                "oce",
                1,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                role,
                lane,
                summonerId)
    }
}