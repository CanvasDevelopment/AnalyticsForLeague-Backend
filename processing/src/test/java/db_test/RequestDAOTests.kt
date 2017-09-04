package db_test

import com.google.appengine.api.memcache.MemcacheServiceFactory
import db.DBHelper
import db.RequestDao
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Josiah Kendall
 */
class RequestDAOTests {

    val dbHelper = DBHelper()
    val requestDao = RequestDao(dbHelper, MemcacheServiceFactory.getMemcacheService())

    @Before
    fun doSetUp() {
        dbHelper.connect()
    }

    @Test
    fun `ensure that we can save our rate limit and fetch it again`() {
        requestDao.setRateLimit(5)
        val rateLimit = requestDao.getRateLimitPerMinute()
        Assert.assertTrue(rateLimit == 5)
    }
}