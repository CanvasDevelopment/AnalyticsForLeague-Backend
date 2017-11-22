package db.matchlist

import model.matchlist.MatchSummary
import java.util.*

/**
 * @author Josiah Kendall
 */
interface MatchSummaryDaoContract {

    fun saveMatchSummary(matchSummary: MatchSummary) : Long
    fun getMatchSummary(id : Long) : MatchSummary
    fun exists(gameId: Long) : Boolean
    fun getMatchSummaryByGameId(gameId : Long) : MatchSummary
    fun getAllMatchesBySummonerId(summonerId: Long) : ArrayList<MatchSummary>
}