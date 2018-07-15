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
class MatchDao(private val dbHelper: DbHelper) : MatchDaoContract {

    private val gameIdColumn = "gameId"
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
            ids.add(resultSet.getLong(summonerIdColumn))
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
            ids.add(resultSet.getLong(summonerIdColumn))
        }

        return ids
    }

    override fun loadTwentyIds(table: String, startingPoint: Int, summonerId: Long, heroChampId: Int, villanChampId: Int): ArrayList<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMatchSummary(table: String, matchId: Long, summonerId: Long): MatchSummary {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadTotalStatsForAMatch(table: String, matchId: Long, summonerId: Long): TotalMatchStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadEarlyGameStageStatsForAMatch(table: String, matchId: Long, summonerId: Long): GameStageStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMidGameStageStatsForAMatch(table: String, matchId: Long, summonerId: Long): GameStageStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadLateGameStageStatsForAMatch(table: String, matchId: Long, summonerId: Long): GameStageStats {
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