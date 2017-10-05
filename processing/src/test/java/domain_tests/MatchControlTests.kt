package domain_tests

import application.domain.MatchControl
import db.match.MatchDAO
import db.matchlist.MatchSummaryDAO
import db.refined_stats.GameSummaryDaoContract
import db.refined_stats.RefinedStatDAOContract
import db.summoner.SummonerDAOContract
import model.Summoner
import model.match.Match
import model.matchlist.MatchList
import model.matchlist.MatchSummary
import model.refined_stats.FullGameStat
import model.refined_stats.GameStageStat
import model.refined_stats.HeroTeamSummaryStat
import network.riotapi.MatchServiceApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*
import util.*
import util.columnnames.StaticColumnNames

/**
 * @author Josiah Kendall
 */
class MatchControlTests {

    private val tables = Tables()
    private val role = SOLO
    private val lane = TOP

    lateinit var matchContol: MatchControl
    lateinit var matchDao: MatchDAO
    lateinit var matchSummaryDao: MatchSummaryDAO
    lateinit var matchServiceApi: MatchServiceApi
    lateinit var summonerDaoContract: SummonerDAOContract
    lateinit var refinedStatDaoContract: RefinedStatDAOContract
    lateinit var gameSummaryDaoContract: GameSummaryDaoContract

    @Before
    fun setUp() {

        // mock dependencies
        gameSummaryDaoContract = mock(GameSummaryDaoContract::class.java)
        matchServiceApi = mock(MatchServiceApi::class.java)
        matchDao = mock(MatchDAO::class.java)
        matchSummaryDao = mock(MatchSummaryDAO::class.java)
        summonerDaoContract = mock(SummonerDAOContract::class.java)
        refinedStatDaoContract = mock(RefinedStatDAOContract::class.java)

        // match control
        matchContol = MatchControl(matchDao,
                matchServiceApi,
                matchSummaryDao,
                summonerDaoContract,
                refinedStatDaoContract,
                gameSummaryDaoContract)
    }

