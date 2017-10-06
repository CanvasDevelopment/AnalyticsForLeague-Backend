package application.domain

import db.match.MatchDAO
import db.matchlist.MatchSummaryDAO
import db.refined_stats.GameSummaryDaoContract
import db.refined_stats.RefinedStatDAOContract
import db.summoner.SummonerDAOContract
import model.matchlist.MatchSummary
import model.refined_stats.RefinedGeneralGameStageColumnNames
import network.riotapi.MatchServiceApi
import util.*
import util.columnnames.StaticColumnNames
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 */
class MatchControl(private val matchDAO: MatchDAO,
                   private val matchServiceApi: MatchServiceApi,
                   private val matchSummaryDAO: MatchSummaryDAO,
                   private val summonerDAOContract: SummonerDAOContract,
                   private val refinedStatDAOContract: RefinedStatDAOContract,
                   private val gameSummaryDaoContract: GameSummaryDaoContract) {

    private val log = Logger.getLogger(this::class.java.name)

    val tables = Tables()

    /**
     * Pulls down the latest matches for a summoner, and stores them in the match database.
     * @param summonerId
     */
    fun downloadAndSaveMatchSummaries(summonerId : Long) {

        // if we don't get a summoner, return
        val summoner = summonerDAOContract.getSummoner(summonerId) ?: return
        val matchList = matchServiceApi.getMatchListForAccount(RIOT_API_KEY, summoner.accountId)

        // If we have not match summaries, no point doing anything here.
        matchList.matches ?: return

        // For each match summary
        matchList.matches.forEach { matchSummary ->
            if (!matchSummaryDAO.exists(matchSummary.gameId)) {
                val result = matchSummaryDAO.saveMatchSummary(matchSummary)
                log.info("Saving match summary with a match id of ${matchSummary.gameId}. Saved with id: $result")
            } else {
                log.info("Match summary with id : ${matchSummary.gameId} already exists. Skipping to next")
            }
        }
    }

    /**
     * Fetch and save all the matches from the riot api for a summoner. This does not fetch matches that we already have
     * fetched and saved previously.
     *
     * @param summonerId The summoner that we want to fetch the matches for.
     * @param numberOfMatchesToFetchForEachRole     The number of matches to fetch. For instance, fetching the last 10 matches, use 10.
     *                                              The last 20, use 20. To fetch all matches that we have a match summary for, use
     *                                              0 or a negative number.
     */
    fun fetchAndSaveMatchesForASummoner(summonerId: Long, numberOfMatchesToFetchForEachRole: Int) {
        // fetch
        if (numberOfMatchesToFetchForEachRole <= 0) {
            log.info("No limit of matches given. Fetching all match summaries.")
            val matchSummaries = matchSummaryDAO.getAllMatchesBySummonerId(summonerId)
            fetchAndSaveMatchesUsingGivenMatchSummaries(summonerId, matchSummaries)
        } else {
            log.info("Limit given of $numberOfMatchesToFetchForEachRole")
            // TODO finish this next

            val matchSummariesTop = matchSummaryDAO.getRecentMatchesBySummonerIdForRole(summonerId, numberOfMatchesToFetchForEachRole, SOLO, TOP)
            val matchSummariesJungle = matchSummaryDAO.getRecentMatchesBySummonerIdForRole(summonerId, numberOfMatchesToFetchForEachRole, NONE, JUNGLE)
            val matchSummariesMid = matchSummaryDAO.getRecentMatchesBySummonerIdForRole(summonerId, numberOfMatchesToFetchForEachRole, SOLO, MID)
            val matchSummariesADC = matchSummaryDAO.getRecentMatchesBySummonerIdForRole(summonerId, numberOfMatchesToFetchForEachRole, DUO_CARRY, BOT)
            val matchSummariesSupport = matchSummaryDAO.getRecentMatchesBySummonerIdForRole(summonerId, numberOfMatchesToFetchForEachRole, DUO_SUPPORT, BOT)
            val allMatchSummaries = ArrayList<MatchSummary>()

            // Add all match summaries together to make them fit
            allMatchSummaries.addAll(matchSummariesTop)
            allMatchSummaries.addAll(matchSummariesJungle)
            allMatchSummaries.addAll(matchSummariesMid)
            allMatchSummaries.addAll(matchSummariesADC)
            allMatchSummaries.addAll(matchSummariesSupport)

            fetchAndSaveMatchesUsingGivenMatchSummaries(summonerId,allMatchSummaries)
        }
    }

    fun fetchAndSaveMatchesUsingGivenMatchSummaries(summonerId: Long, matchSummaries : ArrayList<MatchSummary>) {
        matchSummaries.forEach { matchSummary ->
            val alreadySaved = gameSummaryDaoContract.doesGameSummaryForSummonerExist(matchSummary.gameId, summonerId)
            if (!alreadySaved) {
                val matchDetails = matchServiceApi.getMatchByMatchId(RIOT_API_KEY, matchSummary.gameId)
                matchDAO.saveMatch(matchDetails)
            }
        }
    }

    /**
     * Refine our raw match data into refined stats.
     *
     * @param summonerId    The summoner Id for whom we are refining the stats.
     * @param role          The role for which we are interested in.
     * @param lane          The lane for which we are interested in.
     */
    fun refineMatchData(summonerId: Long, role : String, lane : String) {
        val tableName = getTableName(lane, role)

        // summary stats
        val heroSummaryStats = refinedStatDAOContract.fetchGameSummaryStatsForHero(summonerId,role,lane)
        var saved = gameSummaryDaoContract.saveHeroTeamSummaryStats(summonerId, heroSummaryStats, tableName)
        if (!saved) {
            return
        }
        val villanSummaryStats = refinedStatDAOContract.fetchGameSummaryStatsForVillan(summonerId, role, lane)
        saved = gameSummaryDaoContract.saveVillanTeamSummaryStats(summonerId,villanSummaryStats, tableName)
        // todo figure out a way to handle failed saves.
        val heroCreeps = refinedStatDAOContract.fetchGameStageStatListForHero(
                tables.CREEPS_PER_MIN,
                summonerId,
                role,
                lane)

        // Save game stages
        saved = gameSummaryDaoContract.saveGameStageStatList(summonerId,heroCreeps, getColumns(tables.CREEPS_PER_MIN, true),tableName)

        val villanCreeps = refinedStatDAOContract.fetchGameStageStatListForVillian(
                tables.CREEPS_PER_MIN,
                summonerId,
                role,
                lane)

        saved = gameSummaryDaoContract.saveGameStageStatList(summonerId, villanCreeps, getColumns(tables.CREEPS_PER_MIN, false),tableName)
        val heroXp = refinedStatDAOContract.fetchGameStageStatListForHero(
                tables.XP_PER_MIN,
                summonerId,
                role,
                lane)

        saved = gameSummaryDaoContract.saveGameStageStatList(summonerId, heroXp, getColumns(tables.XP_PER_MIN, true),tableName)
        val villanXp = refinedStatDAOContract.fetchGameStageStatListForVillian(
                tables.XP_PER_MIN,
                summonerId,
                role,
                lane)
        saved = gameSummaryDaoContract.saveGameStageStatList(summonerId, villanXp, getColumns(tables.XP_PER_MIN, false),tableName)

        val heroGold = refinedStatDAOContract.fetchGameStageStatListForHero(
                tables.GOLD_PER_MIN,
                summonerId,
                role,
                lane)
        saved = gameSummaryDaoContract.saveGameStageStatList(summonerId, heroGold, getColumns(tables.GOLD_PER_MIN, true),tableName)

        val villanGold = refinedStatDAOContract.fetchGameStageStatListForVillian(
                tables.GOLD_PER_MIN,
                summonerId,
                role,
                lane)

        saved = gameSummaryDaoContract.saveGameStageStatList(summonerId, villanGold, getColumns(tables.GOLD_PER_MIN, false),tableName)
        val heroDamage = refinedStatDAOContract.fetchGameStageStatListForHero(
                tables.DAMAGE_TAKEN_PER_MIN,
                summonerId,
                role,
                lane)

        saved = gameSummaryDaoContract.saveGameStageStatList(summonerId, heroDamage, getColumns(tables.DAMAGE_TAKEN_PER_MIN, true),tableName)

        val villanDamage = refinedStatDAOContract.fetchGameStageStatListForVillian(
                tables.DAMAGE_TAKEN_PER_MIN,
                summonerId,
                role,
                lane)

        saved = gameSummaryDaoContract.saveGameStageStatList(summonerId, villanDamage, getColumns(tables.DAMAGE_TAKEN_PER_MIN, false),tableName)

        // save full game stats
        val heroStats = refinedStatDAOContract.fetchPlayerStatisticsForHero(summonerId,role,lane)
        gameSummaryDaoContract.savePlayerGameSummaryStatsListForHero(summonerId,heroStats,tableName)

        val villanStats = refinedStatDAOContract.fetchPlayerStatisticsForVillian(summonerId, role, lane)
        gameSummaryDaoContract.savePlayerGameSummaryStatsListForVillan(summonerId,villanStats,tableName)
    }

    /**
     * Generate a table based on our role and lane. This is the 'gamesummary' table that we are going to be saving our
     * refined stats to.
     *
     * @param lane The lane the stats are for.
     * @param role The role the stats are for.
     */
    fun getTableName(lane: String, role: String) : String {
        when(lane) {
            BOT -> {
                if (role == DUO_CARRY) {
                    return ADC
                }
                if (role == DUO_SUPPORT) {
                    return SUPPORT
                }
            }
            MID -> {
                if (role == SOLO) {
                    return MID
                }
            }
            TOP -> {
                if (role == SOLO) {
                    return TOP
                }
            }
            JUNGLE -> {
                if (role == NONE) {
                    return JUNGLE
                }
            }
        }
        // We check for this
        throw IllegalStateException("Incorrect role and lane combo supplied")
    }

    /**
     * Get the column names for our game summary table.
     *
     * @param tableName     The name of the table we want to get columns for.
     * @param hero          True if we are getting columns for the hero, false if for villan
     * @return              An instance of [RefinedGeneralGameStageColumnNames] that has the appropriate column names
     *                      for our game summary table
     */
    fun getColumns(tableName: String, hero : Boolean): RefinedGeneralGameStageColumnNames {

        val staticColumnNames = StaticColumnNames()

        when(tableName) {
            tables.CREEPS_PER_MIN -> {
                return if (hero)
                    RefinedGeneralGameStageColumnNames(
                            staticColumnNames.HERO_CREEPS_EARLY_GAME,
                            staticColumnNames.HERO_CREEPS_MID_GAME,
                            staticColumnNames.HERO_CREEPS_LATE_GAME)
                else
                    RefinedGeneralGameStageColumnNames(
                            staticColumnNames.VILLAN_CREEPS_EARLY_GAME,
                            staticColumnNames.VILLAN_CREEPS_MID_GAME,
                            staticColumnNames.VILLAN_CREEPS_LATE_GAME)
            }
            tables.XP_PER_MIN -> {
                return if (hero)
                    RefinedGeneralGameStageColumnNames(
                            staticColumnNames.HERO_XP_EARLY_GAME,
                            staticColumnNames.HERO_XP_MID_GAME,
                            staticColumnNames.HERO_XP_LATE_GAME)
                else
                    RefinedGeneralGameStageColumnNames(
                            staticColumnNames.VILLAN_XP_EARLY_GAME,
                            staticColumnNames.VILLAN_XP_MID_GAME,
                            staticColumnNames.VILLAN_XP_LATE_GAME)
            }
            tables.GOLD_PER_MIN -> {
                return if (hero)
                    RefinedGeneralGameStageColumnNames(
                            staticColumnNames.HERO_GOLD_EARLY_GAME,
                            staticColumnNames.HERO_GOLD_MID_GAME,
                            staticColumnNames.HERO_GOLD_LATE_GAME)
                else
                    RefinedGeneralGameStageColumnNames(
                            staticColumnNames.VILLAN_GOLD_EARLY_GAME,
                            staticColumnNames.VILLAN_GOLD_MID_GAME,
                            staticColumnNames.VILLAN_GOLD_LATE_GAME)
            }
            tables.DAMAGE_TAKEN_PER_MIN -> {
                return if (hero)
                    RefinedGeneralGameStageColumnNames(
                            staticColumnNames.HERO_DAMAGE_EARLY_GAME,
                            staticColumnNames.HERO_DAMAGE_MID_GAME,
                            staticColumnNames.HERO_DAMAGE_LATE_GAME)
                else
                    RefinedGeneralGameStageColumnNames(
                            staticColumnNames.VILLAN_DAMAGE_EARLY_GAME,
                            staticColumnNames.VILLAN_DAMAGE_MID_GAME,
                            staticColumnNames.VILLAN_DAMAGE_LATE_GAME)
            }
        }
        throw IllegalStateException("Get columns used with invalid table")
    }

    fun clearRawDatabasesOfSummoner(summonerId: Long) {
        matchDAO.deleteAllMatchesFromRawDBForASummoner(summonerId)
    }


}