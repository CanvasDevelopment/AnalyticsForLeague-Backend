package db.requests

import model.networking.EndpointRateLimit
import network.riotapi.header.RateLimitBucket
import java.lang.IllegalArgumentException
import java.sql.ResultSet
import java.util.*

/**
 * @author Josiah Kendall
 *
 * Data access object for the rate limit status for different endpoints
 */
class EndpointRateLimitStatusDao(val dbHelper: DBHelper,
                                 private val rateLimitDao: RateLimitDao){

    val TABLE_NAME = "RiotApiEndpointRateLimitStatus"
    private val ENDPOINT_ID = "id"
    private val RETRY_AFTER = "retryAfter"

    /**
     * Save or update the rate limit status for a certain Riot API endpoint.
     * @param endpointRateLimit Holds data about the current ratelimits for that status
     * @return true of saved successfully, false if not.
     */
    fun saveEndpointRateLimitStatus(endpointRateLimit: EndpointRateLimit) : Boolean {
        return if (doesEndpointExistInDb(endpointRateLimit.endpointId)) {
            updateEndpointRLStatus(endpointRateLimit)
        } else {
            saveEndpointRLStatus(endpointRateLimit)
        }
    }

    /**
     * save an endpoint. Used for when we have no previously existing endpoint with that id
     * @return true if saved correctly, false if not.
     */
    private fun saveEndpointRLStatus(endpointRateLimit: EndpointRateLimit): Boolean {

        val sql = "INSERT INTO $TABLE_NAME(" +
                "$ENDPOINT_ID, " +
                "$RETRY_AFTER) VALUES (" +
                "${endpointRateLimit.endpointId}," +
                "${endpointRateLimit.retryAfter})"

        val result = dbHelper.executeSQLScript(sql)
        rateLimitDao.saveRateLimits(endpointRateLimit.endpointId, endpointRateLimit.rateLimitBuckets)
        return result != (-1).toLong()
    }

    /**
     * Update an already existing endpoint.
     * @return true if updated successfully, false if not
     */
    private fun updateEndpointRLStatus(endpointRateLimit: EndpointRateLimit): Boolean {
        val sql = "UPDATE $TABLE_NAME " +
                "SET $RETRY_AFTER = ${endpointRateLimit.retryAfter} " +
                "WHERE $ENDPOINT_ID = ${endpointRateLimit.endpointId}"

        val result = dbHelper.executeSQLScript(sql)
        rateLimitDao.saveRateLimits(endpointRateLimit.endpointId, endpointRateLimit.rateLimitBuckets)
        return result != (-1).toLong()
    }

    /**
     * @return True if the endpoint does exist, false if not.
     */
    fun doesEndpointExistInDb(endpointId: Int) : Boolean {
        val sql = "SELECT * FROM $TABLE_NAME WHERE $ENDPOINT_ID = $endpointId"
        val result = dbHelper.executeSqlQuery(sql)
        return result.next()
    }

    /**
     * Fetch the current rate limit status for a certain endpoint.
     * @param endpointId The endpoint id. Use [model.networking.Endpoints] to load the endpoint
     * @return An [EndpointRateLimit] with the data for the requested endpoint.
     * @throws IllegalArgumentException If an endpoint id that does not exist is requested. Use  [doesEndpointExistInDb]
     *          before calling this.
     */
    fun getEndPointRateLimitStatus(endpointId : Int) : EndpointRateLimit {
        val sql = "SELECT * FROM $TABLE_NAME WHERE $ENDPOINT_ID = $endpointId"
        val result = dbHelper.executeSqlQuery(sql)
        if (!result.next()) {
            throw IllegalArgumentException("Failed to find endpoint with specified id. Refer to method documentation to prevent this")
        }
        val rates = rateLimitDao.getRateLimitsForEndpoint(endpointId)
        return result.produceEndpointRateLimitStatus(rates)
    }

    /**
     * Produce an [EndpointRateLimit] object from a sql query [ResultSet]
     */
    private fun ResultSet.produceEndpointRateLimitStatus(rates : ArrayList<RateLimitBucket>) : EndpointRateLimit {
        return EndpointRateLimit(
                getInt(ENDPOINT_ID),
                getInt(RETRY_AFTER),
                rates)
    }
}