package db.match

import model.match.Match

/**
 * @author Josiah Kendall
 */
interface MatchDAOContracts {
    interface MatchDAOContract {
        fun saveMatch(match : Match)
        fun getMatch(gameId: Long) : Match
    }
}