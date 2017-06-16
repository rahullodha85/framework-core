package com.hbcd.common.service;

import java.io.FileInputStream;
import java.util.*;

import com.hbcd.scripting.core.Report;
import com.hbcd.utility.configurationsetting.ApplicationToggleLoad;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.dao.ProductDao;
import com.hbcd.utility.dao.impl.MongoProductImpl;
import com.hbcd.utility.dao.impl.OracleProductImpl;
import com.hbcd.utility.helper.Common;
import com.hbcd.logging.log.Log;

public class DynamicData extends DataServiceBase {
    private static Properties queries;
    private static Map<String, ArrayList<String>> items = new HashMap<>();
    private static ProductDao sql;
    private static MongoProductImpl mongo;
    private final String className = this.getClass().getSimpleName();


    public DynamicData(){
        sql = new OracleProductImpl();
        mongo = new MongoProductImpl();
        mapQueries();
    }
    public ArrayList<String> get(String key){
        return items.get(key);
    }
    private void mapQueries() {
        long startTime = System.currentTimeMillis();
        try{
            queries = new Properties();
            String itemQueryFileName = "itemQueries.properties";
            String windowsFilePath;

            if(Common.DefaultParameterDirectory.contains("/e/")){
                windowsFilePath = Common.DefaultParameterDirectory+"/resources/"+itemQueryFileName;
            }else {
                windowsFilePath = Common.DefaultParameterDirectory+"/src/main/resources/"+itemQueryFileName;
            }

            if (windowsFilePath != null) {
                queries.load(new FileInputStream(windowsFilePath));
            } else {
                queries.load(ApplicationToggleLoad.class.getClassLoader().getResourceAsStream(itemQueryFileName));
            }
            setItems();
            long endTime   = System.currentTimeMillis();

            StringBuffer sb = new StringBuffer();
            for(String k : items.keySet()){
                if(sb.length()!=0)
                    sb.append(", ");
                sb.append(k + ": " + items.get(k).size());
            }

            Report.info("time taken for data load - "+(endTime - startTime) + " milliseconds\nItems Retrieved: " + sb.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void setItems(){
        try{
            Iterator<?> i = queries.keySet().iterator();
            while(i.hasNext()){
                String key = i.next().toString();
                if(key.equals("regular") && ConfigurationLoader.getDynamicDataUrl().contains("qasto_saks_apps")){
                    ArrayList<String> mongoItems = setMongoItems();
                    items.put(key, mongoItems);
                    if(items.get(key).get(0).equals("data not found")){
                        items.get(key).clear();
                        populateKey(key, queries.getProperty(key));
                        Log.Info("Dynamic Data - "+ key+" : no items queried");
                    }else{
                        Log.Info("Dynamic Data - "+ key+" : "+ mongoItems.size() + "  items queried");
                    }
                }else {
                    populateKey(key, queries.getProperty(key));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("com.hbcd.execution.load.DynamicData: Populating of items from DB failed.");
        }
    }
    private void populateKey(String key, String query) throws Exception{
        ArrayList<String> i = sql.getProducts("BM_ITEM",query);
        if(i.size()==0) {
            Log.Info("Dynamic Data - "+ key+" : no items queried");
        }else {
            Log.Info("Dynamic Data - "+ key+" : "+ i.size() + "  items queried");
            items.put(key, i);
        }
    }
    private ArrayList<String> setMongoItems() {
        return mongo.getRegularItems();
    }
}
