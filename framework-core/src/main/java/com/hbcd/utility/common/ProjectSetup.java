package com.hbcd.utility.common;

import com.hbcd.utility.configurationsetting.ApplicationGenericLoad;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.configurationsetting.ProcessTaskManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectSetup {
    public static Task GetDefaultSetup() {
        Task _objSetupConfig = new Task();
        List<String> listDefaultProcess = new ArrayList<String>();
        String dp = ConfigurationLoader.getSystemStringValue("RUN_DEFAULT_PROCESS_SITE");
        if (dp != null) {
            String newDp = dp.trim();
            if (newDp.length() > 0) {
                listDefaultProcess = new ArrayList<String>(Arrays.asList(dp.split("\\|")));
            }
        }

        if (listDefaultProcess.size() == 1) {
            for (String _siteModule : listDefaultProcess) {
                List<String> siteModule = Arrays.asList(_siteModule.split("\\."));
                if (siteModule.size() != 2) {
                    return null;
                }

                //Init Driver set to true
                _objSetupConfig = ProcessTaskManager.get(siteModule); // new Task(_site, _moduleName, _tool, _browser, "", "", true, _hipchatNotificationRoom, _email);
            }
        } else {
            System.out.println("PLEASE SETUP Configuration.Properties with ONE Project ONLY.");
        }
        return _objSetupConfig;
    }

    public static void Setup(Task sco) {
        try {
            if (sco == null) {
                sco = GetDefaultSetup();
                if (sco == null) {
                    throw new Exception("Error: Configuration.properties does not setup correctly.");
                }
            }
            ApplicationSetup.set(Setting.CONFIGURATION_SETUP_OBJECT, sco);
            ApplicationSetup.set(Setting.REQUEST_ID, sco.getRequestId());
            ApplicationSetup.set(Setting.SITE, sco.getSite());
            ApplicationSetup.set(Setting.MODULE_ID, sco.getModuleId());
            ApplicationSetup.set(Setting.MODULE_NAME, sco.getModuleName());
            ApplicationSetup.set(Setting.DEFAULT_RUN_DIR, sco.getDefaultDir());
            ApplicationSetup.set(Setting.DRIVER, sco.getDriver());
            ApplicationSetup.set(Setting.BROWSER_TYPE, sco.getBrowserType());
            ApplicationSetup.set(Setting.BROWSER_NAME, sco.getBrowserName());
            ApplicationSetup.set(Setting.BROWSER_VERSION, sco.getBrowserVersion());
            ApplicationSetup.set(Setting.RESOLUTION, sco.getResolution());
            ApplicationSetup.set(Setting.REMOTE_HUB, sco.getRemoteHub());
            ApplicationSetup.set(Setting.BROWSER_PLATFORM, sco.getBrowserPlatform());
            ApplicationSetup.set(Setting.PROXY_SETTING, sco.getProxySetting());
            ApplicationSetup.set(Setting.SYSTEM, "SYSTEM");
            ApplicationSetup.set(Setting.IS_INITIALIZE_DRIVER, sco.getIsInitializeDriver());
            ApplicationSetup.set(Setting.ENVIRONMENT_URL, sco.getEnvironmentURL());
            ApplicationSetup.set(Setting.HIPCHAT_NOTIFICATION, sco.getHipChatRoomNumber());
            ApplicationSetup.set(Setting.EMAIL_NOTIFICATION, sco.getEmail());
            ApplicationSetup.set(Setting.EXECUTION_TYPE, sco.getExecutionType());
            ApplicationSetup.set(Setting.PARALLEL_EXECUTION, sco.getParallelExecution());
            ApplicationSetup.set(Setting.FILTER_TEST_CASE, sco.getFilterTestCase());
            ApplicationSetup.setupCompleted();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static String getSite() {
        try {
            ApplicationSetup.get(Setting.SITE);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "";
    }


}
