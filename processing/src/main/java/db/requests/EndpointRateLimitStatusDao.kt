package db.requests

import model.networking.EndpointRateLimitStatus
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 *
 * Data access object for the rate limit status for different endpoints
 */
class EndpointRateLimitStatusDao(val dbHelper: DBHelper){

    val TABLE_NAME = "RiotApiEndpointRateLimitStatus"
    private val ENDPOINT_ID = "id"
    private val RETRY_AFTER = "retryAfter"
    private val RATE_LIMIT_SECOND = "rateLimitSecond"
    private val RATE_LIMIT_MINUTE = "rateLimitMinute"
    private val RATE_LIMIT_HOUR = "rateLimitHour"
    private val RATE_LIMIT_SECOND_COUNT = "rateLimitSecondCount"
    private val RATE_LIMIT_MINUTE_COUNT = "rateLimitMinuteCount"
    private val RATE_LIMIT_HOUR_COUNT = "rateLimitHourCount"
    private val FIRST_REQUEST_TIME_SECOND = "frt_second"
    private val FIRST_REQUEST_TIME_MINUTE = "frt_minute"
    private val FIRST_REQUEST_TIME_HOUR = "frt_hour"



    /**
     * Save or update the rate limit status for a certain Riot API endpoint.
     * @param endpointRateLimitStatus Holds data about the current ratelimits for that status
     * @return true of saved successfully, false if not.
     */
    fun saveEndpointRateLimitStatus(endpointRateLimitStatus: EndpointRateLimitStatus) : Boolean {
        if (doesEndpointExistInDb(endpointRateLimitStatus.endpointId)) {
            return updateEndpointRLStatus(endpointRateLimitStatus)
        } else {
            return saveEndpointRLStatus(endpointRateLimitStatus)
        }
    }

    /**
     * save an endpoint. Used for when we have no previously existing endpoint with that id
     * @return true if saved correctly, false if not.
     */
    private fun saveEndpointRLStatus(endpointRateLimitStatus: EndpointRateLimitStatus): Boolean {

        val sql = "INSERT INTO $TABLE_NAME(" +
                "$ENDPOINT_ID, " +
                "$RETRY_AFTER, " +
                "$RATE_LIMIT_SECOND, " +
                "$RATE_LIMIT_MINUTE, " +
                "$RATE_LIMIT_HOUR, " +
                "$RATE_LIMIT_SECOND_COUNT, " +
                "$RATE_LIMIT_MINUTE_COUNT, " +
                "$RATE_LIMIT_HOUR_COUNT, " +
                "$FIRST_REQUEST_TIME_SECOND, " +
                "$FIRST_REQUEST_TIME_MINUTE, " +
                "$FIRST_REQUEST_TIME_HOUR) VALUES (" +
                "${endpointRateLimitStatus.endpointId}," +
                "${endpointRateLimitStatus.retryAfter}," +
                "${endpointRateLimitStatus.rateLimitPerSecond}," +
                "${endpointRateLimitStatus.rateLimitPerMinute}," +
                "${endpointRateLimitStatus.rateLimitPerHour}," +
                "${endpointRateLimitStatus.rateLimitPerSecondCount}," +
                "${endpointRateLimitStatus.rateLimitPerMinuteCount}," +
                "${endpointRateLimitStatus.rateLimitPerHourCount}," +
                "${endpointRateLimitStatus.firstRequestTimeSeconds}," +
                "${endpointRateLimitStatus.firstRequestTimeMinutes}," +
                "${endpointRateLimitStatus.firstRequestTimeHours})"

        val result = dbHelper.executeSQLScript(sql)
        return result != (-1).toLong()
    }

    /**
     * Update an already existing endpoint.
     * @return true if updated successfully, false if not
     */
    private fun updateEndpointRLStatus(endpointRateLimitStatus: EndpointRateLimitStatus): Boolean {
        val sql = "UPDATE $TABLE_NAME " +
                "SET $RETRY_AFTER = ${endpointRateLimitStatus.retryAfter}, " +
                "$RATE_LIMIT_SECOND = ${endpointRateLimitStatus.rateLimitPerSecond}, " +
                "$RATE_LIMIT_MINUTE = ${endpointRateLimitStatus.rateLimitPerMinute}, " +
                "$RATE_LIMIT_HOUR = ${endpointRateLimitStatus.rateLimitPerHour}, " +
                "$RATE_LIMIT_SECOND_COUNT =${endpointRateLimitStatus.rateLimitPerSecondCount}, " +
                "$RATE_LIMIT_MINUTE_COUNT = ${endpointRateLimitStatus.rateLimitPerMinuteCount}, " +
                "$RATE_LIMIT_HOUR_COUNT = ${endpointRateLimitStatus.rateLimitPerHourCount}," +
                "$FIRST_REQUEST_TIME_SECOND = ${endpointRateLimitStatus.firstRequestTimeSeconds}, " +
                "$FIRST_REQUEST_TIME_MINUTE = ${endpointRateLimitStatus.firstRequestTimeMinutes}, " +
                "$FIRST_REQUEST_TIME_HOUR = ${endpointRateLimitStatus.firstRequestTimeHours}"

        val result = dbHelper.executeSQLScript(sql)
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
            throw IllegalArgumentException("Failed to find endpoint with specified id. Refer to method documentation to prevent this");
        }

        return result.produceEndpointRateLimitStatus()
    }

    /**
     * Produce an [EndpointRateLimitStatus] object from a sql query [ResultSet]
     */
    private fun ResultSet.produceEndpointRateLimitStatus() : EndpointRateLimitStatus {
        return EndpointRateLimitStatus(
                getInt(ENDPOINT_ID),
                getInt(RETRY_AFTER),
                getInt(RATE_LIMIT_SECOND),
                getInt(RATE_LIMIT_MINUTE),
                getInt(RATE_LIMIT_HOUR),
                getInt(RATE_LIMIT_SECOND_COUNT),
                getInt(RATE_LIMIT_MINUTE_COUNT),
                getInt(RATE_LIMIT_HOUR_COUNT),
                getInt(FIRST_REQUEST_TIME_SECOND),
                getInt(FIRST_REQUEST_TIME_MINUTE),
                getInt(FIRST_REQUEST_TIME_HOUR))
    }
}