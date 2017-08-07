package db.matchlist

import db.DBHelper
import extensions.produceMatchSummary
import model.matchlist.MatchSummary
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 */
class MatchSummaryDAO(val dbHelper: DBHelper) {

    val MATCH_SUMMARY = "matchsummary2"
    val PLATFORM_ID = "PlatformId"
    val GAME_ID = "GameId"
    val CHAMPION = "Champion"
    val QUEUE = "Queue"
    val SEASON = "Season"
    val TIMESTAMP = "Timestamp"
    val ROLE = "Role"
    val LANE = "Lane"
    val ID = "Id"
    val SUMMONER_ID = "SummonerId"

    /**
     * Save a match summary.
     * @return return the id of the save, or -1 if the save failed.
     */
    fun saveMatchSummary(matchSummary: MatchSummary) : Int {
        val queryString =
                "INSERT INTO $MATCH_SUMMARY (" +
                        "$PLATFORM_ID, " +
                        "$GAME_ID, " +
                        "$CHAMPION, " +
                        "$QUEUE," +
                        "$SEASON, " +
                        "$TIMESTAMP, " +
                        "$ROLE, " +
                        "$LANE," +
                        "$SUMMONER_ID) VALUES (" +
                        "'${matchSummary.platformId}'," +
                        "${matchSummary.gameId}," +
                        "${matchSummary.champion}," +
                        "${matchSummary.queue}," +
                        "${matchSummary.season}," +
                        "${matchSummary.timestamp}," +
                        "'${matchSummary.role}'," +
                        "'${matchSummary.lane}'," +
                        "${matchSummary.summonerId})"

        return dbHelper.executeSQLScript(queryString)
    }

    fun getMatchSummary(id : Int) : MatchSummary {
        val queryString = String.format("SELECT * FROM %s WHERE Id = %s", MATCH_SUMMARY, id)
        val result : ResultSet = dbHelper.ExecuteSqlQuery(queryString)
        if (result.next()) {
            val ms = MatchSummary()
            ms.id = id
            ms.platformId = result.getString(PLATFORM_ID)
            ms.gameId = result.getLong(GAME_ID)
            ms.champion = result.getInt(CHAMPION)
            ms.queue = result.getInt(QUEUE)
            ms.season = result.getInt(SEASON)
            ms.timestamp = result.getLong(TIMESTAMP)
            ms.role = result.getString(ROLE)
            ms.lane = result.getString(LANE)
            ms.summonerId = result.getLong(SUMMONER_ID)
            return ms
        }
        // need to handle this in our tests
        return MatchSummary()
    }

    /**
     * Get a match summary by gameId
     */
    fun getMatchSummaryByGameId(gameId : Long) : MatchSummary {
        val queryString = "SELECT * FROM $MATCH_SUMMARY WHERE GameId = $gameId"
        val result : ResultSet = dbHelper.ExecuteSqlQuery(queryString)
        if (result.next()) {
            return result.produceMatchSummary()
        }
        // need to handle this in our tests
        return MatchSummary()
    }

    fun getAllMatchesBySummonerId(summonerId: Int) : ArrayList<MatchSummary> {
        val queryString = "SELECT * FROM $MATCH_SUMMARY WHERE $SUMMONER_ID = $summonerId"
        val result = dbHelper.ExecuteSqlQuery(queryString)
        val summaries = ArrayList<MatchSummary>()
        while (result.next()) {
            summaries.add(result.produceMatchSummary())
        }

        return summaries
    }
}