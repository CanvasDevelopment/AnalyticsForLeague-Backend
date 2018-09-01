package database.match

import database.match.model.MatchIdentifier
import model.GameStageStats
import model.MatchSummary

/**
 * @author Josiah Kendall
 */
interface MatchDaoContract {

    fun loadTwentyIds(startingPoint : Int, summonerId: Long) : ArrayList<MatchIdentifier>
    fun loadTwentyIds(startingPoint : Int, summonerId: Long, heroChampId : Int) : ArrayList<Long>
    fun loadTwentyIds(startingPoint : Int, summonerId: Long, heroChampId : Int, role : String) : ArrayList<Long>
    fun loadTwentyIds(role : String, startingPoint : Int, summonerId : Long, heroChampId : Int, villanChampId : Int) : ArrayList<Long>
    fun loadEarlyGameStageStatsForAMatch(role: String, matchId: Long, summonerId: Long) : GameStageStats
    fun loadMidGameStageStatsForAMatch(role: String, matchId: Long, summonerId: Long) : GameStageStats
    fun loadLateGameStageStatsForAMatch(role: String, matchId: Long, summonerId: Long) : GameStageStats
    fun loadTwentyIds(startingPoint: Int, summonerId: Long, lane: String): ArrayList<Long>
}