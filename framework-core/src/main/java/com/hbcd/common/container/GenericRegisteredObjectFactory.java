package com.hbcd.common.container;

import java.util.HashMap;

public class GenericRegisteredObjectFactory<K, T> {

    private static HashMap<String, Object> _listObjectRegistered = new HashMap<String, Object>();

    public static void registerObject(String key, String typeName) {
        if (!_listObjectRegistered.containsKey(key)) {
            _listObjectRegistered.put(key, typeName);
        } else {
            _listObjectRegistered.replace(key, typeName);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObject(String key) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        if (_listObjectRegistered.containsKey(key)) {
//            Class<?> classType = Class.forName(_listObjectRegistered.get((String)key));
//            return (T) classType.newInstance();
        }
        return null; //(T)(new Object());
    }
}
