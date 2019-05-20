package db.refined_stats

import db.requests.DBHelper
import extensions.produceFullGameStat
import extensions.produceGameStageRefinedStat
import extensions.produceSummaryStat
import model.refined_stats.GameStageStat
import model.refined_stats.TeamSummaryStat
import util.Tables
import model.positions.Jungle
import model.refined_stats.FullGameStat
import java.util.*


/**
 * @author Josiah Kendall
 *
 * A DAO to fetch statistics from the raw tables that we save from the initial download from riot. This allows us an
 * easy way to pull out stats that we want about our hero and our villian. These stats will then be saved to our refined
 * table for easier access.
 */
class RefinedStatsDAO(val dbHelper: DBHelper) : RefinedStatDAOContract {

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
    override fun fetchGameStageStatListForHero(
            deltaName: String,
            summonerId: String,
            role: String,
            lane: String): ArrayList<GameStageStat> {

        val sql = "SELECT\n" +
                "  ${tables.PARTICIPANT_IDENTITY}.gameId,\n" +
                "  zeroToTen * 10      AS EarlyGame,\n" +
                "  tenToTwenty * 10    AS MidGame,\n" +
                "  twentyToThirty * 10 AS LateGame\n" +
                "FROM ${tables.PARTICIPANT_IDENTITY}\n" +
                "  JOIN ${tables.PARTICIPANT} ON\n" +
                "                     ${tables.PARTICIPANT_IDENTITY}.gameId = ${tables.PARTICIPANT}.GameId AND\n" +
                "                     ${tables.PARTICIPANT}.ParticipantId = ${tables.PARTICIPANT_IDENTITY}.ParticipantId\n" +
                "  LEFT JOIN ${tables.TIMELINE} ON ${tables.TIMELINE}.participantRowId = ${tables.PARTICIPANT}.Id\n" +
                "  LEFT JOIN $deltaName ON $deltaName.timelineId = ${tables.TIMELINE}.Id\n" +
                "  LEFT JOIN ${tables.MATCH_TABLE} on ${tables.PARTICIPANT}.GameId = ${tables.MATCH_TABLE}.GameId\n" +
                "WHERE ${tables.PARTICIPANT_IDENTITY}.SummonerId = '$summonerId'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.lane = '$lane'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.role = '$role'\n" +
                "      AND GameDuration > 300"

        val result = dbHelper.executeSqlQuery(sql)

        val results = ArrayList<GameStageStat>()
        while (result.next()) {
            results.add(result.produceGameStageRefinedStat())
        }
        result.close()
        return results
    }

    /**
     * Fetch the game stage for our villian, for a certain game stage stat.
     * @param deltaName The name of the table to get the game stages from. Use [Tables] to get a table
     * @param summonerId The summoner id of our HERO. You must use the hero Id here, not the villan Id.
     * @param role The role of the hero and the villan
     * @param lane The lane of the hero and the villan
     */
    override fun fetchGameStageStatListForVillian(deltaName: String,
                                                  summonerId: String,
                                                  role: String,
                                                  lane: String) : ArrayList<GameStageStat> {
        val sql = "SELECT\n" +
                "  ${tables.PARTICIPANT_IDENTITY}.gameId,\n" +
                "  zeroToTen * 10 as EarlyGame,\n" +
                "  tenToTwenty * 10 as MidGame,\n" +
                "  twentyToThirty * 10 as LateGame\n" +
                "FROM ${tables.PARTICIPANT_IDENTITY}\n" +
                "  JOIN ${tables.PARTICIPANT} ON\n" +
                "                     ${tables.PARTICIPANT_IDENTITY}.gameId = ${tables.PARTICIPANT}.GameId AND\n" +
                "                     ${tables.PARTICIPANT}.lane = ${tables.PARTICIPANT_IDENTITY}.lane AND\n" +
                "                     ${tables.PARTICIPANT}.role = ${tables.PARTICIPANT_IDENTITY}.role AND\n" +
                "                     ${tables.PARTICIPANT_IDENTITY}.teamId != ${tables.PARTICIPANT}.TeamId\n" +
                "  LEFT JOIN ${tables.TIMELINE} ON ${tables.TIMELINE}.participantRowId = ${tables.PARTICIPANT}.Id\n" +
                "  LEFT JOIN $deltaName ON $deltaName.timelineId = ${tables.TIMELINE}.Id\n" +
                "    LEFT JOIN ${tables.MATCH_TABLE} on ${tables.PARTICIPANT}.GameId = ${tables.MATCH_TABLE}.GameId\n" +
                "WHERE ${tables.PARTICIPANT_IDENTITY}.SummonerId = '$summonerId'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.lane = '$lane'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.role = '$role'\n" +
                "      AND GameDuration > 300"

        val result = dbHelper.executeSqlQuery(sql)

        val results = ArrayList<GameStageStat>()
        while (result.next()) {
            results.add(result.produceGameStageRefinedStat())
        }
        result.close()
        return results
    }


