package com.hbcd.execution.core;

import com.hbcd.utility.configurationsetting.ConfigurationLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utility {

    public static List<String> getDefaultProcessSite() {
        String dp = ConfigurationLoader.getSystemStringValue("RUN_DEFAULT_PROCESS_SITE");
        if (dp != null) {
            String newDp = dp.trim();
            if (newDp.length() > 0) {
                //return new ArrayList<String>(Arrays.asList(dp.split(Regexp.quote("|"))));
                return new ArrayList<String>(Arrays.asList(dp.split("\\|")));
            }
        }
        return new ArrayList<String>();
    }
}
