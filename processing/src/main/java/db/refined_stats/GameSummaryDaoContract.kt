package db.refined_stats

import model.refined_stats.FullGameStat
import model.refined_stats.GameStageStat
import model.refined_stats.RefinedGeneralGameStageColumnNames
import model.refined_stats.RefinedStatSummary

/**
 * @author Josiah Kendall
 */
interface GameSummaryDaoContract {
    fun saveHeroSummaryStats(summonerId : Long, summaryStats : ArrayList<RefinedStatSummary>) : Boolean
    fun insertHeroSummaryStat(summaryStat : RefinedStatSummary) : Long
    fun saveVillanSummaryStat(heroSummonerId: Long, summaryStat : RefinedStatSummary) : Long
    fun saveGameStageStatList(summonerId: Long, statList : ArrayList<GameStageStat>,
                              generalGameStageColumnNames: RefinedGeneralGameStageColumnNames) : Boolean
    fun saveVillanSummaryStats(summonerId: Long, summaryStats: ArrayList<RefinedStatSummary>) : Boolean
    fun saveGameStageStat(summonerId: Long,
                          stat : GameStageStat,
                          columnNames: RefinedGeneralGameStageColumnNames) : Long
    fun saveTeamStatsListForHero(summonerId: Long,
                                 stats : ArrayList<FullGameStat>)
    fun saveTeamStatsItemForHero(heroSummonerId : Long,
                                 stat : FullGameStat) : Long
    fun saveTeamStatsItemForVillan(heroSummonerId : Long,
                                   stat : FullGameStat) : Long
    fun doesGameSummaryForSummonerExist(gameId : Long, summonerId: Long) : Boolean
}