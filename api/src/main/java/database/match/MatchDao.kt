package database.match

import database.DbHelper
import model.GameStageStats
import model.MatchSummary
import model.TotalMatchStats

/**
 * @author Josiah Kendall
 */
class MatchDao(private val dbHelper: DbHelper) : MatchDaoContract {
    override fun loadTwentyIds(table: String, startingPoint: Int, summonerId: Long): ArrayList<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadTwentyIds(table: String, startingPoint: Int, summonerId: Long, heroChampId: Int): ArrayList<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadTwentyIds(table: String, startingPoint: Int, summonerId: Long, heroChampId: Int, villanChampId: Int): ArrayList<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMatchSummary(table: String, matchId: Long, summonerId: Long): MatchSummary {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadTotalStatsForAMatch(table: String, matchId: Long, summonerId: Long): TotalMatchStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadEarlyGameStageStatsForAMatch(table: String, matchId: Long, summonerId: Long): GameStageStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMidGameStageStatsForAMatch(table: String, matchId: Long, summonerId: Long): GameStageStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadLateGameStageStatsForAMatch(table: String, matchId: Long, summonerId: Long): GameStageStats {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}