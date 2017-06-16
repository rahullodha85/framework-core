package com.hbcd.storage.base;

import java.util.HashMap;
import java.util.Map;

public class CommonStorageBase<K, T> implements CommonStorage {

    Map<String, Object> _localStorage = new HashMap<String, Object>();

    public <K, T> T get(K key) {
        String myKey = key.toString();
        T retObj = null;
        synchronized (_localStorage) {
            if (_localStorage.containsKey(myKey)) {
                retObj = (T) _localStorage.get(myKey);
            }
        }
        return retObj;
    }

    public <K> boolean contain(K key) {
        String myKey = key.toString();
        synchronized (_localStorage) {
            if (_localStorage.containsKey(myKey)) {
                return true;
            }
        }
        return false;
    }

    public <K, T> void destroy(K key) {
        String myKey = key.toString();
        synchronized (_localStorage) {
            if (_localStorage.containsKey(myKey)) {
                _localStorage.remove(myKey);
            }
        }
    }


    public <K, T> void save(K key, T value) {
        String myKey = key.toString();
        synchronized (_localStorage) {
            if (_localStorage.containsKey(myKey)) {
                _localStorage.replace(myKey, value);
            } else {
                _localStorage.put(myKey, value);
            }
        }
    }


    public void clear() {
        _localStorage.clear();
    }

}
