package db.stats

import db.requests.DBHelper
import model.stats.CreepsPerMin

import java.sql.SQLException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 */
class CreepsPerMinDAOImpl(private val dbHelper: DBHelper) : CreepsPerMinDAO {
    private val tableName = "creepspermin"
    override fun saveCreepsPerMin(creepsPerMin: CreepsPerMin): Long {

        try {
            val queryString = String.format(
                    "Insert into $tableName ("
                            + "ZeroToTen,"
                            + "TenToTwenty,"
                            + "TwentyToThirty,"
                            + "ThirtyToEnd) values (%s, %s, %s, %s)",
                    creepsPerMin.getZeroToTen(),
                    creepsPerMin.getTenToTwenty(),
                    creepsPerMin.getTwentyToThirty(),
                    creepsPerMin.getThirtyToEnd()
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

    override fun deleteCreepsPerMin(creepsPerMinId: Long) {
        // not actually sure where this will be ever used so im going to leave it for the time being. if use cases arive it
        // will be implemented
    }

    override fun getCreepsPerMin(creepsPerMinId: Long): CreepsPerMin {
        try {
            val query = "SELECT * from $tableName WHERE Id = " + creepsPerMinId
            val resultSet = dbHelper.executeSqlQuery(query)
            if (resultSet.next()) {
                val cmpd = CreepsPerMin()
                cmpd.id = creepsPerMinId
                cmpd.setZeroToTen(resultSet.getDouble("ZeroToTen"))
                cmpd.setTenToTwenty(resultSet.getDouble("TenToTwenty"))
                cmpd.setTwentyToThirty(resultSet.getDouble("TwentyToThirty"))
                cmpd.setThirtyToEnd(resultSet.getDouble("ThirtyToEnd"))

                return cmpd
            }
        } catch (ex: SQLException) {
            Logger.getLogger(CreepsPerMinDAOImpl::class.java.name).log(Level.SEVERE, null, ex)
        } catch (ex: IllegalStateException) {
            Logger.getLogger(CreepsPerMinDAOImpl::class.java.name).log(Level.SEVERE, null, ex)
        }

        return CreepsPerMin()
    }
}
