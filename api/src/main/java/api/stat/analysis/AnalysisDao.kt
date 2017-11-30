package api.stat.analysis

import api.stat.analysis.model.CreepsPerMinuteDeltasCard
import api.stat.analysis.model.RawDelta
import database.DbHelper
import database.sql_builder.Builder
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

    /**
     * Fetch the average for a delta type [e.g heroCreeps]
     *
     * @param summonerId    The id of the heroSummoner
     * @param numberOfGames The number of games that we want to limit to.
     * @param lane          The lane that we want to get the results for.
     * @param deltaType     The delta type that we want to fetch.
     * @throws              IllegalStateException if we fail to find any data.
     */
    fun fetchAvgDeltas(summonerId: Long, numberOfGames: Int, lane: String, deltaType : String) : RawDelta {
        val sql = Builder()
                .stat("avg(${deltaType}EarlyGame) as EarlyGame," +
                "avg(${deltaType}MidGame) as MidGame," +
                        "avg(${deltaType}LateGame) as LateGame").tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId")
                .games(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sql)
        if (result.next()) {
            return result.produceRawDelta()
        }
        throw IllegalStateException("Failed to find stats, Please fix this query")
    }

    /**
     * Fetch the max for a delta type [e.g heroCreeps]
     *
     * @param summonerId    The id of the heroSummoner
     * @param numberOfGames The number of games that we want to limit to.
     * @param lane          The lane that we want to get the results for.
     * @param deltaType     The delta type that we want to fetch.
     * @throws              IllegalStateException if we fail to find any data.
     */
    fun fetchMaxDeltas(summonerId: Long, numberOfGames: Int, lane: String, deltaType : String) : RawDelta {
        val sql = Builder()
                .stat("max(${deltaType}EarlyGame) as EarlyGame," +
                        "max(${deltaType}MidGame) as MidGame," +
                        "max(${deltaType}LateGame) as LateGame").tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId")
                .games(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sql)
        if (result.next()) {
            return result.produceRawDelta()
        }
        throw IllegalStateException("Failed to find stats, Please fix this query")
    }

    /**
     * Fetch the min for a delta type [e.g heroCreeps]
     *
     * @param summonerId    The id of the heroSummoner
     * @param numberOfGames The number of games that we want to limit to.
     * @param lane          The lane that we want to get the results for.
     * @param deltaType     The delta type that we want to fetch.
     * @throws              IllegalStateException if we fail to find any data.
     */
    fun fetchMinDeltas(summonerId: Long, numberOfGames: Int, lane: String, deltaType : String) : RawDelta {
        val gameStages = ArrayList<String>()
        gameStages.add("EarlyGame")
        gameStages.add("MidGame")
        gameStages.add("LateGame")

        val sqlEarlyGame = Builder()
                .stat("min(${deltaType}EarlyGame) as EarlyGame")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${deltaType}EarlyGame Is Not Null and ${deltaType}EarlyGame > 0")
                .games(numberOfGames)
                .toSql()
        val resultEarlyGame = dbHelper.executeSqlQuery(sqlEarlyGame)

        val sqlMidGame = Builder()
                .stat("min(${deltaType}MidGame) as MidGame")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${deltaType}MidGame Is Not Null and ${deltaType}MidGame > 0")
                .games(numberOfGames)
                .toSql()

        val resultMidGame = dbHelper.executeSqlQuery(sqlMidGame)

        val sqlLateGame = Builder()
                .stat("min(${deltaType}LateGame) as LateGame")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${deltaType}LateGame Is Not Null and ${deltaType}LateGame > 0")
                .games(numberOfGames)
                .toSql()

        val resultLateGame = dbHelper.executeSqlQuery(sqlLateGame)

        if (resultEarlyGame.next() && resultMidGame.next() && resultLateGame.next()) {
            return RawDelta(resultEarlyGame.getFloat("EarlyGame"),
                    resultMidGame.getFloat("MidGame"),
                    resultLateGame.getFloat("LateGame"))
        }

        throw IllegalStateException("Failed to find stats, Please fix this query")
    }

    /**
     * Fetch the max for a delta type [e.g heroCreeps]
     *
     * @param summonerId    The id of the heroSummoner
     * @param numberOfGames The number of games that we want to limit to.
     * @param lane          The lane that we want to get the results for.
     * @param deltaType     The delta type that we want to fetch.
     * @throws              IllegalStateException if we fail to find any data.
     */
    fun fetchDeltas(summonerId: Long, numberOfGames: Int, lane: String, deltaType : String) : RawDelta {
        val sql = Builder()
                .stat("${deltaType}EarlyGame as EarlyGame," +
                        "${deltaType}MidGame as MidGame," +
                        "${deltaType}LateGame as LateGame").tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId")
                .games(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sql)
        if (result.next()) {
            return result.produceRawDelta()
        }
        throw IllegalStateException("Failed to find stats, Please fix this query")
    }

    private fun ResultSet.produceRawDelta() : RawDelta {
        return RawDelta(getFloat("EarlyGame"),
                getFloat("MidGame"),
                getFloat("LateGame"))
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

    private fun ResultSet.produceFloat(floatName : String) : Float = getFloat(floatName)
}