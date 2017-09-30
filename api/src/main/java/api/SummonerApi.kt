package api

import database.DbHelper
import db.summoner.SummonerDao
import model.SummonerDetails

/**
 * @author Josiah Kendall
 */
class SummonerApi(val dbHelper : DbHelper,
                  val summonerDao: SummonerDao) {


    /**
     * Find out if a summoner is registered with our database.
     *
     * @param summonerName  The name of the summoner we are checking for
     * @return              True if the summoner exists, false if not.
     */
    fun isSummonerRegistered(summonerName: String) : Boolean {
        return summonerDao.getSummoner(summonerName) != null
    }

    /**
     * Register a summoner with our service. This triggers a fetch of the summoner from the riot server, and saves their
     * details in the database.
     *
     * @param summonerName  The name of the summoner who we are registering.
     * @return              The summoner id, or -1 if the registration process was unsuccessful.
     */
    fun registerSummoner(summonerName: String) : Long {
        TODO()
    }

    /**
     * Fetch the details for a summoner
     * @param summonerId The id of the summoner for whom we want the details
     * @return A [SummonerDetails] object with the relevant info for our summoner.
     */
    fun fetchSummonerDetails(summonerId : Long) : SummonerDetails {
        TODO()
    }
}