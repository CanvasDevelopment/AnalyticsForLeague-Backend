package di

import api.match.MatchController
import api.stat.analysis.AnalysisDao
import api.stat.analysis.AnalysisPresenter
import com.github.salomonbrys.kodein.*
import database.DbHelper
import database.match.MatchDao
import database.match.MatchSummaryDao
import service_contracts.ProcessingImpl
import util.TableNames


/**
 * @author Josiah Kendall
 *
 * This is the our module for our initialisation of all the dependencies we need, using [Kodein] for the injection
 */

class KodeinManager_api {

    val kodein = Kodein {

        bind<DbHelper>() with singleton {
            DbHelper()
        }

        bind<ProcessingImpl>() with provider {
            ProcessingImpl()
        }

        bind<AnalysisDao>() with provider {
            AnalysisDao(kodein.instance())
        }

        bind<AnalysisPresenter>() with provider {
            AnalysisPresenter(
                    kodein.instance() ,
                    kodein.instance())
        }

        bind<MatchSummaryDao>() with provider {
            MatchSummaryDao(kodein.instance())
        }

        bind<MatchController>() with provider {
            MatchController(
                    kodein.instance(),
                    kodein.instance())
        }

        bind<MatchDao>() with provider {
            MatchDao(kodein.instance())
        }

        bind<TableNames>() with provider {
            TableNames()
        }
    }
}