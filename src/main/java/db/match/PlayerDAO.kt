package db.match

import db.DBHelper
import extensions.producePlayer
import model.match.Player
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 */
class PlayerDAO(val dbHelper: DBHelper) {

    val PLAYER_TABLE_NAME : String = "player"

    fun savePlayer(player: Player, participantIdentityRowId : Long) : Long {
        val insertSQL = "insert into $PLAYER_TABLE_NAME (" +
                "${dbHelper.PARTICIPANT_IDENTITY_ROW_COLUMN}," +
                "${dbHelper.PLATFORM_ID_COLUMN}, " +
                "${dbHelper.ACCOUNT_ID_COLUMN}, " +
                "${dbHelper.SUMMONER_NAME_COLUMN}, " +
                "${dbHelper.SUMMONER_ID_COLUMN}, " +
                "${dbHelper.CURRENT_PLATFORM_ID_COLUMN}, " +
                "${dbHelper.MATCH_HISTORY_URI_COLUMN}, " +
                "${dbHelper.PROFILE_ICON_COLUMN}) VALUES(" +
                "$participantIdentityRowId, " +
                "'${player.platformId}', " +
                "${player.accountId}, " +
                "'${player.summonerName}', " +
                "${player.summonerId}, " +
                "'${player.currentPlatformId}', " +
                "'${player.matchHistoryUri}', " +
                "${player.profileIcon})"

        return dbHelper.executeSQLScript(insertSQL)
    }

    fun getPlayer(rowId : Long) : Player {
        val sqlQuery = "SELECT * FROM $PLAYER_TABLE_NAME WHERE ${dbHelper.ID_COLUMN} = $rowId"
        val result : ResultSet = dbHelper.executeSqlQuery(sqlQuery)
        return result.producePlayer()
    }
}