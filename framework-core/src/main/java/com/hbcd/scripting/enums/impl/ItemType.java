package com.hbcd.scripting.enums.impl;

public enum ItemType {
    REGULAR("regular"),
    STL("stl"),
    PREORDER("preorder"),
    GWP("gwp"),
    CHANEL("chanel"),
    PERSONALIZED("personalized"),
    DROPSHIP("dropship");

    private String propKey;

    ItemType(String k) {
        this.propKey = k;
    }

    public String toString() {
        return propKey;
    }
}
