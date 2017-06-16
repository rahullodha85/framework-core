package com.hbcd.utility.dao.impl;

/**
 * Created by williskong on 7/22/2015.
 */
import java.util.ArrayList;
import java.util.Map;

import com.hbcd.utility.accessor.impl.MongoAccessor;
import com.hbcd.utility.dao.ProductDao;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class MongoProductImpl implements ProductDao{
    private MongoDatabase mongo;

    public MongoProductImpl(){
        try{
            setConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<String> getRegularItems(){
        BasicDBObject queryObject = new BasicDBObject();
        queryObject.put("ON_ORDER_QTY", new BasicDBObject("$gt", 20));
        queryObject.put("ON_HAND_QTY", new BasicDBObject("$gt", 20));
        queryObject.put("PO_NUMBER", new BasicDBObject("$ne", "9999999"));

        return getProducts("SSN_NO", queryObject);
    }
    @Override
    public ArrayList<String> getProducts(String retData, Object query) {
        ArrayList<String> products = new ArrayList<>();

        //using MongoDatabase
        MongoCursor<Document> c= MongoAccessor.getMongoDb().getCollection("inventory").find((BasicDBObject)query)
                .iterator();
        int count= 0;

        while(c.hasNext() && count < 20){
            Document d = c.next();
            if(d.containsKey(retData)){
                String item = d.get(retData).toString();
                if(inMongo(item))
                    products.add(item);
                count++;
            }
        }
        c.close();
        return products;
    }
    public static boolean inMongo(String productCode){
        return getDetails(productCode) != null;
    }
    public static Map getDetails(String productCode){
        try{
            BasicDBObject query = new BasicDBObject();
            query.put("product_code", productCode);

            //using collection to call product with query
            for(Document d : MongoAccessor.getMongoDb().getCollection("products").find(query)){
                return d;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private void setConnection() throws Exception {
        mongo = MongoAccessor.getMongoDb();
    }
}
