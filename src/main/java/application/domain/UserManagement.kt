package application.domain

import db.DBHelper
import db.match.MatchDAO
import db.matchlist.MatchSummaryDAO
import db.stats.CreepsPerMinDAOImpl
import model.match.Match
import model.matchlist.MatchSummary
import network.NetworkInterface
import retrofit.RetrofitError
import util.RIOT_API_KEY
import java.util.logging.Level
import java.util.logging.Logger


/**
 * @author Josiah Kendall
 */
class UserManagement(val matchDAO: MatchDAO,
                     val matchSummaryDAO: MatchSummaryDAO,
                     val networkInterface: NetworkInterface) {

    /**
     * Fetch and save all [MatchSummary] and [Match] instances for a user.
     * @param limit A limit for the amount of games to download
     * @param accountId The id of the account that we want to fetch all the games for
     */
    fun FetchAndSaveAllMatchesForAUser(limit : Int, accountId : Long) {
        val matchService = networkInterface.getMatchService()
        // TODO find a way to not have to specify the api key every request
        val matchList = matchService.getMatchListForAccount(RIOT_API_KEY, accountId)
        val matches = matchList.matches
        matches.forEach {
            // fetch the match
            try {
                val match = matchService.getMatchByMatchId(RIOT_API_KEY, it.gameId)
                Logger.getLogger(CreepsPerMinDAOImpl::class.java.name).log(Level.SEVERE, "saved match with Id : ${match.gameId}")
                matchDAO.saveMatch(match)
                Thread.sleep(1200)
            } catch (error : RetrofitError) {
                Logger.getLogger(CreepsPerMinDAOImpl::class.java.name).log(Level.SEVERE, null, error)

            }

        }
    }
}