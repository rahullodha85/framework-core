package com.hbcd.scripting.core;

import com.hbcd.common.utility.Data;
import com.hbcd.core.genericfunctions.GenericFunctions;
import com.hbcd.logging.log.Log;
import com.hbcd.scripting.enums.impl.Keys;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.ProjectSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.configurationsetting.SiteConfigurationManager;
import com.hbcd.utility.schedulerUtility.SchedulerManager;

public class BrowserAction {

    public static String screenshot() {
        String screenshot = "";
        try {
            //System.out.println(ApplicationSetup.get(Setting.BROWSER_TYPE));
            String browserType = ApplicationSetup.get(Setting.BROWSER_NAME).trim();
            if (!"".equals(browserType)) {
                if (!browserType.equalsIgnoreCase("HTMLUNITDRIVER")) {
                    if (ConfigurationLoader.getModuleToggle("SCREENSHOT")) {
                        screenshot = GenericFunctions.coreScreenshot();
                    } else {
                        Log.Info("Action: NO SCREENSHOT");
                    }
                } else {
                    Log.Info("Action: NO SCREENSHOT");
                }
            } else {
                Log.Info("Action: NO SCREENSHOT");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenshot;
    }

    public static void start() //For script not inherit from ScenarioBase ONLY
    {
        try {
            if (!ApplicationSetup.isSetup()) {
                ProjectSetup.Setup(null); //Default
            }
            Data.LoadRepository();
            Report.init();
            initializeDriver();
            SchedulerManager.updateSchedulerInProgress(ApplicationSetup.getLong(Setting.REQUEST_ID));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initializeDriver() throws Exception {
        try {
            initializeDriver(ApplicationSetup.get(Setting.BROWSER_TYPE)
                            , ApplicationSetup.get(Setting.BROWSER_NAME)
                            , ApplicationSetup.get(Setting.BROWSER_VERSION)
                            , ApplicationSetup.get(Setting.RESOLUTION)
                            , ApplicationSetup.get(Setting.REMOTE_HUB)
                            , ApplicationSetup.get(Setting.BROWSER_PLATFORM)
                            , ApplicationSetup.get(Setting.PROXY_SETTING)
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void initializeDriver(String browserType, String browserName, String browserVersion, String resolution, String remoteHub, String browserPlatform, String proxy) throws Exception {
        if (ApplicationSetup.getBoolean(Setting.IS_INITIALIZE_DRIVER)) {
            try {
                GenericFunctions.coreInitializeDriver(browserType, browserName, browserVersion, resolution, remoteHub, browserPlatform, proxy);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public static void tearDown() throws Exception {

        try {
            GenericFunctions.coreTearDown();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public static void close() {

        try {
            SchedulerManager.updateSchedulerCompleted(ApplicationSetup.getLong(Setting.REQUEST_ID));
            if (ApplicationSetup.getBoolean(Setting.IS_INITIALIZE_DRIVER)) {
                BrowserAction.deleteAllCookies();

                if (SiteConfigurationManager.isCloseBrowserAfterFinish(true)) {
                    tearDown();
                }
            }
            Report.generate();
            Report.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//		try {
//			GenericFunctions.coreClose();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}

    }

    public static  Object javascriptExecuteScript(String data) throws Exception {
        try {
            return GenericFunctions.coreJavascriptExecuteScript(data);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void sendKeys(Keys key) throws Exception {
        try {
            GenericFunctions.coreSendKey(key);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void deleteAllCookies() throws Exception {
        try {
            GenericFunctions.coreDeleteCookies();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean ManualValidation(String message) throws Exception
    {
        return GenericFunctions.coreManualConfirmation(message);

    }

    public static String getPageHTMLSource()
    {
        return GenericFunctions.coreGetHTMLSource();
    }

}
