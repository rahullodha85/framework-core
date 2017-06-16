package com.hbcd.utility.configurationsetting;

import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;

public class SiteConfigurationManager {
    public static boolean isCloseBrowserAfterFinish(boolean defaultValue) throws Exception {
        return getBoolean(getModuleConfigurationValue(Setting.IS_CLOSE_BROWSER_AFTER_FINISH.toString()), defaultValue);
    }

    public static boolean isReRun(boolean defaultValue) throws Exception {
        return getBoolean(getModuleConfigurationValue("IS_RERUN"), defaultValue);
    }

    public static boolean isOpenBrowser(boolean defaultValue) throws Exception {
        return getBoolean(getModuleConfigurationValue(Setting.IS_INITIALIZE_DRIVER.toString()), defaultValue);
    }

    public static String getSiteConfigurationValue(String aftersitedot) throws Exception {
        return ConfigurationLoader.getValue(String.format("%s.%", ApplicationSetup.get(Setting.SITE), aftersitedot));
    }

    public static String getModuleConfigurationValue(String aftersitemoduledot) throws Exception {
        return ConfigurationLoader.getValue(String.format("%s.%s.%s", ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME), aftersitemoduledot));
    }

    //Internally Use.
    private static boolean getBoolean(String v, boolean defaultValue) {
        boolean rtrn = defaultValue;
        if ((v != null) && !v.isEmpty()) {
            rtrn = v.toUpperCase().equals("TRUE");
        }
        return rtrn;
    }

//	private static String getModuleValue(String key) throws Exception
//	{
//		String value = "";
//		value = ConfigurationLoader.getValue(ApplicationSetup.get(Setting.SITE) + "." + ApplicationSetup.get(Setting.MODULE_NAME) + "." + key);
//		if ((value == null) || value.isEmpty())
//		{
//			value = "";
//		}
//		return value;
//	}
}
