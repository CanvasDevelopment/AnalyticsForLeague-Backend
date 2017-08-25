package di

import com.github.salomonbrys.kodein.*
import db.DBHelper
import db.champion.ChampionDAO
import db.match.*
import db.refined_stats.RefinedStatsDAO
import db.stats.GameStageDeltaDAOImpl
import db.stats.GameStageDeltasDAO

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


    }
}