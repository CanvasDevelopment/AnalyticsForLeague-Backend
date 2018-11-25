package match_tests

import api.match.MatchController
import api.stat.analysis.model.HeadToHeadStat
import database.match.MatchDao
import extensions.produceHeadToHeadStat
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import util.Constant
import util.TableNames
import java.sql.Array
import java.sql.ResultSet

class MatchControllerTests {
    val matchDao = mock(MatchDao::class.java)
    val tableNames = TableNames()
    val matchController = MatchController(matchDao,tableNames)

    @Test
    fun makeSureThatWeCanTestResultSet() {
        val resultSet : ResultSet = mock(ResultSet::class.java)
        `when`(resultSet.first()).thenReturn(true)
        assert(matchController.testResultSet(resultSet))
    }


    // This test is fucked, leave it for the moment
    @Test
    fun `Make sure that we can fetch match card summary data`() {
        val role = 1
        val summonerId = 1L
        val matchId = 1L
        val tableName = "top_summarystats"
        val resultSet = mock(ResultSet::class.java)
        val resultSet2 = mock(ResultSet::class.java)
        val headToHeadStat = HeadToHeadStat(50f, 50f)
        val earlyGamePerformanceProfile = produceMockPerformanceHashMap(Constant.GameStage.EARLY_GAME)
        val mgpp= produceMockPerformanceHashMap(Constant.GameStage.MID_GAME)
        val lgpp= produceMockPerformanceHashMap(Constant.GameStage.LATE_GAME)
        `when`(matchDao
                .produceMockPerformanceHashMap(Constant.GameStage.EARLY_GAME))
                .thenReturn(earlyGamePerformanceProfile)
        `when`(matchDao
                .produceMockPerformanceHashMap(Constant.GameStage.MID_GAME))
                .thenReturn(mgpp)
        `when`(matchDao
                .produceMockPerformanceHashMap(Constant.GameStage.LATE_GAME))
                .thenReturn(lgpp)

        val earlyGameColumns = ArrayList<String>()
        val midGameColumns= ArrayList<String>()
        val lateGameColumns = ArrayList<String>()

        earlyGamePerformanceProfile.keys.toTypedArray().toCollection(earlyGameColumns)
        mgpp.keys.toTypedArray().toCollection(midGameColumns)
        lgpp.keys.toTypedArray().toCollection(lateGameColumns)

        `when`(resultSet.produceHeadToHeadStat(earlyGamePerformanceProfile))
                .thenReturn(headToHeadStat)
        `when`(resultSet
                .produceHeadToHeadStat(
                        mgpp))
                .thenReturn(headToHeadStat)
        `when`(resultSet
                .produceHeadToHeadStat(
                        lgpp))
                .thenReturn(headToHeadStat)

        `when`(matchDao.fetchStatsForHeroAndVillan(
                summonerId,
                matchId,
                earlyGameColumns,
                tableName)).thenReturn(resultSet)
        `when`(matchDao.fetchStatsForHeroAndVillan(
                summonerId,
                matchId,
                midGameColumns,
                tableName)).thenReturn(resultSet)
        `when`(matchDao.fetchStatsForHeroAndVillan(
                summonerId,
                matchId,
                lateGameColumns,
                tableName)).thenReturn(resultSet)

         // todo make this more robust - should not have text
        `when`(resultSet2.getBoolean("heroWin")).thenReturn(true)
        `when`(resultSet2.getInt("heroChampId")).thenReturn(5)
        `when`(resultSet2.getInt("villanChampId")).thenReturn(10)
        `when`(matchDao.fetchWinAndChampIds(
                anyLong(),
                anyLong(),
                anyString()))
                .thenReturn(resultSet2)

        val cardSummary = matchController.loadMatchSummary(1,1,1)
        assert(cardSummary.won)
        assert(cardSummary.champId ==5)
        assert(cardSummary.enemyChampId==10)
//        assert(cardSummary.earlyGameHero == 50f)
//        assert(cardSummary.midGameVillan== 50f)
    }

    fun produceMockPerformanceHashMap(gameStage : String) : HashMap<String, Float> {
        val hero = "hero"
        val villan = "villan"
        val performanceProfile = HashMap<String, Float>()
        performanceProfile.put( "${hero}Creeps$gameStage",0.25f)
        performanceProfile.put( "${villan}Creeps$gameStage",0.25f)
        performanceProfile.put( "${hero}Gold$gameStage",0.3f)
        performanceProfile.put( "${villan}Gold$gameStage",0.3f)
        performanceProfile.put( "${hero}damageDealt$gameStage",0.30f)
        performanceProfile.put( "${villan}damageDealt$gameStage",0.30f)
        performanceProfile.put( "${hero}Xp$gameStage",0.15f)
        performanceProfile.put( "${villan}Xp$gameStage",0.15f)
        return performanceProfile
    }
}