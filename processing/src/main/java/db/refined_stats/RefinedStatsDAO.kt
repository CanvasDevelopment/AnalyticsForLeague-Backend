package db.refined_stats

import db.DBHelper
import extensions.produceGameStageAverages
import extensions.produceSummaryStat
import model.refined_stats.GameStageAverages
import model.refined_stats.GameStages
import model.refined_stats.RefinedStatSummary
import util.columnnames.GameStageAveragesColumns
import util.Tables
import model.positions.Jungle


/**
 * @author Josiah Kendall
 */
class RefinedStatsDAO(val dbHelper: DBHelper) : RefinedStatDAOContract {



    val tables = Tables()
    val gameStageColumns = GameStageAveragesColumns()

    /**
     * Fetch the averages for the early, mid and late game for a summoner at a specific stat, in a given position.
     *
     * @param deltaName     The stat table name that we want to fetch the data from. Use [Tables] to get the table name
     *                      you are after.
     * @param summonerId    The hero - the summoner for whom we want to get these stats for.
     * @param role          The role relevant to the position that we are getting the stats for. Use a positions model E.g [Jungle]
     *                      to get the correct role / lane strings
     * @param lane          The lane relevant to the position that we are getting the stats for. Use a positions model e.g [Jungle]
     *                      to get the correct role / lane strings
     *
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


    /**
     * Fetch a list of [RefinedStatSummary] for a specific role.
     * @param summonerId The id of the hero
     * @param role String
     */
    override fun fetchGameSummaryStatsForHero(summonerId: Long, role: String, lane: String): ArrayList<RefinedStatSummary> {
        val sql = "SELECT\n" +
                "  ${tables.PARTICIPANT}.SummonerId,\n" +
                "  ${tables.PARTICIPANT}.GameId,\n" +
                "  ${tables.PARTICIPANT}.championId,\n" +
                "  ${tables.PARTICIPANT}.teamId,\n" +
                "  team.Win,\n" +
                "  team.TowerKills,\n" +
                "  team.DragonKills,\n" +
                "  team.BaronKills,\n" +
                "  team.RiftHeraldKills\n" +
                "FROM ${tables.PARTICIPANT_IDENTITY}\n" +
                "  LEFT JOIN ${tables.PARTICIPANT} ON\n" +
                "                             ${tables.PARTICIPANT_IDENTITY}.gameId = ${tables.PARTICIPANT}.GameId AND\n" +
                "                             ${tables.PARTICIPANT_IDENTITY}.ParticipantId = ${tables.PARTICIPANT}.ParticipantId\n" +
                "  LEFT JOIN team on ${tables.PARTICIPANT}.TeamId = team.TeamId and ${tables.PARTICIPANT_IDENTITY}.gameId = team.GameId\n" +
                "  LEFT JOIN matchtable on ${tables.PARTICIPANT}.GameId = matchtable.GameId\n" +
                "WHERE ${tables.PARTICIPANT_IDENTITY}.SummonerId = '$summonerId'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.lane = '$lane'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.role = '$role'\n" +
                "      AND matchtable.GameDuration > 300"

        val result = dbHelper.executeSqlQuery(sql)
        val statList = ArrayList<RefinedStatSummary>()
        while (result.next()) {
            statList.add(result.produceSummaryStat())
        }

        return statList
    }

    override fun fetchGameSummaryStatsForVillan(summonerId: Long, role: String, lane: String): ArrayList<RefinedStatSummary> {
       val sql = "SELECT\n" +
               "  participantidentity.SummonerId,\n" +
               "  participant.GameId,\n" +
               "  participant.championId,\n" +
               "  participant.teamId,\n" +
               "  team.Win,\n" +
               "  team.TowerKills,\n" +
               "  team.DragonKills,\n" +
               "  team.BaronKills,\n" +
               "  team.RiftHeraldKills\n" +
               "FROM participantidentity\n" +
               "  LEFT JOIN participant ON\n" +
               "                           participantidentity.gameId = participant.GameId AND\n" +
               "                           participant.lane = participantidentity.lane AND\n" +
               "                            participant.role = participantidentity.role AND\n" +
               "                          participantidentity.teamId != participant.TeamId\n" +
               "  LEFT JOIN team on participantidentity.TeamId != team.TeamId and participantidentity.gameId = team.GameId\n" +
               "  LEFT JOIN matchtable on participant.GameId = matchtable.GameId\n" +
               "WHERE participantidentity.SummonerId = '$summonerId'\n" +
               "      AND participantidentity.lane = '$lane'\n" +
               "      AND participantidentity.role = '$role'\n" +
               "      AND matchtable.GameDuration > 300"
        val result = dbHelper.executeSqlQuery(sql)
        val statList = ArrayList<RefinedStatSummary>()
        while (result.next()) {
            statList.add(result.produceSummaryStat())
        }

        return statList
    }

    override fun fetchHeroGameStageStats(summonerId: Long, role: String, lane: String, tableName: String): GameStages {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    fun fetchHeroGameStagesLists(gameStageTable: String,
//                                             summonerId: Long,
//                                             role: String,
//                                             lane: String) : GameStages {
//        val sql = "SELECT\n" +
//                "  $gameStageTable.zeroToTen * 10           AS EarlyGame,\n" +
//                "  $gameStageTable.tenToTwenty * 10         AS MidGame,\n" +
//                "  $$gameStageTable.twentyToThirty * 10      AS LateGame,\n" +
//
//                "FROM ${tables.PARTICIPANT_IDENTITY}\n" +
//                "  JOIN ${tables.PARTICIPANT} ON\n" +
//                "                     ${tables.PARTICIPANT_IDENTITY}.gameId = ${tables.PARTICIPANT}.GameId AND\n" +
//                "                     ${tables.PARTICIPANT}.lane = ${tables.PARTICIPANT_IDENTITY}.lane AND\n" +
//                "                     ${tables.PARTICIPANT}.role = ${tables.PARTICIPANT_IDENTITY}.role AND\n" +
//                "                     ${tables.PARTICIPANT_IDENTITY}.teamId = ${tables.PARTICIPANT}.TeamId\n" +
//                "  LEFT JOIN ${tables.TIMELINE} ON ${tables.TIMELINE}.participantRowId = ${tables.PARTICIPANT}.Id\n" +
//                "  LEFT JOIN $gameStageTable ON $gameStageTable.timelineId = ${tables.TIMELINE}.Id\n" +
//                "WHERE ${tables.PARTICIPANT_IDENTITY}.SummonerId = $summonerId\n" +
//                "      AND ${tables.PARTICIPANT_IDENTITY}.lane = '$lane'\n" +
//                "      AND ${tables.PARTICIPANT_IDENTITY}.role = '$role'"
//
//        val result = dbHelper.executeSqlQuery(sql)
//        return result.produceGameStagesLists(summonerId)
//    }
}