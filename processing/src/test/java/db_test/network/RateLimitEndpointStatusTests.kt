package db_test.network

import db.requests.DBHelper
import db.requests.EndpointRateLimitStatusDao
import model.networking.EndpointRateLimitStatus
import model.networking.Endpoints
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * @author Josiah Kendall
 */
class RateLimitEndpointStatusTests {

    val endpointIds = Endpoints()
    lateinit var dbHelper : DBHelper
    lateinit var rateLimitDao : EndpointRateLimitStatusDao
    @Before
    fun setUp() {
        dbHelper = DBHelper()
        dbHelper.connect()
        rateLimitDao = EndpointRateLimitStatusDao(dbHelper)
    }

    @After
    fun tearDown() {
        // clear the rate limit db
        dbHelper.executeSQLScript("DELETE FROM ${rateLimitDao.TABLE_NAME}")
    }

    @Test
    fun `Make sure that we can save rate limit to database`() {
        // save new object
        val random = Random()
        val endpoint = EndpointRateLimitStatus(endpointIds.V3_MATCHES,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

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
        val random = Random()
        val endpoint = EndpointRateLimitStatus(
                endpointIds.V3_MATCHES,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        rateLimitDao.saveEndpointRateLimitStatus(endpoint)
        assert(rateLimitDao.doesEndpointExistInDb(endpoint.endpointId))
    }

    @Test
    fun `Make sure we can update already existing endpoint`() {
        val random = Random()
        val endpoint = EndpointRateLimitStatus(
                endpointIds.V3_MATCHES,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        val endPoint2 = EndpointRateLimitStatus(
                endpointIds.V3_MATCHES,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt())

        rateLimitDao.saveEndpointRateLimitStatus(endpoint)
        assert(rateLimitDao.doesEndpointExistInDb(endpoint.endpointId))
        val result = rateLimitDao.getEndPointRateLimitStatus(endpointIds.V3_MATCHES)
        assert(result == endpoint)
        rateLimitDao.saveEndpointRateLimitStatus(endPoint2)

        // The chances of the two being the same is pretty close to 0
        val result2 = rateLimitDao.getEndPointRateLimitStatus(endpointIds.V3_MATCHES)
        assert(result2 == endPoint2)
    }
}