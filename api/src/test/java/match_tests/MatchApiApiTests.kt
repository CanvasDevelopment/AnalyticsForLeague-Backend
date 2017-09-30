package match_tests

import api.MatchApi
import database.DbHelper
import org.junit.Before
import org.junit.Test
import kotlin.test.fail

/**
 * @author Josiah Kendall
 */
class MatchApiApiTests {

    lateinit var matchApi : MatchApi

    @Before
    fun setUp() {
        val dbHelper = DbHelper()
        val connected = dbHelper.connect()
        if (!connected) {
            fail("Failed to connect to the database")
        }
        matchApi = MatchApi(dbHelper)

    }

    @Test
    fun `Make sure that we can fetch match ids from 0 to 20`() {

    }
}
