package db.stats

import db.DBHelper
import extensions.produceGameStageStat
import model.stats.GameStageDelta

import java.sql.SQLException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 */
class GameStageDeltaDAOImpl(private val dbHelper: DBHelper) : GameStageDeltasDAO {

    override fun saveDeltas(gameStageDelta: GameStageDelta, tableName: String, timelineId: Long): Long {
        try {
            val queryString = String.format(
                    "Insert into %s ("
                            + "timelineId,"
                            + "ZeroToTen,"
                            + "TenToTwenty,"
                            + "TwentyToThirty,"
                            + "ThirtyToEnd) values (%s, %s, %s, %s, %s)",
                    tableName,
                    timelineId,
                    gameStageDelta.zeroToTen,
                    gameStageDelta.tenToTwenty,
                    gameStageDelta.twentyToThirty,
                    gameStageDelta.thirtyToEnd
            )
            return dbHelper.executeSQLScript(queryString)
        } catch (ex: SQLException) {
            Logger.getLogger(CreepsPerMinDAOImpl::class.java.name).log(Level.SEVERE, null, ex)
        } catch (ex: IllegalStateException) {
            Logger.getLogger(CreepsPerMinDAOImpl::class.java.name).log(Level.SEVERE, null, ex)
        } catch (ex: NullPointerException) {
            Logger.getLogger(CreepsPerMinDAOImpl::class.java.name).log(Level.SEVERE, null, ex)
        }

        return -1
    }

    override fun deleteDeltas(gameStageDeltasId: Long, tableName: String) {

    }

    fun getGameStageDeltasForTimeline(timelineId: Long, tableName: String) : GameStageDelta {
        val sql = "SELECT * FROM $tableName " +
                "WHERE timelineId = $timelineId"
        val result = dbHelper.executeSqlQuery(sql)
        result.next()
        return result.produceGameStageStat()
    }

    override fun getGameStageDeltas(gameStageDeltasId: Long, tableName: String): GameStageDelta? {
        try {

            val query = "SELECT * from $tableName WHERE Id = $gameStageDeltasId"
            val resultSet = dbHelper.executeSqlQuery(query)
            if (resultSet.next()) {
                val gameStageDelta = GameStageDelta()
                gameStageDelta.id = gameStageDeltasId
                gameStageDelta.zeroToTen = resultSet.getDouble("ZeroToTen")
                gameStageDelta.tenToTwenty = resultSet.getDouble("TenToTwenty")
                gameStageDelta.twentyToThirty = resultSet.getDouble("TwentyToThirty")
                gameStageDelta.thirtyToEnd = resultSet.getDouble("ThirtyToEnd")

                return gameStageDelta
            }
        } catch (ex: SQLException) {
            Logger.getLogger(CreepsPerMinDAOImpl::class.java.name).log(Level.SEVERE, null, ex)
        } catch (ex: IllegalStateException) {
            Logger.getLogger(CreepsPerMinDAOImpl::class.java.name).log(Level.SEVERE, null, ex)
        }

        return null
    }
}
