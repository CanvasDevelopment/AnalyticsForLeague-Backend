package db.requests

/**
 * @author Josiah Kendall
 */
interface RequestDAOContract {

    fun getRateLimit() : Int
    fun clearRateLimitRequests()
    fun timeSinceLastClearedRates() : Int
    fun requestsSinceLastClearedRates() : Int
    fun recordRequest(timestamp : Long)
    fun setRequestRateLimit(numberOfRequests : Int)
    fun setRateTimeFrame(timeFrameInSeconds : Int)
}