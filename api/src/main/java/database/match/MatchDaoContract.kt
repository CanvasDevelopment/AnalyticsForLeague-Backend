package database.match

import model.GameStageStats
import model.MatchSummary
import model.TotalMatchStats

/**
 * @author Josiah Kendall
 */
interface MatchDaoContract {

    fun loadTwentyIds(startingPoint: Int, summonerId: Long) : ArrayList<Long>
    fun loadTwentyIds(startingPoint: Int, summonerId: Long, heroChampId: Int) : ArrayList<Long>
    fun loadTwentyIds(table : String, startingPoint : Int, summonerId : Long, heroChampId : Int, villanChampId : Int) : ArrayList<Long>
    fun loadMatchSummary(table: String, matchId : Long, summonerId: Long) : MatchSummary
    fun loadTotalStatsForAMatch(table : String, matchId : Long, summonerId:Long) : TotalMatchStats
    fun loadEarlyGameStageStatsForAMatch(table: String, matchId: Long, summonerId: Long) : GameStageStats
    fun loadMidGameStageStatsForAMatch(table: String, matchId: Long, summonerId: Long) : GameStageStats
    fun loadLateGameStageStatsForAMatch(table: String, matchId: Long, summonerId: Long) : GameStageStats
}