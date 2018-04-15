package api.stat.analysis

import api.stat.analysis.model.FullStatCard
import api.stat.analysis.model.HeadToHeadStat
import api.stat.analysis.model.RawDelta
import database.DbHelper
import database.sql_builder.Builder
import java.sql.ResultSet
import util.GameStages

/**
 * @author Josiah Kendall
 */
class AnalysisDao (private val dbHelper: DbHelper){

    /**
     * Fetch a stat value for the hero and the equivalent value for the villan. This is intended for the delta type stats
     * that have three game stages - e.g Creeps, damage etc.
     *
     * @param summonerId    Our hero summoner id
     * @param numberOfGames The number of games to calculate this stat from. Goes from most recent first, so a numberOfGames = 20
     *                      would mean that we only fetch from the most recent 20 games.
     * @param lane          The lane to get the results for
     * @param accumulatorType   The stat accumulator type. This can be average, max or min
     * @param gameStage         The game stage we want this stat for - see [GameStages]
     * @param statName          The statName that we want to
     */ // todo TEST me for all
    fun fetchHeadToHeadStat(summonerId: Long, numberOfGames: Int, lane: String, accumulatorType: String, gameStage: String, statName: String) : HeadToHeadStat {
        val hero = fetchStat(numberOfGames, "hero$statName", lane, summonerId, gameStage, accumulatorType)
        val villan = fetchStat(numberOfGames, "villan$statName", lane, summonerId, gameStage, accumulatorType)
        return HeadToHeadStat(villan, hero)
    }

    /**
     * Fetch a stat value for the hero and the equivalent value for the villan. This is intended for the delta type stats
     * that have three game stages - e.g Creeps, damage etc.
     *
     * @param summonerId        Our hero summoner id
     * @param numberOfGames     The number of games to calculate this stat from. Goes from most recent first, so a numberOfGames = 20
     *                          would mean that we only fetch from the most recent 20 games.
     * @param accumulatorType   The stat accumulator type. This can be average, max or min
     * @param gameStage         The game stage we want this stat for - see [GameStages]
     * @param statName          The statName that we want to
     * @param champId           The champ we want to filter our results to.
     *
     * @return a [HeadToHeadStat] representing the results of the filter.
     */ // todo TEST me for all
    fun fetchHeadToHeadStat(summonerId: Long, numberOfGames: Int, lane: String, accumulatorType: String, gameStage: String, statName: String, champId: Int) : HeadToHeadStat {
        val hero = fetchStat(numberOfGames, "hero$statName", lane, summonerId, gameStage, accumulatorType, champId)
        val villan = fetchStat(numberOfGames, "villan$statName", lane, summonerId, gameStage, accumulatorType, champId)
        return HeadToHeadStat(villan, hero)
    }

    fun fetchAvgCreepsPerMinStatCard(summonerId : Long, numberOfGames : Int, lane : String) : FullStatCard {

        val heroEarlyGame = fetchStat(numberOfGames, "heroCreeps",lane,summonerId,"EarlyGame","avg")
        val heroMidGame = fetchStat(numberOfGames, "heroCreeps",lane,summonerId,"MidGame","avg")
        val heroLateGame = fetchStat(numberOfGames, "heroCreeps",lane,summonerId,"LateGame","avg")
        val villanEarlyGame = fetchStat(numberOfGames, "villanCreeps",lane,summonerId,"EarlyGame","avg")
        val villanMidGame = fetchStat(numberOfGames, "villanCreeps",lane,summonerId,"MidGame","avg")
        val villanLateGame = fetchStat(numberOfGames, "villanCreeps",lane,summonerId,"LateGame","avg")

        return FullStatCard(heroEarlyGame, villanEarlyGame, heroMidGame, villanMidGame, heroLateGame, villanLateGame)
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

    /**
     * A flexible fetch method used to build sql queries and return a given stat.
     *
     * @param numberOfGames     The number of recent games to limit the filter to.
     * @param statType          The type of stat that we are fetching.
     * @param lane              The lane that we want the results for.
     * @param summonerId        The summoner id for whom the results are for.
     * @param gameStage         The stage of the game that this specific stat was for.
     * @param statAccumulator   The type of stat presentation- average, max min etc
     *
     * @return A [Float] value, based on the given parameters.
     */
    private fun fetchStat(numberOfGames : Int, statType: String, lane: String, summonerId: Long, gameStage: String, statAccumulator: String) : Float {
        val sqlEarlyGame = Builder()
                .stat("$statAccumulator(${statType+gameStage}) as $gameStage")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${statType+gameStage} Is Not Null and ${statType+gameStage} > 0")
                .games(numberOfGames)
                .toSql()
        val result = dbHelper.executeSqlQuery(sqlEarlyGame)
        if (result.next()) {
            return result.produceFloat(gameStage)
        }

        throw IllegalStateException("Found no data")
    }

    /**
     * A flexible fetch method used to build sql queries and return a given stat.
     *
     * @param numberOfGames     The number of recent games to limit the filter to.
     * @param statType          The type of stat that we are fetching.
     * @param lane              The lane that we want the results for.
     * @param summonerId        The summoner id for whom the results are for.
     * @param gameStage         The stage of the game that this specific stat was for.
     * @param statAccumulator   The type of stat presentation- average, max min etc
     * @param champId           The id of the champ that we want to limit the filter to.
     *
     * @return A [Float] value, based on the given parameters.
     */
    private fun fetchStat(numberOfGames : Int, statType: String, lane: String, summonerId: Long, gameStage: String, statAccumulator: String, champId: Int) : Float {
        val sqlEarlyGame = Builder()
                .stat("$statAccumulator(${statType+gameStage}) as $gameStage")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${statType+gameStage} Is Not Null and ${statType+gameStage} > 0 AND champId")
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

    private fun ResultSet.produceCreepsPerMinDeltasCard() : FullStatCard {
        return FullStatCard(
                getFloat("avg(heroEarlyGame)"),
                getFloat("avg(villanEarlyGame)"),
                getFloat("avg(heroMidGame)"),
                getFloat("avg(villanMidGame)"),
                getFloat("avg(heroLateGame)"),
                getFloat("avg(villanLateGame)")
        )
    }

    private fun ResultSet.produceFloat(floatName : String) : Float = getFloat(floatName)
}