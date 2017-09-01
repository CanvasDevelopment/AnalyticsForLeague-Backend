package db.refined_stats

import db.DBHelper
import di.KodeinManager
import model.refined_stats.RefinedStatSummary

/**
 * @author Josiah Kendall
 */
class GameSummaryDAO(val dbHelper : DBHelper) {

    fun saveHeroSummaryStats(summonerId : Long,
                             summaryStats : ArrayList<RefinedStatSummary>) {
        // for every item in the list, we want to create a new list item.

    }

    /**
     * Insert a single summary stat into the gameSummaryStats table. This is the
     */
    fun insertHeroSummaryStat(summonerId: Long, summaryStat : RefinedStatSummary) : Long {
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
                "$summonerId," +
                "${summaryStat.champId}," +
                "${summaryStat.teamId}," +
                "${summaryStat.win}," +
                "${summaryStat.teamTowerKills}," +
                "${summaryStat.teamDragonKills}," +
                "${summaryStat.teamRiftHeraldKills}," +
                "${summaryStat.teamBaronKills})"

        return dbHelper.executeSQLScript(sql)
    }
}