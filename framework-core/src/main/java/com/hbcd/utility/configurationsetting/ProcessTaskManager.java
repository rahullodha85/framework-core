package com.hbcd.utility.configurationsetting;

import com.hbcd.logging.log.Log;
import com.hbcd.utility.common.Task;
import com.hbcd.utility.helper.Common;
import com.hbcd.utility.schedulerUtility.SchedulerManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

public class ProcessTaskManager {

    private static String checkEnvironmentSetting(String env, String envSite) {
        if (env == null || env.isEmpty()) {
            if (envSite != null && !envSite.isEmpty()) {
                return envSite;
            } else {
                return "";
            }
        } else {
            return env;
        }
    }

    private static String getProcessSettingValueSiteOrModuleOrProcessDefaultOrDefaultSystem(String site, String module, String key)
    {
        String value = getProcessSettingValueSiteOrModuleOrProcessDefault(site, module, key);
        String systemDefaultValue = ConfigurationLoader.getSystemStringValue(key);
        if (Common.isNullOrEmpty(value) && Common.isNotNullAndNotEmpty(systemDefaultValue))
        {
            return systemDefaultValue;
        }
        return value;
    }

    private static String getProcessSettingValueSiteOrModuleOrProcessDefault(String site, String module, String key)
    {
        String processDefaultValue = ConfigurationLoader.getDefaultProcessValue(key);
        return getProcessSettingValueSiteOrModule(site, module, key, processDefaultValue);
    }

    private static String getProcessSettingValueSiteOrModule(String site, String module, String key, String defaultValue)
    {
        String _valReturn = defaultValue;
        String _siteValue = ConfigurationLoader.getValue(String.format("%s.%s", site, key));
        String _moduleValue = ConfigurationLoader.getValue(String.format("%s.%s.%s", site, module, key));

        if (Common.isNotNullAndNotEmpty(site)) {
            //Return Module Configuration
            if (Common.isNotNullAndNotEmpty(module))
            {
                if (Common.isNotNullAndNotEmpty(_moduleValue)) {
                    _valReturn = _moduleValue;
                }
                else if (Common.isNotNullAndNotEmpty(_siteValue))
                {
                    _valReturn = _siteValue;
                }
            }
            else  //Return Site Configuration
            {
                if (Common.isNotNullAndNotEmpty(_siteValue))
                {
                    _valReturn = _siteValue;
                }
            }
        }
        //_valReturn = ConfigurationLoader.getDefaultProcessValue("MULTI_THREAD");
        //else Return default
        return _valReturn;
    }

    public static boolean IsMultiThread(String site, String module) {
        String defaultValue = ConfigurationLoader.getSystemStringValue("MULTI_THREAD");
        if (Common.isNullOrEmpty(defaultValue)) {
            defaultValue = "FALSE";
        }
        String rtnValue = getProcessSettingValueSiteOrModule(site, module, "MULTI_THREAD", defaultValue);
        return rtnValue.toUpperCase().equals("TRUE") ? true : false;
    }

