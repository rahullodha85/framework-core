package com.hbcd.utility.testscriptdata;

import java.util.ArrayList;
import java.util.List;

public class ValidationDescriptionObject {
    private String Path = "";
    private String ContainText = "";
    private String HasAttribute = "|";
    private List<KeyValuePair> Validates = new ArrayList<KeyValuePair>();

    public String getPath() {
        return Path;
    }

    public void setPath(String p) {
        Path = p;
    }

    public String getContainText() {
        return ContainText;
    }

    public String getHasAttribute() {
        return HasAttribute;
    }

    public List<KeyValuePair> getValidates() {
        return Validates;
    }

    public void setValidates(List<KeyValuePair> param) {
        Validates.addAll(param);
    }

    public String toString() {
        String rtrn = "";
        for (KeyValuePair kv : Validates) {
            rtrn += String.format(" %s", kv.toString());
        }
        return String.format("Object in path %s validate with %s", Path, rtrn);

    }
}
