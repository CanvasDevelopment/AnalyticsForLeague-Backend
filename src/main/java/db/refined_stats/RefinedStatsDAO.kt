package db.refined_stats

import db.DBHelper

/**
 * @author Josiah Kendall
 */
class RefinedStatsDAO(val dbHelper: DBHelper) {

    fun fetchGameStageStatAverageForHero(
            tableName : String,
            summonerId : Long,
            role : String,
            lane : String) : Float {
        // fetch summoner Id.
        return 0.00f
    }
}