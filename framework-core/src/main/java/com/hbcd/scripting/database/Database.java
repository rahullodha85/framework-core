package com.hbcd.scripting.database;

import com.hbcd.utility.configurationsetting.ConfigurationLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    private static Connection connect = null;
    private static Statement statement = null;

    private static Connection getConnection() throws Exception {

        Class.forName("oracle.jdbc.driver.OracleDriver");

        return DriverManager.getConnection(
                ConfigurationLoader.getConnectionURL(),
                ConfigurationLoader.getDBUser(),
                ConfigurationLoader.getDBPassword());


    }

    public static void executeQuery(String query) throws Exception {

        statement = getConnection().createStatement();
        statement.executeUpdate(query);
        statement.executeUpdate("commit");

    }
}
