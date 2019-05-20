package db.refined_stats

import db.requests.DBHelper
import model.refined_stats.FullGameStat
import model.refined_stats.GameStageStat
import model.refined_stats.TeamSummaryStat
import model.refined_stats.RefinedGeneralGameStageColumnNames
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 *
 * This is the game summary data access object, used to save the stats that we 'refine' using the refinement dao.
 * We save all these stats into a table named gamesummarystats. This is done for efficiency - with 10 participants for
 * each match, 10 participantIdentities, tables can grow very quickly as each user will probably have over 100 games,
 * some over 1000. We have to join 5 tables just to get the number of creeps - and with 20000 users some of these tables
 * will probably be nearing 20 millon rows, meaning queries cannot be done in real time. Storing the stats we need for
 * early release in a summary table will make things easy to query in real time.
 *
 * Eventually it will be neccessary to move to a multi table query structure, as client side requests come for more
 * customizable and complex data. By the time this comes, we should have a better understanding of how data will fit
 * together, and what data we want.
 */
class GameSummaryDAO(val dbHelper: DBHelper) : GameSummaryDaoContract {

    private val log = Logger.getLogger(this::class.java.toString())

    override fun doesGameSummaryForSummonerExist(gameId: Long, summonerId: String): Boolean {
        val top = "Select * from Top_SummaryStats where gameId = $gameId AND heroSummonerId = '$summonerId'"
        val mid = "Select * from Mid_SummaryStats where gameId = $gameId AND heroSummonerId = '$summonerId'"
        val adc = "Select * from ADC_SummaryStats where gameId = $gameId AND heroSummonerId = '$summonerId'"
        val sup = "Select * from Support_SummaryStats where gameId = $gameId AND heroSummonerId = '$summonerId'"
        val jg = "Select * from Jungle_SummaryStats where gameId = $gameId AND heroSummonerId = '$summonerId'"

        val topResult = dbHelper.executeSqlQuery(top)
        if (topResult.next()) {
            return true
        }
        val midResult = dbHelper.executeSqlQuery(mid)
        if (midResult.next()) {
            return true
        }
        val adcResult = dbHelper.executeSqlQuery(adc)
        if (adcResult.next()) {
            return true
        }
        val supResult = dbHelper.executeSqlQuery(sup)
        if (supResult.next()) {
            return true
        }
        val jgResult = dbHelper.executeSqlQuery(jg)
        if (jgResult.next()) {
            return true
        }

        return false
    }

    /**
     * Save an Array of [TeamSummaryStat] match objects for our hero. Each object will represent one game.
     * @param summonerId        The hero summonerId. Currently unused, unsure if it will be in the future.
     * @param summaryStatStats      The array list of [TeamSummaryStat] objects to save.
     */
    override fun saveHeroTeamSummaryStats(summonerId: String, summaryStatStats: ArrayList<TeamSummaryStat>, tableName: String): Boolean {
        // for every item in the list, we want to create a new list item.
        var success = true
        for(summarystat in summaryStatStats) {
            val result = insertHeroTeamSummaryStat(summarystat, tableName)
            success = result != (-1).toLong()  && success
        }

        return success
    }

    /**
     * Insert a single summary stat into the gameSummaryStats table.
     * @param summaryStatStat The [TeamSummaryStat] to save
     */
    override fun insertHeroTeamSummaryStat(summaryStatStat: TeamSummaryStat, tableName: String): Long {
        val sql = "INSERT INTO ${tableName}_SummaryStats (" +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills) VALUES (" +
                "${summaryStatStat.gameId}," +
                "'${summaryStatStat.summonerId}'," +
                "${summaryStatStat.champId}," +
                "${summaryStatStat.teamId}," +
                "${summaryStatStat.win}," +
                "${summaryStatStat.teamTowerKills}," +
                "${summaryStatStat.teamDragonKills}," +
                "${summaryStatStat.teamRiftHeraldKills}," +
                "${summaryStatStat.teamBaronKills})"
        log.log(Level.SEVERE,"SAVING HERO TEAM SUMMARY STAT")
        return dbHelper.executeSQLScript(sql)
    }

