package db_test.network

import db.requests.EndpointRateLimitStatusDao
import model.networking.Endpoints
import network.riotapi.header.Headers
import network.riotapi.header.RiotApiResponseHeaderParser
import org.junit.Before
import org.junit.Test

import org.mockito.Mockito.*
import java.net.HttpURLConnection

/**
 * @author Josiah Kendall
 */
class EndpointRateLimitParsingTests {

    private lateinit var parser : RiotApiResponseHeaderParser
    private val rateLimitStatusDao = mock(EndpointRateLimitStatusDao::class.java)

    @Before
    fun setUp() {
        parser = RiotApiResponseHeaderParser(rateLimitStatusDao)
    }

    @Test
    fun `Make sure that we get the right amount of rate limits`() {
        val rateLimitDef = "100:1,1000:10,60000:600,360000:3600"
        val rateLimitCount = "1:1,1:10,1:600,1:3600"
        val buckets = parser.parseRateLimit(rateLimitDef, rateLimitCount)
        assert(buckets.size ==4)
    }

    @Test
    fun `Make sure that we can parse the rate limit duration correctly`() {
        val rateLimitDef = "100:1,1000:10,60000:600,360000:3600"
        val rateLimitCount = "1:1,1:10,1:600,1:3600"
        val buckets = parser.parseRateLimit(rateLimitDef, rateLimitCount)
        assert(buckets[0].rateDuration ==1)
        assert(buckets[1].rateDuration == 10)
        assert(buckets[2].rateDuration == 600)
        assert(buckets[3].rateDuration == 3600)
    }

    @Test
    fun `Make sure that we can parse the rate limit count correctly`() {
        val rateLimitDef = "100:1,1000:10,60000:600,360000:3600"
        val rateLimitCount = "5:1,50:10,93:600,185:3600"
        val buckets = parser.parseRateLimit(rateLimitDef, rateLimitCount)
        assert(buckets[0].requestCount ==5)
        assert(buckets[1].requestCount == 50)
        assert(buckets[2].requestCount == 93)
        assert(buckets[3].requestCount == 185)
    }

    @Test
    fun `Make sure that we can parse the rate limit max count correctly`() {
        val rateLimitDef = "100:1,1000:10,60000:600,360000:3600"
        val rateLimitCount = "5:1,50:10,93:600,185:3600"
        val buckets = parser.parseRateLimit(rateLimitDef, rateLimitCount)
        assert(buckets[0].maxRequests ==100)
        assert(buckets[1].maxRequests == 1000)
        assert(buckets[2].maxRequests == 60000)
        assert(buckets[3].maxRequests == 360000)
    }

    @Test
    fun `Make sure that we can set first request time when it is the first request for a bucket`() {
        val rateLimitDef = "100:1,1000:10,60000:600,360000:3600"
        val rateLimitCount = "5:1,1:10,93:600,185:3600"
        val buckets = parser.parseRateLimit(rateLimitDef, rateLimitCount)
        assert(buckets[0].firstRequestTime == (-1).toLong())
        assert(buckets[1].firstRequestTime < System.currentTimeMillis()
                && buckets[1].firstRequestTime > System.currentTimeMillis()-300) // 200 ms should be plenty for this method to run
        assert(buckets[2].firstRequestTime == (-1).toLong())
        assert(buckets[3].firstRequestTime == (-1).toLong())
    }

