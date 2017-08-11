package db.match

import db.DBHelper
import extensions.produceMastery
import model.match.Mastery
import util.ColumnNames
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 */
class MasteryDAO(val dbHelper : DBHelper) {
    val MASTERY_TABLE = "mastery"
    val columnNames = ColumnNames()
    val DEFAULT_PARTICIPANT_ID = -1

    fun saveMastery(mastery: Mastery, participantRowId : Long) : Int {
        val insertString = "insert into $MASTERY_TABLE (" +
                "${dbHelper.MASTERY_ID_COLUMN}, " +
                "${dbHelper.PARTICIPANT_ROW_ID_COLUMN}, " +
                "${dbHelper.RANK_COLUMN}) VALUES (" +
                "${mastery.masteryId}, " +
                "$participantRowId, " +
                "${mastery.rank})"
        return dbHelper.executeSQLScript(insertString)
    }

    fun getMastery(id : Int) : Mastery{
        val querySql = "Select * from $MASTERY_TABLE where " +
                "${dbHelper.ID_COLUMN} = $id"
        val result = dbHelper.executeSqlQuery(querySql)
        result.next()
        return result.produceMastery()
    }

    fun clearMasteriesForDefaultParticipant() {
        val SQL = "DELETE FROM $MASTERY_TABLE WHERE ${columnNames.PARTICIPANT_ROW_ID} = $DEFAULT_PARTICIPANT_ID"
        dbHelper.executeSQLScript(SQL);
    }

    /**
     * Get all masteries using the participant row id for which the mastery belonged to.
     * During a game, a player has a set of masteries. For each match a user and their details
     * is saved as a participant, so we use the participant id to reference the masteries for that game they participated in.
     */
    fun getAllMasteriesForParticipantInstance(participantRowId: Long) : ArrayList<Mastery> {
        val querySQL = "SELECT * FROM $MASTERY_TABLE where ${columnNames.PARTICIPANT_ROW_ID} = $participantRowId"
        val results = dbHelper.executeSqlQuery(querySQL)
        val masteries = ArrayList<Mastery>()
        while (results.next()) {
            masteries.add(results.produceMastery())
        }

        return masteries
    }
}


