package com.hbcd.scripting.core;

import com.hbcd.storage.data.SafeStorage;

public class Storage {

    public static <T> void save(String key, T value) {
        SafeStorage.Current().save(key, value);
    }

    public static <T> T get(String key) {
        return SafeStorage.Current().get(key);
    }
}
