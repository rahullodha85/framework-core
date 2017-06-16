package com.hbcd.utility.testscriptdata;

public class KeyValuePair {

    private String key = "";
    private String value = "";

    public KeyValuePair() {

    }

    public KeyValuePair(String k, String v) {
        key = k;
        value = v;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return String.format("Key [%s] has Value of %s", key, value);
    }
}
