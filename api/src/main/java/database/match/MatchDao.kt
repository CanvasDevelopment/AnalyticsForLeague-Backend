package database.match

import database.DbHelper
import database.match.model.MatchIdentifier
import database.sql_builder.Builder
import model.GameStageStats
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 */
class MatchDao(private val dbHelper : DbHelper) : MatchDaoContract {

    private val gameIdColumn = "gameId"
    private val laneColumn = "Lane"
    private val roleColumn = "Role"
    private val timestampColumn = "TIMESTAMP"
    private val matchSummaryTable = "MatchSummary"
    private val summonerIdColumn = "SummonerId"
    private val heroSummonerIdColumn = "heroSummonerId"
    private val matchTable = "MatchTable"
    private val champColumn = "Champion"
    private val heroChampId = "heroChampId"
    private val villainChampId = "villanChampId"
    private val heroWin = "heroWin"

    /**
     * @param startingPoint The starting point for our query - for instance if we want to fetch the next
     * twenty ids, we would set this as 20.
     *
     * @param summonerId    The summoner for whom we are fetching the matches.
     *
     * @return An [ArrayList] of match ids.
     */
    override fun loadTwentyIds(startingPoint: Int, summonerId: String): ArrayList<MatchIdentifier> {
        val sql = Builder()
                .select("$gameIdColumn, $summonerIdColumn, $laneColumn, $roleColumn, $timestampColumn, $champColumn")
                .tableName(matchSummaryTable)
                .where("$summonerIdColumn = '$summonerId'")
                .orderBy(gameIdColumn)
                .limit(startingPoint, startingPoint+20)
                .toSql()
        val resultSet = dbHelper.executeSqlQuery(sql)
        val results = ArrayList<MatchIdentifier>()
        // todo move this into the controller. Also move the code from the android client to here, that gets the correct role
        while (resultSet.next()) {
            results.add(MatchIdentifier(
                    resultSet.getLong(gameIdColumn),
                    resultSet.getString(roleColumn),
                    resultSet.getString(laneColumn),
                    resultSet.getInt(champColumn),
                    resultSet.getLong(timestampColumn),
                    summonerId))
        }
        resultSet.close()
        return results
    }

    /**
     * @param startingPoint The starting point for our query - for instance if we want to fetch the next
     * twenty ids, we would set this as 20.
     *
     * @param summonerId    The summoner for whom we are fetching the matches.
     *
     * @return An [ArrayList] of match ids.
     */
    override fun loadTwentyIds(startingPoint: Int, summonerId: String, lane: String): ArrayList<MatchIdentifier> {
        val sql = Builder()
                .select("$gameIdColumn, $summonerIdColumn, $laneColumn, $roleColumn, $timestampColumn, $champColumn")
                .tableName(matchSummaryTable)
                .where("$summonerIdColumn = '$summonerId' AND $laneColumn = '$lane'")
                .orderBy(gameIdColumn)
                .limit(startingPoint, startingPoint+20)
                .toSql()
        val resultSet = dbHelper.executeSqlQuery(sql)
        val results = ArrayList<MatchIdentifier>()
        while (resultSet.next()) {
                results.add(MatchIdentifier(
                        resultSet.getLong(gameIdColumn),
                        resultSet.getString(roleColumn),
                        resultSet.getString(laneColumn),
                        resultSet.getInt(champColumn),
                        resultSet.getLong(timestampColumn),
                        summonerId))
        }
        resultSet.close()
        return results
    }

    override fun loadTwentyIds(startingPoint: Int,
                               summonerId: String,
                               heroChampId: Int): ArrayList<MatchIdentifier> {
        val sql = Builder()
                .select("$gameIdColumn, $summonerIdColumn, $laneColumn, $roleColumn, $timestampColumn, $champColumn")
                .tableName(matchSummaryTable)
                .where("$summonerIdColumn = '$summonerId' and $champColumn = $heroChampId")
                .orderBy(gameIdColumn)
                .limit(startingPoint, startingPoint+20)
                .toSql()

        val resultSet = dbHelper.executeSqlQuery(sql)
        val results = ArrayList<MatchIdentifier>()
        while (resultSet.next()) {
            results.add(MatchIdentifier(
                    resultSet.getLong(gameIdColumn),
                    resultSet.getString(roleColumn),
                    resultSet.getString(laneColumn),
                    resultSet.getInt(champColumn),
                    resultSet.getLong(timestampColumn),
                    summonerId))
        }
        resultSet.close()
        return results
    }

