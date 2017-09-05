package network.riotapi

import model.Summoner
import model.NetworkResult
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query

/**
 * @author Josiah Kendall
 *
 * The rest interface for interacting with /lol/summoner/v3/summoners api.
 * See https://developer.riotgames.com/api-methods/#summoner-v3 for more details
 */
interface SummonerService {

    /**
     * Fetch a summoner object by using the summoners name.
     * @param apiKey        The riot api key
     * @param summonerName  The name of the summoner
     */
    @GET("/lol/summoner/v3/summoners/by-name/{summonerName}")
    fun fetchSummonerBySummonerName(
            @Query("api_key") apiKey: String,
            @Path("summonerName") summonerName : String) : NetworkResult<Summoner>


}