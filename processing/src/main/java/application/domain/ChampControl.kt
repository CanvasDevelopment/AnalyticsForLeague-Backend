package application.domain

import db.champion.ChampionDAO
import model.networking.Endpoints
import network.RequestHandler
import network.riotapi.ChampionService
import util.RIOT_API_KEY

/**
 * @author Josiah Kendall
 *
 * This class handles the fetching of champion details from the riot servers, and saving these stats
 * into our database.
 */
class ChampControl(private val championService: ChampionService,
                   private val championDAO: ChampionDAO,
                   private val requestHandler: RequestHandler) {

    val endpoints = Endpoints()
    // Rate limits for static apis is really low
    private val requestsAllowed = 1
    private val timeFrameInSeconds = 400

    /**
     * Fetch and save all champs, and all their details to the database. This will overwrite any previous
     * details about any previously saved champs with their values from the server.
     */
    fun fetchAndSaveAllChamps() {
        val champList = championService.fetchChampList(RIOT_API_KEY)
        requestHandler.setApiKeyRate(requestsAllowed, timeFrameInSeconds)
        champList.champions
                .map {
                    requestHandler.requestDataWithRateLimiting (
                            {championService.getChampById(it.id, "image", RIOT_API_KEY)},
                            endpoints.V3_CHAMPION)
                }
                .forEach { championDAO.saveChampion(it) }
    }
}