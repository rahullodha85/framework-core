package com.hbcd.utility.common;

import java.util.HashMap;

public class ApplicationSetup {

    private static boolean _isSetup = false;

    //private static ThreadLocal<HashMap<String, String>> _mySetup = new ThreadLocal<HashMap<String, String>>()
    private static ThreadLocal<HashMap<String, Object>> _mySetup = new InheritableThreadLocal<HashMap<String, Object>>() {
        public HashMap<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    public static boolean isSetup() {
        return _isSetup;
    }

    public static void setupCompleted() {
        _isSetup = true;
    }

    public static <T1, T2> void set(T1 k, T2 v) {
        String key = k.toString();
        if (_mySetup.get().containsKey(key)) {
            _mySetup.get().replace(key, v);
        } else {
            _mySetup.get().put(key, v);
        }
    }

    public static <T1, T2> String get(T1 k) {
        String key = k.toString();

        if (!_isSetup) {
            return "";
        }
        if (_mySetup.get().size() == 0) {
            //Log.Info("Application setting for [" + key + "] has not been setup yet.s");
        }
        if (_mySetup.get().containsKey(key)) {
            return (String) _mySetup.get().get(key);
        }
        return "";
    }

    public static <T1, T2> boolean getBoolean(T1 k) {
        String key = k.toString();

        if (!_isSetup) {
            return true;
        }
        if (_mySetup.get().size() == 0) {
            //Log.Info("Application setting for [" + key + "] has not been setup yet.b");
        }
        if (_mySetup.get().containsKey(key)) {
            return (boolean) _mySetup.get().get(key);
        }
        return true;
    }

    public static <T1, T2> int getInteger(T1 k) {
        String key = k.toString();

        if (!_isSetup) {
            return 0;
        }
        if (_mySetup.get().containsKey(key)) {
            return (int) _mySetup.get().get(key);
        }
        return 0;
    }

    public static <T1, T2> long getLong(T1 k) {
        String key = k.toString();

        if (!_isSetup) {
            return 0L;
        }
        if (_mySetup.get().containsKey(key)) {
            return (long) _mySetup.get().get(key);
        }
        return 0L;
    }

    public static <T1, T2> T2 getConfigurationObject(T1 k) {
        String key = k.toString();
        if (_mySetup.get().containsKey(key)) {
            return (T2) _mySetup.get().get(key);
        }
        return null;
    }
}
