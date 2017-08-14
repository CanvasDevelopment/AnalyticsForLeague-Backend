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

    fun saveParticipantIdentity(participantIdentity: ParticipantIdentity) : Long {
        val insertSQL = "Insert into $PARTICIPANT_IDENTITY_TABLE (" +
                "${dbHelper.PARTICIPANT_ID_COLUMN}," +
                "${dbHelper.GAME_ID_COLUMN}) values (" +
                "${participantIdentity.participantId}," +
                "${participantIdentity.gameId})"

        val result = dbHelper.executeSQLScript(insertSQL)
        playerDAO.savePlayer(participantIdentity.player, result)
        return result
    }

    fun getParticipantIdentity(gameId : Long, summonerId : Long) : ParticipantIdentity  {
        val selectSQL = "select * from $PARTICIPANT_IDENTITY_TABLE\n" +
                "join player on participantidentity.Id = player.ParticipantIdentityRowId\n" +
                "where participantidentity.gameId = $gameId and player.summonerId = $summonerId"
        val result : ResultSet = dbHelper.executeSqlQuery(selectSQL)

        return result.produceParticipantIdentity()
    }

//    fun getParticipantIdentity(participantId : Int, gameId : Int) : ParticipantIdentity {
//
//    }

}


