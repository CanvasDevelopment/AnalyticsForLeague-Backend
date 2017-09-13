package application.domain

import db.DBHelper
import db.champion.ChampionDAO

/**
 * @author Josiah Kendall
 *
 * This class handles the fetching of champion details from the riot servers, and saving these stats
 * into our database.
 */
class ChampControl(val championDAO: ChampionDAO) {

    /**
     * Fetch and save all champs, and all their details to the database. This will overwrite any previous
     * details about any previously saved champs with their values from the server.
     */
    fun fetchAndSaveAllChamps() {

    }

    /**
     * Fetch and save the champ details for a specific champ
     * @param champId The champ id assigned to a champ by riot.
     */
    fun fetchAndSaveChampDetails(champId : Int) {

    }
}