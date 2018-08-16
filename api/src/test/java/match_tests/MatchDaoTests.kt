package match_tests

import database.DbHelper
import database.match.MatchDao
import database.match.MatchSummaryDao
import database.match.model.MatchSummary
import org.junit.Before
import org.junit.Test
import java.util.*

class MatchDaoTests {

    val dbHelper = DbHelper()
    val matchSummaryDao = MatchSummaryDao(dbHelper)
    val matchDao = MatchDao(dbHelper)
    val random = Random()

    @Before
    fun setUp() {
        val sql = "Delete from matchSummary"
        dbHelper.executeSQLScript(sql)
    }

    @Test
    fun `Test We Can List 20 matches for any role`() {
        val summonerId = random.nextLong()
        var index = 0
        while (index < 25) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId))
            index += 1
        }

        val summaries = matchDao.loadTwentyIds(0,summonerId)
        assert(summaries.size == 20)
    }

    @Test
    fun `Test We Can List 20 matches for any role with offset`() {
        val summonerId = random.nextLong()
        var index = 0
        while (index < 25) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId))
            index += 1
        }

        val summaries = matchDao.loadTwentyIds(10,summonerId)
        assert(summaries.size == 15)
    }

    @Test
    fun `Test We Can List 20 matches for any role with a certain champ`() {
        val summonerId = random.nextLong()
        val champId = random.nextInt()
        var index = 0
        while (index < 15) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId, champId))
            index += 1
        }

        while (index < 25) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId))
            index += 1
        }

        val summaries = matchDao.loadTwentyIds(10,summonerId, champId)
        assert(summaries.size == 5)
    }

    @Test
    fun `Test We Can List 20 matches for a single role with a certain champ`() {
        val summonerId = random.nextLong()
        val champId = random.nextInt()
        val lane = random.nextInt().toString()
        var index = 0
        while (index < 15) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId, champId,lane))
            index += 1
        }

        while (index < 25) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId))
            index += 1
        }

        val summaries = matchDao.loadTwentyIds(5,summonerId, champId, lane)
        assert(summaries.size == 10)
    }

    @Test
    fun `Test We Can List 20 matches for a single role with any champ`() {
        val summonerId = random.nextLong()
        val lane = random.nextInt().toString()
        val champId = random.nextInt()
        var index = 0
        while (index < 15) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId,lane))
            index += 1
        }

        while (index < 25) {
            matchSummaryDao.saveMatchSummary(produceRandomMatchSummary(summonerId,champId))
            index += 1
        }

        val summaries = matchDao.loadTwentyIds(10,summonerId,lane)
        assert(summaries.size == 5)
    }

    fun produceRandomMatchSummary(summonerId : Long) : MatchSummary {
        return MatchSummary(
                random.nextInt(),
                random.nextFloat().toString(),
                random.nextLong(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                random.nextFloat().toString(),
                random.nextFloat().toString(),
                summonerId)
    }

    fun produceRandomMatchSummary(summonerId : Long, champId :Int) : MatchSummary {
        return MatchSummary(
                random.nextInt(),
                random.nextFloat().toString(),
                random.nextLong(),
                champId,
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                random.nextFloat().toString(),
                random.nextFloat().toString(),
                summonerId)
    }

    fun produceRandomMatchSummary(summonerId : Long, champId :Int, lane : String) : MatchSummary {
        return MatchSummary(
                random.nextInt(),
                random.nextFloat().toString(),
                random.nextLong(),
                champId,
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                random.nextFloat().toString(),
                lane,
                summonerId)
    }

    fun produceRandomMatchSummary(summonerId : Long, lane: String) : MatchSummary {
        return MatchSummary(
                random.nextInt(),
                random.nextFloat().toString(),
                random.nextLong(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                random.nextFloat().toString(),
                lane,
                summonerId)
    }
}