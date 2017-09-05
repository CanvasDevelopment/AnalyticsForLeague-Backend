package network.riotapi


import application.region.RegionController
import model.Summoner
import model.NetworkResult
import network.RequestHandler
import java.net.URL


/**
 * @author Josiah Kendall
 *
 * A customised version of the Retrofit [SummonerService], which has error handling and rate limiting.
 */
class SummonerServiceImpl(private val requestHandler: RequestHandler,
                                 private val regionController : RegionController) : SummonerService {

    /**
     * Fetch a summoner using the summoner name given.
     * @param apiKey        The api key to use
     * @param summonerName  The name of the summoner we want to retrieve from riot
     * @return The summoner, or null if not found.
     */
    override fun fetchSummonerBySummonerName(apiKey: String, summonerName: String): NetworkResult<Summoner> {
        val url = "https://" +
                regionController.getRiotRegionName() +
                ".api.riotgames.com/" +
                "lol/summoner/v3/summoners/by-name/$summonerName?api_key=$apiKey"
        return requestHandler.requestDataWithRateLimiting { requestHandler.sendHttpGetRequest(Summoner::class.java, URL(url)) }
    }
}