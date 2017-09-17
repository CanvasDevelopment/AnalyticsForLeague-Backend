package model.networking

/**
 * @author Josiah Kendall
 *
 * Holds data about each request to the riot api servers in relation to the rate limits.
 */
data class EndpointRateLimitStatus(val endpointId : Int,
                                   val retryAfter : Int,
                                   val rateLimitPerSecond: Int,
                                   val rateLimitPerMinute: Int,
                                   val rateLimitPerHour: Int,
                                   val rateLimitPerSecondCount: Int,
                                   val rateLimitPerMinuteCount: Int,
                                   val rateLimitPerHourCount : Int,
                                   val firstRequestTimeSeconds : Int,
                                   val firstRequestTimeMinutes : Int,
                                   val firstRequestTimeHours : Int) {

    override fun equals(other: Any?): Boolean {
        if (other is EndpointRateLimitStatus) {
            return other.rateLimitPerSecondCount == rateLimitPerSecondCount
            && other.rateLimitPerSecond == rateLimitPerSecond
            && other.rateLimitPerHour == rateLimitPerHour
            && other.rateLimitPerMinute == rateLimitPerMinute
            && other.rateLimitPerMinuteCount == rateLimitPerMinuteCount
            && other.rateLimitPerHourCount == rateLimitPerHourCount
            && other.firstRequestTimeHours == firstRequestTimeHours
            && other.firstRequestTimeMinutes == firstRequestTimeMinutes
            && other.firstRequestTimeSeconds == firstRequestTimeSeconds
            && other.retryAfter == retryAfter
            && other.endpointId == endpointId
        }
        return false
    }

    override fun hashCode(): Int {
        var result = endpointId
        result = 31 * result + retryAfter
        result = 31 * result + rateLimitPerSecond
        result = 31 * result + rateLimitPerMinute
        result = 31 * result + rateLimitPerHour
        result = 31 * result + rateLimitPerSecondCount
        result = 31 * result + rateLimitPerMinuteCount
        result = 31 * result + rateLimitPerHourCount
        result = 31 * result + firstRequestTimeSeconds
        result = 31 * result + firstRequestTimeMinutes
        result = 31 * result + firstRequestTimeHours
        return result
    }
}