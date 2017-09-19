package network.riotapi.header

/**
 * @author Josiah Kendall
 *
 * A Rate limit bucket. Holds onto the data for our rate limits.
 * For instance, if [rateDuration] was 10, and [maxRequests] was 100, we would be able to make 100 requests in 10 seconds.
 * The [firstRequestTime] was the time the [rateDuration] was effectively started, so add the [rateDuration] *1000 to
 * get the end of this current limit.
 * @param maxRequests       The max number of requests that can be made in the [rateDuration]
 * @param requestCount      The number of requests that have already been made within the [rateDuration]
 * @param firstRequestTime  The time (in milliseconds) the first request in this [rateDuration] was made. In other words
 *                          the start time of the current [rateDuration]
 * @param rateDuration      The length of time in seconds over which this rate limit applies.
 */
class RateLimitBucket(
        val maxRequests : Int,
        val requestCount : Int,
        val firstRequestTime : Long,
        val rateDuration : Int) {

    override fun equals(other: Any?): Boolean {
        if (other is RateLimitBucket) {
            return other.maxRequests == maxRequests
            && other.requestCount == requestCount
            && other.rateDuration == rateDuration
        }
        return false
    }

    override fun hashCode(): Int {
        var result = maxRequests
        result = 31 * result + requestCount
        result = 31 * result + firstRequestTime.hashCode()
        result = 31 * result + rateDuration
        return result
    }
}