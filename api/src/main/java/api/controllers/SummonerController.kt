package api.controllers

import db.summoner.SummonerDao
import model.Response
import model.response_beans.SummonerDetails
import model.response_beans.SyncProgress
import service_contracts.ProcessingContract

/**
 * @author Josiah Kendall
 */
class SummonerController(private val summonerDao: SummonerDao,
                         private val processingInterface : ProcessingContract) {

    /**
     * Find out if a summoner is registered with our database.
     *
     * @param summonerName  The name of the summoner we are checking for
     * @return              True if the summoner exists, false if not.
     */
    fun isSummonerRegistered(summonerName: String) : Response<Boolean> {
        val exists = summonerDao.getSummoner(summonerName) != null
        if (exists) {
            return Response(200, exists)
        }
        return Response(404, exists)
    }

    /**
     * Register a summoner with our service. This triggers a fetch of the summoner from the riot server, and saves their
     * details in the database.
     *
     * @param summonerName  The name of the summoner who we are registering.
     * @return              The summoner id, or -1 if the registration process was unsuccessful.
     */
    fun registerSummoner(summonerName: String) : Response<SummonerDetails> {
        val creationResultCode = processingInterface.createNewUser(summonerName)
        if (creationResultCode == 200) {
            val summonerDetails = summonerDao.getSummoner(summonerName)
            if (summonerDetails != null) {
                return Response(200, summonerDetails)
            }
        }

        // failed
        return Response(creationResultCode, getBlankSummonerDetails())
    }

    /**
     * Fetch the details for a summoner
     * @param summonerId The id of the summoner for whom we want the details
     * @return A [SummonerDetails] object with the relevant info for our summoner.
     */
    fun fetchSummonerDetails(summonerId : Long) : Response<SummonerDetails> {
        val summonerDetails = summonerDao.getSummoner(summonerId)
                // If null, return 404
                ?: return Response(404, getBlankSummonerDetails())
        return Response(200, summonerDetails)
    }

    fun syncSummoner(summonerId: Long) : Response<String> {
//        val syncResult = processingInterface.syncUser(summonerId)
        val syncResult = processingInterface.syncUserMatchList(summonerId)
        if (syncResult) {

            return Response(200, "success")
        }
        return Response(500, "Error occurred - sync interface returned false")
    }

    fun refineUserStats(summonerId: Long): Response<String> {
        val syncResult = processingInterface.refineUserStats(summonerId)
        if (syncResult) {
            return Response(200, "success")
        }
        return Response(500, "Error occurred - processing request returned false")
    }

    private fun getBlankSummonerDetails() : SummonerDetails = SummonerDetails(-1,-1,"",-1,-1,-1)

    fun syncProgress(summonerId: Long) : Response<SyncProgress> {
        val syncProgress = processingInterface.getSyncProgress(summonerId)
        return Response(200, syncProgress)
    }

}