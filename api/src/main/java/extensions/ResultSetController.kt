package extensions

import api.stat.analysis.model.HeadToHeadStat

import util.Utils
import java.sql.ResultSet

class ResultSetController(val resultSet: ResultSet) {

    /**
     * Produce a head to head stat for a game stage. This allows us to compare the performance
     * of the hero and their opponenet
     * @param performanceProfile The [HashMap] representing the columns and their weights
     */
    fun produceHeadToHeadStat(performanceProfile : HashMap<String, Float>) : HeadToHeadStat {
        val utils = Utils()
        // so we need to get the villan and hero scores for the same object
        // get the hero key.
        // then extract the hero word, add villan, and that is the comparison object
        var totalHeroScore = 0f
        var totalVillainScore = 0f
        val results = ArrayList<Float>()
        val villainResults = ArrayList<Float>()
        for (key in performanceProfile.keys) {
            if (key.startsWith("hero")) {
                val heroScore = if (resultSet.getObject(key) is Int) {
                    (resultSet.getObject(key) as Int).toFloat()
                } else {
                    resultSet.getFloat(key)
                }
//                val heroScore : Float = ( as Int
                val heroAdjustedScore : Float = heroScore * performanceProfile[key]!!

                val villanKey = utils.getVillainKey(key)
                val villanScore = if (resultSet.getObject(villanKey) is Int) {
                    (resultSet.getObject(villanKey) as Int).toFloat()
                } else {
                    resultSet.getFloat(villanKey)
                }
                val villanAdjustedScore : Float = villanScore * performanceProfile[villanKey]!!

                results.add(heroAdjustedScore / (villanAdjustedScore + heroAdjustedScore))
                villainResults.add(villanAdjustedScore / (villanAdjustedScore + heroAdjustedScore))
            }
        }
        val villanScore : Float = villainResults.sum() / villainResults.size
        val heroScore : Float = results.sum() / results.size
        return HeadToHeadStat(villanScore, heroScore)
    }
}