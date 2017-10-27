package di

import api.stat.analysis.AnalysisDao
import api.stat.analysis.AnalysisPresenter
import com.github.salomonbrys.kodein.*
import database.DbHelper
import service_contracts.ProcessingImpl


/**
 * @author Josiah Kendall
 *
 * This is the our module for our initialisation of all the dependencies we need, using [Kodein] for the injection
 */

class KodeinManager {

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
                    kodein.instance<ProcessingImpl>() ,
                    kodein.instance<AnalysisDao>())
        }


    }
}