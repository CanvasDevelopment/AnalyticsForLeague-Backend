package db.match

import db.DBHelper
import extensions.produceParticipantIdentity
import model.match.ParticipantIdentity
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 */
class ParticipantIdentityDAO(val dbHelper: DBHelper, val playerDAO: PlayerDAO) {

    val PARTICIPANT_IDENTITY_TABLE = "participantidentity"

    fun saveParticipantIdentity(participantIdentity: ParticipantIdentity, gameId : Long) : Long {
        val insertSQL = "Insert into $PARTICIPANT_IDENTITY_TABLE (" +
                "${dbHelper.PARTICIPANT_ID_COLUMN}," +
                "${dbHelper.GAME_ID_COLUMN}) values (" +
                "${participantIdentity.participantId}," +
                "$gameId)"

        val result = dbHelper.executeSQLScript(insertSQL)
        playerDAO.savePlayer(participantIdentity.player, result)
        return result
    }

    fun getParticipantIdentity(gameId : Long, summonerId : Long) : ParticipantIdentity  {
        val selectSQL = "select * from $PARTICIPANT_IDENTITY_TABLE\n" +
                "join player on participantidentity.Id = player.ParticipantIdentityRowId\n" +
                "where participantidentity.gameId = $gameId and player.summonerId = $summonerId"
        val result : ResultSet = dbHelper.executeSqlQuery(selectSQL)
        result.next()
        return result.produceParticipantIdentity()
    }

    // This gets? todo fix me
    fun getAllParticipantIdentitiesForAMatch(gameId: Long) : ArrayList<ParticipantIdentity> {
        val selectSQL = "select * from $PARTICIPANT_IDENTITY_TABLE\n" +
                "join player on participantidentity.Id = player.ParticipantIdentityRowId\n" +
                "where participantidentity.gameId = $gameId"
        val result : ResultSet = dbHelper.executeSqlQuery(selectSQL)
        val participantIdentities = ArrayList<ParticipantIdentity>()
        while (result.next()) {
            participantIdentities.add(result.produceParticipantIdentity())
        }
        return participantIdentities
    }

//    // This gets the players for
//    fun getAllParticipantIdentitiesForAMatch(gameId: Long) : ArrayList<ParticipantIdentity> {
//        val selectSQL = "select * from $PARTICIPANT_IDENTITY_TABLE\n" +
//                "where participantidentity.gameId = $gameId"
//        val result : ResultSet = dbHelper.executeSqlQuery(selectSQL)
//        val participantIdentities = ArrayList<ParticipantIdentity>()
//        while (result.next()) {
//            participantIdentities.add(result.produceParticipantIdentity())
//        }
//        return participantIdentities
//    }

}


