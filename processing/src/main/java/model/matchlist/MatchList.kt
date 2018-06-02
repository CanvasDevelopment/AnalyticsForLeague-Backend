package model.matchlist

import java.util.ArrayList

/**
 * @author Josiah Kendall
 *
 * The pojo wrapper for the match list object.
 * Associated Url:
 * https://oc1.api.riotgames.com/lol/match/v3/matchlists/by-account/{accountId}
 */
class MatchList {
    lateinit var matches : ArrayList<MatchSummary>
    var startIndex : Int = 0
    var endIndex: Int = 0
    var totalGames : Int = 0

    constructor()
    constructor(matches :ArrayList<MatchSummary>,
                startIndex: Int,
                endIndex: Int ,
                totalGames: Int ) {
        this.matches = matches
        this.startIndex = startIndex
        this.endIndex = endIndex
        this.totalGames = totalGames
    }
}
