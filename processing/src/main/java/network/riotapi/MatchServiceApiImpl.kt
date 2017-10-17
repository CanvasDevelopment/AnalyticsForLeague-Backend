package network.riotapi

import application.region.RegionController
import model.Summoner
import model.match.Match
import model.matchlist.MatchList
import model.matchlist.MatchSummary
import model.networking.Endpoints
import model.networking.NetworkResult
import network.RequestHandler
import java.net.URL

/**
 * @author Josiah Kendall
 */
class MatchServiceApiImpl(private val requestHandler: RequestHandler,
                          private val regionController : RegionController) {

    private val endpoints = Endpoints()

    fun getMatchListForAccount(apiKey: String, accountId: Long): NetworkResult<MatchList> {
        val url = "https://" +
                regionController.getRiotRegionName() +
                ".api.riotgames.com/" +
                "/lol/match/v3/matchlists/by-account/$accountId?api_key=$apiKey"
        return requestHandler.requestDataWithRateLimiting (
                { requestHandler.sendHttpGetRequest(MatchList::class.java, URL(url), endpoints.V3_MATCHLISTS) },
                endpoints.V3_MATCHLISTS
        )
    }

    fun getMatchByMatchId(apiKey: String, matchId: Long): NetworkResult<Match> {
        val url = "https://" +
                regionController.getRiotRegionName() +
                ".api.riotgames.com/" +
                "lol/match/v3/matches/$matchId?api_key=$apiKey"
        return requestHandler.requestDataWithRateLimiting (
                { requestHandler.sendHttpGetRequest(Match::class.java, URL(url), endpoints.V3_MATCHES) },
                endpoints.V3_MATCHES
        )
    }

}