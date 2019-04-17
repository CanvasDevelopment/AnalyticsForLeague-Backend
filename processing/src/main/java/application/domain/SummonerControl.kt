package application.domain

import db.summoner.SummonerDAOContractImpl
import model.Summoner
import network.riotapi.SummonerServiceImpl
import util.RIOT_API_KEY

/**
 * @author Josiah Kendall
 */
class SummonerControl(private val summonerDao: SummonerDAOContractImpl,
                      private val summonerService: SummonerServiceImpl) {

    /**
     * Check if a summoner exists in our database.
     * @param summonerId id of the summoner that we want to check for.
     * @return True of the summoner exists, false if not.
     */
    fun checkSummonerExists(summonerId : String) : Boolean {
        val summoner = summonerDao.getSummoner(summonerId)
        return summoner != null
    }

    /**
     * Check if a summoner exists in our database.
     * @param summonerName name of the summoner that we want to check for.
     * @return True of the summoner exists, false if not.
     */
    fun checkSummonerExistsUsingSummonerName(summonerName: String) : Boolean {
        val summoner = summonerDao.getSummonerByName(summonerName)
        return summoner != null
    }

    /**
     * Register a summoner with our database
     * @param summonerName The id of the summoner that we want to register
     * @return If we were successful - true if so, false if not.
     */
    fun registerSummoner(summonerName: String): Boolean {
        val result = summonerService.fetchSummonerBySummonerName(RIOT_API_KEY, summonerName)
        val summoner : Summoner? = result.data
        if (summoner == null ||result.code == 404) {
            return false
        }
        if (result.code == 200) {
            if (summonerDao.getSummonerByName(summoner.id) == null) {
                val saveSummonerResult = summonerDao.saveSummoner(summoner)
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