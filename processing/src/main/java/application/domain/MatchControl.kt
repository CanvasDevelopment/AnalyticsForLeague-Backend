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
class MatchControl(val matchDAO: MatchDAO,
                   val matchSummaryDAO: MatchSummaryDAO,
                   val networkInterface: NetworkInterface) {

    /**
     * Fetch and save all [MatchSummary] and [Match] instances for a user.
     * @param accountId The id of the account that we want to fetch all the games for
     */
    fun UpdateMatchListForSummoner(accountId : Long) {
        val matchService = networkInterface.getMatchService()
        // TODO find a way to not have to specify the api key every request
        val matchList = matchService.getMatchListForAccount(RIOT_API_KEY, accountId)
        val matches = matchList.matches
        matches.forEach {
            // fetch the match
            try {
                // if match summary doesnt exist in db, add it to our array of stuff to fetch.
                // after that, fetch and save each new match.
                // run processing for each of the matches in the temp list
                // clear the temp list
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