    /**
     * Save the [TeamSummaryStat] list for a villan
     * @param summonerId The hero summoner Id
     * @param summaryStatStats The list of [TeamSummaryStat] to save
     * @param tableName The table to save these stats in.
     */
    override fun saveVillanTeamSummaryStats(summonerId: String, summaryStatStats: ArrayList<TeamSummaryStat>, tableName: String): Boolean {
        var success = true
        summaryStatStats.map { saveVillanTeamSummaryStat(summonerId,it, tableName) }
                .forEach {
                    success = it != (-1).toLong() && success
                }
        return success
    }

    /**
     * Insert a single summary stat into the gameSummaryStats table.
     * @param heroSummonerId    The summoner Id of our hero
     * @param summaryStatStat       The [TeamSummaryStat] to save
     */
    override fun saveVillanTeamSummaryStat(heroSummonerId: String, summaryStatStat: TeamSummaryStat, tableName: String): Long {
        val sql = "Update ${tableName}_SummaryStats " +
                "SET villanChampId = ${summaryStatStat.champId},\n" +
                "villanTeamId = ${summaryStatStat.teamId},\n" +
                "villanWin = ${summaryStatStat.win},\n" +
                "villanTeamTowerKills = ${summaryStatStat.teamTowerKills},\n" +
                "villanTeamDragonKills = ${summaryStatStat.teamDragonKills},\n" +
                "villanTeamRiftHeraldKills = ${summaryStatStat.teamRiftHeraldKills},\n" +
                "villanTeamBaronKills = ${summaryStatStat.teamBaronKills} " +
                "where gameId = ${summaryStatStat.gameId} and heroSummonerId = '$heroSummonerId'"
        log.log(Level.SEVERE,"SAVING VILLAN TEAM SUMMARY STAT")
        return dbHelper.executeSQLScript(sql)
    }

    /**
     * Save an array of [GameStageStat] objects. See [saveGameStageStat] for more details on saving
     * @param summonerId The hero summoner id
     * @param statList The list of [GameStageStat] objects to save
     * @param generalGameStageColumnNames The column names. Used to save our [GameStageStat]'s into the correct columns
     * @param tableName The role table we are going to save these stats into
     */
    override fun saveGameStageStatList(summonerId: String,
                                       statList: ArrayList<GameStageStat>,
                                       generalGameStageColumnNames: RefinedGeneralGameStageColumnNames,
                                       tableName: String): Boolean {
        var success = true
        statList
                .map { saveGameStageStat(summonerId, it, generalGameStageColumnNames, tableName) }
                .forEach { success = it != (-1).toLong() && success }
        return success
    }

    /**
     * Save a [GameStageStat]. This can be used for any game stage, just define your column names in the
     * [RefinedGeneralGameStageColumnNames] object.
     *  examples of a game stage:
     *      - "villanXpEarlyGame"
     *      - "heroCreepsMidGame"
     * @param summonerId                    The hero summonerId. This is used to determine which row we are updating, as we may
     *                                      have multiple rows with the same gameId (different summoners, same game)
     * @param stat                          The [GameStageStat] which holds the game stage details
     * @param columnNames                   The column names to save our stats against for the early, mid and late game.
     *                                      See [RefinedGameStageColumnNames]
     */
    override fun saveGameStageStat(summonerId: String,
                                   stat: GameStageStat,
                                   columnNames: RefinedGeneralGameStageColumnNames,
                                   tableName: String): Long {
        val sql = "UPDATE ${tableName}_SummaryStats\n" +
                "SET ${columnNames.earlyGame} = ${stat.earlyGame},\n" +
                " ${columnNames.midGame} = ${stat.midGame},\n" +
                " ${columnNames.lateGame} = ${stat.lateGame}\n" +
                "where gameId = ${stat.gameId} and heroSummonerId = '$summonerId'"

        return dbHelper.executeSQLScript(sql)
    }

