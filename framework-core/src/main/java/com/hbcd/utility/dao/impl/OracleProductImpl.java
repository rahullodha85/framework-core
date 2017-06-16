package com.hbcd.utility.dao.impl;

/**
 * Created by williskong on 7/22/2015.
 */

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import com.hbcd.utility.accessor.Accessor;
import com.hbcd.utility.accessor.impl.OracleAccessor;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.dao.ProductDao;
import com.hbcd.utility.helper.Common;

public class OracleProductImpl implements ProductDao{

    private static Connection connection;
    private static Properties queries = new Properties();
    String configureFileName = "ItemQueries.properties";
    private static String propertyPath = Common.getDefaultDirectory() + "\\ItemQueries.properties";

    private static String url = String.format("jdbc:oracle:thin:@%s", ConfigurationLoader.getDynamicDataUrl());
    private static String user =  ConfigurationLoader.getDynamicDataUsername();
    private static String pw = ConfigurationLoader.getDynamicDataPassword();



    public OracleProductImpl(){
        try{
            mapQueries();
            setConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getProducts(String retData, Object criteria) throws Exception{
        ArrayList<String> r = new ArrayList<String>();
        if(connection==null){
            setConnection();
        }
        ResultSet rs = connection.createStatement().executeQuery((String)criteria);
        while(rs.next()){
            String item = rs.getString("BM_ITEM");
            if(MongoProductImpl.inMongo(item))
                r.add(item);
        }
        return r;
    }

    private void setConnection() throws Exception{
        Accessor dao = new OracleAccessor();
        connection = dao.getConnection(url, user, pw);
    }
    private void mapQueries() throws Exception{
        String fullPathFileName = new com.hbcd.common.utility.Common().getFullPathFileName(configureFileName);
        if (Common.isNotNullAndNotEmpty(fullPathFileName)) {
            queries.load(new FileInputStream(fullPathFileName));
        } else {
            queries.load(OracleProductImpl.class.getClassLoader().getResourceAsStream(configureFileName));
        }
//        InputStream testFile = new FileInputStream(propertyPath);
//        queries.load(testFile);
    }
}
