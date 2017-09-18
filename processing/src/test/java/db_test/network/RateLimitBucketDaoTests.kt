package db_test.network

import db.requests.DBHelper
import db.requests.RateLimitDao
import network.riotapi.header.RateLimitBucket
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Josiah Kendall
 */
class RateLimitBucketDaoTests {

    private val random = Random()

    lateinit var rateLimitBucketDao : RateLimitDao

    @Before
    fun setUp() {
        val dbHelper = DBHelper()
        dbHelper.connect()
        rateLimitBucketDao = RateLimitDao(dbHelper)
    }

    @After
    fun cleanUp() {
        val dbHelper = DBHelper()
        dbHelper.connect()
        dbHelper.executeSQLScript("DELETE FROM ${rateLimitBucketDao.TABLE_NAME}")
    }

    @Test
    fun TestThatWeCanSaveRateLimit() {
        val endpointId = random.nextInt()
        val bucket = produceRandomBucket()
        rateLimitBucketDao.saveRateLimit(endpointId, bucket)
        val retrievedBucket = rateLimitBucketDao.rateExists(endpointId, bucket)
        assert(retrievedBucket)
    }

    @Test
    fun TestThatWeCanFetchRatelimits() {
        val endpointId = random.nextInt()
        val bucket = produceRandomBucket()
        rateLimitBucketDao.saveRateLimit(endpointId, bucket)
        val retrievedBuckets = rateLimitBucketDao.getRateLimitsForEndpoint(endpointId)
        assert(retrievedBuckets.size == 1)
        assert(retrievedBuckets[0] == bucket)
    }

    @Test
    fun `Ensure that we can save an array of limits`() {
        val endpointId = random.nextInt()
        val buckets = ArrayList<RateLimitBucket>()
        buckets.add(produceRandomBucket())
        buckets.add(produceRandomBucket())
        buckets.add(produceRandomBucket())
        buckets.add(produceRandomBucket())
        rateLimitBucketDao.saveRateLimits(endpointId, buckets)
        val retrievedBuckets = rateLimitBucketDao.getRateLimitsForEndpoint(endpointId)
        assert(retrievedBuckets == buckets)
    }

    @Test
    fun `Ensure that we remove old rates that are not included in rate limit anymore`() {
        val endpointId = random.nextInt()
        val buckets = ArrayList<RateLimitBucket>()
        val bucket1 = produceRandomBucket()
        val bucket2 = produceRandomBucket()
        val bucket3 = produceRandomBucket()
        buckets.add(bucket1)
        buckets.add(bucket2)
        buckets.add(bucket3)
        rateLimitBucketDao.saveRateLimits(endpointId, buckets)
        val retrievedBuckets = rateLimitBucketDao.getRateLimitsForEndpoint(endpointId)
        assert(retrievedBuckets == buckets)
        buckets.remove(bucket1)
        rateLimitBucketDao.saveRateLimits(endpointId, buckets)
        val secondRetrievedBuckets = rateLimitBucketDao.getRateLimitsForEndpoint(endpointId)
        assert(secondRetrievedBuckets == buckets)
    }

    @Test
    fun `Ensure that we can update a rate limit with new values`() {
        val endpointId = random.nextInt()
        val buckets = ArrayList<RateLimitBucket>()
        val bucket1 = produceRandomBucket()
        val bucket2 = produceRandomBucket()
        val bucket3 = produceRandomBucket()
        buckets.add(bucket1)
        buckets.add(bucket2)
        buckets.add(bucket3)
        rateLimitBucketDao.saveRateLimits(endpointId, buckets)
        val retrievedBuckets = rateLimitBucketDao.getRateLimitsForEndpoint(endpointId)
        assert(retrievedBuckets == buckets)
        buckets.remove(bucket1)
        val newBucket1 = RateLimitBucket(
                bucket1.maxRequests+100,
                bucket1.requestCount,
                bucket1.firstRequestTime,
                bucket1.rateDuration)
        buckets.add(newBucket1)
        rateLimitBucketDao.saveRateLimits(endpointId, buckets)
        val updatedBuckets = rateLimitBucketDao.getRateLimitsForEndpoint(endpointId)

        updatedBuckets
                .filter {
                    // chances of having two the same are very low
                    it.rateDuration == bucket1.rateDuration
                }
                .forEach{
                    assert(it == newBucket1)
                }

        assert(buckets.size == updatedBuckets.size)
    }

    private fun produceRandomBucket() : RateLimitBucket {
        return RateLimitBucket(
                random.nextInt(),
                random.nextInt(),
                random.nextLong(),
                random.nextInt())
    }
}