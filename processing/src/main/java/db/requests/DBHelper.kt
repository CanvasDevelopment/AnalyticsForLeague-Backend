/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.requests

import com.google.appengine.api.utils.SystemProperty
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import util.Constant

import java.sql.*
import java.util.logging.Level
import java.util.logging.Logger
import java.sql.DriverManager





/**

 * @author Josiah Kendall.
 * *
 * * A helper class for the connecting to and run queries against a database.
 */
class DBHelper {

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
    val RANK_COLUMN = "_Rank"
    val PARTICIPANT_ROW_ID_COLUMN = "ParticipantRowId"

    private var host: String? = null
    private var username: String? = null
    private var password: String? = null

    private var connection: Connection? = null
    private var connectionPool : HikariDataSource? = null
    private var currentlyConnected = false
    private val logger = Logger.getLogger(DBHelper::class.java.name)

    /**
     * Creates a default localhost connection;
     */
    constructor() {
        // Parameters hidded due to repo being public on github. Statics class is not uploaded.
        this.host = Constant.DEFAULT_HOST
        this.username = Constant.DEFAULT_USERNAME
        this.password = Constant.DEFAULT_PASSWORD
    }

    constructor(host: String, username : String, password : String) {
        this.host = host
        this.username = username
        this.password = password
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
                    connection = getConnectionPool().connection
                    currentlyConnected = connection!!.isValid(1000)
                    currentlyConnected
                } catch (sqlException: SQLException) {
                    println("An error occurred whist getting a connection to the database.\n"
                            + "host: " + host + "\n"
                            + "username: " + username)
                    println(sqlException.message)
                    false
                }

            } else {
                // Local MySQL instance to use during development.
                Class.forName("com.mysql.jdbc.Driver")
                // For some reason the local connection variable doesnt work, even though it seems to be correct. Therefore i use this connection method.
                return try {
                    connection = DriverManager.getConnection(host!!, username, password)
                    currentlyConnected = connection!!.isValid(5000)
                    currentlyConnected
                } catch (sqlException: SQLException) {
                    println("An error occurred whist getting a connection to the database.\n"
                            + "host is: " + host + "\n"
                            + "username: " + username)
                    println(sqlException.message)
                    false
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
            Logger.getLogger(DBHelper::class.java.name).log(Level.SEVERE, null, ex)
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
        }

        val statement = connection!!.createStatement()
        statement.closeOnCompletion()
        return statement.executeQuery(query)
    }

    /**
     * Execute a statement that updates the database
     * @param script The script to run against the database.
     *
     * @return 0 if nothing, or the number of rows effected (i think).
     *
     * @throws java.sql.SQLException
     *
     * @throws IllegalStateException
     */
    @Throws(SQLException::class, IllegalStateException::class)
    fun executeSQLScript(script: String): Long {
        if (connection == null || !currentlyConnected) {
            throw IllegalStateException("No Current database connection. Use DbConnect() to connect to a database")
        }
        try {
            val statement = connection!!.prepareStatement(script)
            statement.closeOnCompletion()
            statement.executeUpdate(script, Statement.RETURN_GENERATED_KEYS)
            val generatedKeys : ResultSet = statement.generatedKeys
            var id : Long = 0
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1)
            }
            statement.close()
            return id
        } catch (error : SQLSyntaxErrorException) {
            Logger.getLogger(DBHelper::class.java.name).log(Level.WARNING, "There was a sql syntax error", error)
            Logger.getLogger(DBHelper::class.java.name).log(Level.WARNING, "SQL script was: ", script)
        }
        return -1
    }

    private fun getConnectionPool() : HikariDataSource {
        if (connectionPool != null) {
            return connectionPool!!
        }

        val config = HikariConfig()

        // Configure which instance and what database user to connect with.
        config.jdbcUrl = String.format("jdbc:mysql:///%s", Constant.DB_NAME)
        config.username = Constant.DEFAULT_USERNAME // e.g. "root", "postgres"
        config.password = Constant.DEFAULT_PASSWORD // e.g. "my-password"

        // For Java users, the Cloud SQL JDBC Socket Factory can provide authenticated connections.
        // See https://github.com/GoogleCloudPlatform/cloud-sql-jdbc-socket-factory for details.
        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory")
        config.addDataSourceProperty("cloudSqlInstance", Constant.PRODUCTION_OCE_SQL_DB_INSTANCE)
        config.addDataSourceProperty("useSSL", "false")
        config.addDataSourceProperty("driverType", "thin")

        // ... Specify additional connection properties here.
        // ...
//        config.connectionTimeout = 10000 // 10 seconds
        // idleTimeout is the maximum amount of time a connection can sit in the pool. Connections that
        // sit idle for this many milliseconds are retried if minimumIdle is exceeded.
        config.maxLifetime = 1800000 // 30 minutes
        config.idleTimeout = 18000
        config.maximumPoolSize = 40
        config.connectionTimeout = 10000 // 10 seconds
        logger.log(Level.INFO, "max connections is: ${config.maximumPoolSize}")
        config.leakDetectionThreshold = 20000
        // Initialize the connection pool using the configuration object.
        connectionPool = HikariDataSource(config)
        return connectionPool!!
    }
}
