package application.domain

import db.DBHelper
import db.match.MatchDAO
import db.matchlist.MatchSummaryDAO
import db.stats.CreepsPerMinDAOImpl
import db.summoner.SummonerDAO
import db.summoner.SummonerDAOImpl
import model.match.Match
import model.matchlist.MatchSummary
import network.NetworkInterface
import network.riotapi.MatchServiceApi
import retrofit.RetrofitError
import util.RIOT_API_KEY
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 */
class MatchControl(val matchDAO: MatchDAO,
                   val matchSummaryDAO: MatchSummaryDAO,
                   val matchServiceApi: MatchServiceApi,
                   val summonerDAO: SummonerDAO) {


    fun sync(summonerId : Long) {
        // if we dont get a summoner, return
        val summoner = summonerDAO.getSummoner(summonerId) ?: return

    }


    /**
     * Fetch and save all [MatchSummary] and [Match] instances for a user.
     * @param accountId The id of the account that we want to fetch all the games for
     */
    fun UpdateMatchListForSummoner(accountId : Long) {
        // TODO find a way to not have to specify the api key every request
        val matchList = matchServiceApi.getMatchListForAccount(RIOT_API_KEY, accountId)
        val matches = matchList.matches
        matches.forEach {
            // fetch the match
            try {
                // if match summary doesnt exist in db, add it to our array of stuff to fetch.
                // after that, fetch and save each new match.
                // run processing for each of the matches in the temp list
                // clear the temp list
                val match = matchServiceApi.getMatchByMatchId(RIOT_API_KEY, it.gameId)
                Logger.getLogger(CreepsPerMinDAOImpl::class.java.name).log(Level.SEVERE, "saved match with Id : ${match.gameId}")
                matchDAO.saveMatch(match)
            } catch (error : RetrofitError) {
                Logger.getLogger(CreepsPerMinDAOImpl::class.java.name).log(Level.SEVERE, null, error)
            }

        }
    }
}