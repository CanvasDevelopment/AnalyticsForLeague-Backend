package db.refined_stats

import db.DBHelper
import extensions.produceFullGameStat
import extensions.produceGameStageRefinedStat
import extensions.produceSummaryStat
import model.refined_stats.GameStageStat
import model.refined_stats.GameStages
import model.refined_stats.RefinedStatSummary
import util.Tables
import model.positions.Jungle
import model.refined_stats.FullGameStat


/**
 * @author Josiah Kendall
 *
 * A DAO to fetch statistics from the raw tables that we save from the initial download from riot. This allows us an
 * easy way to pull out stats that we want about our hero and our villian. These stats will then be saved to our refined
 * table for easier access.
 */
class RefinedStatsDAO(val dbHelper: DBHelper) {

    private val tables = Tables()

    /**
     * Fetch a list for the early, mid and late game results for a summoner at a specific stat, in a given position.
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
    fun fetchGameStageStatListForHero(
            deltaName: String,
            summonerId: Long,
            role: String,
            lane: String): ArrayList<GameStageStat> {

        val sql = "SELECT\n" +
                "  participantidentity.gameId,\n" +
                "  zeroToTen * 10      AS EarlyGame,\n" +
                "  tenToTwenty * 10    AS MidGame,\n" +
                "  twentyToThirty * 10 AS LateGame\n" +
                "FROM participantidentity\n" +
                "  JOIN participant ON\n" +
                "                     participantidentity.gameId = participant.GameId AND\n" +
                "                     participant.ParticipantId = participantidentity.ParticipantId\n" +
                "  LEFT JOIN timeline ON timeline.participantRowId = participant.Id\n" +
                "  LEFT JOIN $deltaName ON $deltaName.timelineId = timeline.Id\n" +
                "  LEFT JOIN matchtable on participant.GameId = matchtable.GameId\n" +
                "WHERE participantidentity.SummonerId = $summonerId\n" +
                "      AND participantidentity.lane = '$lane'\n" +
                "      AND participantidentity.role = '$role'\n" +
                "      AND GameDuration > 300"

        val result = dbHelper.executeSqlQuery(sql)

        val results = ArrayList<GameStageStat>()
        while (result.next()) {
            results.add(result.produceGameStageRefinedStat())
        }
        return results
    }

    /**
     * Fetch the game stage for our villian, for a certain game stage stat.
     * @param deltaName The name of the table to get the game stages from. Use [Tables] to get a table
     * @param summonerId The summoner id of our HERO. You must use the hero Id here, not the villan Id.
     * @param role The role of the hero and the villan
     * @param lane The lane of the hero and the villan
     */
    fun fetchGameStageStatListForVillian(deltaName: String,
                                         summonerId: Long,
                                         role: String,
                                         lane: String) : ArrayList<GameStageStat> {
        val sql = "SELECT\n" +
                "  participantidentity.gameId,\n" +
                "  zeroToTen * 10 as EarlyGame,\n" +
                "  tenToTwenty * 10 as MidGame,\n" +
                "  twentyToThirty * 10 as LateGame\n" +
                "FROM participantidentity\n" +
                "  JOIN participant ON\n" +
                "                     participantidentity.gameId = participant.GameId AND\n" +
                "                     participant.lane = participantidentity.lane AND\n" +
                "                     participant.role = participantidentity.role AND\n" +
                "                     participantidentity.teamId != participant.TeamId\n" +
                "  LEFT JOIN timeline ON timeline.participantRowId = participant.Id\n" +
                "  LEFT JOIN $deltaName ON $deltaName.timelineId = timeline.Id\n" +
                "    LEFT JOIN matchtable on participant.GameId = matchtable.GameId\n" +
                "WHERE participantidentity.SummonerId = $summonerId\n" +
                "      AND participantidentity.lane = '$lane'\n" +
                "      AND participantidentity.role = '$role'\n" +
                "      AND GameDuration > 300"

        val result = dbHelper.executeSqlQuery(sql)

        val results = ArrayList<GameStageStat>()
        while (result.next()) {
            results.add(result.produceGameStageRefinedStat())
        }
        return results
    }


    /**
     * Fetch a list of [RefinedStatSummary] for a specific role.
     *
     * @param summonerId    The riot given SummonerId of the hero
     * @param role          The role that we want to filter to.
     * @param lane          The Lane that we want to filter to.
     */
    fun fetchGameSummaryStatsForHero(summonerId: Long, role: String, lane: String): ArrayList<RefinedStatSummary> {
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

    fun fetchGameSummaryStatsForVillan(summonerId: Long, role: String, lane: String): ArrayList<RefinedStatSummary> {
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

    /**
     * Fetch an array of [FullGameStat] for a summoner in a specific role and lane.
     * @param heroSummonerId The hero summonerId
     * @param heroRole The hero role
     * @param heroLane The hero lane
     */
    fun fetchPlayerStatisticsForHero(heroSummonerId: Long,
                                     heroRole: String,
                                     heroLane: String) : ArrayList<FullGameStat> {
        val sql = "SELECT\n" +
                "  participantidentity.gameId,\n" +
                "  stats.kills,\n" +
                "  stats.deaths,\n" +
                "  stats.assists,\n" +
                "  stats.wardsPlaced,\n" +
                "  stats.wardsKilled\n" +
                "FROM participantidentity\n" +
                "  JOIN participant ON participantidentity.gameId = participant.GameId\n" +
                "                      AND participant.ParticipantId = participantidentity.ParticipantId\n" +
                "  JOIN stats ON stats.ParticipantRowId = participant.Id\n" +
                "  LEFT JOIN matchtable ON participant.GameId = matchtable.GameId\n" +
                "WHERE participantidentity.lane = '$heroLane'\n" +
                "      AND participantidentity.role = '$heroRole'\n" +
                "      AND participantidentity.SummonerId = $heroSummonerId\n" +
                "      AND GameDuration > 300"
        val result = dbHelper.executeSqlQuery(sql)
        val statList = ArrayList<FullGameStat>()
        while (result.next()) {
            statList.add(result.produceFullGameStat())
        }
        return statList
    }

    fun fetchPlayerStatisticsForVillian(heroSummonerId: Int, heroRole: String, heroLane: String): ArrayList<FullGameStat> {
        val sql = "SELECT\n" +
                "  participantidentity.gameId,\n" +
                "  stats.kills,\n" +
                "  stats.deaths,\n" +
                "  stats.assists,\n" +
                "  stats.wardsPlaced,\n" +
                "  stats.wardsKilled\n" +
                "FROM participantidentity\n" +
                "  JOIN participant ON\n" +
                "                     participantidentity.gameId = participant.GameId AND\n" +
                "                     participant.lane = participantidentity.lane AND\n" +
                "                     participant.role = participantidentity.role AND\n" +
                "                     participantidentity.teamId != participant.TeamId\n" +
                "  JOIN stats ON stats.ParticipantRowId = participant.Id\n" +
                "  LEFT JOIN matchtable ON participant.GameId = matchtable.GameId\n" +
                "WHERE participantidentity.lane = '$heroLane'\n" +
                "      AND participantidentity.role = '$heroRole'\n" +
                "      AND participantidentity.SummonerId = $heroSummonerId\n" +
                "      AND GameDuration > 300"
        val result = dbHelper.executeSqlQuery(sql)
        val statList = ArrayList<FullGameStat>()
        while (result.next()) {
            statList.add(result.produceFullGameStat())
        }
        return statList
    }


}