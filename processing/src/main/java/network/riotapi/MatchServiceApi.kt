package network.riotapi

import model.Summoner
import model.match.Match
import model.matchlist.MatchList
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query

/**
 * @author Josiah Kendall
 *
 * This is the retrofit interface for the match-v3 api of riots servers
 */
interface MatchServiceApi {

    /**
     * Get a [MatchList] for a [Summoner]
     * @param accountId The accountId for that summoner
     */
    @GET("/lol/match/v4/matchlists/by-account/{accountId}")
    fun getMatchListForAccount(@Query("api_key") apiKey : String, @Path("accountId") accountId : Long) : MatchList

    /**
     * Fetch a specific [Match] based on the [matchId]
     * @param matchId The unique id assigned by riot for this match
     */
    @GET("/lol/match/v4/matches/{matchId}")
    fun getMatchByMatchId(@Query("api_key") apiKey : String, @Path("matchId") matchId : Long) : Match
}