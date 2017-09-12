package db.refined_stats

import model.refined_stats.FullGameStat
import model.refined_stats.GameStageStat
import model.refined_stats.RefinedGeneralGameStageColumnNames
import model.refined_stats.HeroTeamSummaryStat

/**
 * @author Josiah Kendall
 */
interface GameSummaryDaoContract {
    fun saveHeroTeamSummaryStats(summonerId : Long, summaryStatStats: ArrayList<HeroTeamSummaryStat>, tableName : String) : Boolean
    fun insertHeroTeamSummaryStat(summaryStatStat: HeroTeamSummaryStat, tableName : String) : Long
    fun saveVillanTeamSummaryStat(heroSummonerId: Long, summaryStatStat: HeroTeamSummaryStat, tableName : String) : Long
    fun saveGameStageStatList(summonerId: Long, statList : ArrayList<GameStageStat>,
                              generalGameStageColumnNames: RefinedGeneralGameStageColumnNames, tableName : String) : Boolean
    fun saveVillanTeamSummaryStats(summonerId: Long, summaryStatStats: ArrayList<HeroTeamSummaryStat>, tableName : String) : Boolean
    fun saveGameStageStat(summonerId: Long,
                          stat : GameStageStat,
                          columnNames: RefinedGeneralGameStageColumnNames, tableName : String) : Long
    fun savePlayerGameSummaryStatsListForHero(summonerId: Long,
                                              stats : ArrayList<FullGameStat>, tableName : String)
    fun savePlayerGameSummaryStatsListForVillan(summonerId: Long,
                                                stats: ArrayList<FullGameStat>, tableName: String)
    fun savePlayerGameSummaryStatsItemForHero(heroSummonerId : Long,
                                              stat : FullGameStat, tableName : String) : Long
    fun savePlayerGameSummaryStatsItemForVillan(heroSummonerId : Long,
                                                stat : FullGameStat, tableName : String) : Long
    fun doesGameSummaryForSummonerExist(gameId : Long, summonerId: Long) : Boolean
}