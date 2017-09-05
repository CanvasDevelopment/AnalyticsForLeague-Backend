package application.domain

import db.summoner.SummonerDAO
import db.summoner.SummonerDAOImpl
import network.riotapi.SummonerService
import util.RIOT_API_KEY

/**
 * @author Josiah Kendall
 */
class SummonerManagement(private val summonerControl : SummonerDAOImpl,
                         private val summonerService: SummonerService) {

    /**
     * Check if a summoner exists in our database.
     * @param summonerId id of the summoner that we want to check for.
     * @return True of the summoner exists, false if not.
     */
    fun checkSummonerExists(summonerId : Long) : Boolean {
        val summoner = summonerControl.getSummoner(summonerId)
        return summoner != null
    }

    /**
     * Check if a summoner exists in our database.
     * @param summonerName name of the summoner that we want to check for.
     * @return True of the summoner exists, false if not.
     */
    fun checkSummonerExists(summonerName: String) : Boolean {
        val summoner = summonerControl.getSummoner(summonerName)
        return summoner != null
    }

    /**
     * Register a summoner with our database
     * @param summonerName The id of the summoner that we want to register
     * @return If we were successful - true if so, false if not.
     */
    fun registerSummoner(summonerName: String): Boolean {
        val result = summonerService.fetchSummonerBySummonerName(RIOT_API_KEY, summonerName)
        val summoner = result.data
        if (summoner == null ||result.code == 404) {
            return false
        }
        if (result.code == 200) {
            if (summonerControl.getSummoner(summoner.id) == null) {
                val saveSummonerResult = summonerControl.saveSummoner(summoner)
                if (saveSummonerResult != (-1).toLong()) {
                    return true
                }
            } else {
                return true // it already exists
            }
        }

        // if we get here, we failed to save the summoner
        return false
    }
}