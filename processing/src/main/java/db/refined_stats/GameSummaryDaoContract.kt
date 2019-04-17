package db.refined_stats

import model.refined_stats.FullGameStat
import model.refined_stats.GameStageStat
import model.refined_stats.RefinedGeneralGameStageColumnNames
import model.refined_stats.TeamSummaryStat
import java.util.*

/**
 * @author Josiah Kendall
 */
interface GameSummaryDaoContract {
    fun saveHeroTeamSummaryStats(summonerId: String, summaryStatStats: ArrayList<TeamSummaryStat>, tableName: String) : Boolean
    fun insertHeroTeamSummaryStat(summaryStatStat: TeamSummaryStat, tableName : String) : Long
    fun saveVillanTeamSummaryStat(heroSummonerId: String, summaryStatStat: TeamSummaryStat, tableName: String) : Long
    fun saveGameStageStatList(summonerId: String, statList: ArrayList<GameStageStat>,
                              generalGameStageColumnNames: RefinedGeneralGameStageColumnNames, tableName: String) : Boolean
    fun saveVillanTeamSummaryStats(summonerId: String, summaryStatStats: ArrayList<TeamSummaryStat>, tableName: String) : Boolean
    fun saveGameStageStat(summonerId: String,
                          stat: GameStageStat,
                          columnNames: RefinedGeneralGameStageColumnNames, tableName: String) : Long
    fun savePlayerGameSummaryStatsListForHero(summonerId: String,
                                              stats: ArrayList<FullGameStat>, tableName: String)
    fun savePlayerGameSummaryStatsListForVillan(summonerId: String,
                                                stats: ArrayList<FullGameStat>, tableName: String)
    fun savePlayerGameSummaryStatsItemForHero(heroSummonerId: String,
                                              stat: FullGameStat, tableName: String) : Long
    fun savePlayerGameSummaryStatsItemForVillan(heroSummonerId: String,
                                                stat: FullGameStat, tableName: String) : Long
    fun doesGameSummaryForSummonerExist(gameId: Long, summonerId: String) : Boolean
}