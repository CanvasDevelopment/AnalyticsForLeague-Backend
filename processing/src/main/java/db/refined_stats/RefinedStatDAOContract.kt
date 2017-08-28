package db.refined_stats

import model.refined_stats.GameStages
import model.refined_stats.RefinedStatSummary

/**
 * @author Josiah Kendall
 */
interface RefinedStatDAOContract {

    fun fetchGameSummaryStatsForHero(summonerId : Long,
                                     role : String,
                                     lane : String) : ArrayList<RefinedStatSummary>

    fun fetchGameSummaryStatsForVillan(summonerId : Long,
                                     role : String,
                                     lane : String) : ArrayList<RefinedStatSummary>

    fun fetchHeroGameStageStats(summonerId: Long,
                                role: String,
                                lane: String,
                                tableName :String) : GameStages

}