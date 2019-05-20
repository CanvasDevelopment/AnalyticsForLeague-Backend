package db.match

import db.requests.DBHelper
import extensions.produceParticipantIdentity
import model.match.ParticipantIdentity
import util.MID
import util.Tables
import util.columnnames.ParticipantIdentityColumns
import java.sql.ResultSet
import java.util.*

/**
 * @author Josiah Kendall
 */
class ParticipantIdentityDAO(val dbHelper: DBHelper, val playerDAO: PlayerDAO) {
    private val tables = Tables()
    private val PARTICIPANT_IDENTITY_TABLE = "ParticipantIdentity"
    private val participantIdentityColumns = ParticipantIdentityColumns()

    fun saveParticipantIdentity(participantIdentity: ParticipantIdentity,
                                gameId: Long,
                                summonerId: String,
                                teamId: Int,
                                role: String,
                                lane: String) : Long {
        val insertSQL = "Insert into $PARTICIPANT_IDENTITY_TABLE (" +
                "${dbHelper.PARTICIPANT_ID_COLUMN}," +
                "${dbHelper.SUMMONER_ID_COLUMN}," +
                "${dbHelper.GAME_ID_COLUMN}," +
                "${participantIdentityColumns.TEAM_ID}," +
                "${participantIdentityColumns.ROLE}," +
                "${participantIdentityColumns.LANE}) values (" +
                "${participantIdentity.participantId}," +
                "'$summonerId'," +
                "$gameId," +
                "$teamId," +
                "'$role'," +
                "'${getLane(lane)}')"

        val result = dbHelper.executeSQLScript(insertSQL)
        playerDAO.savePlayer(participantIdentity.player, result)
        return result
    }

    /**
     * Riot returns MID and MIDDLE
     */
    private fun getLane(lane:String) : String {
        if (lane == "MIDDLE") {
            return MID
        }
        return lane
    }

    fun getParticipantIdentity(gameId: Long, summonerId: String) : ParticipantIdentity  {
        val selectSQL = "select * from $PARTICIPANT_IDENTITY_TABLE\n" +
                "join player on ${tables.PARTICIPANT_IDENTITY}.Id = player.ParticipantIdentityRowId\n" +
                "where ${tables.PARTICIPANT_IDENTITY}.gameId = $gameId and ${tables.PARTICIPANT_IDENTITY}.summonerId = '$summonerId'"
        val result : ResultSet = dbHelper.executeSqlQuery(selectSQL)
        result.next()
        val participantIdentity = result.produceParticipantIdentity()
        result.close()
        return participantIdentity
    }

    // This gets? todo fix me
    fun getAllParticipantIdentitiesForAMatch(gameId: Long) : ArrayList<ParticipantIdentity> {
        val selectSQL = "select * from $PARTICIPANT_IDENTITY_TABLE\n" +
                "join player on ${tables.PARTICIPANT_IDENTITY}.Id = player.ParticipantIdentityRowId\n" +
                "where ${tables.PARTICIPANT_IDENTITY}.gameId = $gameId"
        val result : ResultSet = dbHelper.executeSqlQuery(selectSQL)
        val participantIdentities = ArrayList<ParticipantIdentity>()
        while (result.next()) {
            participantIdentities.add(result.produceParticipantIdentity())
        }
        result.close()
        return participantIdentities
    }

//    // This gets the players for
//    fun getAllParticipantIdentitiesForAMatch(gameId: Long) : ArrayList<ParticipantIdentity> {
//        val selectSQL = "select * from $PARTICIPANT_IDENTITY_TABLE\n" +
//                "where ${tables.PARTICIPANT_IDENTITY}.gameId = $gameId"
//        val result : ResultSet = dbHelper.executeSqlQuery(selectSQL)
//        val participantIdentities = ArrayList<ParticipantIdentity>()
//        while (result.next()) {
//            participantIdentities.add(result.produceParticipantIdentity())
//        }
//        return participantIdentities
//    }

}


