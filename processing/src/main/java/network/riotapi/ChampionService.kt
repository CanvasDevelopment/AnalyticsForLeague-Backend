package network.riotapi

import model.champion.ChampList
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query

/**
 * @author Josiah Kendall
 */
interface ChampionService {

    @GET("/lol/platform/v3/champions?freeToPlay=false")
    fun fetchChampList(@Query("api_key") apiKey : String) : ChampList
}