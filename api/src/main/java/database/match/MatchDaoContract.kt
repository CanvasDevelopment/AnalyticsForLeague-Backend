package database.match

import database.match.model.MatchIdentifier
import model.GameStageStats

/**
 * @author Josiah Kendall
 */
interface MatchDaoContract {

    fun loadTwentyIds(startingPoint: Int, summonerId: String) : ArrayList<MatchIdentifier>
    fun loadTwentyIds(startingPoint: Int, summonerId: String, heroChampId: Int) : ArrayList<MatchIdentifier>
    fun loadTwentyIds(startingPoint: Int, summonerId: String, heroChampId: Int, role: String) : ArrayList<MatchIdentifier>
    fun loadTwentyIds(role : String, startingPoint : Int, summonerId : String, heroChampId : Int, villanChampId : Int) : ArrayList<MatchIdentifier>
    fun loadEarlyGameStageStatsForAMatch(role: String, matchId: Long, summonerId : String) : GameStageStats
    fun loadMidGameStageStatsForAMatch(role: String, matchId: Long, summonerId : String) : GameStageStats
    fun loadLateGameStageStatsForAMatch(role: String, matchId: Long, summonerId : String) : GameStageStats
    fun loadTwentyIds(startingPoint: Int, summonerId: String, lane: String): ArrayList<MatchIdentifier>
}