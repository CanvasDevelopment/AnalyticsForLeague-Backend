package network

import com.google.gson.GsonBuilder
import network.riotapi.MatchServiceApi
import retrofit.RestAdapter
import retrofit.client.UrlConnectionClient
import retrofit.converter.GsonConverter

/**
 * @author Josiah Kendall
 */
class NetworkInterface(val regionServer: String) {

    val adapterBuilder : RestAdapter.Builder = RestAdapter.Builder()
            .setEndpoint("https://$regionServer.api.riotgames.com")
            .setConverter(GsonConverter(GsonBuilder().create()))
            .setClient(UrlConnectionClient())

    val serviceAdapter = adapterBuilder.build()!!


    fun getMatchService() : MatchServiceApi {
        return serviceAdapter.create<MatchServiceApi>(MatchServiceApi::class.java)
    }

}