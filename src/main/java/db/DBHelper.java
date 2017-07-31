/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.google.appengine.api.utils.SystemProperty;
import Util.Constant;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Josiah Kendall.
 * 
 * A helper class for the connecting to and run queries against a database.
 */
public class DBHelper {
    private String host;
    private String username;
    private String password;
    
    private Connection connection;
    private boolean currentlyConnected = false;
    
    /**
     * Creates a default localhost connection;
     */
    public DBHelper() {
        // Parameters hidded due to repo being public on github. Statics class is not uploaded.
        this.host = Constant.DEFAULT_HOST;
        this.username = Constant.DEFAULT_USERNAME;
        this.password = Constant.DEFAULT_PASSWORD;
    }
    
    public DBHelper(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }
    /**
     * Connect to a database instance.
     */
    public boolean Connect() {
        try {
            // Check if we are currently running in app engine, and set the appropriate parameters.
            if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
                String  url = System.getProperty("ae-cloudsql.cloudsql-database-url");
                // App engine sql driver
                Class.forName("com.mysql.jdbc.GoogleDriver");
                try {
                    connection = DriverManager.getConnection(url);
                    currentlyConnected = connection.isValid(1000);
                    return currentlyConnected;
                } catch (SQLException sqlException) {
                    System.out.println("An error occured whist getting a connection to the database.\n"
                            + "host: "+ host + "\n"
                            + "username: " + username );
                    System.out.println(sqlException.getMessage());
                    return false;
                }
            } else {
                // Local MySQL instance to use during development.
                Class.forName("com.mysql.jdbc.Driver");
                // For some reason the local connection variable doesnt work, even though it seems to be correct. Therefore i use this connection method.
                try {
                    connection = DriverManager.getConnection(host, username, password);
                    currentlyConnected = connection.isValid(1000);
                    return currentlyConnected;
                } catch (SQLException sqlException) {
                    System.out.println("An error occured whist getting a connection to the database.\n"
                            + "host: " + host + "\n"
                            + "username: " + username);
                    System.out.println(sqlException.getMessage());
                    return false;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Disconect from the SQL server.
     */
    public void Disconnect() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Execute a query against the database that we are currently connected to.
     * Use execute sql script if you are updating, deleting, or creating data.
     * @param query The query to execute
     * @return The result.
     * @throws java.sql.SQLException
     * @throws IllegalStateException
     */
    public ResultSet ExecuteSqlQuery(String query) throws SQLException, IllegalStateException {
        if (connection == null || !currentlyConnected) {
            throw new IllegalStateException("No Current database connection. Use DbConnect() to connect to a database");
        }
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }

    /**
     * Execute a statement that updates the database
     * @param query The query to run against the database.
     * @return 0 if nothing, or the number of rows effected (i think).
     * @throws java.sql.SQLException
     * @throws IllegalStateException 
     */
    public int ExecuteSqlScript(String query) throws SQLException, IllegalStateException {
        if (connection == null || !currentlyConnected) {
            throw new IllegalStateException("No Current database connection. Use DbConnect() to connect to a database");
        }
        
        Statement statement = connection.prepareStatement(query);
        int resultSet = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            resultSet = (int) generatedKeys.getLong(1);
        }
        return resultSet;
    }
}
