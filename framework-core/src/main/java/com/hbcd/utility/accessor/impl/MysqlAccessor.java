package com.hbcd.utility.accessor.impl;

/**
 * Created by williskong on 7/22/2015.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import com.hbcd.utility.accessor.Accessor;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;

public class MysqlAccessor implements Accessor{

    private Connection connection = null;
    public Connection getConnection() throws Exception {
        //Log.Info("Returning SQL connection MysqlDao");
        if(connection == null){
            connection = getDBConnection();
        }
        connection.setAutoCommit(true);
        return connection;
    }

    private Connection getDBConnection() throws Exception {
        //Log.Info("Creating SQL connection in MysqlDao");
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String _connectionString = ConfigurationLoader.getSystemStringValue("DATABASE.CONNECTIONSTRING");
        String _dbUser = ConfigurationLoader.getSystemStringValue("DATABASE.USER");
        String _dbPassword = ConfigurationLoader.getSystemStringValue("DATABASE.PASSWORD");
        return DriverManager.getConnection(_connectionString, _dbUser, _dbPassword);
//		return connection = DriverManager.getConnection(""
//				+ "jdbc:mysql://10.32.150.107:3306/reporting?"
//				+ "user=automation&password=automation123");
    }

    public void closeConnection() throws Exception
    {
        if(connection != null){
            connection.close();
            connection = null;
        }
    }

    @Override
    public Connection getConnection(String url, String user, String password)
            throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        return DriverManager.getConnection(url, user, password);
    }
}
