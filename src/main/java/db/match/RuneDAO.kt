package db.match

import db.DBHelper
import extensions.produceRune
import model.match.Rune
import util.ColumnNames

/**
 * @author Josiah Kendall
 */
class RuneDAO(val dbHelper: DBHelper) {
    val columnNames = ColumnNames()
    val RUNE_TABLE = "rune"
    val DEFAULT_PARTICIPANT_ID = -1

    fun saveRune(rune: Rune, participantRowId : Long) : Long {
        //insert into rune (ParticipantRowId, RuneId, Rank) VALUES (1, 2, 3)

        val sql = "Insert into $RUNE_TABLE (" +
                "${columnNames.PARTICIPANT_ROW_ID}," +
                "${columnNames.RUNE_ID}," +
                "${columnNames.RANK}) VALUES (" +
                "$participantRowId, " +
                "${rune.runeId}, " +
                "${rune.rank})"

        return dbHelper.executeSQLScript(sql)
    }

    fun getRuneById(id : Long) : Rune {
        val sql = "SELECT * FROM $RUNE_TABLE " +
                "WHERE ${columnNames.ID} = $id"
        val result = dbHelper.executeSqlQuery(sql)
        result.next()
        return result.produceRune()
    }

    /**
     * Get all runes for a participant instance by the id of the participant table. This gets all the runes associated with
     * a specific participant entry in the database.
     */
    fun getAllRunesForAParticipantRowId(participantRowId: Long) : ArrayList<Rune> {
        val sql = "SELECT * FROM $RUNE_TABLE " +
                "WHERE ${columnNames.PARTICIPANT_ROW_ID} = $participantRowId"
        val result = dbHelper.executeSqlQuery(sql)
        val runes = ArrayList<Rune>()
        while (result.next()) {
            runes.add(result.produceRune())
        }

        return runes
    }

    fun clearRunesForDefaultParticipant() {
        val SQL = "DELETE FROM $RUNE_TABLE WHERE ${columnNames.PARTICIPANT_ROW_ID} = $DEFAULT_PARTICIPANT_ID"
        dbHelper.executeSQLScript(SQL)
    }
}

