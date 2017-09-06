package db.refined_stats

import db.DBHelper
import model.refined_stats.FullGameStat
import model.refined_stats.GameStageStat
import model.refined_stats.RefinedStatSummary
import model.refined_stats.RefinedGeneralGameStageColumnNames

/**
 * @author Josiah Kendall
 *
 * This is the game summary data access object, used to save the stats that we 'refine' using the refinement dao.
 * We save all these stats into a table named gamesummarystats. This is done for efficiency - with 10 participants for
 * each match, 10 participantIdentities, tables can grow very quickly as each user will probably have over 100 games,
 * some over 1000. We have to join 5 tables just to get the number of creeps - and with 20000 users some of these tables
 * will probably be nearing a millon rows, meaning queries cannot be done in real time. Storing the stats we need for
 * early release in a summary table will make things easy to query in real time.
 *
 * Eventually it will be neccessary to move to a multi table query structure, as client side requests come for more
 * customizable and complex data. By the time this comes, we should have a better understanding of how data will fit
 * together, and what data we want.
 */
class GameSummaryDAO(val dbHelper : DBHelper) {

    /**
     * Save an Array of [RefinedStatSummary] match objects for our hero. Each object will represent one game.
     * @param summonerId        The hero summonerId. Currently unused, unsure if it will be in the future.
     * @param summaryStats      The array list of [RefinedStatSummary] objects to save.
     */
    fun saveHeroSummaryStats(summonerId : Long, summaryStats : ArrayList<RefinedStatSummary>)  {
        // for every item in the list, we want to create a new list item.
        for(summaryStat in summaryStats) {
            insertHeroSummaryStat(summaryStat)
        }
    }

    /**
     * Insert a single summary stat into the gameSummaryStats table.
     * @param summaryStat The [RefinedStatSummary] to save
     */
    fun insertHeroSummaryStat(summaryStat : RefinedStatSummary) : Long {
        val sql = "INSERT INTO gamesummarystats (" +
                "gameId,\n" +
                "heroSummonerId,\n" +
                "heroChampId,\n" +
                "heroTeamId,\n" +
                "heroWin,\n" +
                "heroTeamTowerKills,\n" +
                "heroTeamDragonKills,\n" +
                "heroTeamRiftHeraldKills,\n" +
                "heroTeamBaronKills) VALUES (" +
                "${summaryStat.gameId}," +
                "${summaryStat.summonerId}," +
                "${summaryStat.champId}," +
                "${summaryStat.teamId}," +
                "${summaryStat.win}," +
                "${summaryStat.teamTowerKills}," +
                "${summaryStat.teamDragonKills}," +
                "${summaryStat.teamRiftHeraldKills}," +
                "${summaryStat.teamBaronKills})"

        return dbHelper.executeSQLScript(sql)
    }

    /**
     * Insert a single summary stat into the gameSummaryStats table.
     * @param heroSummonerId    The summoner Id of our hero
     * @param summaryStat       The [RefinedStatSummary] to save
     */
    fun saveVillanSummaryStat(heroSummonerId: Long, summaryStat : RefinedStatSummary) : Long {
        val sql = "Update gamesummarystats " +
                "SET villanChampId = ${summaryStat.champId},\n" +
                "villanTeamId = ${summaryStat.teamId},\n" +
                "villanWin = ${summaryStat.win},\n" +
                "villanTeamTowerKills = ${summaryStat.teamTowerKills},\n" +
                "villanTeamDragonKills = ${summaryStat.teamDragonKills},\n" +
                "villanTeamRiftHeraldKills = ${summaryStat.teamRiftHeraldKills},\n" +
                "villanTeamBaronKills = ${summaryStat.teamBaronKills} " +
                "where gameId = ${summaryStat.gameId} and heroSummonerId = $heroSummonerId"

        return dbHelper.executeSQLScript(sql)
    }

    /**
     * Save an array of [GameStageStat] objects. See [saveGameStageStat] for more details on saving
     */
    fun saveHeroGameStageStatList(summonerId: Long, statList : ArrayList<GameStageStat>,
                                  generalGameStageColumnNames: RefinedGeneralGameStageColumnNames) {
        for (gameStage in statList) {
            saveGameStageStat(summonerId, gameStage, generalGameStageColumnNames)
        }

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
    fun saveGameStageStat(summonerId: Long,
                          stat : GameStageStat,
                          columnNames: RefinedGeneralGameStageColumnNames) : Long {
        val sql = "UPDATE gamesummarystats\n" +
                "SET ${columnNames.earlyGame} = ${stat.earlyGame},\n" +
                " ${columnNames.midGame} = ${stat.midGame},\n" +
                " ${columnNames.lateGame} = ${stat.lateGame}\n" +
                "where gameId = ${stat.gameId} and heroSummonerId = $summonerId"

        return dbHelper.executeSQLScript(sql)
    }

    /**
     * Save an array of [FullGameStat]. Each [FullGameStat] will represent a game for the hero.
     * @param summonerId    The hero summonerId
     * @param stats         The full game stats for the hero that we want to save.
     */
    fun saveTeamStatsListForHero(summonerId: Long,
                      stats : ArrayList<FullGameStat>) {
        for (stat in stats) {
            saveTeamStatsItemForHero(summonerId, stat)
        }
    }

    /**
     * Save the team stats item : [FullGameStat] for our hero.
     * @param heroSummonerId    The hero summoner id
     * @param stat              The [FullGameStat] object to save.
     * @return                  The column row id. -1 if update fails.
     */
    fun saveTeamStatsItemForHero(heroSummonerId : Long,
                                 stat : FullGameStat) : Long {
        val sql = "UPDATE GameSummaryStats\n" +
                "SET heroKills = ${stat.kills},\n" +
                "  heroDeaths = ${stat.deaths},\n" +
                "  heroAssists = ${stat.assists},\n" +
                "  heroWardsPlaced = ${stat.wardsPlaced},\n" +
                "  heroWardsKilled = ${stat.wardsKilled}\n" +
                "where GameSummaryStats.gameId = ${stat.gameId} and GameSummaryStats.heroSummonerId = $heroSummonerId"

        return dbHelper.executeSQLScript(sql)
    }

    /**
     * Save the team stats item : [FullGameStat] for our villan.
     * @param heroSummonerId    The heros summoner Id
     * @param stat              The full game stat to save for our villan.
     * @return                  The row id of the updated row. Returns -1 if not successfull.
     */
    fun saveTeamStatsItemForVillan(heroSummonerId : Long,
                                 stat : FullGameStat) : Long {
        val sql = "UPDATE GameSummaryStats\n" +
                "SET villanKills = ${stat.kills},\n" +
                "  villanDeaths = ${stat.deaths},\n" +
                "  villanAssists = ${stat.assists},\n" +
                "  villanWardsPlaced = ${stat.wardsPlaced},\n" +
                "  villanWardsKilled = ${stat.wardsKilled}\n" +
                "where GameSummaryStats.gameId = ${stat.gameId} and GameSummaryStats.heroSummonerId = $heroSummonerId"

        return dbHelper.executeSQLScript(sql)
    }

}