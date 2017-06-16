package com.hbcd.storage.data;

import com.hbcd.storage.base.CommonStorage;
//import com.hbcd.storage.base.DataStorage;

public class SafeStorage {
    private static ThreadLocal<CommonStorage> _myStorage = new ThreadLocal<CommonStorage>() {
        public CommonStorage initialValue() {
            return new DataStorageImpl();

        }
    };

    public static CommonStorage Current() {
        return _myStorage.get();
    }
}
