package com.hbcd.service.utility;

import com.hbcd.service.HTTP.HTTPRequest;

public class HTTPUtility {

    public static String get(String url) {
        String value = "";
        value = HTTPRequest.get(url);
        return value;
    }
}
