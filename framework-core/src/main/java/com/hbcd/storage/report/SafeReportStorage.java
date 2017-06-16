package com.hbcd.storage.report;

import java.util.ArrayList;
import java.util.List;

public class SafeReportStorage {
    //private static Map<String, Object> _localStorage = new HashMap<String, Object>();
    private static ThreadLocal<List<Object>> _myStorage = new ThreadLocal<List<Object>>() {
        public List<Object> initialValue() {
            return new ArrayList<Object>();

        }
    };

    public static void Store(Object step) {
        synchronized (_myStorage) {
            _myStorage.get().add(step);
        }
    }

    public static List<Object> Get() {
        return _myStorage.get();
    }

    public static void Clear() {
        _myStorage.get().clear();
    }
}
