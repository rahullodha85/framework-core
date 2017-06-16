package com.hbcd.scripting.core;

import com.hbcd.core.genericfunctions.GenericFunctions;
import com.hbcd.scripting.core.fluentInterface.ObjectAction_CommonAction_Performance;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;

public class HomePage {
    public static ObjectAction_CommonAction_Performance Go() throws Exception {
        BrowserAction.deleteAllCookies();
        String _url = ConfigurationLoader.getWebSiteURL(ApplicationSetup.get(Setting.SITE));
        String time[] = GenericFunctions.coreGo(_url);
        return new ApplyObjectAction_Common_Performance(null, time[0], time[1]);
    }

    public static String ExtractPageSource() throws Exception {
        return GenericFunctions.coreGetHTMLSource();
    }
}
