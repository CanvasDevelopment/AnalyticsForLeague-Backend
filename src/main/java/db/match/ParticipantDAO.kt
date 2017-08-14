package db.match

import db.DBHelper
import extensions.produceParticipant
import model.match.Mastery
import model.match.Participant
import model.match.Rune
import util.columnnames.ParticipantColumns

/**
 * @author Josiah Kendall
 */
class ParticipantDAO(val dbHelper : DBHelper,
                     val timelineDAO: TimelineDAO,
                     val runeDAO: RuneDAO,
                     val masteryDAO: MasteryDAO,
                     val statDAO: StatDAO) {

    val PARTICIPANT_TABLE = "participant"
    val participantColumns = ParticipantColumns()

    /**
     * Save a participant, and all the child items of it.
     *
     */
    fun saveParticipant(participant : Participant, gameId : Long) : Long {
        val sql = "insert into $PARTICIPANT_TABLE(" +
                "${participantColumns.PARTICIPANT_ID}, " +
                "${participantColumns.TEAM_ID}, " +
                "${participantColumns.CHAMPION_ID}, " +
                "${participantColumns.SPELL_1_ID}, " +
                "${participantColumns.SPELL_2_ID}, " +
                "${participantColumns.HIGHEST_SEASON_ACHIEVED_TIER}, " +
                "${participantColumns.GAME_ID}) VALUES (" +
                "${participant.participantId}," +
                "${participant.teamId}," +
                "${participant.championId}," +
                "${participant.spell1Id}," +
                "${participant.spell2Id}," +
                "${participant.highestAchievedSeasonTier}," +
                "$gameId)"

        val particpantRowId = dbHelper.executeSQLScript(sql).toLong()
        // save timeline
        timelineDAO.saveTimeline(participant.timeline, particpantRowId)
        // save runes
        saveRunes(participant.runes, particpantRowId)
        // save stats
        statDAO.saveStats(participant.stats, particpantRowId)
        //save masteries
        saveMasteries(participant.masteries, particpantRowId)

        return particpantRowId
    }

//    fun getParticipantByGameIdAndParticipantId(participantId : Int, gameId: Long) : Participant {
//
//    }

    fun getParticipantByRowId(participantRowId: Long) : Participant {
        val timeline = timelineDAO.getTimelineByParticipantRowId(participantRowId)
        val stats = statDAO.getStatForParticipant(participantRowId)
        val runes = runeDAO.getAllRunesForAParticipantRowId(participantRowId)
        val masteries = masteryDAO.getAllMasteriesForParticipantInstance(participantRowId)

        val sql = "select * from $PARTICIPANT_TABLE where Id = $participantRowId"
        val result = dbHelper.executeSqlQuery(sql)

        return result.produceParticipant(timeline,stats,masteries,runes)
    }

    private fun saveRunes(runes : ArrayList<Rune>, participantRowId : Long) {
        val runesIterator = runes.iterator()
        while (runesIterator.hasNext()) {
            val rune = runesIterator.next()
            runeDAO.saveRune(rune, participantRowId)
        }
    }

    private fun saveMasteries(masteries : ArrayList<Mastery>, participantRowId: Long) {
        val masteriesIterator = masteries.iterator()
        while (masteriesIterator.hasNext()) {
            val mastery = masteriesIterator.next()
            masteryDAO.saveMastery(mastery, participantRowId)
        }
    }
}