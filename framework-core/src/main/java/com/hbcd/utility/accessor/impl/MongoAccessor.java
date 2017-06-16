package com.hbcd.utility.accessor.impl;

/**
 * Created by williskong on 7/22/2015.
 */
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;


public class MongoAccessor {
    private static MongoDatabase mongoDb;
    private static String url = ConfigurationLoader.getMongoUrl();
    private static String port = ConfigurationLoader.getMongoPort();
    private static String user =  ConfigurationLoader.getMongoUsername();
    private static String pw = ConfigurationLoader.getMongoPassword();
    private static String db = ConfigurationLoader.getMongoDB();
    public static MongoDatabase getMongoDb(){
        if(mongoDb == null){
            MongoCredential credential= MongoCredential.createMongoCRCredential(user, user, pw.toCharArray());
            MongoClient client = new MongoClient(new ServerAddress(url+":"+port), Arrays.asList(credential));
            mongoDb = client.getDatabase(db);
            mongoDb.getCollection("products").find();
            return mongoDb;
        }else return mongoDb;
    }
}
