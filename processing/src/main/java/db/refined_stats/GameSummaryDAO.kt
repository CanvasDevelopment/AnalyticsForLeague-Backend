package db.refined_stats

import db.DBHelper
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
     * @param summonerId The hero summonerId. Currently unused, unsure if it will be in the future.
     * @param summaryStats The array list of [RefinedStatSummary] objects to save.
     */
    fun saveHeroSummaryStats(summonerId : Long, summaryStats : ArrayList<RefinedStatSummary>) {
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
     * Save an array of [GameStageStat] objects. See [saveGameStageStat] for more details on saving
     */
    fun saveHeroGameStageStatList(summonerId: Long, statList : ArrayList<GameStageStat>,
                                  generalGameStageColumnNames: RefinedGeneralGameStageColumnNames) {

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
     * @param refinedGeneralGameStageColumnNames   The column names to save our stats against for the early, mid and late game.
     *                                      See [RefinedGameStageColumnNames]
     */
    fun saveGameStageStat(summonerId: Long,
                          stat : GameStageStat,
                          refinedGeneralGameStageColumnNames: RefinedGeneralGameStageColumnNames) : Long {
        val sql = "UPDATE gamesummarystats\n" +
                "SET ${refinedGeneralGameStageColumnNames.earlyGame} = ${stat.earlyGame},\n" +
                " ${refinedGeneralGameStageColumnNames.midGame} = ${stat.midGame},\n" +
                " ${refinedGeneralGameStageColumnNames.lateGame} = ${stat.lateGame},\n" +
                "where gameId = ${stat.gameId} and heroSummonerId = $summonerId"

        return dbHelper.executeSQLScript(sql)
    }

}