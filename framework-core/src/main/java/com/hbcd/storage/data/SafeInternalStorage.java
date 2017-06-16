package com.hbcd.storage.data;

//import com.hbcd.storage.base.DataStorage;

import com.hbcd.storage.base.CommonStorage;

//Should use to stored within framework(between layers) ONLY
public class SafeInternalStorage {
    private static ThreadLocal<CommonStorage> _myInternalStorage = new InheritableThreadLocal<CommonStorage>() {
        public CommonStorage initialValue() {
            return new DataStorageImpl();

        }
    };

    public static CommonStorage Current() {
        return _myInternalStorage.get();
    }
}