    /**
     * Fetch a list of [TeamSummaryStat] for a specific role.
     *
     * @param summonerId    The riot given SummonerId of the hero
     * @param role          The role that we want to filter to.
     * @param lane          The Lane that we want to filter to.
     */
    override fun fetchGameSummaryStatsForHero(summonerId: String, role: String, lane: String): ArrayList<TeamSummaryStat> {
        val sql = "SELECT\n" +
                "  ${tables.PARTICIPANT}.SummonerId,\n" +
                "  ${tables.PARTICIPANT}.GameId,\n" +
                "  ${tables.PARTICIPANT}.championId,\n" +
                "  ${tables.PARTICIPANT}.teamId,\n" +
                "  ${tables.TEAM}.Win,\n" +
                "  ${tables.TEAM}.TowerKills,\n" +
                "  ${tables.TEAM}.DragonKills,\n" +
                "  ${tables.TEAM}.BaronKills,\n" +
                "  ${tables.TEAM}.RiftHeraldKills\n" +
                "FROM ${tables.PARTICIPANT_IDENTITY}\n" +
                "  LEFT JOIN ${tables.PARTICIPANT} ON\n" +
                "                             ${tables.PARTICIPANT_IDENTITY}.gameId = ${tables.PARTICIPANT}.GameId AND\n" +
                "                             ${tables.PARTICIPANT_IDENTITY}.ParticipantId = ${tables.PARTICIPANT}.ParticipantId\n" +
                "  LEFT JOIN ${tables.TEAM} on ${tables.PARTICIPANT}.TeamId = ${tables.TEAM}.TeamId and ${tables.PARTICIPANT_IDENTITY}.gameId = ${tables.TEAM}.GameId\n" +
                "  LEFT JOIN ${tables.MATCH_TABLE} on ${tables.PARTICIPANT}.GameId = ${tables.MATCH_TABLE}.GameId\n" +
                "WHERE ${tables.PARTICIPANT_IDENTITY}.SummonerId = '$summonerId'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.lane = '$lane'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.role = '$role'\n" +
                "      AND ${tables.MATCH_TABLE}.GameDuration > 300"

        val result = dbHelper.executeSqlQuery(sql)
        val statList = ArrayList<TeamSummaryStat>()
        while (result.next()) {
            statList.add(result.produceSummaryStat())
        }
        result.close()
        return statList
    }

    override fun fetchGameSummaryStatsForVillan(summonerId: String, role: String, lane: String): ArrayList<TeamSummaryStat> {
       val sql = "SELECT\n" +
               "  ${tables.PARTICIPANT_IDENTITY}.SummonerId,\n" +
               "  ${tables.PARTICIPANT}.GameId,\n" +
               "  ${tables.PARTICIPANT}.championId,\n" +
               "  ${tables.PARTICIPANT}.teamId,\n" +
               "  ${tables.TEAM}.Win,\n" +
               "  ${tables.TEAM}.TowerKills,\n" +
               "  ${tables.TEAM}.DragonKills,\n" +
               "  ${tables.TEAM}.BaronKills,\n" +
               "  ${tables.TEAM}.RiftHeraldKills\n" +
               "FROM ${tables.PARTICIPANT_IDENTITY}\n" +
               "  LEFT JOIN ${tables.PARTICIPANT} ON\n" +
               "                           ${tables.PARTICIPANT_IDENTITY}.gameId = ${tables.PARTICIPANT}.GameId AND\n" +
               "                           ${tables.PARTICIPANT}.lane = ${tables.PARTICIPANT_IDENTITY}.lane AND\n" +
               "                            ${tables.PARTICIPANT}.role = ${tables.PARTICIPANT_IDENTITY}.role AND\n" +
               "                          ${tables.PARTICIPANT_IDENTITY}.teamId != ${tables.PARTICIPANT}.TeamId\n" +
               "  LEFT JOIN ${tables.TEAM} on ${tables.PARTICIPANT_IDENTITY}.TeamId != ${tables.TEAM}.TeamId and ${tables.PARTICIPANT_IDENTITY}.gameId = ${tables.TEAM}.GameId\n" +
               "  LEFT JOIN ${tables.MATCH_TABLE} on ${tables.PARTICIPANT}.GameId = ${tables.MATCH_TABLE}.GameId\n" +
               "WHERE ${tables.PARTICIPANT_IDENTITY}.SummonerId = '$summonerId'\n" +
               "      AND ${tables.PARTICIPANT_IDENTITY}.lane = '$lane'\n" +
               "      AND ${tables.PARTICIPANT_IDENTITY}.role = '$role'\n" +
               "      AND ${tables.MATCH_TABLE}.GameDuration > 300"
        val result = dbHelper.executeSqlQuery(sql)
        val statList = ArrayList<TeamSummaryStat>()
        while (result.next()) {
            statList.add(result.produceSummaryStat())
        }
        result.close()
        return statList
    }

