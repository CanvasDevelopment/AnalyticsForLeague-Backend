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
 *
 */
class AnalysisDao (private val dbHelper: DbHelper){

    private val villan = "villan"
    private val hero = "hero"

    private val heroEarlyGame = "heroEarlyGame"
    private val villanEarlyGame = "villanEarlyGame"
    private val heroMidGame = "heroMidGame"
    private val villanMidGame = "villanMidGame"
    private val heroLateGame = "heroLateGame"
    private val villanLateGame = "villanLateGame"
    /**
     * Fetch a select value for the hero and the equivalent value for the villan. This is intended for the delta type stats
     * that have three game stages - e.g Creeps, damage etc.
     *
     * @param summonerId    Our hero summoner id
     * @param numberOfGames The number of limit to calculate this select from. Goes from most recent first, so a numberOfGames = 20
     *                      would mean that we only fetch from the most recent 20 limit.
     * @param lane          The lane to get the results for
     * @param accumulatorType   The select accumulator type. This can be average, max or min
     * @param gameStage         The game stage we want this select for - see [GameStages]
     * @param statName          The statName that we want to
     */
    fun fetchHeadToHeadStat(summonerId: Long, numberOfGames: Int, lane: String, accumulatorType: String, gameStage: String, statName: String) : HeadToHeadStat {
        val sqlEarlyGame = Builder()
                .select("$accumulatorType(${hero+statName+gameStage}) as ${hero+statName+ gameStage}, " +
                        "$accumulatorType(${villan + statName + gameStage}) as ${villan+statName+ gameStage}")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${hero+statName+gameStage} Is Not Null and ${hero+statName+gameStage} > 0")
                .limit(numberOfGames)
                .toSql()
        val result = dbHelper.executeSqlQuery(sqlEarlyGame)

        if (result.next()) {
            return result.produceHeadToHeadStat(statName + gameStage)
        }

        throw IllegalStateException("Found no data")
    }

    /**
     * Fetch a select value for the hero and the equivalent value for the villan. This is intended for the delta type stats
     * that have three game stages - e.g Creeps, damage etc.
     *
     * @param summonerId        Our hero summoner id
     * @param numberOfGames     The number of limit to calculate this select from. Goes from most recent first, so a numberOfGames = 20
     *                          would mean that we only fetch from the most recent 20 limit.
     * @param accumulatorType   The select accumulator type. This can be average, max or min
     * @param gameStage         The game stage we want this select for - see [GameStages]
     * @param statName          The statName that we want to
     * @param champId           The champ we want to filter our results to.
     *
     * @return a [HeadToHeadStat] representing the results of the filter.
     */
    fun fetchHeadToHeadStat(summonerId: Long, numberOfGames: Int, lane: String, accumulatorType: String, gameStage: String, statName: String, champId: Int) : HeadToHeadStat {

         val sqlEarlyGame = Builder()
                .select("$accumulatorType(${hero+statName+gameStage}) as ${hero+statName+ gameStage}, " +
                        "$accumulatorType(${villan + statName + gameStage}) as ${villan+statName+ gameStage}")
                .tableName("${lane}_summarystats")
                // note that we put > 0 for the select type - that is because the number of delta should never be 0
                .where("heroSummonerId = $summonerId And ${hero+statName+gameStage} Is Not Null and ${hero+statName+gameStage} > 0 AND heroChampId = $champId")
                .limit(numberOfGames)
                .toSql()
        val result = dbHelper.executeSqlQuery(sqlEarlyGame)
        if (result.next()) {
            return result.produceHeadToHeadStat(statName + gameStage)
        }

        throw IllegalStateException("Found no data")
    }

    /**
     * Fetch a select value for the hero and the equivalent value for the villan.
     *
     * @param summonerId        Our hero summoner id
     * @param numberOfGames     The number of limit to calculate this select from. Goes from most recent first, so a numberOfGames = 20
     *                          would mean that we only fetch from the most recent 20 limit.
     * @param accumulatorType   The select accumulator type. This can be average, max or min
     * @param statName          The statName that we want to
     * @param champId           The champ we want to filter our results to.
     *
     * @return a [HeadToHeadStat] representing the results of the filter.
     */
    fun fetchHeadToHeadStat(summonerId: Long, numberOfGames: Int, lane : String, accumulatorType: String, statName: String, champId: Int) : HeadToHeadStat {
        val sqlEarlyGame = Builder()
                .select("$accumulatorType(hero$statName) as hero$statName,$accumulatorType(villan$statName) as villan$statName")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And hero$statName Is Not Null and hero$statName > 0 AND heroChampId = $champId")
                .limit(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sqlEarlyGame)

        if (result.next()) {
            return result.produceHeadToHeadStat(statName)
        }

        throw IllegalStateException("Found no data")
    }