    @Test
    fun `Make sure that we can set apiKey endpoint`() {
        val endpoints = Endpoints()
        val headers = Headers()
        val appLimits = "100:1,1000:10,60000:600,360000:3600"
        val appLimitCount = "1:1,1:10,1:600,1:3600"
        val methodRateLimit = "100:1,1000:10,60000:600,360000:3600"
        val methodRateLimitCount = "1:1,2:10,2:600,2:3600"
        val retryAfter = "3476"
        val connection = mock(HttpURLConnection::class.java)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT)).thenReturn(appLimits)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)).thenReturn(appLimitCount)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT)).thenReturn(methodRateLimit)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)).thenReturn(methodRateLimitCount)
        `when`(connection.getHeaderField(headers.RETRY_AFTER)).thenReturn(retryAfter)

        val resultsFromParser = parser.parseRateLimitsForAppAndEndpoint(connection,endpoints.COMPLETE_APP)
        assert(resultsFromParser.appKeyRateLimit.endpointId == endpoints.COMPLETE_APP)
    }

    @Test
    fun `Make sure that we can set apiKey rate limits`() {
        val endpoints = Endpoints()
        val headers = Headers()
        val appLimits = "100:1,1000:10,60000:600,360000:3600"
        val appLimitCount = "1:1,1:10,1:600,1:3600"
        val methodRateLimit = "100:1,1000:10,60000:600,360000:3600"
        val methodRateLimitCount = "1:1,2:10,2:600,2:3600"
        val retryAfter = "3476"
        val connection = mock(HttpURLConnection::class.java)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT)).thenReturn(appLimits)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)).thenReturn(appLimitCount)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT)).thenReturn(methodRateLimit)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)).thenReturn(methodRateLimitCount)
        `when`(connection.getHeaderField(headers.RETRY_AFTER)).thenReturn(retryAfter)

        val resultsFromParser = parser.parseRateLimitsForAppAndEndpoint(connection,endpoints.COMPLETE_APP)
        assert(resultsFromParser.appKeyRateLimit.rateLimitBuckets[0].maxRequests == 100)
        assert(resultsFromParser.appKeyRateLimit.rateLimitBuckets[1].maxRequests == 1000)
        assert(resultsFromParser.appKeyRateLimit.rateLimitBuckets[2].maxRequests == 60000)
        assert(resultsFromParser.appKeyRateLimit.rateLimitBuckets[3].maxRequests == 360000)
    }

    @Test
    fun `Make sure the we can set apiKey count`() {
        val endpoints = Endpoints()
        val headers = Headers()
        val appLimits = "100:1,1000:10,60000:600,360000:3600"
        val appLimitCount = "1:1,1:10,1:600,1:3600"
        val methodRateLimit = "100:1,1000:10,60000:600,360000:3600"
        val methodRateLimitCount = "1:1,2:10,2:600,2:3600"
        val retryAfter = "3476"
        val connection = mock(HttpURLConnection::class.java)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT)).thenReturn(appLimits)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)).thenReturn(appLimitCount)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT)).thenReturn(methodRateLimit)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)).thenReturn(methodRateLimitCount)
        `when`(connection.getHeaderField(headers.RETRY_AFTER)).thenReturn(retryAfter)

        val resultsFromParser = parser.parseRateLimitsForAppAndEndpoint(connection,endpoints.COMPLETE_APP)
        assert(resultsFromParser.appKeyRateLimit.rateLimitBuckets[0].requestCount == 1)
        assert(resultsFromParser.appKeyRateLimit.rateLimitBuckets[1].requestCount == 1)
        assert(resultsFromParser.appKeyRateLimit.rateLimitBuckets[2].requestCount == 1)
        assert(resultsFromParser.appKeyRateLimit.rateLimitBuckets[3].requestCount == 1)
    }

    @Test
    fun `Make sure that we can set method rate limits`() {
        val endpoints = Endpoints()
        val headers = Headers()
        val appLimits = "100:1,1000:10,60000:600,360000:3600"
        val appLimitCount = "1:1,1:10,1:600,1:3600"
        val methodRateLimit = "100:1,1000:10,60000:600,360000:3600"
        val methodRateLimitCount = "1:1,2:10,2:600,2:3600"
        val retryAfter = "3476"
        val connection = mock(HttpURLConnection::class.java)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT)).thenReturn(appLimits)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)).thenReturn(appLimitCount)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT)).thenReturn(methodRateLimit)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)).thenReturn(methodRateLimitCount)
        `when`(connection.getHeaderField(headers.RETRY_AFTER)).thenReturn(retryAfter)

        val resultsFromParser = parser.parseRateLimitsForAppAndEndpoint(connection,endpoints.COMPLETE_APP)
        assert(resultsFromParser.endpointRateLimit.rateLimitBuckets[0].maxRequests == 100)
        assert(resultsFromParser.endpointRateLimit.rateLimitBuckets[1].maxRequests == 1000)
        assert(resultsFromParser.endpointRateLimit.rateLimitBuckets[2].maxRequests == 60000)
        assert(resultsFromParser.endpointRateLimit.rateLimitBuckets[3].maxRequests == 360000)
    }

    @Test
    fun `Make sure the we can set method count`() {
        val endpoints = Endpoints()
        val headers = Headers()
        val appLimits = "100:1,1000:10,60000:600,360000:3600"
        val appLimitCount = "1:1,1:10,1:600,1:3600"
        val methodRateLimit = "100:1,1000:10,60000:600,360000:3600"
        val methodRateLimitCount = "1:1,2:10,8:600,23:3600"
        val retryAfter = "3476"
        val connection = mock(HttpURLConnection::class.java)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT)).thenReturn(appLimits)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)).thenReturn(appLimitCount)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT)).thenReturn(methodRateLimit)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)).thenReturn(methodRateLimitCount)
        `when`(connection.getHeaderField(headers.RETRY_AFTER)).thenReturn(retryAfter)

        val resultsFromParser = parser.parseRateLimitsForAppAndEndpoint(connection,endpoints.COMPLETE_APP)
        assert(resultsFromParser.endpointRateLimit.rateLimitBuckets[0].requestCount == 1)
        assert(resultsFromParser.endpointRateLimit.rateLimitBuckets[1].requestCount == 2)
        assert(resultsFromParser.endpointRateLimit.rateLimitBuckets[2].requestCount == 8)
        assert(resultsFromParser.endpointRateLimit.rateLimitBuckets[3].requestCount == 23)
    }

    @Test
    fun `Make sure that we can handle a null on the App rate Limit and count`() {
        val endpoints = Endpoints()
        val headers = Headers()
        val methodRateLimit = "100:1,1000:10,60000:600,360000:3600"
        val methodRateLimitCount = "1:1,2:10,8:600,23:3600"
        val retryAfter = "3476"
        val connection = mock(HttpURLConnection::class.java)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT)).thenReturn(null)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)).thenReturn(null)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT)).thenReturn(methodRateLimit)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)).thenReturn(methodRateLimitCount)
        `when`(connection.getHeaderField(headers.RETRY_AFTER)).thenReturn(retryAfter)

        val resultsFromParser = parser.parseRateLimitsForAppAndEndpoint(connection,endpoints.COMPLETE_APP)
        assert(resultsFromParser.appKeyRateLimit.rateLimitBuckets.size == 0)
    }

    @Test
    fun `Make sure that we can handle set retry after`() {
        val endpoints = Endpoints()
        val headers = Headers()
        val appLimits = "100:1,1000:10,60000:600,360000:3600"
        val appLimitCount = "1:1,1:10,1:600,1:3600"
        val methodRateLimit = "100:1,1000:10,60000:600,360000:3600"
        val methodRateLimitCount = "1:1,2:10,8:600,23:3600"
        val retryAfter = "3476"
        val connection = mock(HttpURLConnection::class.java)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT)).thenReturn(appLimits)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)).thenReturn(appLimitCount)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT)).thenReturn(methodRateLimit)
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)).thenReturn(methodRateLimitCount)
        `when`(connection.getHeaderField(headers.RETRY_AFTER)).thenReturn(retryAfter)

        val resultsFromParser = parser.parseRateLimitsForAppAndEndpoint(connection,endpoints.COMPLETE_APP)
        assert(resultsFromParser.appKeyRateLimit.retryAfter== 3476)
    }


    @Test
    fun `Make sure that we can handle empty key values`() {
        val endpoints = Endpoints()
        val headers = Headers()
        val retryAfter = "3476"
        val connection = mock(HttpURLConnection::class.java)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT)).thenReturn("")
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)).thenReturn("")
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT)).thenReturn("")
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)).thenReturn("")
        `when`(connection.getHeaderField(headers.RETRY_AFTER)).thenReturn(retryAfter)

        val resultsFromParser = parser.parseRateLimitsForAppAndEndpoint(connection,endpoints.COMPLETE_APP)
        assert(resultsFromParser.appKeyRateLimit.rateLimitBuckets.size == 0)
        assert(resultsFromParser.endpointRateLimit.rateLimitBuckets.size == 0)
    }

    @Test
    fun `Make sure that we can handle null retry after`() {
        val endpoints = Endpoints()
        val headers = Headers()
        val connection = mock(HttpURLConnection::class.java)
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT)).thenReturn("")
        `when`(connection.getHeaderField(headers.APP_RATE_LIMIT_COUNT)).thenReturn("")
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT)).thenReturn("")
        `when`(connection.getHeaderField(headers.METHOD_RATE_LIMIT_COUNT)).thenReturn("")
        `when`(connection.getHeaderField(headers.RETRY_AFTER)).thenReturn(null)

        val resultsFromParser = parser.parseRateLimitsForAppAndEndpoint(connection,endpoints.COMPLETE_APP)
        assert(resultsFromParser.appKeyRateLimit.rateLimitBuckets.size == 0)
        assert(resultsFromParser.endpointRateLimit.rateLimitBuckets.size == 0)
    }
}