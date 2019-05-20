package db.matchlist

import db.requests.DBHelper
import extensions.produceMatchSummary
import model.matchlist.MatchSummary
import util.logToConsole
import java.sql.ResultSet
import java.util.*

/**
 * @author Josiah Kendall
 */
class MatchSummaryDAO(val dbHelper: DBHelper) : MatchSummaryDaoContract{

    private val MATCH_SUMMARY = "MatchSummary"
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
    override fun saveMatchSummary(matchSummary: MatchSummary) : Long {
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
                        "'${matchSummary.summonerId}')"

        return dbHelper.executeSQLScript(queryString)
    }

    override fun getMatchSummary(id : Long) : MatchSummary {
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
            result.close()
            return ms
        }
        result.close()
        // need to handle this in our tests
        return MatchSummary()
    }

    /**
     * Check if a certain game already is saved in our match summary list.
     * @param gameId The game id that we want to check for
     * @return true if exists, false if not.
     */
    override fun exists(gameId: Long) : Boolean {
        val queryString = "SELECT * From $MATCH_SUMMARY WHERE GameId = $gameId"
        val result = dbHelper.executeSqlQuery(queryString)
        if (result.next()) {
            result.close()
            return true
        }

        result.close()
        return false
    }

    /**
     * Get a match summary by gameId
     * @param gameId The game Id we are searching for.
     * @return A [MatchSummary]. Will be a blank match summary if it does not exist.
     */
    override fun getMatchSummaryByGameId(gameId : Long) : MatchSummary {
        val queryString = "SELECT * FROM $MATCH_SUMMARY WHERE GameId = $gameId"
        val result : ResultSet = dbHelper.executeSqlQuery(queryString)
        if (result.next()) {
            val ms = result.produceMatchSummary()
            result.close()
            return ms
        }
        // need to handle this in our tests
        return MatchSummary()
    }

    /**
     * Get all summaries for a specific summoner
     * @param summonerId The summoner that we want to get the summaries for.
     * @return An [ArrayList] of [MatchSummary] that belong to that specific summoner.
     */
    override fun getAllMatchesBySummonerId(summonerId: String) : ArrayList<MatchSummary> {
        val queryString = "SELECT * FROM $MATCH_SUMMARY WHERE $SUMMONER_ID = '$summonerId'"
        logToConsole("running query: $queryString")
        val result = dbHelper.executeSqlQuery(queryString)
        val summaries = ArrayList<MatchSummary>()
        while (result.next()) {
            summaries.add(result.produceMatchSummary())
        }
        result.close()
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
        result.close()
        return summaries
    }

    /**
     * Fetch the number of match summaries that we have saved in a database for a certain summoner.
     * This is a reflection of how many games a summoner has played
     */
    fun loadNumberOfMatchSummariesForASummoner(summonerId: String) : Int {
        val numberOfMatches = "numberOfMatches"
        val sql = "SELECT count($GAME_ID) as $numberOfMatches from $MATCH_SUMMARY where $SUMMONER_ID = '$summonerId'"
        val result = dbHelper.executeSqlQuery(sql)
        while (result.first()) {
            val numberOfMatches = result.getInt(numberOfMatches)
            result.close()
            return numberOfMatches
        }

        return -1
    }

    /**
     * Fetch the number of match summaries for a match.
     *
     * @param summonerId The summoner for whom we are fetching the stats
     * @param gameId     The max game id. We want to fetch all matches up to and including this game id.
     * @return The number of games that match the criteria as an Int. Returns -1 if no games are found.
     */
    fun loadNumberOfMatchSummariesUpToAndIncludingGivenMatchId(summonerId: String, gameId: Long) : Int {
        val numberOfMatches = "count"
        val sql = "Select count(*) as $numberOfMatches from $MATCH_SUMMARY where $SUMMONER_ID = '$summonerId'" +
                " AND $GAME_ID <= $gameId"

        val result = dbHelper.executeSqlQuery(sql)
        if (result.first()) {
            val numberOfMatches = result.getInt(numberOfMatches)
            result.close()
            return numberOfMatches
        }

        return -1
    }

}