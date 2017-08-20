package db.match

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import db.DBHelper
import extensions.produceBan
import model.match.Ban
import util.columnnames.BanColumns

/**
 * @author Josiah Kendall
 */
class BanDAO(val dbHelper: DBHelper) {

    val BAN_TABLE = "Ban"
    val banColumns = BanColumns()
    fun saveBan(ban: Ban, teamRowId: Long) : Long {
        dbHelper.connect()
        val sql = "INSERT INTO $BAN_TABLE (" +
                "${banColumns.CHAMPION_ID}, " +
                "${banColumns.TEAM_ID}, " +
                "${banColumns.PICK_TURN}) VALUES (" +
                "${ban.championId}," +
                "$teamRowId," +
                "${ban.pickTurn})"
        val result = dbHelper.executeSQLScript(sql)
        return result
    }

    fun getBanById(id : Long) : Ban {
        dbHelper.connect()
        val sql = "SELECT * from $BAN_TABLE " +
                "WHERE Id = $id"
        val result = dbHelper.executeSqlQuery(sql)
        result.next()
        val ban = result.produceBan()
        return ban
    }

    fun getAllBansByTeamRowId(teamRowId: Long) : ArrayList<Ban> {
        dbHelper.connect()

        val sql = "SELECT * from $BAN_TABLE " +
                "WHERE ${banColumns.TEAM_ID} = $teamRowId"
        val result = dbHelper.executeSqlQuery(sql)

        val bans = ArrayList<Ban>()
        while(result.next()) {
            bans.add(result.produceBan())
        }

        return bans
    }
}