package com.hbcd.storage.object;

import com.hbcd.storage.base.CommonStorage;
import com.hbcd.storage.data.DataStorageImpl;
//import com.hbcd.storage.base.DataStorage;

public class SafeObjectContentStorage {
    private static ThreadLocal<CommonStorage> _myStorage = new ThreadLocal<CommonStorage>() {
        public CommonStorage initialValue() {
            return new ObjectContentStorageImpl();
        }
    };

    public static CommonStorage Current() {
        return _myStorage.get();
    }
}
