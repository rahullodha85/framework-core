package com.hbcd.common.container;

import com.hbcd.common.service.DataService;
import com.hbcd.common.utility.Data;
import com.hbcd.logging.log.Log;

import java.util.HashMap;
import java.util.Map;

public class DataServiceContainer {

    private static DataServiceContainer _soleInstance = new DataServiceContainer();
    private Map<String, Object> _services = new HashMap<String, Object>();

//	public static void load (DataServiceContainer dsl)
//	{
//		_soleInstance = dsl;
//	}

    public static <T> DataService getService(T k, boolean ignorCheckFileUpdate) {
        String key = k.toString();
        //
        DataService srv =  (DataService)_soleInstance._services.get(key);
        if (!ignorCheckFileUpdate && (srv!= null) && srv.hasTheDataFileChanged())
        {
            try {
                Data.ReLoadService(key);
                srv =  (DataService)_soleInstance._services.get(key); //Re-Retrieve DataService
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return srv;
    }

    public static <T> DataService getService(T k) {
        return getService(k, false);
    }

    public static <T> void loadService(T k, Object service) {
        String key = k.toString();
        if (service == null) {
            Log.Info(String.format("WARNING: %s is Empty.", key));
        } else if (((DataService) service).size() > 0) {
            Log.Info(String.format("SUCCESSFULLY LOADED : %s Data.", key));
            Log.Info("----------------------------------------------------------------");
        }
        _soleInstance._services.put(key, service);
    }


    public static <T> void appendToService(T k, Object service) {
        String key = k.toString();
        _soleInstance._services.put(key, service);
    }

    public static <T> boolean existKey(T k) {
        String key = k.toString();
        return _soleInstance._services.containsKey(key);
    }

    private void dbImport() {

    }
}
