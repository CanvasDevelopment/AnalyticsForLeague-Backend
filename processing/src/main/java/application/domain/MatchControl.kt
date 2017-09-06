package application.domain

import db.match.MatchDAOContracts
import db.refined_stats.GameSummaryDaoContract
import db.refined_stats.RefinedStatDAOContract
import db.summoner.SummonerDAOContract
import model.refined_stats.RefinedGeneralGameStageColumnNames
import network.riotapi.MatchServiceApi
import util.RIOT_API_KEY
import util.Tables
import util.columnnames.StaticColumnNames

/**
 * @author Josiah Kendall
 */
class MatchControl(private val matchDAO: MatchDAOContracts.MatchDAOContract,
                   private val matchServiceApi: MatchServiceApi,
                   private val summonerDAOContract: SummonerDAOContract,
                   private val refinedStatDAOContract: RefinedStatDAOContract,
                   private val gameSummaryDaoContract: GameSummaryDaoContract) {

    val tables = Tables()


    /**
     * Sync pulls down the latest matches for a summoner, and stores them in the match database.
     * @param summonerId
     */
    fun sync(summonerId : Long) {
        // if we don't get a summoner, return
        val summoner = summonerDAOContract.getSummoner(summonerId) ?: return
        val matchList = matchServiceApi.getMatchListForAccount(RIOT_API_KEY, summoner.accountId)

        // If we have not match summaries, no point doing anything here.
        matchList.matches ?: return

        // For each match summary
        matchList.matches.forEach { matchSummary ->
            val alreadySaved = gameSummaryDaoContract.doesGameSummaryForSummonerExist(matchSummary.gameId, summonerId)
            if (!alreadySaved) {
                val matchDetails = matchServiceApi.getMatchByMatchId(RIOT_API_KEY, matchSummary.gameId)
                matchDAO.saveMatch(matchDetails)
            }
        }
    }

    /**
     * Refine our raw match data into refined stats.
     */
    fun refineMatchData(summonerId: Long, role : String, lane : String) {
        val heroSummaryStats = refinedStatDAOContract.fetchGameSummaryStatsForHero(summonerId,role,lane)
        var saved = gameSummaryDaoContract.saveHeroSummaryStats(summonerId, heroSummaryStats)
        if (!saved) {
            return
        }
        val villanSummaryStats = refinedStatDAOContract.fetchGameSummaryStatsForVillan(summonerId, role, lane)
        saved = gameSummaryDaoContract.saveVillanSummaryStats(summonerId,villanSummaryStats)
        // todo figure out a way to handle failed saves.
        val heroCreeps = refinedStatDAOContract.fetchGameStageStatListForHero(
                tables.CREEPS_PER_MIN,
                summonerId,
                role,
                lane)

        saved = gameSummaryDaoContract.saveGameStageStatList(summonerId,heroCreeps, getColumns(tables.CREEPS_PER_MIN, true))

        val villanCreeps = refinedStatDAOContract.fetchGameStageStatListForVillian(
                tables.CREEPS_PER_MIN,
                summonerId,
                role,
                lane)

        val heroXp = refinedStatDAOContract.fetchGameStageStatListForHero(
                tables.XP_PER_MIN,
                summonerId,
                role,
                lane)

        val villanXp = refinedStatDAOContract.fetchGameStageStatListForVillian(
                tables.XP_PER_MIN,
                summonerId,
                role,
                lane)

        val heroGold = refinedStatDAOContract.fetchGameStageStatListForHero(
                tables.GOLD_PER_MIN,
                summonerId,
                role,
                lane)
        val villanGold = refinedStatDAOContract.fetchGameStageStatListForVillian(
                tables.GOLD_PER_MIN,
                summonerId,
                role,
                lane)

        val heroDamage = refinedStatDAOContract.fetchGameStageStatListForHero(
                tables.DAMAGE_TAKEN_PER_MIN,
                summonerId,
                role,
                lane)
        val villanDamage = refinedStatDAOContract.fetchGameStageStatListForVillian(
                tables.DAMAGE_TAKEN_PER_MIN,
                summonerId,
                role,
                lane)



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


}