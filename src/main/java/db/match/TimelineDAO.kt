package db.match

import db.DBHelper
import db.stats.GameStageDeltaDAOImpl
import extensions.columnNames
import extensions.produceTimeline
import model.match.Timeline
import util.Tables
import util.columnnames.TimelineColumns
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 */
class TimelineDAO(val dbHelper : DBHelper, val gameStageDeltasDAO: GameStageDeltaDAOImpl) {

    val TIMELINE_TABLE = "timeline"
    val timelineColumns = TimelineColumns()
    val tables = Tables()

    fun saveTimeline(timeline : Timeline, participantRowId : Long) : Int {
        val sql = "INSERT into $TIMELINE_TABLE(" +
                "${timelineColumns.PARTICIPANT_ID}, " +
                "${timelineColumns.PARTICIPANT_ROW_ID}, " +
                "${timelineColumns.ROLE}, " +
                "${timelineColumns.LANE}) VALUES (" +
                "${timeline.participantId}, " +
                "$participantRowId, " +
                "'${timeline.role}', " +
                "'${timeline.lane}')"

        val timelineId : Long = dbHelper.executeSQLScript(sql).toLong()
        if (timelineId != -1.toLong()) {
            // save all the creeps
            val cpmId = gameStageDeltasDAO.saveDeltas(timeline.creepsPerMin, tables.CREEPS_PER_MIN, timelineId)
            val xpmId = gameStageDeltasDAO.saveDeltas(timeline.xpPerMin, tables.XP_PER_MIN, timelineId)
            val gpmId = gameStageDeltasDAO.saveDeltas(timeline.goldPerMin, tables.GOLD_PER_MIN, timelineId)
            val csDiffPerMinId = gameStageDeltasDAO.saveDeltas(timeline.csDiffPerMin, tables.CS_DIFF_PER_MIN, timelineId)
            val xpdiffPerMinId = gameStageDeltasDAO.saveDeltas(timeline.xpDiffPerMin, tables.XP_DIFF_PER_MIN, timelineId)
            val dtpmId = gameStageDeltasDAO.saveDeltas(timeline.damageTakenPerMin, tables.DAMAGE_TAKEN_PER_MIN, timelineId)
            val dtdpmId = gameStageDeltasDAO.saveDeltas(timeline.damageTakenDiffPerMin, tables.DAMAGE_TAKEN_DIFF_PER_MIN, timelineId)
            cpmId.logId(tables.CREEPS_PER_MIN)
            xpmId.logId(tables.XP_PER_MIN)
            gpmId.logId(tables.GOLD_PER_MIN)
            csDiffPerMinId.logId(tables.CS_DIFF_PER_MIN)
            xpdiffPerMinId.logId(tables.XP_DIFF_PER_MIN)
            dtpmId.logId(tables.DAMAGE_TAKEN_PER_MIN)
            dtdpmId.logId(tables.DAMAGE_TAKEN_DIFF_PER_MIN)
        }
        return timelineId.toInt()
    }

    fun getTimelineByParticipantRowId(participantRowId: Long) : Timeline {
        val sql = "SELECT * from $TIMELINE_TABLE " +
                "WHERE ${timelineColumns.PARTICIPANT_ROW_ID} = $participantRowId"
        val result = dbHelper.executeSqlQuery(sql)
        result.next()
        val id = result.getLong(columnNames.ID)
        val cpm = gameStageDeltasDAO.getGameStageDeltasForTimeline(id, tables.CREEPS_PER_MIN)
        val xpm = gameStageDeltasDAO.getGameStageDeltasForTimeline(id, tables.XP_PER_MIN)
        val gpm = gameStageDeltasDAO.getGameStageDeltasForTimeline(id, tables.GOLD_PER_MIN)
        val dtpm = gameStageDeltasDAO.getGameStageDeltasForTimeline(id, tables.DAMAGE_TAKEN_PER_MIN)
        val csdpm = gameStageDeltasDAO.getGameStageDeltasForTimeline(id, tables.CS_DIFF_PER_MIN)
        val xpdpm = gameStageDeltasDAO.getGameStageDeltasForTimeline(id, tables.XP_DIFF_PER_MIN)
        val dtdpm = gameStageDeltasDAO.getGameStageDeltasForTimeline(id, tables.DAMAGE_TAKEN_DIFF_PER_MIN)
        return result.produceTimeline(cpm, xpm, gpm, dtpm, csdpm, xpdpm, dtdpm)

    }

    private fun Int.logId(tableName : String) {
        val message = "Saved item to table $tableName with id result : $this"
        Logger.getLogger(TimelineDAO::class.java.name).log(Level.SEVERE, message, "")
    }
}