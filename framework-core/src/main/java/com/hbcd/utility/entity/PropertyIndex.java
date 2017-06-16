package com.hbcd.utility.entity;

public class PropertyIndex {

    int _idx = -1;
    int _type = -1; //0 int, 1 String
    Object _value = null;

    public PropertyIndex(int i, Object v) {
        _idx = i;
        _value = v;
    }

    public PropertyIndex(int i, int t, Object v) {
        _idx = i;
        _type = t;
        _value = v;
    }

    private String trimEnding(Object v) {
        String rtrn = v.toString();
        if (rtrn.endsWith(".0")) {
            rtrn.substring(0, rtrn.length() - 3);
        }
        return rtrn;
    }

    public int getIndex() {
        return _idx;
    }

    public int getType() {
        return _type;
    }

    public Object getValue() {
        return _value;
    }

    public String getValueAsString() {
        return trimEnding(_value);
    }

    public int getValueAsInt() {
        return (int) Double.parseDouble(_value.toString());
    }

    public Double getValueAsDouble() {
        return Double.parseDouble(_value.toString());
    }

    public boolean getValueAsBoolean() {
        return _value.toString().equalsIgnoreCase("true") ? true : false;
    }
}

