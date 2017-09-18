package model.networking

import network.riotapi.header.RateLimitBucket

/**
 * @author Josiah Kendall
 *
 * Holds data about each request to the riot api servers in relation to the rate limits.
 */
data class EndpointRateLimitStatus(val endpointId : Int,
                                   val retryAfter : Int,
                                   val rateLimitBuckets : ArrayList<RateLimitBucket>) {

    override fun equals(other: Any?): Boolean {
        if (other is EndpointRateLimitStatus) {
            return other.rateLimitBuckets.sameValuesInArray(rateLimitBuckets)
            && other.retryAfter == retryAfter
            && other.endpointId == endpointId
        }
        return false
    }

    private fun ArrayList<RateLimitBucket>.sameValuesInArray(other : ArrayList<RateLimitBucket>) : Boolean {
        var count = 0

        if (size != other.size) {
            return false
        }

        for (item in other) {
            val itemInIt = get(count)
            if (item != itemInIt) {
                return false
            }
            count += 1
        }

        return true
    }

    override fun hashCode(): Int {
        var result = endpointId
        result = 31 * result + retryAfter
        return result
    }
}