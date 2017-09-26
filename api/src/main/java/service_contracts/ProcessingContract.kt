package service_contracts

/**
 * @author Josiah Kendall
 */
interface ProcessingContract {

    /**
     * Creates a new user in our database. This fetches the user from the riot service and stores it
     * in our database.
     *
     * @return True if the user was successfully found and saved, false if not
     */
    fun createNewUser(accountName : String) : Boolean

    /**
     * Fetches all matches for this user on that are currently on the riot games server but not ours. This
     * method triggers the download and save of these matches, and creates the processed stats before storing
     * them in the database.
     *
     * @return True if the sync was successful, false if not.
     */
    fun syncUser(accountId : Long) : Boolean
}