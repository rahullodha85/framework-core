package com.hbcd.utility.common;

public enum SystemToggleType {
    Validation("VALIDATION"),
    Performance("PERFORMANCE"),
    ScreenShot("SCREENSHOT");

    SystemToggleType(final String v) {
        this.value = v;
    }

    public String getvalue() {

        return value;
    }

    private String value;
}
