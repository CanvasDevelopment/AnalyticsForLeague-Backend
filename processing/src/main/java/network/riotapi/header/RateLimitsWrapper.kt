package network.riotapi.header

import model.networking.EndpointRateLimit

/**
 * @author Josiah Kendall
 */
data class RateLimitsWrapper(
        val appKeyRateLimit: EndpointRateLimit,
        val endpointRateLimit: EndpointRateLimit,
        val endpointId : Int)