    private static String checkModuleSetting(String module) {
        if (module == null || module.isEmpty()) {
            try {
                module = ConfigurationLoader.getDefaultProcessValue("DEFAULT_MODULE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return module;
    }

    private static String checkBrowserSetting(String browser) {
        if (browser == null || browser.isEmpty()) {
            try {
                browser = ConfigurationLoader.getDefaultProcessValue("DEFAULT_BROWSER");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return browser;
    }

    private static String checkBrowserTypeSetting(String browserType) {
        if (browserType == null || browserType.isEmpty()) {
            try {
                browserType = ConfigurationLoader.getDefaultProcessValue("DEFAULT_BROWSER_TYPE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return browserType;
    }

    private static String checkRemoteHubSetting(String remoteHub) {
        if (remoteHub == null || remoteHub.isEmpty()) {
            try {
                remoteHub = ConfigurationLoader.getDefaultProcessValue("DEFAULT_REMOTE_HUB");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return remoteHub;
    }
    private static String checkResolutionSetting(String resolution) {
        if (resolution == null || resolution.isEmpty()) {
            try {
                resolution = ConfigurationLoader.getDefaultProcessValue("DEFAULT_RESOLUTION");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resolution;
    }

    private static String checkToolSetting(String tool) {
        if (tool == null || tool.isEmpty()) {
            try {
                tool = ConfigurationLoader.getDefaultProcessValue("DEFAULT_TOOL");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tool;
    }

    private static String checkEmailSetting(String email) {
        if (email == null || email.isEmpty()) {
            try {
                email = ConfigurationLoader.getDefaultProcessValue("DEFAULT_EMAIL");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return email;
    }

    private static long checkHipChatRoomSetting(String roomNumber) {
        long _hipchatNotificationRoom = 0;
        try {
            _hipchatNotificationRoom = Long.parseLong(roomNumber);
            return checkHipChatRoomSetting(_hipchatNotificationRoom);
        } catch (Exception ex) {

        }
        return _hipchatNotificationRoom;
    }

    private static long checkHipChatRoomSetting(long roomNumber) {
        long _hipchatNotificationRoom = 0;
        if (roomNumber == 0) {
            String defaultHCR = "";
            try {
                defaultHCR = ConfigurationLoader.getDefaultProcessValue("DEFAULT_HIPCHAT_ROOM");
                if (defaultHCR != null && !defaultHCR.isEmpty()) {
                    _hipchatNotificationRoom = Integer.parseInt(defaultHCR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            _hipchatNotificationRoom = roomNumber;
        }
        return _hipchatNotificationRoom;
    }

    /*
     *  determine wether to open browser for the TESTSCRIPT.   If it's the service, it's not require to open browser.
     *  Otherwise, Initialize browser should be set to true for front-end browser run.
     */
    private static boolean checkInitializeDriverSetting(String initDriver) {
        boolean isInitDriver = true;

        if (initDriver == null || initDriver.isEmpty()) {
            try {
                initDriver = ConfigurationLoader.getDefaultProcessValue("INITIALIZE_DRIVER");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (initDriver == null || initDriver.isEmpty()) {
            isInitDriver = true;
        } else {
            if (initDriver.equalsIgnoreCase("false")) {
                isInitDriver = false;
            }
        }

        return isInitDriver;
    }


    private static String checkExecutionTypeSetting(String executionType, String defaultValue) {

        if (executionType == null || executionType.isEmpty()) {
            try {
                return ConfigurationLoader.getDefaultProcessWithDefaultValue("DEFAULT_EXECUTION_TYPE", defaultValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
        {
            return "DEFAULT";
        }
        return defaultValue;
    }

    /*
     *  Default from Configuration File  "Configuration.properties"
     */
    public static Task get(List<String> moduleRun) {
        Task sc = new Task();
        String _site = moduleRun.get(0).trim();
        String _moduleName = moduleRun.get(1).trim();

        sc.setSite(_site);
        sc.setModuleName(_moduleName);

        String _browserType = getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "DEFAULT_BROWSER_TYPE");
        sc.setBrowserType(_browserType);

//        String _browser = ConfigurationLoader.getValue(_site + "." + "DEFAULT_BROWSER");
        String _browser = getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "DEFAULT_BROWSER");
        sc.setBrowserName(checkBrowserSetting(_browser));

        String _browserVersion = getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "DEFAULT_BROWSER_VERSION");
        sc.setBrowserVersion(_browserVersion);

        String _resolution = getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "DEFAULT_RESOLUTION");
        sc.setResolution(_resolution);

        String _remoteHub = getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "DEFAULT_REMOTE_HUB");
        sc.setRemoteHub(_remoteHub);

        String _platform = getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "DEFAULT_PLATFORM");
        sc.setBrowserPlatform(_platform);

        String _filterTestCase = getProcessSettingValueSiteOrModule(_site, _moduleName, "DEFAULT_FILTER_TEST_CASE", "");
        sc.setFilterTestCase(_filterTestCase);

        String _parallelExec = getProcessSettingValueSiteOrModuleOrProcessDefaultOrDefaultSystem(_site, _moduleName, "NUMBER_OF_THREAD");
        int parallel = 1;
        try
        {
            parallel = Integer.parseInt(_parallelExec);
        }
        catch (Exception ex)
        {
            parallel = 1;
        }
        sc.setParallelExecution(parallel);

        String _proxy = getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "DEFAULT_BROWSER_PROXY_SETTING");
        sc.setProxySetting(_proxy);

//        String _tool = ConfigurationLoader.getValue(_site + "." + "DEFAULT_TOOL");
        String _tool = getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "DEFAULT_TOOL");
        sc.setDriverType(checkToolSetting(_tool));

//        String hcRoomNumber = ConfigurationLoader.getValue(_site + "." + "NOTIFICATION_HIPCHAT_ROOM");
        String _hcRoomNumber = getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "NOTIFICATION_HIPCHAT_ROOM");
        sc.setHipchatRoomNumber(checkHipChatRoomSetting(_hcRoomNumber));

        String _email = ConfigurationLoader.getValue(String.format("%s.DEFAULT_EMAIL", _site));
        getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "DEFAULT_EMAIL");
        sc.setEmail(checkEmailSetting(_email));

//        String _initDriver = ConfigurationLoader.getValue(_site + "." + "INITIALIZE_DRIVER");
        String _initDriver = getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "INITIALIZE_DRIVER");
        sc.setIsInitializeDriver(checkInitializeDriverSetting(_initDriver));

//        String _moduleExecutionType = ConfigurationLoader.getValue(_site + "." + _moduleName + "." + "DEFAULT_EXECUTION_TYPE");
//        String _siteExecutionType = ConfigurationLoader.getValue(_site + "." + "DEFAULT_EXECUTION_TYPE");
//        sc.setExecutionType(checkExecutionTypeSetting(_moduleExecutionType, _siteExecutionType, "DEFAULT"));
        String _siteExecutionType = getProcessSettingValueSiteOrModuleOrProcessDefault(_site, _moduleName, "DEFAULT_EXECUTION_TYPE");
        sc.setExecutionType(checkExecutionTypeSetting(_siteExecutionType, "DEFAULT"));

        String _siteEnvironmentURL = ConfigurationLoader.getValue(String.format("%s.URL", _site));
        sc.setEnvironmentURL(checkEnvironmentSetting(_siteEnvironmentURL, _siteEnvironmentURL));

        if (ConfigurationManager.IsReportingToDB()) {
            //generate ExecuteScheduler record and populate Request ID
            sc.setRequestId(SchedulerManager.insertDebugScheduler());
        }
        return sc;

    }

    /* NOT USE FOR NOW */
    public static Task get(HashMap<String, Object> task) throws Exception {
        Task tsk = new Task();

        long _reqId = (long) task.get("ReqID");
        Log.Info(String.format("Start Execute Request #: %s", _reqId));
        System.out.println(String.format("Start Execute Request #: %s", _reqId));
        long _moduleId = (long) task.get("ModuleID");
        String _moduleName = (String) task.get("ModuleName");
        int _statusId = (int) task.get("StatusID");
        String _statusDesc = (String) task.get("StatusDesc");
        String _siteName = (String) task.get("SiteName");
        String _environmentURL = (String) task.get("EnvironmentURL");
        String _tool = (String) task.get("Tool");
        String _browsertype = (String) task.get("BrowserType");
        long _hipchatRoomNumberNotification = (long) task.get("HipChatNotificationRoom");
        String _emailNotification = (String) task.get("emailNotification");
        long _reRun_ReqId = (long) task.get("ReRunReqId");

        String _initDriver = ConfigurationLoader.getValue(String.format("%s.INITIALIZE_DRIVER", _siteName));
        String _siteEnvironmentURL = ConfigurationLoader.getValue(String.format("%s.URL", _siteName));
        String _executionType = ConfigurationLoader.getValueWithDefault(String.format("%s.DEFAULT_EXECUTION_TYPE", _siteName), "DEFAULT");

        //new Task(_reqId, _siteName, _moduleId, _moduleName.toUpperCase(), _environmentURL, _tool, _browsertype, "", "", isInitDriver, _hipchatRoomNumberNotification, _emailNotification)
        tsk.setRequestId(_reqId);
        tsk.setSite(_siteName);
        tsk.setModuleId(_moduleId);
        tsk.setModuleName(_moduleName.toUpperCase());
        tsk.setExecutionStatus(_statusId);
        tsk.setEnvironmentURL(checkEnvironmentSetting(_environmentURL, _siteEnvironmentURL));
        tsk.setDriverType(checkToolSetting(_tool));
        tsk.setBrowserType(checkBrowserSetting(_browsertype));
        tsk.setIsInitializeDriver(checkInitializeDriverSetting(_initDriver));
        tsk.setHipchatRoomNumber(checkHipChatRoomSetting(_hipchatRoomNumberNotification));
        tsk.setEmail(checkEmailSetting(_emailNotification));
        tsk.setExecutionType(_executionType);
        tsk.setReRunRequestId(_reRun_ReqId);

        return tsk;

    }

    /* Load from DB Record */
    public static List<Task> get(ResultSet rs) throws Exception {
        List<Task> tskList = new ArrayList<Task>();

        while(rs.next()) {
            Task tsk = new Task();
            String _siteName = rs.getString("SiteName");
            String _initDriver = ConfigurationLoader.getValue(String.format("%s.INITIALIZE_DRIVER", _siteName));
            String _siteEnvironmentURL = ConfigurationLoader.getValue(String.format("%s.URL", _siteName));
            String _executionType = ConfigurationLoader.getValueWithDefault(String.format("%s.DEFAULT_EXECUTION_TYPE", _siteName), "DEFAULT");
            String browserType = (rs.getString("BrowserType") == null)? "" : rs.getString("BrowserType");
            String browserName = (rs.getString("BrowserName") == null)? "" : rs.getString("BrowserName");
            String resolution = (rs.getString("Resolution") == null)? "" : rs.getString("Resolution");
            String remoteHub = (rs.getString("RemoteHub") == null)? "" : rs.getString("RemoteHub");
            String tool = (rs.getString("Tool") == null)? "" : rs.getString("Tool");
            int parallel_exec = rs.getInt("ParallelExecution");
            String filterExpression =  (rs.getString("FilterTestCase") == null)? "" : rs.getString("FilterTestCase");

            tsk.setRequestId(rs.getLong("ReqID"));
            tsk.setSite(_siteName);
            tsk.setModuleId(rs.getLong("ModuleId"));
            tsk.setModuleName(rs.getString("ModuleName").toUpperCase());
            tsk.setExecutionStatus(rs.getInt("StatusId"));
            tsk.setEnvironmentURL(checkEnvironmentSetting(rs.getString("EnvironmentURL"), _siteEnvironmentURL));
            tsk.setDriverType(checkToolSetting(rs.getString("Tool")));
            tsk.setBrowserType(checkBrowserTypeSetting(browserType));
            tsk.setBrowserName(checkBrowserSetting(browserName));
            tsk.setRemoteHub(remoteHub);
            tsk.setResolution(resolution);
            tsk.setParallelExecution(parallel_exec);
            tsk.setFilterTestCase(filterExpression);
            tsk.setIsInitializeDriver(checkInitializeDriverSetting(_initDriver));
            tsk.setHipchatRoomNumber(checkHipChatRoomSetting(rs.getLong("HipChatNotificationRoom")));
            tsk.setEmail(checkEmailSetting(rs.getString("emailNotification")));
            tsk.setReRunRequestId(rs.getLong("ReRunReqId"));
            tsk.setExecutionType(_executionType);
            tskList.add(tsk);
        }

        return tskList;

    }
}
