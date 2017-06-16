package com.hbcd.scripting.core;

import com.hbcd.core.genericfunctions.GenericFunctions;


public class Navigate {
    public static void go(String url) throws Exception {
        GenericFunctions.coreGo(url);
    }
    public static void back() throws Exception{
        GenericFunctions.coreBack();
    }
    public static String currentUrl() {
        return GenericFunctions.coreGetCurrentUrl();
    }


}
