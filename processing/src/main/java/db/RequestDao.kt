package db

import com.google.appengine.api.memcache.MemcacheService
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 */
class RequestDao(val dbHelper: DBHelper,
                 val memcacheService: MemcacheService) : RequestDAOContract {

    private val RIOT_API_REQUEST_TABLE = "RiotApiRequests"
    private val NUMBER_OF_REQUESTS = "numberOfRequests"
    private val RATE_LIMIT_KEY = "rateLimit"
    /**
     * Insert a record for a request against riots api.
     * @param timestamp The time that this request occurred
     */
    override fun recordRequest(timestamp: Long) {
        val sql = "INSERT INTO $RIOT_API_REQUEST_TABLE(requestTime) VALUES ($timestamp)"
        dbHelper.executeSQLScript(sql)
    }

    /**
     * Clear all our records of requests against the riot api.
     */
    override fun clearRateLimitRequests() {
        val sql = "DELETE from $RIOT_API_REQUEST_TABLE"
        dbHelper.executeSQLScript(sql)
    }

    /**
     * Get the amount of time since we last cleared the requests against the riot api.
     * @return The difference in milliseconds between the first time stamp and the current time. Returns -1 if no
     *         timestamps were found
     */
    override fun timeSinceLastClearedRates(): Int {
        val sql = "SELECT * from $RIOT_API_REQUEST_TABLE Where id > 0"
        val result = dbHelper.executeSqlQuery(sql)
        if (result.next()) {
            return result.produceDifferenceBetweenEarliestTimeStampAndNow().toInt()
        }
        return -1
    }

    /**
     * Get the number of requests that have been made since we last cleared
     */
    override fun requestsSinceLastClearedRates(): Int {
        val sql = "select count(*) as $NUMBER_OF_REQUESTS from $RIOT_API_REQUEST_TABLE"
        val result = dbHelper.executeSqlQuery(sql)
        if (result.next())
            return result.getInt(NUMBER_OF_REQUESTS)
        return 0
    }

    /**
     * Get our current rate limit
     * @return The rate as an int. Will return -1 if does not exist.
     */
    override fun getRateLimitPerMinute(): Int {
        return 50
//        val result = memcacheService.get(RATE_LIMIT_KEY) ?: return -1
//        return result as Int
    }

    /**
     * Set the rate limit for the RIOT api.
     */
    override fun setRateLimit(numberOfRequests: Int) {
//        memcacheService.put(RATE_LIMIT_KEY, numberOfRequests)
    }

    private fun ResultSet.produceDifferenceBetweenEarliestTimeStampAndNow() : Long {
        val stamp = getLong("requestTime")
        val now = System.currentTimeMillis()
        return now - stamp
    }
}