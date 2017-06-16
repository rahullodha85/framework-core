package com.hbcd.utility.accessor;
import java.sql.Connection;
/**
 * Created by williskong on 7/22/2015.
 */
public interface Accessor {
    Connection getConnection() throws Exception;
    Connection getConnection(String url, String user, String password) throws Exception ;
    void closeConnection() throws Exception;
}
