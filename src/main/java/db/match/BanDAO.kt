package db.match

import db.DBHelper
import extensions.produceBan
import model.match.Ban
import util.columnnames.BanColumns

/**
 * @author Josiah Kendall
 */
class BanDAO(val dbHelper : DBHelper) {

    val BAN_TABLE = "Ban"
    val banColumns = BanColumns()
    fun saveBan(ban: Ban, teamRowId: Long) : Long {
        val sql = "INSERT INTO $BAN_TABLE (" +
                "${banColumns.CHAMPION_ID}, " +
                "${banColumns.TEAM_ID}, " +
                "${banColumns.PICK_TURN}) VALUES (" +
                "${ban.championId}," +
                "$teamRowId," +
                "${ban.pickTurn})"
        return dbHelper.executeSQLScript(sql)
    }

    fun getBanById(id : Long) : Ban {
        val sql = "SELECT * from $BAN_TABLE " +
                "WHERE Id = $id"
        val result = dbHelper.executeSqlQuery(sql)
        result.next()
        return result.produceBan()
    }

    fun getBanByTeamRowId(teamRowId: Long) : Ban {
        val sql = "SELECT * from $BAN_TABLE " +
                "WHERE ${banColumns.TEAM_ID} = $teamRowId"
        val result = dbHelper.executeSqlQuery(sql)
        result.next()
        return result.produceBan()
    }
}