    /**
     * Save an array of [FullGameStat]. Each [FullGameStat] will represent a game for the hero.
     * @param summonerId    The hero summonerId
     * @param stats         The full game stats for the hero that we want to save.
     */
    override fun savePlayerGameSummaryStatsListForHero(summonerId: String,
                                                       stats: ArrayList<FullGameStat>, tableName: String) {
        for (stat in stats) {
            savePlayerGameSummaryStatsItemForHero(summonerId, stat, tableName)
        }
    }

    /**
     * Save the team stats item : [FullGameStat] for our hero.
     * @param heroSummonerId    The hero summoner id
     * @param stat              The [FullGameStat] object to save.
     * @return                  The column row id. -1 if update fails.
     */
    override fun savePlayerGameSummaryStatsItemForHero(heroSummonerId: String,
                                                       stat: FullGameStat, tableName: String): Long {
        val sql = "UPDATE ${tableName}_SummaryStats\n" +
                "SET heroKills = ${stat.kills},\n" +
                "  heroDeaths = ${stat.deaths},\n" +
                "  heroAssists = ${stat.assists},\n" +
                "  heroWardsPlaced = ${stat.wardsPlaced},\n" +
                "  heroWardsKilled = ${stat.wardsKilled}\n" +
                "where ${tableName}_SummaryStats.gameId = ${stat.gameId} and ${tableName}_SummaryStats.heroSummonerId = '$heroSummonerId'"

        return dbHelper.executeSQLScript(sql)
    }

    /**
     * Save a list of [FullGameStat] for the villan
     * @param summonerId The hero summoner id
     * @param stats Our list of [FullGameStat] items to save. Each item represents once game.
     * @param tableName The name of the table to save these stats into
     * @return The row of the updated id. -1 if not successful.
     */
    override fun savePlayerGameSummaryStatsListForVillan(summonerId: String, stats: ArrayList<FullGameStat>, tableName: String) {
        for (stat in stats) {
            savePlayerGameSummaryStatsItemForVillan(summonerId, stat, tableName)
        }
    }

    /**
     * Save the team stats item : [FullGameStat] for our villan.
     * @param heroSummonerId    The heros summoner Id
     * @param stat              The full game stat to save for our villan.
     * @return                  The row id of the updated row. Returns -1 if not successfull.
     */
    override fun savePlayerGameSummaryStatsItemForVillan(heroSummonerId: String,
                                                         stat: FullGameStat, tableName: String): Long {
        val sql = "UPDATE ${tableName}_SummaryStats\n" +
                "SET villanKills = ${stat.kills},\n" +
                "  villanDeaths = ${stat.deaths},\n" +
                "  villanAssists = ${stat.assists},\n" +
                "  villanWardsPlaced = ${stat.wardsPlaced},\n" +
                "  villanWardsKilled = ${stat.wardsKilled}\n" +
                "where ${tableName}_SummaryStats.gameId = ${stat.gameId} and ${tableName}_SummaryStats.heroSummonerId = '$heroSummonerId'"

        return dbHelper.executeSQLScript(sql)
    }


    /**
     * Select the creeps killed early game for a summoner in a specific match and role.
     * @param gameId        The game that we want the stat for.
     * @param summonerId    The summoner we want the stat for.
     * @param tableName     The table to look for. This relates to role/lane
     * @return              A float with the value. Can be 0. Will be -1 if we do not find any results in that table
     *                      for the given summoner / game id combo
     */
    fun fetchCreepsEarlyGameForMatch(gameId: Long, summonerId: String, tableName: String) : Float {
        val sql = "select heroCreepsEarlyGame from ${tableName}_summarystats \n" +
                "where heroSummonerId = '$summonerId' AND gameId = $gameId"

        val result = dbHelper.executeSqlQuery(sql)
        if (result.next()) {
            val returnVal = result.getFloat("heroCreepsEarlyGame")
            result.close()
            return returnVal
        }

        return -1f
    }

}