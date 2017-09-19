package db_test.network

import db.requests.DBHelper
import db.requests.EndpointRateLimitStatusDao
import db.requests.RateLimitDao
import model.networking.EndpointRateLimit
import model.networking.Endpoints
import network.riotapi.header.RateLimitBucket
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Josiah Kendall
 */
class RateLimitEndpointStatusTests {
    private val random = Random()

    private val endpointIds = Endpoints()
    lateinit var dbHelper : DBHelper
    lateinit var ratesDao: RateLimitDao
    private lateinit var rateLimitDao : EndpointRateLimitStatusDao
    @Before
    fun setUp() {
        dbHelper = DBHelper()
        dbHelper.connect()
        ratesDao = RateLimitDao(dbHelper)
        rateLimitDao = EndpointRateLimitStatusDao(dbHelper, ratesDao)
    }

    @After
    fun tearDown() {
        // clear the rate limit db
        dbHelper.executeSQLScript("DELETE FROM ${rateLimitDao.TABLE_NAME}")
        dbHelper.executeSQLScript("DELETE FROM ${ratesDao.TABLE_NAME}")
    }

    @Test
    fun `Make sure that we can save rate limit to database`() {
        // save new object
        val rateLimits = ArrayList<RateLimitBucket>()
        val endpoint = EndpointRateLimit(
                endpointIds.V3_MATCHES,
                random.nextInt(),
                rateLimits)

        rateLimitDao.saveEndpointRateLimitStatus(endpoint)
        val exists = rateLimitDao.doesEndpointExistInDb(endpointIds.V3_MATCHES)
        assert(exists)
        val result = rateLimitDao.getEndPointRateLimitStatus(endpointIds.V3_MATCHES)
        assert(result == endpoint)
    }

    @Test
    fun `Make sure that doesEndpointExist() returns false if the endpoint does not exist`() {
        assert(!rateLimitDao.doesEndpointExistInDb(-1))
    }
    @Test
    fun `Make sure the doesEndPointExist() returns true of the endpoint does exist`() {
        val endpoint = EndpointRateLimit(
                endpointIds.V3_MATCHES,
                random.nextInt(),
                ArrayList())

        rateLimitDao.saveEndpointRateLimitStatus(endpoint)
        assert(rateLimitDao.doesEndpointExistInDb(endpoint.endpointId))
    }

    @Test
    fun `Make sure we can update already existing endpoint`() {
        val endpoint = EndpointRateLimit(
                endpointIds.V3_MATCHES,
                random.nextInt(),
                produceRandomArrayListOfRates())

        val endPoint2 = EndpointRateLimit(
                endpointIds.V3_MATCHES,
                random.nextInt(),
                produceRandomArrayListOfRates())

        rateLimitDao.saveEndpointRateLimitStatus(endpoint)
        assert(rateLimitDao.doesEndpointExistInDb(endpoint.endpointId))
        val result = rateLimitDao.getEndPointRateLimitStatus(endpointIds.V3_MATCHES)
        assert(result == endpoint)
        rateLimitDao.saveEndpointRateLimitStatus(endPoint2)

        // The chances of the two being the same is pretty close to 0
        val result2 = rateLimitDao.getEndPointRateLimitStatus(endpointIds.V3_MATCHES)
        assert(result2 == endPoint2)
    }

    @Test
    fun `Make sure that we can save and retrieve buckets and rates`() {

    }

    private fun produceRandomArrayListOfRates() : ArrayList<RateLimitBucket> {
        var count = 0
        val rates = ArrayList<RateLimitBucket>()
        while (count <5) {
            rates.add(RateLimitBucket(random.nextInt(),
                    random.nextInt(),
                    random.nextLong(),
                    random.nextInt()))
            count += 1
        }

        return rates
    }
}