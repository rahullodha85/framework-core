package com.hbcd.scripting.enums.impl;

public enum Check {
    TextPresent("TextPresent"),
    TextNotPresent("TextNotPresent"),
    ObjectPresent("ObjectDisplayed"),
    ObjectNotPresent("ObjectNotDisplayed"),
    DynamicCheck("DynamicObjectCheck");

    Check(final String value) {
        this.value = value;
    }

    public String getvalue() {
        return value;
    }

    private String value;
}
