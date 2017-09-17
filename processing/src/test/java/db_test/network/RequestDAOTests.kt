package db_test.network

import com.google.appengine.api.memcache.MemcacheServiceFactory
import db.requests.DBHelper
import db.requests.RequestDao
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

    // TODO fix this
    @Test
    fun `ensure that we can save our rate limit and fetch it again`() {
        requestDao.setRequestRateLimit(5)
        val rateLimit = requestDao.getRateLimit()
        Assert.assertTrue(rateLimit == 50)
    }

    @Test
    fun `ensure that we can compare against the first timestamp saved when fetching timeSinceLastClearedRates()`() {
        requestDao.clearRateLimitRequests()
        requestDao.recordRequest(System.currentTimeMillis())
        requestDao.recordRequest(123)
        requestDao.recordRequest(123)
        requestDao.recordRequest(123)
        requestDao.recordRequest(123)
        requestDao.recordRequest(123)
        val timeSinceClear = requestDao.timeSinceLastClearedRates()
        Assert.assertTrue(timeSinceClear < 1000)
    }

    @Test
    fun `ensure that we can get the number of requests since last cleared requests`() {
        requestDao.clearRateLimitRequests()
        requestDao.recordRequest(System.currentTimeMillis())
        requestDao.recordRequest(123)
        requestDao.recordRequest(123)
        requestDao.recordRequest(123)
        requestDao.recordRequest(123)
        requestDao.recordRequest(123)
        val count = requestDao.requestsSinceLastClearedRates()
        Assert.assertEquals(count, 6)

        requestDao.recordRequest(123)
        requestDao.recordRequest(123)
        val count2 = requestDao.requestsSinceLastClearedRates()
        Assert.assertEquals(count2, 8)
    }

    @Test
    fun `ensure that we can fetch a 0 from request count`() {
        requestDao.clearRateLimitRequests()
        val count = requestDao.requestsSinceLastClearedRates()
        Assert.assertEquals(count, 0)
    }
}