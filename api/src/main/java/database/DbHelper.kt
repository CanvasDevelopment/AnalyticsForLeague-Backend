package database

import com.google.appengine.api.utils.SystemProperty
import util.Constant
import java.sql.*
import java.util.logging.Level
import java.util.logging.Logger


/** @author Josiah Kendall.
 *
 * A helper class for the connecting to and run queries against a database.
 */
class DbHelper {

    val PARTICIPANT_IDENTITY_ROW_COLUMN: String = "ParticipantIdentityRowId"
    val PLATFORM_ID_COLUMN: String = "platformId"
    val ACCOUNT_ID_COLUMN: String = "accountId"
    val SUMMONER_NAME_COLUMN : String = "summonerName"
    val SUMMONER_ID_COLUMN : String = "summonerId"
    val CURRENT_PLATFORM_ID_COLUMN = "currentPlatformId"
    val MATCH_HISTORY_URI_COLUMN :String = "matchHistoryUri"
    val PROFILE_ICON_COLUMN = "profileIcon"
    val ID_COLUMN = "id"
    val GAME_ID_COLUMN = "gameId"
    val PARTICIPANT_ID_COLUMN = "ParticipantId"
    val MASTERY_ID_COLUMN = "MasteryId"
    val RANK_COLUMN = "Rank"
    val PARTICIPANT_ROW_ID_COLUMN = "ParticipantRowId"

    private var host: String? = null
    private var username: String? = null
    private var password: String? = null

    private var connection: Connection? = null
    private var currentlyConnected = false

    /**
     * Creates a default localhost connection;
     */
    constructor() {

        this.host = Constant.DEFAULT_HOST
        this.username = Constant.DEFAULT_USERNAME
        this.password = Constant.DEFAULT_PASSWORD
        connect()
    }

    constructor(host: String, username: String, password: String) {
        this.host = host
        this.username = username
        this.password = password
        connect()
    }

    /**
     * connect to a database instance.
     */
    fun connect(): Boolean {
        if (currentlyConnected) {
            return true
        }
        try {
            // Check if we are currently running in app engine, and set the appropriate parameters.
            if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
                val url = System.getProperty("ae-cloudsql.cloudsql-database-url")
                // App engine sql driver
                Class.forName("com.mysql.jdbc.GoogleDriver")
                return try {
                    connection = DriverManager.getConnection(url)
                    currentlyConnected = connection!!.isValid(1000)
                    currentlyConnected
                } catch (sqlException: SQLException) {
                    println("An error occured whist getting a connection to the database.\n"
                            + "host: " + host + "\n"
                            + "username: " + username)
                    println(sqlException.message)
                    false
                }

            } else {
                // Local MySQL instance to use during development.
                Class.forName("com.mysql.jdbc.Driver")
                // For some reason the local connection variable doesnt work, even though it seems to be correct. Therefore i use this connection method.
                try {
                    connection = DriverManager.getConnection(host!!, username, password)
                    currentlyConnected = connection!!.isValid(1000)
                    return currentlyConnected
                } catch (sqlException: SQLException) {
                    println("An error occured whist getting a connection to the database.\n"
                            + "host: " + host + "\n"
                            + "username: " + username)
                    println(sqlException.message)
                    return false
                }

            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return false

    }

    /**
     * Disconect from the SQL server.
     */
    fun disconnect() {
        try {
            connection!!.close()
        } catch (ex: SQLException) {
            Logger.getLogger(this::class.java.name).log(Level.SEVERE, null, ex)
        }

    }

    /**
     * Execute a query against the database that we are currently connected to.
     * Use execute sql script if you are updating, deleting, or creating data.
     * @param query The query to execute
     * *
     * @return The result.
     * *
     * @throws java.sql.SQLException
     * *
     * @throws IllegalStateException
     */
    @Throws(SQLException::class, IllegalStateException::class)
    fun executeSqlQuery(query: String): ResultSet {
        if (connection == null || !currentlyConnected) {
            connect()
//            throw IllegalStateException("No Current database connection. Use DbConnect() to connect to a database")
        }
        val statement = connection!!.createStatement()
        val resultSet = statement.executeQuery(query)
        return resultSet
    }

    /**
     * Execute a statement that updates the database
     * @param query The query to run against the database.
     * *
     * @return 0 if nothing, or the number of rows effected (i think).
     * *
     * @throws java.sql.SQLException
     * *
     * @throws IllegalStateException
     */
    @Throws(SQLException::class, IllegalStateException::class)
    fun executeSQLScript(query: String): Long { // TODO change this return type to LONG
        if (connection == null || !currentlyConnected) {
            throw IllegalStateException("No Current database connection. Use DbConnect() to connect to a database")
        }

        val statement = connection!!.prepareStatement(query)
        statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS)
        val generatedKeys = statement.generatedKeys
        var id : Long = 0
        if (generatedKeys.next()) {
            id = generatedKeys.getLong(1)
        }
        return id
    }
}