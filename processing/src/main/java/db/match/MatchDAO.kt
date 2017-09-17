package db.match

import db.requests.DBHelper
import extensions.produceMatch
import model.match.Match
import util.Tables
import util.columnnames.MatchColumns

/**
 * @author Josiah Kendall
 */
class MatchDAO(val dbHelper : DBHelper,
               val teamDAO: TeamDAO,
               val participantDAO: ParticipantDAO,
               val participantIdentityDAO: ParticipantIdentityDAO) : MatchDAOContracts.MatchDAOContract {

    /**
     * Delete alll the matches from our raw database for a specific summoner.
     * This is basically the clean up task that we do after processing
     */
    override fun deleteAllMatchesFromRawDBForASummoner(summonerId: Long) {
        val tables = Tables()
        dbHelper.executeSQLScript("DELETE FROM matchtable")
        dbHelper.executeSQLScript("DELETE FROM mastery")
        dbHelper.executeSQLScript("DELETE FROM participantIdentity")
        dbHelper.executeSQLScript("DELETE FROM participant")
        dbHelper.executeSQLScript("DELETE FROM ban")
        dbHelper.executeSQLScript("DELETE FROM team")
        dbHelper.executeSQLScript("DELETE FROM ${tables.CS_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.XP_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.XP_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.DAMAGE_TAKEN_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.DAMAGE_TAKEN_DIFF_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.CREEPS_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM ${tables.GOLD_PER_MIN}")
        dbHelper.executeSQLScript("DELETE FROM player")
        dbHelper.executeSQLScript("DELETE FROM rune")
        dbHelper.executeSQLScript("DELETE FROM stats")
        dbHelper.executeSQLScript("DELETE FROM timeline")

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

        dbHelper.executeSQLScript(sql)
        //save teams and children
        for (team in match.teams) {
            teamDAO.saveTeamForMatch(team, match.gameId)
        }

        // save participants and children
        var participantIndex = 0
        var summonerId : Long
        for (participant in match.participants) {
            val participantIdentity = match.participantIdentities[participant.participantId-1]

            val player = participantIdentity.player
            if (player != null) {
                summonerId = player.summonerId
                participantDAO.saveParticipant(participant, match.gameId, summonerId)
            }

        }

        var teamId = -1
        var role = ""
        var lane = ""
        // save participantIdentities and children
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
        return result.produceMatch(teams, participants, participantIdentities)
    }
}