    /**
     * Fetch a list of past results for a hero. Each [HeadToHeadStat] in the array represents one game in the filtered
     * results. This query should not be used for delta stats (i.e stats that rely on a game stage), but rather 'whole game stats'
     *
     * @param summonerId    The hero summonerID
     * @param numberOfGames The number of limit to limit the results to. Ordered from most recent to oldest.
     * @param lane          Filter results to limit where the hero (and their opponent) played this lane
     * @param statName      The name of the select we want to get data about
     *
     * @return an array of [HeadToHeadStat]'s where each head to head select represents one game performance.
     */
    fun fetchPerformanceHistory(summonerId: Long, numberOfGames: Int, lane: String, statName: String) : ArrayList<HeadToHeadStat> {

        val sqlEarlyGame = Builder()
                .select("${hero+statName} , ${villan+statName}")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${hero + statName} Is Not Null") // we do not put > 0 here, as it is not a delta select
                .limit(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sqlEarlyGame)
        val resultArray : ArrayList<HeadToHeadStat> = ArrayList()

        while(result.next()) {
            resultArray.add(result.produceHeadToHeadStat(statName))
        }

        return resultArray
    }

    /**
     * Fetch a list of past results for a hero. Each [HeadToHeadStat] in the array represents one game in the filtered
     * results. This querry should not be used for delta stats (i.e stats that rely on a game stage), but rather 'whole game stats'
     *
     * @param summonerId    The hero summonerID
     * @param numberOfGames The number of limit to limit the results to. Ordered from most recent to oldest.
     * @param lane          Filter results to limit where the hero (and their opponent) played this lane
     * @param statName      The name of the select we want to get data about
     * @param heroChampId   Filter results to just limit where the hero played a specific champ.
     *
     * @return an array of [HeadToHeadStat]'s where each head to head select represents one game performance.
     */
    fun fetchPerformanceHistory(summonerId: Long, numberOfGames: Int, lane: String, statName: String, heroChampId: Int) : ArrayList<HeadToHeadStat> {

        val sqlEarlyGame = Builder()
                .select("${hero+statName} , ${villan+statName}")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${hero + statName} Is Not Null And heroChampId = $heroChampId") // we do not put > 0 here, as it is not a delta select
                .limit(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sqlEarlyGame)
        val resultArray : ArrayList<HeadToHeadStat> = ArrayList()

        while(result.next()) {
            resultArray.add(result.produceHeadToHeadStat(statName))
        }

        return resultArray
    }

    /**
     * Fetch a list of past results for a hero. Each [HeadToHeadStat] in the array represents one game in the filtered
     * results
     *
     * @param summonerId    The hero summonerID
     * @param numberOfGames The number of limit to limit the results to. Ordered from most recent to oldest.
     * @param lane          Filter results to limit where the hero (and their opponent) played this lane
     * @param statName      The name of the select we want to get data about
     * @param heroChampId   Filter results to just limit where the hero played a specific champ.
     * @param gameStage     Filter results to just this game stage
     *
     * @return an array of [HeadToHeadStat]'s where each head to head select represents one game performance.
     */
    fun fetchPerformanceHistory(summonerId: Long, numberOfGames: Int, lane: String, statName: String, heroChampId: Int, gameStage: String) : ArrayList<HeadToHeadStat> {

        val sql = Builder()
                .select("${hero+statName+gameStage} , ${villan+statName + gameStage}")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId " +
                        "And ${hero + statName + gameStage} Is Not Null " +
                        "And ${hero+statName+gameStage} > 0 " +
                        "And ${villan + statName + gameStage} Is Not Null " +
                        "And ${villan + statName + gameStage} > 0 " +
                        "And heroChampId = $heroChampId") // we do not put > 0 here, as it is not a delta select
                .limit(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sql)
        val resultArray : ArrayList<HeadToHeadStat> = ArrayList()
        while(result.next()) {
            resultArray.add(result.produceHeadToHeadStat(statName+gameStage))
        }

