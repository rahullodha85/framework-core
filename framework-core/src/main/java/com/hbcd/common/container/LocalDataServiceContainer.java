package com.hbcd.common.container;

import java.util.HashMap;
import java.util.Map;

public class LocalDataServiceContainer {
    private static ThreadLocal<LocalDataServiceContainer> _soleInstance = new ThreadLocal<LocalDataServiceContainer>() {
        public LocalDataServiceContainer initialValue() {
            return new LocalDataServiceContainer();
        }
    };

    private Map<String, Object> _services = new HashMap<String, Object>();
//	private static ThreadLocal<Map<String, Object>> _services = new ThreadLocal<Map<String, Object>>()
//			{
//		public HashMap<String, Object> initialValue()
//		{
//			
//		}
//	}

    public static <T> Object getService(T k) {
        String key = k.toString();
        return _soleInstance.get()._services.get(key);
    }

    public static <T> void loadService(T k, Object service) {
        String key = k.toString();
        _soleInstance.get()._services.put(key, service);
    }

    public static <T> boolean existKey(T k) {
        String key = k.toString();
        return _soleInstance.get()._services.containsKey(key);
    }

    public static <T> void appendToService(T k, Object service) {
        String key = k.toString();
        _soleInstance.get()._services.put(key, service);
    }
}
