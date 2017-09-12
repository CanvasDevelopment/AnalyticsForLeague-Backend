package network

import application.region.RegionController
import com.google.gson.GsonBuilder
import network.riotapi.MatchServiceApi
import network.riotapi.SummonerService
import retrofit.RestAdapter
import retrofit.client.UrlConnectionClient
import retrofit.converter.GsonConverter

/**
 * @author Josiah Kendall
 */
class NetworkInterface(regionController: RegionController) {

    val adapterBuilder : RestAdapter.Builder = RestAdapter.Builder()
            .setEndpoint("https://${regionController.getRiotRegionName()}.api.riotgames.com")
            .setConverter(GsonConverter(GsonBuilder().create()))
            .setClient(UrlConnectionClient())

    val serviceAdapter = adapterBuilder.build()!!


    fun getMatchService() : MatchServiceApi {
        return serviceAdapter.create<MatchServiceApi>(MatchServiceApi::class.java)
    }

    fun getSummonerService() : SummonerService {
        return serviceAdapter.create<SummonerService>(SummonerService::class.java)
    }

}