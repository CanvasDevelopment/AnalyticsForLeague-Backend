package db.match

import db.requests.DBHelper
import extensions.GAME_ID
import extensions.SUMMONER_ID
import extensions.produceMatch
import model.match.Match
import util.Tables
import util.columnnames.MatchColumns
import util.logToConsole
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 */
class MatchDAO(val dbHelper : DBHelper,
               val teamDAO: TeamDAO,
               val participantDAO: ParticipantDAO,
               val participantIdentityDAO: ParticipantIdentityDAO) : MatchDAOContracts.MatchDAOContract {
    private val tables = Tables()
    fun doesMatchTableExist() : Boolean {
        return false
    }

//    fun createMatchTable() : Boolean {
//        val createTableSql = "Create table $MATCH_TABLE(" +
//                "${matchColumns.GAME_ID} BIGINT, " +
//                "${matchColumns.PLATFORM_ID} VARCHAR(32), " +
//                "${matchColumns.GAME_CREATION} BIGINT," +
//                "${matchColumns.GAME_DURATION}, " +
//                "${matchColumns.QUEUE_ID}, " +
//                "${matchColumns.MAP_ID}, " +
//                "${matchColumns.SEASON_ID}, " +
//                "${matchColumns.GAME_VERSION}, " +
//                "${matchColumns.GAME_MODE}, " +
//                "${matchColumns.GAME_TYPE})"
//    }

    /**
     * Delete alll the matches from our raw database for a specific summoner.
     * This is basically the clean up task that we do after processing
     */
    override fun deleteAllMatchesFromRawDBForASummoner(summonerId: String) {
        val tables = Tables()
        dbHelper.connect()
        dbHelper.executeSQLScript("DELETE FROM MatchTable")
        dbHelper.executeSQLScript("DELETE FROM Mastery")
        dbHelper.executeSQLScript("DELETE FROM ParticipantIdentity")
        dbHelper.executeSQLScript("DELETE FROM Participant")
        dbHelper.executeSQLScript("DELETE FROM Ban")
        dbHelper.executeSQLScript("DELETE FROM Team")
        dbHelper.executeSQLScript("DELETE FROM ${tables.CS_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.XP_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.XP_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.DAMAGE_TAKEN_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.DAMAGE_TAKEN_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.CREEPS_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.GOLD_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM Player")
        dbHelper.executeSQLScript("DELETE FROM Rune")
        dbHelper.executeSQLScript("DELETE FROM Stats")
        dbHelper.executeSQLScript("DELETE FROM Timeline")

    }

    val MATCH_TABLE = "MatchTable"
    val matchColumns = MatchColumns()



    /**
     * Save a [Match] (buy a farm). This saves a match, and also saves (through their DAO's) all the children of this
     * [Match].
     */
    override fun saveMatch(match : Match) {
        dbHelper.connect()
        val sql = "insert into $MATCH_TABLE(" +
                "${matchColumns.GAME_ID}, " +
                "${matchColumns.PLATFORM_ID}, " +
                "${matchColumns.GAME_CREATION}, " +
                "${matchColumns.GAME_DURATION}, " +
                "${matchColumns.QUEUE_ID}, " +
                "${matchColumns.MAP_ID}, " +
                "${matchColumns.SEASON_ID}, " +
                "${matchColumns.GAME_VERSION}, " +
                "${matchColumns.GAME_MODE}, " +
                "${matchColumns.GAME_TYPE}) VALUES (" +
                "${match.gameId}," +
                "'${match.platformId}'," +
                "${match.gameCreation}," +
                "${match.gameDuration}," +
                "${match.queueId}," +
                "${match.mapId}," +
                "${match.seasonId}," +
                "'${match.gameVersion}'," +
                "'${match.gameMode}'," +
                "'${match.gameType}')"

        val result = dbHelper.executeSQLScript(sql)
        util.logToConsole("Saved match with game id : ${match.gameId}. Result was : $result")
        //save teams and children
        val startTime = System.currentTimeMillis()
        util.logToConsole("Starting saving of teams")
        for (team in match.teams) {
            teamDAO.saveTeamForMatch(team, match.gameId)
        }
        util.logToConsole("Finished saving teams. Took ${System.currentTimeMillis() - startTime}")

        // save participants and children
        var participantIndex = 0
        var summonerId : String
        val participantStartTime = System.currentTimeMillis()
        for (participant in match.participants) {
            val participantIdentity = match.participantIdentities[participant.participantId-1]

            val player = participantIdentity.player
            if (player != null) {
                summonerId = player.summonerId
                participantDAO.saveParticipant(participant, match.gameId, summonerId)
            }
        }

        util.logToConsole("finished saving participants. Took ${System.currentTimeMillis() - participantStartTime}")

        var teamId = -1
        var role = ""
        var lane = ""
        // save participantIdentities and children

        // logging shit
        val particpantIdentityStartTime = System.currentTimeMillis()

        for (participantIdentity in match.participantIdentities) {
            // todo tidy this shitty code
            val piSummonerId = participantIdentity.player?.summonerId

            // grab our variables that we store in this table to make for more efficient queries.
            teamId = match.participants[participantIdentity.participantId-1].teamId
            role = match.participants[participantIdentity.participantId-1].timeline.role
            lane = match.participants[participantIdentity.participantId-1].timeline.lane

            // Save participant
            if (piSummonerId != null) {
                participantIdentityDAO.saveParticipantIdentity(
                        participantIdentity,
                        match.gameId,
                        piSummonerId,
                        teamId,
                        role,
                        lane)
            }
        }

        logToConsole("Finished saving participantIdenties. Took ${System.currentTimeMillis() - particpantIdentityStartTime}")
    }

    /**
     * Check if a match already exists.
     * @param gameId The Id of the match that we want to check exists
     * @return
     */
    fun exists(gameId: Long) : Boolean {
        val sql = "Select ${matchColumns.GAME_ID} from $MATCH_TABLE " +
                "WHERE ${matchColumns.GAME_ID} = $gameId"
        val result = dbHelper.executeSqlQuery(sql)
        val exists =  result.next()
        result.close()
        return exists
    }

    /**
     * Load a [Match] by the matches [MatchColumns.GAME_ID] provided by riot
     * @param gameId The matchId
     */
    override fun getMatch(gameId: Long) : Match {
        val sql = "Select * from $MATCH_TABLE " +
                "WHERE ${matchColumns.GAME_ID} = $gameId"
        val result = dbHelper.executeSqlQuery(sql)

        val teams = teamDAO.getAllTeamsForGameId(gameId)
        val participants = participantDAO.getAllParticipantsForMatch(gameId)
        val participantIdentities = participantIdentityDAO.getAllParticipantIdentitiesForAMatch(gameId)
        result.next()
        val match =  result.produceMatch(teams, participants, participantIdentities)
        result.close()
        return match
    }

    /**
     * Fetch the most recently saved match in the database for the given user.
     *
     * @param summonerId The id of the summoner for whom we are getting the most recent match
     */
    fun fetchIdOfMostRecentlySavedMatchForSummoner(summonerId: String) : Long {
        val sql = "SELECT Max($MATCH_TABLE.$GAME_ID) as $GAME_ID, $SUMMONER_ID from $MATCH_TABLE " +
                "JOIN ${tables.PARTICIPANT} ON $MATCH_TABLE.$GAME_ID = ${tables.PARTICIPANT}.GameId where $SUMMONER_ID = '$summonerId'"
        val result : ResultSet = dbHelper.executeSqlQuery(sql)
        if (result.first()) {
            return result.getLong(GAME_ID)
        }
        result.close()
        return -1
    }

    fun fetchNumberOfMatchesForUser(summonerId: String): Int {
        val sql = "SELECT count($MATCH_TABLE.$GAME_ID) as numberOfGames FROM $MATCH_TABLE " +
                "JOIN ${tables.PARTICIPANT} ON $MATCH_TABLE.$GAME_ID = ${tables.PARTICIPANT}.$GAME_ID where $SUMMONER_ID = '$summonerId'"
        val result : ResultSet = dbHelper.executeSqlQuery(sql)
        var numberOfMatches = 0
        if (result.first()) {
            numberOfMatches = result.getInt("numberOfGames")
        }
        result.close()
        return numberOfMatches
    }
}