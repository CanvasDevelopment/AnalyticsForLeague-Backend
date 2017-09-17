package di

import application.Sync
import application.domain.MatchControl
import application.region.RegionController
import com.github.salomonbrys.kodein.*
import com.google.appengine.api.memcache.MemcacheService
import com.google.appengine.api.memcache.MemcacheServiceFactory
import com.google.gson.GsonBuilder
import db.requests.DBHelper
import db.requests.RequestDAOContract
import db.requests.RequestDao
import db.champion.ChampionDAO
import db.match.*
import db.refined_stats.GameSummaryDAO
import db.refined_stats.GameSummaryDaoContract
import db.refined_stats.RefinedStatDAOContract
import db.refined_stats.RefinedStatsDAO
import db.stats.GameStageDeltaDAOImpl
import db.summoner.SummonerDAOContract
import db.summoner.SummonerDAOContractImpl
import network.NetworkInterface
import network.RequestHandler
import network.riotapi.ChampionService
import network.riotapi.MatchServiceApi
import retrofit.RestAdapter
import retrofit.client.UrlConnectionClient
import retrofit.converter.GsonConverter

/**
 * @author Josiah Kendall
 *
 * This is the our module for our initialisation of all the dependencies we need, using [Kodein] for the injection
 */

class KodeinManager {

    val kodein = Kodein {

        bind<DBHelper>() with singleton {
            DBHelper()
        }

        bind<ChampionDAO>() with provider {
            ChampionDAO(
                    kodein)
        }

        bind<BanDAO>() with provider {
            BanDAO(
                    kodein.instance())
        }

        bind<MasteryDAO>() with provider {
            MasteryDAO(
                    kodein.instance())
        }

        bind<PlayerDAO>() with provider {
            PlayerDAO(
                    kodein.instance())
        }

        bind<RuneDAO>() with provider {
            RuneDAO(
                    kodein.instance())
        }

        bind<StatDAO>() with provider {
            StatDAO(
                    kodein.instance())
        }

        bind<TeamDAO>() with provider {
            TeamDAO(
                    kodein.instance(),
                    kodein.instance())
        }

        bind<GameStageDeltaDAOImpl>() with provider {
            GameStageDeltaDAOImpl(
                    kodein.instance())
        }

        bind<TimelineDAO>() with provider {
            TimelineDAO(
                    kodein.instance(),
                    kodein.instance())
        }

        bind<ParticipantIdentityDAO>() with provider {
            ParticipantIdentityDAO(
                    kodein.instance(),
                    kodein.instance())
        }

        bind<ParticipantDAO>() with provider {
            ParticipantDAO(
                    kodein.instance(),
                    kodein.instance(),
                    kodein.instance(),
                    kodein.instance(),
                    kodein.instance())
        }

        bind<MatchDAO>() with provider {
            MatchDAO(
                    kodein.instance(),
                    kodein.instance(),
                    kodein.instance(),
                    kodein.instance())
        }

        bind<RefinedStatsDAO>() with provider {
            RefinedStatsDAO(
                    kodein.instance())
        }

        bind<RegionController>() with provider {
            RegionController()
        }

        bind<NetworkInterface>() with provider {
            NetworkInterface(kodein.instance())
        }

        bind<MatchControl>() with provider {
            MatchControl(
                    kodein.instance(),
                    kodein.instance(),
                    kodein.instance(),
                    kodein.instance(),
                    kodein.instance())
        }

        bind<SummonerDAOContract>() with provider {
            SummonerDAOContractImpl(kodein.instance())
        }

        bind<RefinedStatDAOContract>() with provider {
            RefinedStatsDAO(kodein.instance())
        }

        bind<GameSummaryDAO>() with provider {
            GameSummaryDAO(kodein.instance())
        }

        bind<GameSummaryDaoContract>() with provider {
            GameSummaryDAO(kodein.instance())
        }

        bind<MatchServiceApi>() with provider {
            val regionController = RegionController()
            RestAdapter.Builder()
                    .setEndpoint("https://${regionController.getRiotRegionName()}.api.riotgames.com")
                    .setConverter(GsonConverter(GsonBuilder().create()))
                    .setClient(UrlConnectionClient())
                    .build()
                    .create<MatchServiceApi>(MatchServiceApi::class.java)
        }

        bind<Sync>() with provider {
            Sync(kodein.instance())
        }

        bind<ChampionService>() with provider {
            val regionController = RegionController()
            RestAdapter.Builder()
                    .setEndpoint("https://${regionController.getRiotRegionName()}.api.riotgames.com")
                    .setConverter(GsonConverter(GsonBuilder().create()))
                    .setClient(UrlConnectionClient())
                    .build()
                    .create<ChampionService>(ChampionService::class.java)
        }

        bind<MemcacheService>() with provider {
            MemcacheServiceFactory.getMemcacheService()
        }

        bind<RequestDAOContract>() with provider {
            RequestDao(
                    kodein.instance(),
                    kodein.instance()
            )
        }

        bind<RequestHandler>() with provider {
            RequestHandler(kodein.instance())
        }
    }
}