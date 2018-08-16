package database.match

import database.DbHelper
import database.sql_builder.Builder
import model.GameStageStats
import model.MatchSummary
import model.TotalMatchStats
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 */
class MatchDao(private val dbHelper : DbHelper) : MatchDaoContract {


    private val gameIdColumn = "gameId"
    private val laneColumn = "lane"
    private val matchSummaryTable = "matchsummary"
    private val summonerIdColumn = "SummonerId"
    private val matchTable = "matchtable"
    private val champColumn = "champion"

    /**
     * @param startingPoint The starting point for our query - for instance if we want to fetch the next
     * twenty ids, we would set this as 20.
     *
     * @param summonerId    The summoner for whom we are fetching the matches.
     *
     * @return An [ArrayList] of match ids.
     */
    override fun loadTwentyIds(startingPoint: Int, summonerId: Long): ArrayList<Long> {
        val sql = Builder()
                .select("$gameIdColumn, $summonerIdColumn")
                .tableName(matchSummaryTable)
                .where("$summonerIdColumn = $summonerId")
                .orderBy(gameIdColumn)
                .limit(startingPoint, startingPoint+20)
                .toSql()
        val resultSet = dbHelper.executeSqlQuery(sql)
        val ids = ArrayList<Long>()
        while (resultSet.next()) {
            ids.add(resultSet.getLong(gameIdColumn))
        }

        return ids
    }

    /**
     * @param startingPoint The starting point for our query - for instance if we want to fetch the next
     * twenty ids, we would set this as 20.
     *
     * @param summonerId    The summoner for whom we are fetching the matches.
     *
     * @return An [ArrayList] of match ids.
     */
    override fun loadTwentyIds(startingPoint: Int, summonerId: Long, lane : String): ArrayList<Long> {
        val sql = Builder()
                .select("$gameIdColumn, $summonerIdColumn, $laneColumn")
                .tableName(matchSummaryTable)
                .where("$summonerIdColumn = $summonerId AND $laneColumn = $lane")
                .orderBy(gameIdColumn)
                .limit(startingPoint, startingPoint+20)
                .toSql()
        val resultSet = dbHelper.executeSqlQuery(sql)
        val ids = ArrayList<Long>()
        while (resultSet.next()) {
            ids.add(resultSet.getLong(gameIdColumn))
        }

        return ids
    }

    override fun loadTwentyIds(startingPoint: Int,
                               summonerId: Long,
                               heroChampId: Int): ArrayList<Long> {
        val sql = Builder()
                .select("$gameIdColumn, $summonerIdColumn, $champColumn")
                .tableName(matchSummaryTable)
                .where("$summonerIdColumn = $summonerId and $champColumn = $heroChampId")
                .orderBy(gameIdColumn)
                .limit(startingPoint, startingPoint+20)
                .toSql()

        val resultSet = dbHelper.executeSqlQuery(sql)
        val ids = ArrayList<Long>()
        while (resultSet.next()) {
            ids.add(resultSet.getLong(gameIdColumn))
        }

        return ids
    }

    override fun loadTwentyIds(startingPoint: Int, summonerId: Long, heroChampId: Int, lane: String): ArrayList<Long> {
        val sql = Builder()
                .select("$gameIdColumn, $summonerIdColumn, $champColumn")
                .tableName(matchSummaryTable)
                .where("$summonerIdColumn = $summonerId and $champColumn = $heroChampId and $laneColumn = $lane" )
                .orderBy(gameIdColumn)
                .limit(startingPoint, startingPoint+20)
                .toSql()

        val resultSet = dbHelper.executeSqlQuery(sql)
        val ids = ArrayList<Long>()
        while (resultSet.next()) {
            ids.add(resultSet.getLong(gameIdColumn))
        }

        return ids
    }

    override fun loadTwentyIds(role: String, startingPoint: Int, summonerId: Long, heroChampId: Int, villanChampId: Int): ArrayList<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMatchSummary(role: String, matchId: Long, summonerId: Long): MatchSummary {
        // select the match details from the summoner id
        /**
         * return "{\"data\" :{" +
        "\"gameId\" : 1," +
        "\"heroChampId\" : 1," +
        "\"champName\" : \"vi\"," + /\- can probably fetch this from the local database
        "\"villanChampId\" : 2,"
        // for this data we need to fetch from the database the early game stats and
        make a simple formula which calculates the user performance.
        so fetch all the stats and summarise it. based on performance
        "\"earlyGame\" : {\"enemyStatValue\": 51.234,\"heroStatValue\": 49.234}," +
        "\"midGame\" : {\"enemyStatValue\": 51.234,\"heroStatValue\": 49.234}," +
        "\"lateGame\" : {\"enemyStatValue\": 51.234,\"heroStatValue\": 49.234}" +
        "}}"
         */



        val tableName = "{$role}_summarystats"
        val sql = Builder()
                .select("$gameIdColumn, $summonerIdColumn, $champColumn")
                .tableName(tableName)
                .where("$summonerIdColumn = $summonerId and $champColumn = $heroChampId and $laneColumn = $lane" )
                .orderBy(gameIdColumn)
                .limit(startingPoint, startingPoint+20)
                .toSql()

//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadTotalStatsForAMatch(role: String, matchId: Long, summonerId: Long): TotalMatchStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadEarlyGameStageStatsForAMatch(role: String, matchId: Long, summonerId: Long): GameStageStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMidGameStageStatsForAMatch(role: String, matchId: Long, summonerId: Long): GameStageStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadLateGameStageStatsForAMatch(role: String, matchId: Long, summonerId: Long): GameStageStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Fetch the most recently saved match in the database for the given user.
     *
     * @param summonerId The id of the summoner for whom we are getting the most recent match
     */
    fun fetchNewestMatchForSummoner(summonerId: Long) : Long {
        val sql = Builder()
                .select(gameIdColumn)
                .tableName(matchTable)
                .where("$summonerIdColumn = $summonerId")
                .orderBy(gameIdColumn)
                .max()
                .toSql()

        val result : ResultSet = dbHelper.executeSqlQuery(sql)
        if (result.first()) {
            return result.getLong("Max($gameIdColumn)")
        }

        return -1
    }

}