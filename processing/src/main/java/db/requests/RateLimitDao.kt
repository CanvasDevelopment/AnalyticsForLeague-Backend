package db.requests

import network.riotapi.header.RateLimitBucket
import java.sql.ResultSet
import java.util.*

import java.util.logging.Logger

/**
 * @author Josiah Kendall
 */
class RateLimitDao(val dbHelper: DBHelper) {

    private val log = Logger.getLogger(this::class.java.name)

    val TABLE_NAME = "RateLimitBucket"
    val ID = "id"
    private val ENDPOINT_ID = "endpointId"
    private val MAX_REQUESTS = "maxRequests"
    private val REQUEST_COUNT = "requestCount"
    private val FIRST_REQUEST_TIME = "firstRequestTime"
    private val RATE_DURATION = "rateDuration"

    /**
     * Save the current rates for an endpoint.
     * @param endpointId The id of the riot endpoint
     * @param buckets The rate limit buckets to save.
     */
    fun saveRateLimits(endpointId : Int, buckets : ArrayList<RateLimitBucket>) {
        // if something with the same endpoint and rate time frame is saved, we need to override it.
        for (bucket in buckets) {
            saveRateLimit(endpointId, bucket)
        }

        val allBuckets = getRateLimitsForEndpoint(endpointId)
        allBuckets
                .filter { it !in buckets }
                .forEach { deleteBucket(endpointId, it) }
    }

    /**
     * Delete a bucket
     * @param endpointId The endpoint id of the bucket
     * @param bucket the bucket to delete
     */
    private fun deleteBucket(endpointId: Int, bucket : RateLimitBucket) {
        val sql = "DELETE FROM $TABLE_NAME WHERE $ENDPOINT_ID = $endpointId AND $RATE_DURATION = ${bucket.rateDuration}"
        val result = dbHelper.executeSQLScript(sql)
        log.info("Attempted to delete rate limit bucket with rate duration of ${bucket.rateDuration} and " +
                "endpoint id of $endpointId. Result was $result")
    }

    /**
     * Get all the rate limits currently set for an endpoint
     * @param endpointId The endpoint for which we want the rates
     * @return An [ArrayList] of [RateLimitBucket]'s
     */
    fun getRateLimitsForEndpoint(endpointId: Int) : ArrayList<RateLimitBucket> {
        val sql = "SELECT * FROM $TABLE_NAME WHERE $ENDPOINT_ID = $endpointId"
        val resultSet = dbHelper.executeSqlQuery(sql)
        val rates = ArrayList<RateLimitBucket>()

        while (resultSet.next()) {
            rates.add(resultSet.produceRateLimit())
        }
        resultSet.close()
        return rates
    }

    /**
     *  /**
     * Save a new rate limit. If the rate limit already exists and values have changed, it will be updated.
     * @param endpointId The endpoint to save the rate limit bucket for.
     * @param bucket The [RateLimitBucket] to save
    */
     */
    fun saveRateLimit(endpointId: Int, bucket: RateLimitBucket) {
        val bucketExists = rateExists(endpointId, bucket)
        if (bucketExists) {
            updateRateLimit(endpointId, bucket)
        } else {
            saveNewRateLimit(endpointId, bucket)
        }
    }

    /**
     * Save a new rate limit that does not currently exist
     * @param endpointId The endpoint to save the rate limit bucket for.
     * @param bucket The [RateLimitBucket] to save
     */
    private fun saveNewRateLimit(endpointId: Int, bucket: RateLimitBucket) {
        val sql = "INSERT INTO $TABLE_NAME(" +
                "$ENDPOINT_ID, " +
                "$MAX_REQUESTS, " +
                "$REQUEST_COUNT, " +
                "$FIRST_REQUEST_TIME, " +
                "$RATE_DURATION) VALUES (" +
                "$endpointId," +
                "${bucket.maxRequests}," +
                "${bucket.requestCount}," +
                "${bucket.firstRequestTime}," +
                "${bucket.rateDuration})"

        val result = dbHelper.executeSQLScript(sql)
        log.info("Saved bucket for endpoint id : $endpointId with bucket rate limit duration of " +
                "${bucket.rateDuration}. Result was $result")
    }

    /**
     * Update a rate limit for a given endpoint and bucket
     * @param endpointId The id of the endpoint that we want to update
     * @param bucket The [RateLimitBucket] details that we want to update to
     */
    private fun updateRateLimit(endpointId: Int, bucket: RateLimitBucket) {
        if (bucket.firstRequestTime != (-1).toLong()) {
            updateRateLimitCompletely(endpointId, bucket)
        } else {
            updateRateLimitButDontChangeFirstRequestTime(endpointId, bucket)
        }
    }

    private fun updateRateLimitButDontChangeFirstRequestTime(endpointId: Int, bucket: RateLimitBucket) {
        val sql ="UPDATE $TABLE_NAME SET\n" +
                "  $MAX_REQUESTS = ${bucket.maxRequests},\n" +
                "  $REQUEST_COUNT = ${bucket.requestCount}\n" +
                "    WHERE $ENDPOINT_ID = $endpointId and $RATE_DURATION = ${bucket.rateDuration}"
        val result = dbHelper.executeSQLScript(sql)
        log.info("Attempting to update rate limit for $endpointId. Result : $result")
    }

    private fun updateRateLimitCompletely(endpointId: Int, bucket: RateLimitBucket) {
        val sql ="UPDATE $TABLE_NAME SET\n" +
                "  $MAX_REQUESTS = ${bucket.maxRequests},\n" +
                "  $REQUEST_COUNT = ${bucket.requestCount},\n" +
                "  $FIRST_REQUEST_TIME = ${bucket.firstRequestTime}\n" +
                "    WHERE $ENDPOINT_ID = $endpointId and $RATE_DURATION = ${bucket.rateDuration}"
        val result = dbHelper.executeSQLScript(sql)
        log.info("Attempting to update rate limit for $endpointId. Result : $result")
    }

    /**
     * Check if a rate limit exists in our db already.
     * @param endpointId The riot api endpoint id
     * @param bucket The [RateLimitBucket] to save
     */
    fun rateExists(endpointId: Int, bucket: RateLimitBucket) : Boolean {
        val sql = "SELECT * FROM $TABLE_NAME WHERE $ENDPOINT_ID = $endpointId " +
                "AND $RATE_DURATION = ${bucket.rateDuration}"
        val result = dbHelper.executeSqlQuery(sql)
        val exists = result.next()
        result.close()
        return exists
    }

    private fun ResultSet.produceRateLimit() : RateLimitBucket {
        return RateLimitBucket(
                getInt(MAX_REQUESTS),
                getInt(REQUEST_COUNT),
                getLong(FIRST_REQUEST_TIME),
                getInt(RATE_DURATION))
    }
}