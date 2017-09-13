package db.matchlist

import model.matchlist.MatchSummary

/**
 * @author Josiah Kendall
 */
interface MatchSummaryDaoContract {

    fun saveMatchSummary(matchSummary: MatchSummary) : Long
    fun getMatchSummary(id : Long) : MatchSummary
    fun checkMatchSummaryExists(gameId: Long) : Boolean
    fun getMatchSummaryByGameId(gameId : Long) : MatchSummary
    fun getAllMatchesBySummonerId(summonerId: Int) : ArrayList<MatchSummary>
}