package api

import database.DbHelper
import model.GameStageStats
import model.MatchSummary
import model.TotalMatchStats
import util.GameStage

/**
 * @author Josiah Kendall
 */

class MatchApi(val dbHelper: DbHelper) {

    /**
     * Load an array of twenty match ids. The matches will be in order from most recent to least recent.
     *
     * @param role          Filter the results to a specific role.
     * @param startingPoint This parameter denotes where in the list of matches stored for a user we need to start
     *                      when grabbing our next twenty match ids. For instance, if we want to the get the most recent
     *                      match ids, we would set the starting point to 0. If we had already retrieved the most recent
     *                      20 matches and wanted to retrieve another 20, then we would use 20 as the starting point.
     * @param summonerId    This is the summoner for whom we are retrieving the matches for.
     * @return              [ArrayList] of [Long]. Each long is a match id. The matches will be in order from most
     *                      recent first to least recent last.
     */
    fun loadTwentyMatchIds(role : Int, startingPoint : Int, summonerId : Long) : ArrayList<Long> {
        val ids = ArrayList<Long>()
        TODO()
        return ids

    }

    /**
     * Load an array of twenty match ids. The matches will be in order from most recent to least recent.
     *
     * @param role          Filter the results to a specific role.
     * @param startingPoint This parameter denotes where in the list of matches stored for a user we need to start
     *                      when grabbing our next twenty match ids. For instance, if we want to the get the most recent
     *                      match ids, we would set the starting point to 0. If we had already retrieved the most recent
     *                      20 matches and wanted to retrieve another 20, then we would use 20 as the starting point.
     * @param summonerId    This is the summoner for whom we are retrieving the matches for.
     * @param heroChampId       The champ for whom we want to filter the results to. When set this will limit the results
     *                      to only matches where we played the champ that is specified.
     * @return              [ArrayList] of [Long]. Each long is a match id. The matches will be in order from most
     *                      recent first to least recent last.
     */
    fun loadTwentyMatchIds(role: Int, startingPoint : Int, summonerId : Long, heroChampId: Int) : ArrayList<Long> {
        val ids = ArrayList<Long>()
        TODO()
        return ids
    }

    /**
     * Load an array of twenty match ids. The matches will be in order from most recent to least recent.
     *
     * @param role          Filter the results to a specific role.
     * @param startingPoint This parameter denotes where in the list of matches stored for a user we need to start
     *                      when grabbing our next twenty match ids. For instance, if we want to the get the most recent
     *                      match ids, we would set the starting point to 0. If we had already retrieved the most recent
     *                      20 matches and wanted to retrieve another 20, then we would use 20 as the starting point.
     * @param summonerId    This is the summoner for whom we are retrieving the matches for.
     * @param heroChampId   The champ for whom we want to filter the results to. When set this will limit the results
     *                      to only matches where we played the champ that is specified.
     * @param villanChampId Filter the results to only contain matches where the enemy played this specific champion
     * @return              [ArrayList] of [Long]. Each long is a match id. The matches will be in order from most
     *                      recent first to least recent last.
     */
    fun loadTwentyMatchIds(role : Int, startingPoint : Int, summonerId : Long, heroChampId : Int, villanChampId : Int) : ArrayList<Long> {
        val ids = ArrayList<Long>()
        TODO()
        return ids
    }

    /**
     * Load the [MatchSummary] for a match. This is used to display the match cards in the match history tab of the app.
     *
     * @param matchId The match that we are wanting the details for.
     * @param summonerId The summoner who we are fetching the match details for.
     */
    fun loadMatchSummary(matchId : Long, summonerId: Long) : MatchSummary {
        TODO()
    }

    /**
     * Load the [TotalMatchStats] for a match. These are the complete stats for the full match, that are not time
     * dependent like the other delta stats.
     *
     * @param role          The role that the summoner played in the match that we want.
     * @param matchId       The id of the match that we are interested in.
     * @param summonerId    The id of the summoner who we want the stats for
     */
    fun loadTotalStatsForAMatch(role: Int, matchId : Long, summonerId:Long) : TotalMatchStats {
        TODO()
    }

    /**
     * Load the [GameStageStats] for a match.
     *
     * @param role          The role that we want to filter the results to
     * @param matchId       The id of the match that we are interested in
     * @param summonerId    The id of the summoner whom we are fetching the stats about
     * @param gameStage     The stage of the game that we want the stats for. See [GameStage]
     * @return A [GameStageStats] instance for a match.
     */
    fun loadGameStageStatsForAMatch(role : Int, gameStage : Int, matchId: Long, summonerId: Long) : GameStageStats {
        TODO()
    }
}