        return resultArray
    }

    /**
     * Fetch a list of past results for a hero. Each [HeadToHeadStat] in the array represents one game in the filtered
     * results
     *
     * @param summonerId    The hero summonerID
     * @param numberOfGames The number of limit to limit the results to. Ordered from most recent to oldest.
     * @param lane          Filter results to limit where the hero (and their opponent) played this lane
     * @param statName      The name of the select we want to get data about
     * @param gameStage     Filter results to just this game stage
     *
     * @return an array of [HeadToHeadStat]'s where each head to head select represents one game performance.
     */
    fun fetchPerformanceHistory(summonerId: Long, numberOfGames: Int, lane: String, statName: String, gameStage: String) : ArrayList<HeadToHeadStat> {

        val sql = Builder()
                .select("${hero+statName+gameStage} , ${villan+statName + gameStage}")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId " +
                        "And ${hero + statName + gameStage} Is Not Null " +
                        "And ${hero+statName+gameStage} > 0 " +
                        "And ${villan + statName + gameStage} Is Not Null " +
                        "And ${villan + statName + gameStage} > 0 ") // we do not put > 0 here, as it is not a delta select
                .limit(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sql)
        val resultArray : ArrayList<HeadToHeadStat> = ArrayList()
        while(result.next()) {
            resultArray.add(result.produceHeadToHeadStat(statName+gameStage))
        }

        return resultArray
    }

    /**
     * Fetch a select value for the hero and the equivalent value for the villan.
     *
     * @param summonerId        Our hero summoner id
     * @param numberOfGames     The number of limit to calculate this select from. Goes from most recent first, so a numberOfGames = 20
     *                          would mean that we only fetch from the most recent 20 limit.
     * @param accumulatorType   The select accumulator type. This can be average, max or min
     * @param statName          The statName that we want to
     *
     * @return a [HeadToHeadStat] representing the results of the filter.
     */
    fun fetchHeadToHeadStat(summonerId: Long, numberOfGames: Int, lane: String, accumulatorType: String, statName: String) : HeadToHeadStat {
        val sqlEarlyGame = Builder()
                .select("$accumulatorType(${hero+statName}) as ${hero+statName}, " +
                        "$accumulatorType(${villan + statName}) as ${villan+statName}")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${hero+statName} Is Not Null") // we do not put > 0 here, as it is not a delta select
                .limit(numberOfGames)
                .toSql()
        val result = dbHelper.executeSqlQuery(sqlEarlyGame)
        if (result.next()) {
            return result.produceHeadToHeadStat(statName)
        }

        throw IllegalStateException("Found no data")
    }

    @Deprecated("I think this is not required any more")
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
     * @param numberOfGames The number of limit that we want to limit to.
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
     * @param numberOfGames The number of limit that we want to limit to.
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
     * @param numberOfGames The number of limit that we want to limit to.
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
     * @param numberOfGames The number of limit that we want to limit to.
     * @param lane          The lane that we want to get the results for.
     * @param deltaType     The delta type that we want to fetch.
     * @throws              IllegalStateException if we fail to find any data.
     */
    fun fetchDeltas(summonerId: Long, numberOfGames: Int, lane: String, deltaType : String, statAccumulator: String) : ArrayList<HeadToHeadStat> {

        val sql = Builder()
                .select("$statAccumulator(${hero+deltaType}EarlyGame) as $heroEarlyGame," +
                        "$statAccumulator(${villan+deltaType}EarlyGame) as $villanEarlyGame," +
                        "$statAccumulator(${hero+deltaType}MidGame) as $heroMidGame," +
                        "$statAccumulator(${villan+deltaType}MidGame) as $villanMidGame," +
                        "$statAccumulator(${hero+deltaType}LateGame) as $heroLateGame,"+
                        "$statAccumulator(${villan+deltaType}LateGame) as $villanLateGame").tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId")
                .limit(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sql)
        if (result.next()) {
            return result.produceHeadToHeadArray()
        }
        throw IllegalStateException("Failed to find stats, Please fix this query")
    }

    /**
     * Fetch the max for a delta type [e.g heroCreeps]
     *
     * @param summonerId    The id of the heroSummoner
     * @param numberOfGames The number of limit that we want to limit to.
     * @param lane          The lane that we want to get the results for.
     * @param deltaType     The delta type that we want to fetch.
     * @throws              IllegalStateException if we fail to find any data.
     */
    fun fetchDeltas(summonerId: Long, numberOfGames: Int, lane: String, deltaType : String, statAccumulator: String, champId : Int) : ArrayList<HeadToHeadStat> {

        val sql = Builder()
                .select("$statAccumulator(${hero+deltaType}EarlyGame) as $heroEarlyGame," +
                        "$statAccumulator(${villan+deltaType}EarlyGame) as $villanEarlyGame," +
                        "$statAccumulator(${hero+deltaType}MidGame) as $heroMidGame," +
                        "$statAccumulator(${villan+deltaType}MidGame) as $villanMidGame," +
                        "$statAccumulator(${hero+deltaType}LateGame) as $heroLateGame,"+
                        "$statAccumulator(${villan+deltaType}LateGame) as $villanLateGame").tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId and heroChampId = $champId")
                .limit(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sql)
        if (result.next()) {
            return result.produceHeadToHeadArray()
        }
        throw IllegalStateException("Failed to find stats, Please fix this query")
    }



    /**
     * A flexible fetch method used to build sql queries and return a given select.
     *
     * @param numberOfGames     The number of recent limit to limit the filter to.
     * @param statType          The type of select that we are fetching.
     * @param lane              The lane that we want the results for.
     * @param summonerId        The summoner id for whom the results are for.
     * @param gameStage         The stage of the game that this specific select was for.
     * @param statAccumulator   The type of select presentation- average, max min etc
     *
     * @return A [Float] value, based on the given parameters.
     */
    fun fetchStat(numberOfGames : Int, statType: String, lane: String, summonerId: Long, gameStage: String, statAccumulator: String) : Float {
        val statName = hero + statType+gameStage
        val sqlEarlyGame = Builder()
                .select("$statAccumulator($statName) as $gameStage")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And $statName Is Not Null and $statName > 0")
                .limit(numberOfGames)
                .toSql()
        val result = dbHelper.executeSqlQuery(sqlEarlyGame)

        if (result.next()) {
            return result.produceFloat(gameStage)
        }

        throw IllegalStateException("Found no data")
    }

    /**
     * A flexible fetch method used to build sql queries and return a given select.
     *
     * @param numberOfGames     The number of recent limit to limit the filter to.
     * @param statType          The type of select that we are fetching.
     * @param lane              The lane that we want the results for.
     * @param summonerId        The summoner id for whom the results are for.
     * @param gameStage         The stage of the game that this specific select was for.
     * @param statAccumulator   The type of select presentation- average, max min etc
     * @param champId           The id of the champ that we want to limit the filter to.
     *
     * @return A [Float] value, based on the given parameters.
     */
    fun fetchStat(numberOfGames : Int, statType: String, lane: String, summonerId: Long, gameStage: String, statAccumulator: String, champId: Int) : Float {
        val sqlEarlyGame = Builder()
                .select("$statAccumulator(${statType+gameStage}) as $gameStage")
                .tableName("${lane}_summarystats")
                // note that we put > 0 for the select type - that is because the number of delta should never be 0
                .where("heroSummonerId = $summonerId And ${statType+gameStage} Is Not Null and ${statType+gameStage} > 0 AND heroChampId = $champId")
                .limit(numberOfGames)
                .toSql()
        val result = dbHelper.executeSqlQuery(sqlEarlyGame)
        if (result.next()) {
            return result.produceFloat(gameStage)
        }

        throw IllegalStateException("Found no data")
    }

    /**
     * A flexible fetch method used to build sql queries and return a given select.
     *
     * @param numberOfGames     The number of recent limit to limit the filter to.
     * @param statType          The type of select that we are fetching.
     * @param lane              The lane that we want the results for.
     * @param summonerId        The summoner id for whom the results are for.
     * @param statAccumulator   The type of select presentation- average, max min etc
     * @param champId           The id of the champ that we want to limit the filter to.
     *
     * @return A [Float] value, based on the given parameters.
     */
    fun fetchStat(numberOfGames : Int, statType: String, lane: String, summonerId: Long, statAccumulator: String, champId: Int) : Float {
        val sqlEarlyGame = Builder()
                .select("$statAccumulator(${hero+statType}) as ${hero+statType}")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${hero+statType} Is Not Null AND heroChampId = $champId")
                .limit(numberOfGames)
                .toSql()
        val result = dbHelper.executeSqlQuery(sqlEarlyGame)
        if (result.next()) {
            return result.produceFloat(hero + statType)
        }

        throw IllegalStateException("Found no data")
    }

    /**
     * A flexible fetch method used to build sql queries and return a given select.
     *
     * @param numberOfGames     The number of recent limit to limit the filter to.
     * @param statType          The type of select that we are fetching.
     * @param lane              The lane that we want the results for.
     * @param summonerId        The summoner id for whom the results are for.
     * @param statAccumulator   The type of select presentation- average, max min etc
     *
     * @return A [Float] value, based on the given parameters.
     */
    fun fetchStat(numberOfGames : Int, statType: String, lane: String, summonerId: Long, statAccumulator: String) : Float {
        val sqlEarlyGame = Builder()
                .select("$statAccumulator(${hero+statType}) as ${hero+statType}")
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And ${hero+statType} Is Not Null") // we do not put > 0 here, as it is not a delta select
                .limit(numberOfGames)
                .toSql()
        val result = dbHelper.executeSqlQuery(sqlEarlyGame)
        if (result.next()) {
            return result.produceFloat(hero+statType)
        }

        throw IllegalStateException("Found no data")
    }

    /**
     * A flexible fetch method used to build sql queries and return a given select.
     *
     * @param numberOfGames     The number of recent limit to limit the filter to.
     * @param statType          The type of select that we are fetching.
     * @param lane              The lane that we want the results for.
     * @param summonerId        The summoner id for whom the results are for.
     * @param heroChampId       Only fetch matches in which the hero was using this champ.
     *
     * @return A [Float] value, based on the given parameters.
     */
    fun fetchStatList(numberOfGames : Int, statType: String, lane: String, summonerId: Long, heroChampId: Int) : ArrayList<Float> {

        val sqlEarlyGame = Builder()
                .select(statType)
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And $statType Is Not Null And heroChampId = $heroChampId") // we do not put > 0 here, as it is not a delta select
                .limit(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sqlEarlyGame)
        val resultArray : ArrayList<Float> = ArrayList()

        while(result.next()) {
            resultArray.add(result.produceFloat(statType))
        }

        return resultArray
    }

    /**
     * A flexible fetch method used to build sql queries and return a given select.
     *
     * @param numberOfGames     The number of recent limit to limit the filter to.
     * @param statType          The type of select that we are fetching.
     * @param lane              The lane that we want the results for.
     * @param summonerId        The summoner id for whom the results are for.
     *
     * @return A [Float] value, based on the given parameters.
     */
    fun fetchStatList(numberOfGames : Int, statType: String, lane: String, summonerId: Long) : ArrayList<Float> {

        val sqlEarlyGame = Builder()
                .select(statType)
                .tableName("${lane}_summarystats")
                .where("heroSummonerId = $summonerId And $statType Is Not Null") // we do not put > 0 here, as it is not a delta select
                .limit(numberOfGames)
                .toSql()

        val result = dbHelper.executeSqlQuery(sqlEarlyGame)
        val resultArray : ArrayList<Float> = ArrayList()

        while(result.next()) {
            resultArray.add(result.produceFloat(statType))
        }

        return resultArray
    }

    private fun ResultSet.produceHeadToHeadArray() : ArrayList<HeadToHeadStat> {
        val heroEarlyGameResult = getFloat(heroEarlyGame)
        val villanEarlyGameResult = getFloat(villanEarlyGame)
        val heroMidGameResult = getFloat(heroMidGame)
        val villanMidGameResult = getFloat(villanMidGame)
        val heroLateGameResult = getFloat(heroLateGame)
        val villanLateGameResult = getFloat(villanLateGame)
        val earlyGameH2H = HeadToHeadStat(villanEarlyGameResult, heroEarlyGameResult)
        val midGameH2H = HeadToHeadStat(villanMidGameResult, heroMidGameResult)
        val lateGameH2H = HeadToHeadStat(villanLateGameResult, heroLateGameResult)
        val results = ArrayList<HeadToHeadStat>()
        results.add(earlyGameH2H)
        results.add(midGameH2H)
        results.add(lateGameH2H)
        return results
    }

    private fun ResultSet.produceFloat(floatName : String) : Float = getFloat(floatName)

    private fun ResultSet.produceHeadToHeadStat(statName : String) : HeadToHeadStat {
         return HeadToHeadStat(
                    getFloat(villan+ statName),
                    getFloat(hero + statName))
    }
}