    override fun loadTwentyIds(startingPoint: Int, summonerId: String, heroChampId: Int, lane: String): ArrayList<MatchIdentifier> {
        val sql = Builder()
                .select("$gameIdColumn, $summonerIdColumn, $champColumn, $roleColumn, $laneColumn, $timestampColumn")
                .tableName(matchSummaryTable)
                .where("$summonerIdColumn = '$summonerId' and $champColumn = $heroChampId and $laneColumn = '$lane'" )
                .orderBy(gameIdColumn)
                .limit(startingPoint, startingPoint+20)
                .toSql()

        val resultSet = dbHelper.executeSqlQuery(sql)
        val results = ArrayList<MatchIdentifier>()
        while (resultSet.next()) {
            results.add(MatchIdentifier(
                    resultSet.getLong(gameIdColumn),
                    resultSet.getString(roleColumn),
                    resultSet.getString(laneColumn),
                    resultSet.getInt(champColumn),
                    resultSet.getLong(timestampColumn),
                    summonerId))
        }
        resultSet.close()
        return results
    }

    fun loadTwentyIds(startingPoint: Int, summonerId: String, lane:String, role:String) : ArrayList<MatchIdentifier> {
        val sql = Builder()
                .select("$gameIdColumn, $summonerIdColumn, $laneColumn, $roleColumn, $champColumn, $timestampColumn")
                .tableName(matchSummaryTable)
                .where("$summonerIdColumn = '$summonerId' and $laneColumn = '$lane' and  $roleColumn = '$role'" )
                .orderBy(gameIdColumn)
                .limit(startingPoint, startingPoint+20)
                .toSql()

        val resultSet = dbHelper.executeSqlQuery(sql)
        val results = ArrayList<MatchIdentifier>()
        while (resultSet.next()) {
            results.add(MatchIdentifier(
                    resultSet.getLong(gameIdColumn),
                    resultSet.getString(roleColumn),
                    resultSet.getString(laneColumn),
                    resultSet.getInt(champColumn),
                    resultSet.getLong(timestampColumn),
                    summonerId))
        }
        resultSet.close()
        return results
    }

    fun loadTwentyIds(startingPoint: Int, summonerId: String, heroChampId: Int, lane:String, role:String) : ArrayList<MatchIdentifier> {
        val sql = Builder()
                .select("$gameIdColumn, $summonerIdColumn, $champColumn, $laneColumn, $roleColumn, $timestampColumn")
                .tableName(matchSummaryTable)
                .where("$summonerIdColumn = '$summonerId' and $champColumn = $heroChampId and $laneColumn = '$lane' and  $roleColumn = '$role'" )
                .orderBy(gameIdColumn)
                .limit(startingPoint, startingPoint+20)
                .toSql()

        val resultSet = dbHelper.executeSqlQuery(sql)
        val results = ArrayList<MatchIdentifier>()
        while (resultSet.next()) {
            results.add(MatchIdentifier(
                    resultSet.getLong(gameIdColumn),
                    resultSet.getString(roleColumn),
                    resultSet.getString(laneColumn),
                    resultSet.getInt(champColumn),
                    resultSet.getLong(timestampColumn),
                    summonerId))
        }
        resultSet.close()
        return results
    }

    override fun loadTwentyIds(role: String, startingPoint: Int, summonerId : String, heroChampId: Int, villanChampId: Int): ArrayList<MatchIdentifier> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // todo move this to the controller, this is too much logic for the dao
     fun loadMatchSummary(role: String, matchId: Long, summonerId : String) {
//        return MatchSummary()
    }
//        // select the match details from the summoner id
//        /**
//         * return "{\"data\" :{" +
//        "\"matchId\" : 1," +
//        "\"heroChampId\" : 1," +
//        "\"champName\" : \"vi\"," + /\- can probably fetch this from the local database
//        "\"villanChampId\" : 2,"
//        // for this data we need to fetch from the database the early game stats and
//        make a simple formula which calculates the user performance.
//        so fetch all the stats and summarise it. based on performance
//        "\"earlyGame\" : {\"enemyStatValue\": 51.234,\"heroStatValue\": 49.234}," +
//        "\"midGame\" : {\"enemyStatValue\": 51.234,\"heroStatValue\": 49.234}," +
//        "\"lateGame\" : {\"enemyStatValue\": 51.234,\"heroStatValue\": 49.234}" +
//        "}}"
//         */
//
//        val performanceWeightings = ArrayList<PerformanceWeight>()
//
//        // todo make this smarter.
//        // we are currently just defaulting stats to the following
//
//        val tableName = "{$role}_SummaryStats"
//        val sql = Builder()
//                .select("$gameIdColumn, $summonerIdColumn, $champColumn")
//                .tableName(tableName)
//                .where("$summonerIdColumn = '$summonerId' and $champColumn = $heroChampId and $laneColumn = $lane" )
//                .orderBy(gameIdColumn)
//                .limit(startingPoint, startingPoint+20)
//                .toSql()
//
////        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }


