package api.match

import api.stat.analysis.model.HeadToHeadStat
import database.match.MatchDao
import database.match.model.MatchIdentifier
import database.tables.StatColumns
import database.tables.StatColumns.HERO_ASSISTS
import database.tables.StatColumns.HERO_CHAMP_ID
import database.tables.StatColumns.HERO_DEATHS
import database.tables.StatColumns.HERO_EARLY_GAME_CREEPS
import database.tables.StatColumns.HERO_EARLY_GAME_DAMAGE
import database.tables.StatColumns.HERO_EARLY_GAME_GOLD
import database.tables.StatColumns.HERO_EARLY_GAME_XP
import database.tables.StatColumns.HERO_KILLS
import database.tables.StatColumns.HERO_LATE_GAME_CREEPS
import database.tables.StatColumns.HERO_LATE_GAME_DAMAGE
import database.tables.StatColumns.HERO_LATE_GAME_GOLD
import database.tables.StatColumns.HERO_LATE_GAME_XP
import database.tables.StatColumns.HERO_MID_GAME_CREEPS
import database.tables.StatColumns.HERO_MID_GAME_DAMAGE
import database.tables.StatColumns.HERO_MID_GAME_GOLD
import database.tables.StatColumns.HERO_MID_GAME_XP
import database.tables.StatColumns.HERO_RESULT
import database.tables.StatColumns.VILLAN_ASSISTS
import database.tables.StatColumns.VILLAN_CHAMP_ID
import database.tables.StatColumns.VILLAN_DEATHS
import database.tables.StatColumns.VILLAN_EARLY_GAME_CREEPS
import database.tables.StatColumns.VILLAN_EARLY_GAME_DAMAGE
import database.tables.StatColumns.VILLAN_EARLY_GAME_GOLD
import database.tables.StatColumns.VILLAN_EARLY_GAME_XP
import database.tables.StatColumns.VILLAN_KILLS
import database.tables.StatColumns.VILLAN_LATE_GAME_CREEPS
import database.tables.StatColumns.VILLAN_LATE_GAME_DAMAGE
import database.tables.StatColumns.VILLAN_LATE_GAME_GOLD
import database.tables.StatColumns.VILLAN_LATE_GAME_XP
import database.tables.StatColumns.VILLAN_MID_GAME_CREEPS
import database.tables.StatColumns.VILLAN_MID_GAME_DAMAGE
import database.tables.StatColumns.VILLAN_MID_GAME_GOLD
import database.tables.StatColumns.VILLAN_MID_GAME_XP
import extensions.produceHeadToHeadStat
import model.GameStageStats
import model.MatchPerformanceDetails
import model.MatchSummary
import util.*
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 */
class MatchController(private val matchDao : MatchDao,
                      private val tableNames: TableNames,
                      private val rolesAndLanes: RolesAndLanes) {
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
    fun loadTwentyMatchIds(role: Int, startingPoint: Int, summonerId: String) : ArrayList<MatchIdentifier> {
        return matchDao.loadTwentyIds(
                startingPoint,
                summonerId,
                rolesAndLanes.getLane(role),
                rolesAndLanes.getRole(role))
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
     * @return              [ArrayList] of [Long]. Each long is a match id. The matches will be in order from most
     *                      recent first to least recent last.
     */
    fun loadTwentyMatchIds(startingPoint: Int, summonerId: String) : ArrayList<MatchIdentifier> {
        return matchDao.loadTwentyIds(
                startingPoint,
                summonerId)
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
    fun loadTwentyMatchIds(role: Int, startingPoint: Int, summonerId: String, heroChampId: Int) : ArrayList<MatchIdentifier> {
        return matchDao.loadTwentyIds(
                startingPoint,
                summonerId,
                heroChampId,
                rolesAndLanes.getLane(role),
                rolesAndLanes.getRole(role))
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
    fun loadTwentyMatchIds(role : Int, startingPoint : Int, summonerId : String, heroChampId : Int, villanChampId : Int) : ArrayList<MatchIdentifier> {
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
    fun loadMatchSummary(role : Int, matchId : Long, summonerId : String) : MatchSummary {
        // load performance profiles todo replace this with proper stuff
        val earlyGamePerformanceProfile = matchDao.produceMockPerformanceHashMap(Constant.GameStage.EARLY_GAME)
        val midGamePerformanceProfile = matchDao.produceMockPerformanceHashMap(Constant.GameStage.MID_GAME)
        val lateGamePerformanceProfile = matchDao.produceMockPerformanceHashMap(Constant.GameStage.LATE_GAME)
        // create column lists to send to dao. This allows us to fetch the stats we need from the db

        val earlyGameColumns = ArrayList<String>()
        val midGameColumns = ArrayList<String>()
        val lateGameColumns = ArrayList<String>()

        earlyGamePerformanceProfile.keys.toTypedArray().toCollection(earlyGameColumns)
        midGamePerformanceProfile.keys.toTypedArray().toCollection(midGameColumns)
        lateGamePerformanceProfile.keys.toTypedArray().toCollection(lateGameColumns)

        // fetch the stats
        val tableName = tableNames.getRefinedStatsTableName(role)

        // fetch the performance
        val resultSetEarlyGame = matchDao.fetchStatsForHeroAndVillan(summonerId,matchId,earlyGameColumns,tableName)
        val resultSetMidGame = matchDao.fetchStatsForHeroAndVillan(summonerId,matchId,midGameColumns,tableName)
        val resultSetLateGame = matchDao.fetchStatsForHeroAndVillan(summonerId,matchId,midGameColumns,tableName)

         val earlyGameResult : HeadToHeadStat = resultSetEarlyGame.produceHeadToHeadStat(earlyGamePerformanceProfile)
         val midGameResult : HeadToHeadStat = resultSetMidGame.produceHeadToHeadStat(midGamePerformanceProfile)
         val lateGameResult : HeadToHeadStat = resultSetLateGame.produceHeadToHeadStat(lateGamePerformanceProfile)

        // fetch game result (win) and champ ids
        val resultSet = matchDao.fetchWinAndChampIds(matchId, summonerId, tableNames.getRefinedStatsTableName(role))
        if (!resultSet.first()) {
            throw IllegalStateException("Failed to find any match with the given criteria")
        }

        val detailsUrl = "$role/$matchId/$summonerId"
        return MatchSummary(
                matchId,
                resultSet.getInt(HERO_CHAMP_ID),
                resultSet.getInt(VILLAN_CHAMP_ID),
                earlyGameResult,
                midGameResult,
                lateGameResult,
                resultSet.getBoolean(HERO_RESULT),
                detailsUrl,
                summonerId
             )
    }

    fun testResultSet(result : ResultSet) : Boolean{
        return result.first()
    }

    /**
     * Load the [TotalMatchStats] for a match. These are the complete stats for the full match, that are not time
     * dependent like the other delta stats.
     *
     * @param role          The role that the summoner played in the match that we want.
     * @param matchId       The id of the match that we are interested in.
     * @param summonerId    The id of the summoner who we want the stats for
     */
    fun loadMatchPerformanceSummary(role: Int, matchId : Long, summonerId : String) : MatchPerformanceDetails {
        val tableName = tableNames.getRefinedStatsTableName(role)
        val columnNames = ArrayList<String>()
        columnNames.add(StatColumns.HERO_KILLS)
        columnNames.add(StatColumns.HERO_ASSISTS)
        columnNames.add(StatColumns.HERO_DEATHS)
        columnNames.add(StatColumns.HERO_EARLY_GAME_CREEPS)
        columnNames.add(StatColumns.HERO_EARLY_GAME_DAMAGE)
        columnNames.add(StatColumns.HERO_EARLY_GAME_GOLD)
        columnNames.add(StatColumns.HERO_EARLY_GAME_XP)
        columnNames.add(StatColumns.HERO_MID_GAME_CREEPS)
        columnNames.add(StatColumns.HERO_MID_GAME_DAMAGE)
        columnNames.add(StatColumns.HERO_MID_GAME_GOLD)
        columnNames.add(StatColumns.HERO_MID_GAME_XP)
        columnNames.add(StatColumns.HERO_LATE_GAME_CREEPS)
        columnNames.add(StatColumns.HERO_LATE_GAME_DAMAGE)
        columnNames.add(StatColumns.HERO_LATE_GAME_GOLD)
        columnNames.add(StatColumns.HERO_LATE_GAME_XP)
        columnNames.add(StatColumns.VILLAN_EARLY_GAME_CREEPS)
        columnNames.add(StatColumns.VILLAN_EARLY_GAME_DAMAGE)
        columnNames.add(StatColumns.VILLAN_EARLY_GAME_GOLD)
        columnNames.add(StatColumns.VILLAN_EARLY_GAME_XP)
        columnNames.add(StatColumns.VILLAN_MID_GAME_CREEPS)
        columnNames.add(StatColumns.VILLAN_MID_GAME_DAMAGE)
        columnNames.add(StatColumns.VILLAN_MID_GAME_GOLD)
        columnNames.add(StatColumns.VILLAN_MID_GAME_XP)
        columnNames.add(StatColumns.VILLAN_LATE_GAME_CREEPS)
        columnNames.add(StatColumns.VILLAN_LATE_GAME_DAMAGE)
        columnNames.add(StatColumns.VILLAN_LATE_GAME_GOLD)
        columnNames.add(StatColumns.VILLAN_LATE_GAME_XP)
        val resultSet = matchDao.fetchMatchDetails(summonerId,matchId,columnNames,tableName)
        return produceMatchPerformanceDetails(resultSet)
        // produce a match val from the result set
    }

    fun produceMatchPerformanceDetails(result: ResultSet) : MatchPerformanceDetails {
        if (!result.first()) {
            throw IllegalStateException("Failed to find a match matching the given criteria")
        }

        val kda = ArrayList<HeadToHeadStat>()
        kda.add(HeadToHeadStat(
                result.getInt(VILLAN_KILLS).toFloat(),
                result.getInt(HERO_KILLS).toFloat()))
        kda.add(HeadToHeadStat(
                result.getInt(VILLAN_DEATHS).toFloat(),
                result.getInt(HERO_DEATHS).toFloat()))
        kda.add(HeadToHeadStat(
                result.getInt(VILLAN_ASSISTS).toFloat(),
                result.getInt(HERO_ASSISTS).toFloat()))

        val creeps = ArrayList<HeadToHeadStat>()
        creeps.add(HeadToHeadStat(
                result.getFloat(VILLAN_EARLY_GAME_CREEPS),
                result.getFloat(HERO_EARLY_GAME_CREEPS)
        ))
        creeps.add(HeadToHeadStat(
                result.getFloat(VILLAN_MID_GAME_CREEPS),
                result.getFloat(HERO_MID_GAME_CREEPS)
        ))
        creeps.add(HeadToHeadStat(
                result.getFloat(VILLAN_LATE_GAME_CREEPS),
                result.getFloat(HERO_LATE_GAME_CREEPS)
        ))

        val damageDealt = ArrayList<HeadToHeadStat>()
        damageDealt.add(HeadToHeadStat(
                result.getFloat(VILLAN_EARLY_GAME_DAMAGE),
                result.getFloat(HERO_EARLY_GAME_DAMAGE)
        ))
        damageDealt.add(HeadToHeadStat(
                result.getFloat(VILLAN_MID_GAME_DAMAGE),
                result.getFloat(HERO_MID_GAME_DAMAGE)
        ))
        damageDealt.add(HeadToHeadStat(
                result.getFloat(VILLAN_LATE_GAME_DAMAGE),
                result.getFloat(HERO_LATE_GAME_DAMAGE)
        ))
        val damageTaken = ArrayList<HeadToHeadStat>() // this one is empty, we dont use this one yet?
        val gold = ArrayList<HeadToHeadStat>()
        gold.add(HeadToHeadStat(
                result.getFloat(VILLAN_EARLY_GAME_GOLD),
                result.getFloat(HERO_EARLY_GAME_GOLD)
        ))
        gold.add(HeadToHeadStat(
                result.getFloat(VILLAN_MID_GAME_GOLD),
                result.getFloat(HERO_MID_GAME_GOLD)
        ))
        gold.add(HeadToHeadStat(
                result.getFloat(VILLAN_LATE_GAME_GOLD),
                result.getFloat(HERO_LATE_GAME_GOLD)
        ))
        val xp = ArrayList<HeadToHeadStat>()
        xp.add(HeadToHeadStat(
                result.getFloat(VILLAN_EARLY_GAME_XP),
                result.getFloat(HERO_EARLY_GAME_XP)
        ))
        xp.add(HeadToHeadStat(
                result.getFloat(VILLAN_MID_GAME_XP),
                result.getFloat(HERO_MID_GAME_XP)
        ))
        xp.add(HeadToHeadStat(
                result.getFloat(VILLAN_LATE_GAME_XP),
                result.getFloat(HERO_LATE_GAME_XP)
        ))
        return MatchPerformanceDetails(
                kda,
                creeps,
                damageDealt,
                damageTaken,
                gold,
                xp)
    }
//    fun produceColumnNames(role: Int) : ArrayList<String>() {}
    /**
     * Load the [GameStageStats] for a match.
     *
     * @param role          The role that we want to filter the results to
     * @param matchId       The id of the match that we are interested in
     * @param summonerId    The id of the summoner whom we are fetching the stats about
     * @param gameStage     The stage of the game that we want the stats for. See [GameStages]
     * @return A [GameStageStats] instance for a match.
     */
    fun loadGameStageStatsForAMatch(role : Int, gameStage : Int, matchId: Long, summonerId : String) : GameStageStats {
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