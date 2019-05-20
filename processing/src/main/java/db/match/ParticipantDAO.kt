package db.match

import db.requests.DBHelper
import extensions.ID
import extensions.produceParticipant
import model.match.Mastery
import model.match.Participant
import model.match.Rune
import util.MID
import util.columnnames.ParticipantColumns
import util.logToConsole
import kotlin.collections.ArrayList

/**
 * @author Josiah Kendall
 */
class ParticipantDAO(val dbHelper : DBHelper,
                     val timelineDAO: TimelineDAO,
                     val runeDAO: RuneDAO,
                     val masteryDAO: MasteryDAO,
                     val statDAO: StatDAO) {

    val PARTICIPANT_TABLE = "Participant"
    val participantColumns = ParticipantColumns()

    /**
     * Save a [Participant], and all the child items of it.
     *
     */
    fun saveParticipant(participant: Participant, gameId: Long, summonerId: String) : Long {
        val sql = "insert into $PARTICIPANT_TABLE(" +
                "${participantColumns.PARTICIPANT_ID}, " +
                "${participantColumns.TEAM_ID}, " +
                "${participantColumns.CHAMPION_ID}, " +
                "${participantColumns.SPELL_1_ID}, " +
                "${participantColumns.SPELL_2_ID}, " +
                "${participantColumns.SUMMONER_ID}, " +
                "${participantColumns.HIGHEST_SEASON_ACHIEVED_TIER}, " +
                "${participantColumns.GAME_ID}," +
                "${participantColumns.ROLE}," +
                "${participantColumns.LANE}) VALUES (" +
                "${participant.participantId}," +
                "${participant.teamId}," +
                "${participant.championId}," +
                "${participant.spell1Id}," +
                "${participant.spell2Id}," +
                "'$summonerId'," +
                "'${participant.highestAchievedSeasonTier}'," +
                "$gameId," +
                "'${participant.timeline.role}'," +
                "'${getLane(participant.timeline.lane)}')"

        val particpantRowId = dbHelper.executeSQLScript(sql)
        // save timeline
        val startTimelineSave = System.currentTimeMillis()
        timelineDAO.saveTimeline(participant.timeline, particpantRowId)
        logToConsole("Saved timeline. Took ${System.currentTimeMillis() - startTimelineSave}")
        // save runes

        val startRuneSaveTime = System.currentTimeMillis()
        saveRunes(participant.runes, particpantRowId)
        logToConsole("Saved runes. Took ${System.currentTimeMillis() - startRuneSaveTime}")

        // save stats
        val saveStatStartTime = System.currentTimeMillis()
        statDAO.saveStats(participant.stats, particpantRowId)
        logToConsole("Saved stats. Took ${System.currentTimeMillis() - saveStatStartTime}")
        //save masteries
        val startTimeMasterySave = System.currentTimeMillis()
        saveMasteries(participant.masteries, particpantRowId)
        logToConsole("Saved masteries. Took ${System.currentTimeMillis() - startTimeMasterySave}")

        return particpantRowId
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
    /**
     * Get all [Participant] instances that are saved for a [Match]
     * @param gameId The id of the match we want the participants for.
     */
    fun getAllParticipantsForMatch(gameId: Long) : ArrayList<Participant> {
        val sql = "SELECT * FROM $PARTICIPANT_TABLE " +
                "WHERE ${participantColumns.GAME_ID} = $gameId"
        val result = dbHelper.executeSqlQuery(sql)

        // Our array of results that we are looking for.
        val participants = ArrayList<Participant>()
        while (result.next()) {
            val participantRowId = result.getLong(ID)
            val timeline = timelineDAO.getTimelineByParticipantRowId(participantRowId)
            val stats = statDAO.getStatForParticipant(participantRowId)
            val runes = runeDAO.getAllRunesForAParticipantRowId(participantRowId)
            val masteries = masteryDAO.getAllMasteriesForParticipantInstance(participantRowId)

            val participant = result.produceParticipant(timeline, stats, masteries, runes)
            participants.add(participant)
        }

        result.close()
        return participants
    }

    fun getParticipantByRowId(participantRowId: Long) : Participant {
        val timeline = timelineDAO.getTimelineByParticipantRowId(participantRowId)
        val stats = statDAO.getStatForParticipant(participantRowId)
        val runes = runeDAO.getAllRunesForAParticipantRowId(participantRowId)
        val masteries = masteryDAO.getAllMasteriesForParticipantInstance(participantRowId)

        val sql = "select * from $PARTICIPANT_TABLE where Id = $participantRowId"
        val result = dbHelper.executeSqlQuery(sql)
        result.next()
        val participant = result.produceParticipant(timeline,stats,masteries,runes)
        result.close()
        return participant
    }

    private fun saveRunes(runes : ArrayList<Rune>?, participantRowId : Long) {
        if (runes != null) {
            runeDAO.saveAllRunes(runes, participantRowId)
        }
    }

    private fun saveMasteries(masteries : ArrayList<Mastery>?, participantRowId: Long) {
        if (masteries != null) {
            masteryDAO.saveMasteries(masteries, participantRowId)
        }
    }
}