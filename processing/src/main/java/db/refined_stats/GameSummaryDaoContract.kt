package db.refined_stats

import model.refined_stats.FullGameStat
import model.refined_stats.GameStageStat
import model.refined_stats.RefinedGeneralGameStageColumnNames
import model.refined_stats.RefinedStatSummary

/**
 * @author Josiah Kendall
 */
interface GameSummaryDaoContract {
    fun saveHeroSummaryStats(summonerId : Long, summaryStats : ArrayList<RefinedStatSummary>, tableName : String) : Boolean
    fun insertHeroSummaryStat(summaryStat : RefinedStatSummary, tableName : String) : Long
    fun saveVillanSummaryStat(heroSummonerId: Long, summaryStat : RefinedStatSummary, tableName : String) : Long
    fun saveGameStageStatList(summonerId: Long, statList : ArrayList<GameStageStat>,
                              generalGameStageColumnNames: RefinedGeneralGameStageColumnNames, tableName : String) : Boolean
    fun saveVillanSummaryStats(summonerId: Long, summaryStats: ArrayList<RefinedStatSummary>, tableName : String) : Boolean
    fun saveGameStageStat(summonerId: Long,
                          stat : GameStageStat,
                          columnNames: RefinedGeneralGameStageColumnNames, tableName : String) : Long
    fun saveTeamStatsListForHero(summonerId: Long,
                                 stats : ArrayList<FullGameStat>, tableName : String)
    fun saveTeamStatsItemForHero(heroSummonerId : Long,
                                 stat : FullGameStat, tableName : String) : Long
    fun saveTeamStatsItemForVillan(heroSummonerId : Long,
                                   stat : FullGameStat, tableName : String) : Long
    fun doesGameSummaryForSummonerExist(gameId : Long, summonerId: Long) : Boolean
}