package database.match

import database.DbHelper
import database.match.model.MatchSummary
import java.sql.ResultSet
import java.util.ArrayList

class MatchSummaryDao(val dbHelper: DbHelper){

    private val MATCH_SUMMARY = "matchsummary"
    private val PLATFORM_ID = "PlatformId"
    private val GAME_ID = "GameId"
    private val CHAMPION = "Champion"
    private val QUEUE = "Queue"
    private val SEASON = "Season"
    private val TIMESTAMP = "Timestamp"
    private val ROLE = "Role"
    private val LANE = "Lane"
    val ID = "Id"
    private val SUMMONER_ID = "SummonerId"

    /**
     * Save a match summary.
     * @return return the id of the save, or -1 if the save failed.
     */
    fun saveMatchSummary(matchSummary: MatchSummary) : Long {
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

    fun getMatchSummary(id : Long) : MatchSummary {
        val queryString = String.format("SELECT * FROM %s WHERE Id = %s", MATCH_SUMMARY, id)
        val result : ResultSet = dbHelper.executeSqlQuery(queryString)
        if (result.next()) {
            val ms = MatchSummary()
            ms.platformId = result.getString(PLATFORM_ID)
            ms.gameId = result.getLong(GAME_ID)
            ms.champion = result.getInt(CHAMPION)
            ms.queue = result.getInt(QUEUE)
            ms.season = result.getInt(SEASON)
            ms.timestamp = result.getLong(TIMESTAMP)
            ms.role = result.getString(ROLE)
            ms.lane = result.getString(LANE)
            ms.summonerId = result.getString(SUMMONER_ID)
            return ms
        }
        // need to handle this in our tests
        return MatchSummary()
    }

    /**
     * Check if a certain game already is saved in our match summary list.
     * @param gameId The game id that we want to check for
     * @return true if exists, false if not.
     */
    fun exists(gameId: Long) : Boolean {
        val queryString = "SELECT * From $MATCH_SUMMARY WHERE GameId = $gameId"
        val result = dbHelper.executeSqlQuery(queryString)
        if (result.next()) return true
        return false

    }
    /**
     * Get a match summary by matchId
     * @param gameId The game Id we are searching for.
     * @return A [MatchSummary]. Will be a blank match summary if it does not exist.
     */
    fun getMatchSummaryByGameId(gameId : Long) : MatchSummary {
        val queryString = "SELECT * FROM $MATCH_SUMMARY WHERE GameId = $gameId"
        val result : ResultSet = dbHelper.executeSqlQuery(queryString)
        if (result.next()) {
            return result.produceMatchSummary()
        }
        return MatchSummary()
    }

    /**
     * Get all summaries for a specific summoner
     * @param summonerId The summoner that we want to get the summaries for.
     * @return An [ArrayList] of [MatchSummary] that belong to that specific summoner.
     */
    fun getAllMatchesBySummonerId(summonerId : String) : ArrayList<MatchSummary> {
        val queryString = "SELECT * FROM $MATCH_SUMMARY WHERE $SUMMONER_ID = '$summonerId'"
        val result = dbHelper.executeSqlQuery(queryString)
        val summaries = ArrayList<MatchSummary>()
        while (result.next()) {
            summaries.add(result.produceMatchSummary())
        }

        return summaries
    }

    /**
     * Fetch a specified number of match summaries for a specific summoner. Note that this will filter from most recent,
     * so if you specify you want 20 [MatchSummary]'s, it will return the most recent 20 [MatchSummary]'s
     *
     * @param summonerId        The summoner to fetch the results for
     * @param numberOfMatches   The number of matches to fetch.
     * @return                  An ArrayList of the [MatchSummary] objects that were found. The most recent will be first
     *                          in the list
     */
    fun getRecentMatchesBySummonerIdForRole(summonerId: String, numberOfMatches: Int, role: String, lane: String) : ArrayList<MatchSummary> {
        val queryString = "SELECT * FROM $MATCH_SUMMARY " +
                "WHERE $SUMMONER_ID = '$summonerId' AND $ROLE = '$role' AND $LANE = '$lane' " +
                "ORDER BY $GAME_ID DESC " +
                "LIMIT $numberOfMatches"
        val result = dbHelper.executeSqlQuery(queryString)
        val summaries = ArrayList<MatchSummary>()
        while (result.next()) {
            summaries.add(result.produceMatchSummary())
        }

        return summaries
    }

    private fun ResultSet.produceMatchSummary(): MatchSummary {
        val result = this
        val ms = MatchSummary()
//        ms.id = result.getInt(ID)
        ms.platformId = result.getString(PLATFORM_ID)
        ms.gameId = result.getLong(GAME_ID)
        ms.champion = result.getInt(CHAMPION)
        ms.queue = result.getInt(QUEUE)
        ms.season = result.getInt(SEASON)
        ms.timestamp = result.getLong(TIMESTAMP)
        ms.role = result.getString(ROLE)
        ms.lane = result.getString(LANE)
        ms.summonerId = result.getString(SUMMONER_ID)
        return ms
    }
}