    @Test
    fun `Make sure we exit if we don't find summoner`() {
        `when`(summonerDaoContract.getSummoner(Matchers.anyLong())).thenReturn(null)
        matchContol.downloadAndSaveMatchSummaries(1)
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
                role,
                lane,
                -1)
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                role,
                lane,
                -1)
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                role,
                lane,
                -1)

        matchSummaries.add(matchSummary1)
        matchSummaries.add(matchSummary2)
        matchSummaries.add(matchSummary3)
        val matchList = MatchList()
        matchList.matches = matchSummaries
        `when`(matchServiceApi.getMatchListForAccount(RIOT_API_KEY, 1)).thenReturn(matchList)
        matchContol.downloadAndSaveMatchSummaries(1)
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
                role,
                lane,
                -1)
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                role,
                "TOP",
                -1)
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                role,
                lane,
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
        matchContol.downloadAndSaveMatchSummaries(-1)
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
                role,
                lane,
                -1)
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                role,
                lane,
                -1)
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                role,
                lane,
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
        matchContol.downloadAndSaveMatchSummaries(-1)
        verify(matchServiceApi, times(1)).getMatchByMatchId(RIOT_API_KEY, 1)
        verify(matchServiceApi, times(1)).getMatchByMatchId(RIOT_API_KEY, 2)
        verify(matchServiceApi, times(1)).getMatchByMatchId(RIOT_API_KEY, 3)
    }

    @Test
    fun `Make sure that we don't try to fetch any match summaries that we already have saved for that summoner`() {
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
                role,
                lane,
                -1)
        val matchSummary2 = MatchSummary(2,
                "oce",
                2,
                34,
                1,
                9,
                123456,
                role,
                lane,
                -1)
        val matchSummary3 = MatchSummary(3,
                "oce",
                3,
                34,
                1,
                9,
                123456,
                role,
                lane,
                -1)

        matchSummaries.add(matchSummary1)
        matchSummaries.add(matchSummary2)
        matchSummaries.add(matchSummary3)
        val matchList = MatchList()
        matchList.matches = matchSummaries
        `when`(matchServiceApi.getMatchListForAccount(RIOT_API_KEY, -1)).thenReturn(matchList)
        `when`(matchSummaryDao.exists(1)).thenReturn(false)
        `when`(matchSummaryDao.exists(2)).thenReturn(false)
        `when`(matchSummaryDao.exists(3)).thenReturn(true)
        matchContol.downloadAndSaveMatchSummaries(-1)
        verify(matchSummaryDao, times(1)).saveMatchSummary(matchSummary1)
        verify(matchSummaryDao, times(1)).saveMatchSummary(matchSummary2)
        verify(matchSummaryDao, times(0)).saveMatchSummary(matchSummary3)
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

    @Test
    fun `Make sure that we save hero summary stats`() {

        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role, lane)).thenReturn(heroSummaryList)
        matchContol.refineMatchData(1,role, lane)
        verify(gameSummaryDaoContract, times(1)).saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save villan summary stats`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(true)
        matchContol.refineMatchData(1, role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats(1, villanSummaryList,matchContol.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we exit if we fail to save initial summary stats`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(false)
        matchContol.refineMatchData(1, role, lane)
        verify(gameSummaryDaoContract, times(0)).saveVillanTeamSummaryStats(1, villanSummaryList,matchContol.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save hero creep stats`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForHero(tables.CREEPS_PER_MIN,1, role, lane)).thenReturn(heroCreeps)
        matchContol.refineMatchData(1, role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats(1, villanSummaryList,matchContol.getTableName(lane,role))
        val columns = matchContol.getColumns(tables.CREEPS_PER_MIN,true)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList(1,heroCreeps,columns,matchContol.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save hero gold stats correctly`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForHero(tables.GOLD_PER_MIN,1, role, lane)).thenReturn(heroCreeps)
        matchContol.refineMatchData(1, role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats(1, villanSummaryList,matchContol.getTableName(lane,role))
        val columns = matchContol.getColumns(tables.GOLD_PER_MIN,true)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList(1,heroCreeps,columns,matchContol.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save hero xp stats correctly`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForHero(tables.XP_PER_MIN,1, role, lane)).thenReturn(heroCreeps)
        matchContol.refineMatchData(1, role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats(1, villanSummaryList,matchContol.getTableName(lane,role))
        val columns = matchContol.getColumns(tables.XP_PER_MIN,true)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList(1,heroCreeps,columns,matchContol.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save hero damage stats correctly`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForHero(tables.DAMAGE_TAKEN_PER_MIN,1, role, lane)).thenReturn(heroCreeps)
        matchContol.refineMatchData(1, role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats(1, villanSummaryList,matchContol.getTableName(lane,role))
        val columns = matchContol.getColumns(tables.DAMAGE_TAKEN_PER_MIN,true)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList(1,heroCreeps,columns,matchContol.getTableName(lane,role))
    }
    @Test
    fun `Make sure that we save villan creep stats`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForVillian(tables.CREEPS_PER_MIN,1, role, lane)).thenReturn(heroCreeps)
        matchContol.refineMatchData(1, role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats(1, villanSummaryList,matchContol.getTableName(lane,role))
        val columns = matchContol.getColumns(tables.CREEPS_PER_MIN,false)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList(1,heroCreeps,columns,matchContol.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save villan gold stats correctly`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForVillian(tables.GOLD_PER_MIN,1, role, lane)).thenReturn(heroCreeps)
        matchContol.refineMatchData(1, role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats(1, villanSummaryList,matchContol.getTableName(lane,role))
        val columns = matchContol.getColumns(tables.GOLD_PER_MIN,false)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList(1,heroCreeps,columns,matchContol.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save villan xp stats correctly`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForVillian(tables.XP_PER_MIN,1, role, lane)).thenReturn(heroCreeps)
        matchContol.refineMatchData(1, role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats(1, villanSummaryList,matchContol.getTableName(lane,role))
        val columns = matchContol.getColumns(tables.XP_PER_MIN,false)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList(1,heroCreeps,columns,matchContol.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save villan damage stats correctly`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(true)
        val heroCreeps = ArrayList<GameStageStat>()
        heroCreeps.add(mock(GameStageStat::class.java))

        `when`(refinedStatDaoContract.fetchGameStageStatListForVillian(tables.DAMAGE_TAKEN_PER_MIN,1, role, lane)).thenReturn(heroCreeps)
        matchContol.refineMatchData(1, role, lane)
        verify(gameSummaryDaoContract, times(1)).saveVillanTeamSummaryStats(1, villanSummaryList,matchContol.getTableName(lane,role))
        val columns = matchContol.getColumns(tables.DAMAGE_TAKEN_PER_MIN,false)
        verify(gameSummaryDaoContract, times(1)).saveGameStageStatList(1,heroCreeps,columns,matchContol.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save hero player stats correctly`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(true)
        val heroFullGameStatList = ArrayList<FullGameStat>()
        heroFullGameStatList.add(mock(FullGameStat::class.java))
        `when`(refinedStatDaoContract.fetchPlayerStatisticsForHero(1, role, lane)).thenReturn(heroFullGameStatList)
        matchContol.refineMatchData(1, role, lane)

        verify(gameSummaryDaoContract, times(1)).savePlayerGameSummaryStatsListForHero(1,heroFullGameStatList,matchContol.getTableName(lane,role))
    }

    @Test
    fun `Make sure that we save villan player stats correctly`() {
        val villanSummaryList = ArrayList<HeroTeamSummaryStat>()
        villanSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        val heroSummaryList = ArrayList<HeroTeamSummaryStat>()
        heroSummaryList.add(mock(HeroTeamSummaryStat::class.java))
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForVillan(1, role, lane)).thenReturn(villanSummaryList)
        `when`(refinedStatDaoContract.fetchGameSummaryStatsForHero(1, role,lane)).thenReturn(heroSummaryList)
        `when`(gameSummaryDaoContract.saveHeroTeamSummaryStats(1, heroSummaryList,matchContol.getTableName(lane,role))).thenReturn(true)
        val villanFullGameStatList = ArrayList<FullGameStat>()
        villanFullGameStatList.add(mock(FullGameStat::class.java))
        `when`(refinedStatDaoContract.fetchPlayerStatisticsForVillian(1, role, lane)).thenReturn(villanFullGameStatList)
        matchContol.refineMatchData(1, role, lane)

        verify(gameSummaryDaoContract, times(1)).savePlayerGameSummaryStatsListForVillan(1,villanFullGameStatList,matchContol.getTableName(lane,role))
    }


    @Test
    fun `Make sure that we can generate table name for solo top`() {
        Assert.assertTrue(matchContol.getTableName(TOP,SOLO) == TOP)
    }

    @Test
    fun `Make sure that we can generate table name for solo mid`() {
        Assert.assertTrue(matchContol.getTableName(MID,SOLO) == MID)
    }

    @Test
    fun `Make sure that we can generate table name for jungle`() {
        Assert.assertTrue(matchContol.getTableName(JUNGLE, NONE) == JUNGLE)
    }

    @Test
    fun `Make sure that we can generate table name for duo carry`() {
        Assert.assertTrue(matchContol.getTableName(BOT, DUO_CARRY) == ADC)
    }

    @Test
    fun `Make sure that we can generate table name for duo support`() {
        Assert.assertTrue(matchContol.getTableName(BOT, DUO_SUPPORT) == SUPPORT)
    }

    @Test(expected = IllegalStateException::class)
    fun `Make sure that we can fail if we try to generate using incorrect string`() {
        Assert.assertTrue(matchContol.getTableName("middle",role) == "")
    }


}