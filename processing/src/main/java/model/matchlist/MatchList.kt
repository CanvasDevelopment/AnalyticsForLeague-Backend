package model.matchlist

import java.util.ArrayList

/**
 * @author Josiah Kendall
 *
 * The pojo wrapper for the match list object.
 * Associated Url:
 * https://oc1.api.riotgames.com/lol/match/v3/matchlists/by-account/{accountId}
 */
data class MatchList(val matches :ArrayList<MatchSummary>,
                     var startIndex: Int = 0,
                     var endIndex: Int = 0,
                     var totalGames: Int = 0)
