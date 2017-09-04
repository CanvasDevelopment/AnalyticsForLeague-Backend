package db

/**
 * @author Josiah Kendall
 */
interface RequestDAOContract {

    fun getRateLimitPerMinute() : Int
    fun clearRateLimitRequests()
    fun timeSinceLastClearedRates() : Int
    fun requestsSinceLastClearedRates() : Int
    fun recordRequest(timestamp : Long)
    fun setRateLimit(numberOfRequests : Int)
}