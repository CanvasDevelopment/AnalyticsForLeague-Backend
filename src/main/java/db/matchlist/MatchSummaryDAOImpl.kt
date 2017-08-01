package db.matchlist

import db.DBHelper
import db.stats.CreepsPerMinDAOImpl
import model.matchlist.MatchSummary
import model.stats.CreepsPerMin
import java.sql.ResultSet
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 */
class MatchSummaryDAOImpl(val dbHelper: DBHelper) {

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

    /**
     * Save a match summary.
     * @return return the id of the save, or -1 if the save failed.
     */
    fun saveMatchSummary(matchSummary: MatchSummary) : Int {
        val queryString = String.format(
                "INSERT INTO %s (" +
                        "%s, " +
                        "%s, " +
                        "%s, " +
                        "%s," +
                        "%s, " +
                        "%s, " +
                        "%s, " +
                        "%s) VALUES (" +
                        "'%s'," +
                        "%s," +
                        "%s," +
                        "%s," +
                        "%s," +
                        "%s," +
                        "'%s'," +
                        "'%s')",
                MATCH_SUMMARY,
                PLATFORM_ID,
                GAME_ID,
                CHAMPION,
                QUEUE,
                SEASON,
                TIMESTAMP,
                ROLE,
                LANE,
                matchSummary.platformId,
                matchSummary.gameId,
                matchSummary.champion,
                matchSummary.queue,
                matchSummary.season,
                matchSummary.timestamp,
                matchSummary.role,
                matchSummary.lane
        )

        return dbHelper.ExecuteSqlScript(queryString)
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
            return ms
        }
        // need to handle this in our tests
        return MatchSummary()
    }

    /**
     * Get a match summary by gameId
     */
    fun getMatchSummaryByGameId(gameId : Long) : MatchSummary {
        val queryString = String.format("SELECT * FROM %s WHERE GameId = %s", MATCH_SUMMARY, gameId)
        val result : ResultSet = dbHelper.ExecuteSqlQuery(queryString)
        if (result.next()) {
            val ms = MatchSummary()
            ms.id = result.getInt(ID)
            ms.platformId = result.getString(PLATFORM_ID)
            ms.gameId = result.getLong(GAME_ID)
            ms.champion = result.getInt(CHAMPION)
            ms.queue = result.getInt(QUEUE)
            ms.season = result.getInt(SEASON)
            ms.timestamp = result.getLong(TIMESTAMP)
            ms.role = result.getString(ROLE)
            ms.lane = result.getString(LANE)
            return ms
        }
        // need to handle this in our tests
        return MatchSummary()
    }
}