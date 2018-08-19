package database.match

import model.GameStageStats
import model.MatchSummary
import model.TotalMatchStats

/**
 * @author Josiah Kendall
 */
interface MatchDaoContract {

    fun loadTwentyIds(startingPoint : Int, summonerId: Long) : ArrayList<Long>
    fun loadTwentyIds(startingPoint : Int, summonerId: Long, heroChampId : Int) : ArrayList<Long>
    fun loadTwentyIds(startingPoint : Int, summonerId: Long, heroChampId : Int, role : String) : ArrayList<Long>
    fun loadTwentyIds(role : String, startingPoint : Int, summonerId : Long, heroChampId : Int, villanChampId : Int) : ArrayList<Long>
    fun loadTotalStatsForAMatch(role : String, matchId : Long, summonerId:Long) : TotalMatchStats
    fun loadEarlyGameStageStatsForAMatch(role: String, matchId: Long, summonerId: Long) : GameStageStats
    fun loadMidGameStageStatsForAMatch(role: String, matchId: Long, summonerId: Long) : GameStageStats
    fun loadLateGameStageStatsForAMatch(role: String, matchId: Long, summonerId: Long) : GameStageStats
    fun loadTwentyIds(startingPoint: Int, summonerId: Long, lane: String): ArrayList<Long>
}