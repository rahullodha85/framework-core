package com.hbcd.scripting.core;

import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;

public class SiteConfiguration {

    public static String getData(String aftersitedot) throws Exception {
        return ConfigurationLoader.getValue(ApplicationSetup.get(Setting.SITE) + aftersitedot);
    }
}
