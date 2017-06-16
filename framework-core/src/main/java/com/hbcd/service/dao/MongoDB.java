package com.hbcd.service.dao;

import com.mongodb.*;

import java.net.UnknownHostException;

public class MongoDB {

    public static String getData(String prodId) throws UnknownHostException {
//	// To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
//	// if it's a member of a replica set:
//	MongoClient mongoClient = new MongoClient();
//	// or
//	MongoClient mongoClient = new MongoClient( "localhost" );
//	// or
//	MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//	// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
//	MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
//	                                      new ServerAddress("localhost", 27018),
//	                                      new ServerAddress("localhost", 27019)));

        //MongoClient mongoClient = new MongoClient( "mongodb://pdp:saksqa123@sd1qxx01lx.saksdirect.com:27023" );
        // ...
//		MongoCredential credential = MongoCredential.createMongoCRCredential("pdp", "test", "saks123".toCharArray());
//
//		MongoClient mongoClient = new MongoClient(new ServerAddress("sd1qxx01lx.saksdirect.com", 27023), Arrays.asList(credential));
//		
//		DB db = mongoClient.getDB("saks_services" );
        MongoClientURI uri = new MongoClientURI("mongodb://pdp:saksqa123@sd1qxx01lx.saksdirect.com:27023/saks_services");
        MongoClient client = new MongoClient(uri);
        DB db = client.getDB(uri.getDatabase());
//        DB mgDB = client.getDatabase(uri.getDatabase());

        //BasicDBObject query = new BasicDBObject("product_code", new BasicDBObject("$eq", prodId));
        DBCollection coll = db.getCollection("products");
        BasicDBObject query = new BasicDBObject("product_code", prodId);
        DBCursor cursor = coll.find(query);
        DBObject rtrn = null;
        try {
            while (cursor.hasNext()) {
                rtrn = cursor.next();
                break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            cursor.close();
        }

        if (rtrn == null) {
            return "";
        } else {
            return rtrn.toString();
        }
    }
}
