package api.controllers

import database.match.MatchDaoContract
import model.GameStageStats
import model.MatchSummary
import model.TotalMatchStats
import util.GameStages
import util.TableNames

/**
 * @author Josiah Kendall
 */
class MatchController(private val matchDao : MatchDaoContract,
                      private val tableNames: TableNames) {
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
        return matchDao.loadTwentyIds(tableNames.getRefinedStatsTableName(role), startingPoint, summonerId)
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
     * @return              [ArrayList] of [Long]. Each long is a match id. The matches will be in order from most
     *                      recent first to least recent last.
     */
    fun loadTwentyMatchIds(role: Int, startingPoint : Int, summonerId : Long, heroChampId: Int) : ArrayList<Long> {
        return matchDao.loadTwentyIds(
                tableNames.getRefinedStatsTableName(role),
                startingPoint,
                summonerId,
                heroChampId)
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
        return matchDao.loadTwentyIds(
                tableNames.getRefinedStatsTableName(role),
                startingPoint,
                summonerId,
                heroChampId,
                villanChampId)
    }

    /**
     * Load the [MatchSummary] for a match. This is used to display the match cards in the match history tab of the app.
     *
     * @param matchId The match that we are wanting the details for.
     * @param summonerId The summoner who we are fetching the match details for.
     */
    fun loadMatchSummary(role: Int, matchId : Long, summonerId: Long) : MatchSummary {
        return matchDao.loadMatchSummary(tableNames.getRefinedStatsTableName(role), matchId, summonerId)
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
        return matchDao.loadTotalStatsForAMatch(
                tableNames.getRefinedStatsTableName(role),
                matchId,
                summonerId)
    }

    /**
     * Load the [GameStageStats] for a match.
     *
     * @param role          The role that we want to filter the results to
     * @param matchId       The id of the match that we are interested in
     * @param summonerId    The id of the summoner whom we are fetching the stats about
     * @param gameStage     The stage of the game that we want the stats for. See [GameStages]
     * @return A [GameStageStats] instance for a match.
     */
    fun loadGameStageStatsForAMatch(role : Int, gameStage : Int, matchId: Long, summonerId: Long) : GameStageStats {
        val gameStages = GameStages()

        when(gameStage) {
            gameStages.EARLY_GAME -> return matchDao.loadEarlyGameStageStatsForAMatch(
                    tableNames.getRefinedStatsTableName(role),
                    matchId,
                    summonerId)
            gameStages.MID_GAME -> return matchDao.loadMidGameStageStatsForAMatch(
                    tableNames.getRefinedStatsTableName(role),
                    matchId,
                    summonerId)
            gameStages.LATE_GAME -> return matchDao.loadMidGameStageStatsForAMatch(
                    tableNames.getRefinedStatsTableName(role),
                    matchId,
                    summonerId)
        }

        throw IllegalStateException("invalid game stage provided")
    }
}