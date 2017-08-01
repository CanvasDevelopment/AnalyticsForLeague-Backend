package db.matchlist

import db.DBHelper
import model.matchlist.MatchList

/**
 * @author Josiah Kendall
 */
class MatchListDAO(val dbHelper: DBHelper) {

    /**
     * Save a match list to the database.
     *
     * @return return the
     */
    fun saveMatchList(matchList: MatchList) : Int {
        val queryString = String.format("")
    }
}