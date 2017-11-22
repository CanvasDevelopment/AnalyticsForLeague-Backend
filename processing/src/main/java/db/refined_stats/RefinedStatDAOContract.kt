package db.refined_stats

import model.positions.Jungle
import model.refined_stats.*
import util.Tables
import java.util.*


/**
 * @author Josiah Kendall
 */
interface RefinedStatDAOContract {

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
            lane: String): ArrayList<GameStageStat>
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
                                         lane: String) : ArrayList<GameStageStat>

    /**
     * Fetch a list of [TeamSummaryStat] for a specific role.
     *
     * @param summonerId    The riot given SummonerId of the hero
     * @param role          The role that we want to filter to.
     * @param lane          The Lane that we want to filter to.
     */
    fun fetchGameSummaryStatsForHero(summonerId: Long, role: String, lane: String): ArrayList<TeamSummaryStat>

    fun fetchGameSummaryStatsForVillan(summonerId: Long, role: String, lane: String): ArrayList<TeamSummaryStat>

    /**
     * Fetch an array of [FullGameStat] for a summoner in a specific role and lane.
     * @param heroSummonerId The hero summonerId
     * @param heroRole The hero role
     * @param heroLane The hero lane
     */
    fun fetchPlayerStatisticsForHero(heroSummonerId: Long,
                                     heroRole: String,
                                     heroLane: String) : ArrayList<FullGameStat>

    /**
     * Fetch a list of player statistics for the enemy laner / opponent of the hero. Each item in the array represents one match.
     * @param heroSummonerId The hero summonerId
     * @param heroLane The lane of the hero
     * @param heroRole The role of the hero.
     */
    fun fetchPlayerStatisticsForVillian(heroSummonerId: Long, heroRole: String, heroLane: String): ArrayList<FullGameStat>

}