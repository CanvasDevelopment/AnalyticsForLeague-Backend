package network.riotapi

import model.champion.ChampList
import model.champion.Champion
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query

/**
 * @author Josiah Kendall
 */
interface ChampionService {

    @GET("/lol/platform/v3/champions?freeToPlay=false")
    fun fetchChampList(@Query("api_key") apiKey : String) : ChampList

    @GET("/lol/static-data/v3/champions/{id}")
    fun getChampById(@Path("id") champId : Int, @Query("tags") tags : String, @Query("api_key") apiKey: String) : Champion

}