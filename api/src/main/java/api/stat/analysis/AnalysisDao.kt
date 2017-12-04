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

        val heroEarlyGame = fetchStat(numberOfGames, "heroCreeps",lane,summonerId,"EarlyGame","avg")
        val heroMidGame = fetchStat(numberOfGames, "heroCreeps",lane,summonerId,"MidGame","avg")
        val heroLateGame = fetchStat(numberOfGames, "heroCreeps",lane,summonerId,"LateGame","avg")
        val villanEarlyGame = fetchStat(numberOfGames, "villanCreeps",lane,summonerId,"EarlyGame","avg")
        val villanMidGame = fetchStat(numberOfGames, "villanCreeps",lane,summonerId,"MidGame","avg")
        val villanLateGame = fetchStat(numberOfGames, "villanCreeps",lane,summonerId,"LateGame","avg")

        return CreepsPerMinuteDeltasCard(heroEarlyGame, villanEarlyGame, heroMidGame, villanMidGame, heroLateGame, villanLateGame)
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
        val earlyGame = fetchStat(numberOfGames, deltaType, lane, summonerId, "EarlyGame", "avg")
        val midGame = fetchStat(numberOfGames, deltaType, lane, summonerId, "MidGame","avg")
        val lateGame = fetchStat(numberOfGames, deltaType, lane, summonerId, "LateGame","avg")

        return RawDelta(earlyGame, midGame, lateGame)
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
        val earlyGame = fetchStat(numberOfGames, deltaType, lane, summonerId, "EarlyGame", "max")
        val midGame = fetchStat(numberOfGames, deltaType, lane, summonerId, "MidGame","max")
        val lateGame = fetchStat(numberOfGames, deltaType, lane, summonerId, "LateGame","max")

        return RawDelta(earlyGame, midGame, lateGame)
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

        val earlyGame = fetchStat(numberOfGames, deltaType, lane, summonerId, "EarlyGame", "min")
        val midGame = fetchStat(numberOfGames, deltaType, lane, summonerId, "MidGame","min")
        val lateGame = fetchStat(numberOfGames, deltaType, lane, summonerId, "LateGame","min")

        return RawDelta(earlyGame, midGame, lateGame)
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

    private fun fetchStat(numberOfGames : Int, deltaType: String, lane: String, summonerId: Long, gameStage: String, statAccumulator: String) : Float {
        val sqlEarlyGame = Builder()
                .stat("$statAccumulator(${deltaType+gameStage}) as $gameStage")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${deltaType+gameStage} Is Not Null and ${deltaType+gameStage} > 0")
                .games(numberOfGames)
                .toSql()
        val result = dbHelper.executeSqlQuery(sqlEarlyGame)
        if (result.next()) {
            return result.produceFloat(gameStage)
        }

        throw IllegalStateException("Found no data")
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