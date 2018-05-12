package match_tests

import database.DbHelper
import database.match.MatchDao
import org.junit.Before
import org.junit.Test
import kotlin.test.fail

/**
 * @author Josiah Kendall
 */
class MatchApiApiTests {

    private lateinit var matchDao : MatchDao

    @Before
    fun setUp() {
        val dbHelper = DbHelper()
        val connected = dbHelper.connect()
        if (!connected) {
            fail("Failed to connect to the database")
        }
        matchDao = MatchDao(dbHelper)
    }

    @Test
    fun `Make sure that we can fetch match ids from 0 to 20`() {
        matchDao.
    }

    @Test
    fun `Make sure that we can fetch match ids from 20 to 40`() {

    }

    // A good example of this test case is where we request 20..40 but only have
    // 35 rows
    @Test
    fun `Make sure that we can fetch ids where there is less data than requested`() {

    }

    @Test
    fun `Make sure that we can handle requesting data when there is not any`() {

    }
}
