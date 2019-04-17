package service_contracts

import model.response_beans.SyncProgress

/**
 * @author Josiah Kendall
 */
interface ProcessingContract {

    /**
     * Creates a new user in our database. This fetches the user from the riot service and stores it
     * in our database.
     *
     * @return 200 if successfully found and saved. 404 if summoner was not found, 500 if an error occurred while saving
     */
    fun createNewUser(accountName : String) : Int

    /**
     * Fetches all matches for this user on that are currently on the riot games server but not ours. This
     * method triggers the download and save of these matches, and creates the processed stats before storing
     * them in the database.
     *
     * @return True if the sync was successful, false if not.
     */
    fun syncUser(summonerId : String) : Boolean

    /**
     * Ask the server to sync the match list for a user. This will trigger the processing service to fetch
     * and save the match summary list, and  return whether the result was successful or not.
     *
     * @param summonerId The id of the summoner for whom we are fetching the match list.
     * @return True if successfully saved, false if not.
     */
    fun syncUserMatchList(summonerId: String) : Boolean

    fun refineUserStats(summonerId : String): Boolean
    fun getSyncProgress(summonerId : String): SyncProgress
}