    /**
     * Fetch a bunch of details from a table. I know this is called FetchMatchDetails but it
     * is actually pretty flexible.
     * @param summonerId The id of the hero summoner
     * @param gameId The id of the game
     * @param columnNames The column names of the database stats that we want to fetch
     * @param tableName The summary table to fetch from.
     */
    fun fetchMatchDetails(summonerId : String, gameId: Long, columnNames : ArrayList<String>, tableName: String) : ResultSet {
        columnNames.add(heroSummonerIdColumn)
        columnNames.add(gameIdColumn)
        val sqlEarlyGame = Builder()
                .select(columnNames)
                .tableName(tableName)
                .where("$heroSummonerIdColumn = '$summonerId' And $gameIdColumn = $gameId") // we do not put > 0 here, as it is not a delta select
                .toSql()
        return dbHelper.executeSqlQuery(sqlEarlyGame)
    }

    override fun loadEarlyGameStageStatsForAMatch(role: String, matchId: Long, summonerId : String): GameStageStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMidGameStageStatsForAMatch(role: String, matchId: Long, summonerId : String): GameStageStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadLateGameStageStatsForAMatch(role: String, matchId: Long, summonerId : String): GameStageStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun fetchStatsForHeroAndVillan(summonerId : String, gameId: Long, statNames : ArrayList<String>,
                                   tableName : String) : ResultSet {
        val selectStats = ArrayList<String>()
        selectStats.add(gameIdColumn)
        selectStats.add(heroSummonerIdColumn)
        selectStats.addAll(statNames)
        val sql = Builder()
                .select(statNames)
                .tableName(tableName)
                .where("$gameIdColumn = $gameId AND $heroSummonerIdColumn = '$summonerId'")
                .toSql()

        return dbHelper.executeSqlQuery(sql)
    }

    /**
     * Fetch the most recently saved match in the database for the given user.
     *
     * @param summonerId The id of the summoner for whom we are getting the most recent match
     */
    fun fetchNewestMatchForSummoner(summonerId : String) : Long {
        val sql = Builder()
                .select(gameIdColumn)
                .tableName(matchTable)
                .where("$summonerIdColumn = '$summonerId'")
                .orderBy(gameIdColumn)
                .max()
                .toSql()

        val result : ResultSet = dbHelper.executeSqlQuery(sql)
        if (result.first()) {
            return result.getLong("Max($gameIdColumn)")
        }

        return -1
    }

    fun fetchWinAndChampIds(gameId: Long, summonerId : String, refinedStatsTableName: String) : ResultSet {
        val itemsToFetch = ArrayList<String>()
        itemsToFetch.add(gameIdColumn)
        itemsToFetch.add(heroChampId)
        itemsToFetch.add(villainChampId)
        itemsToFetch.add(heroWin)
        itemsToFetch.add(heroSummonerIdColumn)

        val sql = Builder()
                .select(itemsToFetch)
                .tableName(refinedStatsTableName)
                .where("$heroSummonerIdColumn = '$summonerId' and $gameIdColumn = $gameId")
                .orderBy(gameIdColumn)
                .toSql()

        return dbHelper.executeSqlQuery(sql)
    }

    fun produceMockPerformanceHashMap(gameStage : String) : HashMap<String, Float> {
        val hero = "hero"
        val villan = "villan"
        val performanceProfile = HashMap<String, Float>()
        performanceProfile.put( "${hero}Creeps$gameStage",0.25f)
        performanceProfile.put( "${villan}Creeps$gameStage",0.25f)
        performanceProfile.put( "${hero}Gold$gameStage",0.3f)
        performanceProfile.put( "${villan}Gold$gameStage",0.3f)
        performanceProfile.put( "${hero}Damage$gameStage",0.30f)
        performanceProfile.put( "${villan}Damage$gameStage",0.30f)
        performanceProfile.put( "${hero}Xp$gameStage",0.15f)
        performanceProfile.put( "${villan}Xp$gameStage",0.15f)
        return performanceProfile
    }


}