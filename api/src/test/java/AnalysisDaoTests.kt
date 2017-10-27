import api.stat.Analysis.AnalysisDao
import api.stat.Analysis.model.CreepsPerMinuteDeltasCard
import database.DbHelper
import org.junit.Test

/**
 * @author Josiah Kendall
 */

class AnalysisDaoTests {

    @Test
    fun `Test That We Can Get Creeps Card Correctly`() {
        val dbHelper = DbHelper()
        dbHelper.connect()
        val ad = AnalysisDao(dbHelper)
        val card =  ad.fetchAvgCreepsPerMinStatCard(1542360,20,"MID")
        assert(card.heroCreepsEarlyGame == 62f)
    }
}