package db.match

import db.requests.DBHelper
import extensions.produceMastery
import model.match.Mastery
import util.ColumnNames
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Josiah Kendall
 */
class MasteryDAO(val dbHelper : DBHelper) {
    val MASTERY_TABLE = "Mastery"
    val columnNames = ColumnNames()
    val DEFAULT_PARTICIPANT_ID = -1

    /**
     * Save a bunch of masteries. Faster than using [saveMastery] individually a bunch of times.
     *
     * @param masteries         The masteries to save
     * @param participantRowId  The participant row id. Needed for each row of mastery that we save to
     *                          the database
     */
    fun saveMasteries(masteries : ArrayList<Mastery>, participantRowId: Long) {
        var sql = "Insert into $MASTERY_TABLE(" +
                "${dbHelper.MASTERY_ID_COLUMN}, " +
                "${dbHelper.PARTICIPANT_ROW_ID_COLUMN}, " +
                "${dbHelper.RANK_COLUMN}) VALUES "
        val iterator = masteries.iterator()
        while (iterator.hasNext()) {
            sql = sql.plus(iterator.next().insertString(participantRowId))
            if (iterator.hasNext()) {
                sql = sql.plus(",")
            }
        }
        if (sql.isNotBlank())
            dbHelper.executeSQLScript(sql)
    }

    fun saveMastery(mastery: Mastery, participantRowId : Long) : Long {
        val insertString = "insert into $MASTERY_TABLE (" +
                "${dbHelper.MASTERY_ID_COLUMN}, " +
                "${dbHelper.PARTICIPANT_ROW_ID_COLUMN}, " +
                "${dbHelper.RANK_COLUMN}) VALUES (" +
                "${mastery.masteryId}, " +
                "$participantRowId, " +
                "${mastery.rank})"
        return dbHelper.executeSQLScript(insertString)
    }

    fun getMastery(id : Long) : Mastery{
        val querySql = "Select * from $MASTERY_TABLE where " +
                "${dbHelper.ID_COLUMN} = $id"
        val result = dbHelper.executeSqlQuery(querySql)
        result.next()
         val mastery = result.produceMastery()
        result.close()
        return mastery
    }

    fun clearMasteriesForDefaultParticipant() {
        val SQL = "DELETE FROM $MASTERY_TABLE WHERE ${columnNames.PARTICIPANT_ROW_ID} = $DEFAULT_PARTICIPANT_ID"
        dbHelper.executeSQLScript(SQL)
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
        results.close()
        return masteries
    }
}


