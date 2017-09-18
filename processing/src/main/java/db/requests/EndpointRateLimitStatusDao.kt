package db.requests

import model.networking.EndpointRateLimitStatus
import network.riotapi.header.RateLimitBucket
import java.sql.ResultSet

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
     * @param endpointRateLimitStatus Holds data about the current ratelimits for that status
     * @return true of saved successfully, false if not.
     */
    fun saveEndpointRateLimitStatus(endpointRateLimitStatus: EndpointRateLimitStatus) : Boolean {
        return if (doesEndpointExistInDb(endpointRateLimitStatus.endpointId)) {
            updateEndpointRLStatus(endpointRateLimitStatus)
        } else {
            saveEndpointRLStatus(endpointRateLimitStatus)
        }
    }

    /**
     * save an endpoint. Used for when we have no previously existing endpoint with that id
     * @return true if saved correctly, false if not.
     */
    private fun saveEndpointRLStatus(endpointRateLimitStatus: EndpointRateLimitStatus): Boolean {

        val sql = "INSERT INTO $TABLE_NAME(" +
                "$ENDPOINT_ID, " +
                "$RETRY_AFTER) VALUES (" +
                "${endpointRateLimitStatus.endpointId}," +
                "${endpointRateLimitStatus.retryAfter})"

        val result = dbHelper.executeSQLScript(sql)
        rateLimitDao.saveRateLimits(endpointRateLimitStatus.endpointId, endpointRateLimitStatus.rateLimitBuckets)
        return result != (-1).toLong()
    }

    /**
     * Update an already existing endpoint.
     * @return true if updated successfully, false if not
     */
    private fun updateEndpointRLStatus(endpointRateLimitStatus: EndpointRateLimitStatus): Boolean {
        val sql = "UPDATE $TABLE_NAME " +
                "SET $RETRY_AFTER = ${endpointRateLimitStatus.retryAfter} " +
                "WHERE $ENDPOINT_ID = ${endpointRateLimitStatus.endpointId}"

        val result = dbHelper.executeSQLScript(sql)
        rateLimitDao.saveRateLimits(endpointRateLimitStatus.endpointId, endpointRateLimitStatus.rateLimitBuckets)
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
     * @return An [EndpointRateLimitStatus] with the data for the requested endpoint.
     * @throws IllegalArgumentException If an endpoint id that does not exist is requested. Use  [doesEndpointExistInDb]
     *          before calling this.
     */
    fun getEndPointRateLimitStatus(endpointId : Int) : EndpointRateLimitStatus {
        val sql = "SELECT * FROM $TABLE_NAME WHERE $ENDPOINT_ID = $endpointId"
        val result = dbHelper.executeSqlQuery(sql)
        if (!result.next()) {
            throw IllegalArgumentException("Failed to find endpoint with specified id. Refer to method documentation to prevent this")
        }
        val rates = rateLimitDao.getRateLimitsForEndpoint(endpointId)
        return result.produceEndpointRateLimitStatus(rates)
    }

    /**
     * Produce an [EndpointRateLimitStatus] object from a sql query [ResultSet]
     */
    private fun ResultSet.produceEndpointRateLimitStatus(rates : ArrayList<RateLimitBucket>) : EndpointRateLimitStatus {
        return EndpointRateLimitStatus(
                getInt(ENDPOINT_ID),
                getInt(RETRY_AFTER),
                rates)
    }
}