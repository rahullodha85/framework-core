package com.hbcd.utility.accessor.impl;
/**
 * Created by williskong on 7/22/2015.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.hbcd.logging.log.Log;
import com.hbcd.utility.accessor.Accessor;
public class OracleAccessor implements Accessor{

    private static Connection connection = null;

    @Override
    public Connection getConnection() throws Exception {
        Log.Info("Returning SQL connection OracleDao");
        if(connection == null){
            connection = getDBConnection();
            connection.setAutoCommit(true);
        }
        return connection;
    }
    @Override
    public Connection getConnection(String url, String user, String password) throws Exception {
        Log.Info("Returning SQL connection OracleDao");
        if(connection == null){
            connection = getDBConnection(url, user, password);
        }
        return connection;
    }

    private Connection getDBConnection() throws Exception {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String user = "willis";
        String password = "willis";
        Log.Info("Creating SQL connection in OracleDao");
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(url, user, password);
    }
    private Connection getDBConnection(String url, String user, String password) throws Exception{
        try{
            Log.Info("Creating SQL connection in OracleDao");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(url, user, password);
        }catch(SQLException sqlException){
            Log.Error("url: " + url + " username: " + user + ", password: " + password +" not working", sqlException);
            Log.Error(sqlException.getMessage());
        }return null;
    }
    @Override
    public void closeConnection() throws Exception {
        if(connection != null){
            connection.close();
        }
    }

}
