package com.hbcd.storage.data;

public class DataStorageManager {

    public static boolean Compare(String storageKey, String compareToValue) {
        Object o = SafeStorage.Current().get(storageKey);
        if (o != null) {
            return compareToValue.equals(o.toString());
        }
        return false;
    }

}
