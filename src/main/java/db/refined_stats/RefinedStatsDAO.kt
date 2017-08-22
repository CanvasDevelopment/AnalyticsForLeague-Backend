package db.refined_stats

import db.DBHelper
import extensions.produceGameStageAverages
import model.refined_stats.GameStageAverages
import util.columnnames.GameStageAveragesColumns
import util.Tables

/**
 * @author Josiah Kendall
 */
class RefinedStatsDAO(val dbHelper: DBHelper) {

    val gameStageColumns = GameStageAveragesColumns()

    /**
     * Fetch the averages for the early, mid and late game for a summoner at a specific stat, in a given position.
     *
     * @param deltaName     The stat table name that we want to fetch the data from. Use [Tables] to get the table name
     *                      you are after.
     * @param summonerId    The hero - the summoner for whom we want to get these stats for.
     * @param role          The role relevant to the position that we are getting the stats for. Use a positions model
     *                      E.g [model.positions.Jungle]
     * @param lane          The lane relevant to the position that we are getting the stats for. Use a positions model
     *                      e.g [model.positions.Jungle]
     */
    fun fetchGameStageStatAverageForHero(
            deltaName: String,
            summonerId: Long,
            role: String,
            lane: String): GameStageAverages {

        val sql = "SELECT\n" +
                "  avg(zeroToTen) * 10      AS ${gameStageColumns.EARLY_GAME},\n" +
                "  avg(tenToTwenty) * 10    AS ${gameStageColumns.MID_GAME},\n" +
                "  avg(twentyToThirty) * 10 AS ${gameStageColumns.LATE_GAME}\n" +
                "FROM participantidentity\n" +
                "  JOIN participant ON\n" +
                "                     participantidentity.gameId = participant.GameId AND\n" +
                "                     participant.ParticipantId = participantidentity.ParticipantId\n" +
                "  LEFT JOIN timeline ON timeline.participantRowId = participant.Id\n" +
                "  LEFT JOIN $deltaName ON $deltaName.timelineId = timeline.Id\n" +
                "WHERE participantidentity.SummonerId = $summonerId\n" +
                "      AND participantidentity.lane = '$lane'\n" +
                "      AND participantidentity.role = '$role'"

        val result = dbHelper.executeSqlQuery(sql)
        if (result.next()) {
            return result.produceGameStageAverages()
        }
        return GameStageAverages(-1f, -1f, -1f)
    }
}