package application.domain

import application.region.RegionController
import db.champion.ChampionDAO
import model.champion.Champion
import model.networking.Endpoints
import network.RequestHandler
import network.riotapi.ChampionService
import util.RIOT_API_KEY
import java.net.URL
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 *
 * This class handles the fetching of champion details from the riot servers, and saving these stats
 * into our database.
 */
class ChampControl(private val championService: ChampionService,
                   private val championDAO: ChampionDAO,
                   private val requestHandler: RequestHandler) {
    private val log = Logger.getLogger(this::class.java.name)
    private val endpoints = Endpoints()

    /**
     * Fetch and save all champs, and all their details to the database. This will overwrite any previous
     * details about any previously saved champs with their values from the server.
     */
    fun fetchAndSaveAllChamps() {
        val regionController = RegionController()
        val champList = championService.fetchChampList(RIOT_API_KEY)
        champList.champions
                .map {
                    requestHandler.requestDataWithRateLimiting (
                            {
                                log.info("requesting champion with id of ${it.id}")
                                requestHandler.sendHttpGetRequest(Champion::class.java,
                                    URL("https://${regionController.getRiotRegionName()}.api.riotgames.com/lol/static-data/v3/champions/${it.id}?locale=en_US&tags=image&api_key=$RIOT_API_KEY"),
                                    endpoints.V3_STATIC_DATA)
                            },
                            endpoints.V3_STATIC_DATA)
                }
                .forEach {
                    if (it.data != null) {
                        log.info("Saving champ called ${it.data!!.name}")
                        championDAO.saveChampion(it.data!!)
                        }
                    }
    }
}