    /**
     * Fetch an array of [FullGameStat] for a summoner in a specific role and lane.
     * @param heroSummonerId The hero summonerId
     * @param heroRole The hero role
     * @param heroLane The hero lane
     */
    override fun fetchPlayerStatisticsForHero(heroSummonerId: String,
                                              heroRole: String,
                                              heroLane: String) : ArrayList<FullGameStat> {
        val sql = "SELECT\n" +
                "  ${tables.PARTICIPANT_IDENTITY}.gameId,\n" +
                "  ${tables.STATS}.kills,\n" +
                "  ${tables.STATS}.deaths,\n" +
                "  ${tables.STATS}.assists,\n" +
                "  ${tables.STATS}.wardsPlaced,\n" +
                "  ${tables.STATS}.wardsKilled\n" +
                "FROM ${tables.PARTICIPANT_IDENTITY}\n" +
                "  JOIN ${tables.PARTICIPANT} ON ${tables.PARTICIPANT_IDENTITY}.gameId = ${tables.PARTICIPANT}.GameId\n" +
                "                      AND ${tables.PARTICIPANT}.ParticipantId = ${tables.PARTICIPANT_IDENTITY}.ParticipantId\n" +
                "  JOIN ${tables.STATS} ON ${tables.STATS}.ParticipantRowId = ${tables.PARTICIPANT}.Id\n" +
                "  LEFT JOIN ${tables.MATCH_TABLE} ON ${tables.PARTICIPANT}.GameId = ${tables.MATCH_TABLE}.GameId\n" +
                "WHERE ${tables.PARTICIPANT_IDENTITY}.lane = '$heroLane'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.role = '$heroRole'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.SummonerId = '$heroSummonerId'\n" +
                "      AND GameDuration > 300"
        val result = dbHelper.executeSqlQuery(sql)
        val statList = ArrayList<FullGameStat>()
        while (result.next()) {
            statList.add(result.produceFullGameStat())
        }
        result.close()
        return statList
    }

    /**
     * Fetch a list of player statistics for the enemy laner / opponent of the hero. Each item in the array represents one match.
     * @param heroSummonerId The hero summonerId
     * @param heroLane The lane of the hero
     * @param heroRole The role of the hero.
     */
    override fun fetchPlayerStatisticsForVillian(heroSummonerId: String, heroRole: String, heroLane: String): ArrayList<FullGameStat> {
        val sql = "SELECT\n" +
                "  ${tables.PARTICIPANT_IDENTITY}.gameId,\n" +
                "  ${tables.STATS}.kills,\n" +
                "  ${tables.STATS}.deaths,\n" +
                "  ${tables.STATS}.assists,\n" +
                "  ${tables.STATS}.wardsPlaced,\n" +
                "  ${tables.STATS}.wardsKilled\n" +
                "FROM ${tables.PARTICIPANT_IDENTITY}\n" +
                "  JOIN ${tables.PARTICIPANT} ON\n" +
                "                     ${tables.PARTICIPANT_IDENTITY}.gameId = ${tables.PARTICIPANT}.GameId AND\n" +
                "                     ${tables.PARTICIPANT}.lane = ${tables.PARTICIPANT_IDENTITY}.lane AND\n" +
                "                     ${tables.PARTICIPANT}.role = ${tables.PARTICIPANT_IDENTITY}.role AND\n" +
                "                     ${tables.PARTICIPANT_IDENTITY}.teamId != ${tables.PARTICIPANT}.TeamId\n" +
                "  JOIN ${tables.STATS} ON ${tables.STATS}.ParticipantRowId = ${tables.PARTICIPANT}.Id\n" +
                "  LEFT JOIN ${tables.MATCH_TABLE} ON ${tables.PARTICIPANT}.GameId = ${tables.MATCH_TABLE}.GameId\n" +
                "WHERE ${tables.PARTICIPANT_IDENTITY}.lane = '$heroLane'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.role = '$heroRole'\n" +
                "      AND ${tables.PARTICIPANT_IDENTITY}.SummonerId = '$heroSummonerId'\n" +
                "      AND GameDuration > 300"
        val result = dbHelper.executeSqlQuery(sql)
        val statList = ArrayList<FullGameStat>()
        while (result.next()) {
            statList.add(result.produceFullGameStat())
        }
        result.close()
        return statList
    }
}