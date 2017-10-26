package api.stat.Analysis

import database.DbHelper
import database.sql_builder.Builder
import service_contracts.ProcessingImpl

/**
 * @author Josiah Kendall
 */
class AnalysisInteractor (private val processingApi : ProcessingImpl,
                          private val analysisDao: AnalysisDao){

    fun creepsPerMinuteDeltasCard(summonerId : Long, games : Int) : String{
       // get creeps per minute early, mid and late for hero, for the enemy, for the villan
        // return them
    }

    fun creepsPerMinuteDeltasDetail(summonerId : Long, games: Int) : String {

    }


}