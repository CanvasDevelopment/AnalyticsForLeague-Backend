package extensions

import api.stat.analysis.model.HeadToHeadStat
import util.Utils
import java.sql.ResultSet


/**
 * Iterate over the column / weight value hashset that we have and pull out the
 * columns from the result set, applying the weight to the values.
 * @param performanceProfile The [HashMap] representing the columns that exist on the [ResultSet], and their associated
 * weighting.
 * @return a [HeadToHeadStat] representing the hero value and the enemy value, based on the values in the result set
 * when applied with the formula
 */
fun ResultSet.produceHeadToHeadStat(performanceProfile : HashMap<String, Float>) : HeadToHeadStat{

    val resultSetController = ResultSetController(this)
    return resultSetController.produceHeadToHeadStat(performanceProfile)
}

