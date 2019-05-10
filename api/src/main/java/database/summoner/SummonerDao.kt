package db.summoner

import database.DbHelper
import database.summoner.SummonerDAOContract
import model.response_beans.SummonerDetails
import java.sql.SQLException
import java.util.logging.Logger

/**
 * @author Josiah Kendall
 */
class SummonerDao(private val dbHelper : DbHelper) : SummonerDAOContract {

    /**
     * Save a summoner to the summoner table.
     * @param summoner The summoner to save.
     * @return -1 if save was unsuccessful, else return the result.
     */
    override fun saveSummoner(summoner: SummonerDetails): Long {
        log.info("Saving summoner : " + summoner.toString())
        val queryString = String.format(
                "Insert into %s ("
                        + "%s,"
                        + "%s,"
                        + "%s,"
                        + "%s,"
                        + "%s,"
                        + "%s)"
                        + " values (%d, %d, '%s', %d, %d, %d)",
                SummonerDAOContract.SUMMONER_TABLE,
                SummonerDAOContract.ID,
                SummonerDAOContract.ACCOUNT_ID,
                SummonerDAOContract.SUMMONER_NAME,
                SummonerDAOContract.PROFILE_ICON_ID,
                SummonerDAOContract.SUMMONER_LEVEL,
                SummonerDAOContract.REVISION_DATE,
                summoner.id,
                summoner.accountId,
                summoner.name,
                summoner.profileIconId,
                summoner.summonerLevel,
                summoner.revisionDate
        )

        try {
            val result = dbHelper.executeSQLScript(queryString)
            log.info("Successfully saved with Id : " + result)
            return result
        } catch (ex: SQLException) {
            log.severe(ex.message)
        } catch (ex: IllegalStateException) {
            log.severe(ex.localizedMessage)
        }

        return -1
    }

    override fun deleteSummoner(summonerId : String) {
        log.info("Attempting to delete summoner with id : " + summonerId)
        val queryString = String.format("DELETE from %s WHERE %s = %s", SummonerDAOContract.SUMMONER_TABLE, SummonerDAOContract.ID, summonerId)
        try {
            dbHelper.executeSQLScript(queryString)
        } catch (e: SQLException) {
            log.info("An error was found deleting summoner with id: " + summonerId)
            log.severe(e.message)
        }
    }

    fun getSummonerByName(summonerName: String): SummonerDetails? {
        log.info("Attempting to find summoner with name : " + summonerName)

        val queryString = "SELECT * FROM ${SummonerDAOContract.SUMMONER_TABLE} WHERE ${SummonerDAOContract.SUMMONER_NAME} = '$summonerName'"
        log.info("Searching for summoner: $queryString")
        try {
            val resultSet = dbHelper.executeSqlQuery(queryString)
            if (resultSet.next()) {
                val summoner = SummonerDetails(resultSet.getString(SummonerDAOContract.ID),
                        resultSet.getString("AccountId"),
                        resultSet.getString("SummonerName"),
                        resultSet.getInt("ProfileIconId"),
                        resultSet.getInt("SummonerLevel"),
                        resultSet.getLong("RevisionDate"))

                log.info("Successfully loaded summoner : \n" + summoner.toString())
                return summoner
            }
        } catch (ex: SQLException) {
            log.severe(ex.message)
        }

        return null
    }

    override fun getSummoner(summonerId : String) : SummonerDetails? {
        log.info("Attempting to find summoner with Id : " + summonerId)
        val queryString = String.format("SELECT * FROM %s WHERE %s = '%s'", SummonerDAOContract.SUMMONER_TABLE, SummonerDAOContract.ID, summonerId)
        try {
            val resultSet = dbHelper.executeSqlQuery(queryString)
            if (resultSet.next()) {
                val summoner = SummonerDetails(resultSet.getString(SummonerDAOContract.ID),
                        resultSet.getString("AccountId"),
                        resultSet.getString("SummonerName"),
                        resultSet.getInt("ProfileIconId"),
                        resultSet.getInt("SummonerLevel"),
                        resultSet.getLong("RevisionDate"))

                log.info("Successfully loaded summoner : \n" + summoner.toString())
                return summoner
            }
        } catch (ex: SQLException) {
            log.severe(ex.message)
        }

        return null
    }

    companion object {
        private val log = Logger.getLogger(SummonerDAOContract::class.java.name)
    }


}
