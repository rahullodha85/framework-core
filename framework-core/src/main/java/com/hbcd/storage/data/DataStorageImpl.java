package com.hbcd.storage.data;

import com.hbcd.storage.base.CommonStorage;
//import com.hbcd.storage.base.DataStorage;
import com.hbcd.storage.base.CommonStorageBase;

public class DataStorageImpl<String, Object> extends CommonStorageBase<String, Object> implements CommonStorage {

//    Map<String, Object> _localStorage = new HashMap<String, Object>();
//
//    public <T1, T2> T2 get(T1 key) {
//        String myKey = key.toString();
//        T2 retObj = null;
//        synchronized (_localStorage) {
//            if (_localStorage.containsKey(myKey)) {
//                retObj = (T2) _localStorage.get(myKey);
//            }
//        }
//        return retObj;
//    }
//
//
//    public <T1, T2> void save(T1 key, T2 value) {
//        String myKey = key.toString();
//        synchronized (_localStorage) {
//            if (_localStorage.containsKey(myKey)) {
//                _localStorage.replace(myKey, value);
//            } else {
//                _localStorage.put(myKey, value);
//            }
//        }
//    }
//
//
//    public void clear() {
//        _localStorage.clear();
//    }

}
