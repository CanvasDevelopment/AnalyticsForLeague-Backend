package api.stat.Analysis

import api.stat.Analysis.model.CreepsPerMinuteDeltasCard
import database.DbHelper
import database.sql_builder.Builder
import java.awt.Cursor
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 */
class AnalysisDao (private val dbHelper: DbHelper){


    fun fetchAvgCreepsPerMinStatCard(summonerId : Long, numberOfGames : Int, lane : String) : CreepsPerMinuteDeltasCard {

        val sql = Builder().stat(
                "avg(heroCreepsEarlyGame)," +
                "avg(heroCreepsMidGame)," +
                "avg(heroCreepsLateGame)," +
                "avg(villanCreepsEarlyGame)," +
                "avg(villanCreepsMidGame)," +
                "avg(villanCreepsLateGame)")
                .tableName(lane + "_summarystats")
                .where("heroSummonerId = $summonerId")
                .games(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sql)
        if (result.next()) {
            return result.produceCreepsPerMinDeltasCard()
        }
        // get the max stat from it

        throw IllegalStateException("Failed to find the stats for this list")
    }

    private fun ResultSet.produceCreepsPerMinDeltasCard() : CreepsPerMinuteDeltasCard {
        return CreepsPerMinuteDeltasCard(
                getFloat("avg(heroCreepsEarlyGame)"),
                getFloat("avg(villanCreepsEarlyGame)"),
                getFloat("avg(heroCreepsMidGame)"),
                getFloat("avg(villanCreepsMidGame)"),
                getFloat("avg(heroCreepsLateGame)"),
                getFloat("avg(villanCreepsLateGame)")
        )
    }
}