package api.controller

import com.google.gson.Gson
import db.summoner.SummonerDao
import model.Response
import model.response_beans.SummonerDetails
import model.response_beans.SummonerExistence
import service_contracts.ProcessingContract

/**
 * @author Josiah Kendall
 */
class SummonerController(private val summonerDao: SummonerDao,
                         private val processingInterface : ProcessingContract) {
    private val gson = Gson()

    /**
     * Find out if a summoner is registered with our database.
     *
     * @param summonerName  The name of the summoner we are checking for
     * @return              True if the summoner exists, false if not.
     */
    fun isSummonerRegistered(summonerName: String) : Response {
        val existence = SummonerExistence(summonerDao.getSummoner(summonerName) != null)
        if (existence.exists) {
            return Response(200, gson.toJson(existence))
        }
        return Response(404, gson.toJson(existence))
    }

    /**
     * Register a summoner with our service. This triggers a fetch of the summoner from the riot server, and saves their
     * details in the database.
     *
     * @param summonerName  The name of the summoner who we are registering.
     * @return              The summoner id, or -1 if the registration process was unsuccessful.
     */
    fun registerSummoner(summonerName: String) : Response {
        val creationResultCode = processingInterface.createNewUser(summonerName)
        if (creationResultCode == 200) {
            val summonerDetails = summonerDao.getSummoner(summonerName)
            if (summonerDetails != null) {
                return Response(200, gson.toJson(summonerDetails))
            }
        }

        // failed
        return Response(creationResultCode, "{}")

    }

    /**
     * Fetch the details for a summoner
     * @param summonerId The id of the summoner for whom we want the details
     * @return A [SummonerDetails] object with the relevant info for our summoner.
     */
    fun fetchSummonerDetails(summonerId : Long) : Response {
        val summonerDetails = summonerDao.getSummoner(summonerId)
                // If null, return 404
                ?: return Response(404, "{}")
        return Response(200, gson.toJson(summonerDetails))
    }
}