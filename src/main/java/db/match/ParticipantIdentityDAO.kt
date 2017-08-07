package db.match

import db.DBHelper
import model.match.ParticipantIdentity

/**
 * @author Josiah Kendall
 */
class ParticipantIdentityDAO(val dbHelper: DBHelper, val playerDAO: PlayerDAO) {

    val PARTICIPANT_IDENTITY_TABLE = "participantidentity"

    fun saveParticipantIdentity(participantIdentity: ParticipantIdentity) : Int {
        val insertSQL = "Insert into $PARTICIPANT_IDENTITY_TABLE (${dbHelper.GAME_ID_COLUMN} values (${participantIdentity.gameId})"
        val result = dbHelper.executeSQLScript(insertSQL)
        playerDAO.savePlayer(participantIdentity.player, result)
        return result
    }

    fun getParticipantIdentity(gameId : Int, summonerId : Int) : ParticipantIdentity {

    }

}