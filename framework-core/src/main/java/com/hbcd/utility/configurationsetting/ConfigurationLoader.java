package com.hbcd.utility.configurationsetting;

import com.hbcd.common.utility.Common;
import com.hbcd.logging.log.Log;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.common.SystemToggleType;

import java.io.FileInputStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ConfigurationLoader {
    private static final Properties proprs = new Properties();
    private static String configureFileName = "Configuration.properties";
    private static boolean _isloaded = false;
    private static Lock _lockme = new ReentrantLock();
    private static long _counter = 0;
    private static long _TotalNanoSecond = 0;
    private static BasicFileAttributes attrb = null;

    private static final List<String> systemRequireConfigurationList = new ArrayList<String>(Arrays.asList("SYSTEM.RUN_AS=", "SYSTEM.MULTI_THREAD=", "SYSTEM.NUMBER_OF_THREAD=", "SYSTEM.REPORT_OUTPUT=", "SYSTEM.EXCEL_DATA_LOADER=", "SYSTEM.RUN_DEFAULT_PROCESS_SITE="));;

    private static void overrideSystemValue()
    {
        if (proprs == null) return;
        for (String name : proprs.stringPropertyNames())
        {
            if ((System.getProperty(name) != null) && (!System.getProperty(name).trim().isEmpty()))
            {
                String value = System.getProperty(name);
                Log.Info(String.format("Override Configure.properties %s with System.getProperty(%s) - new Value is %s", name, name, value));
                proprs.setProperty(name, value);
            }
        }
    }
    private static synchronized Properties Current() {
        try {
            _counter++;
            if (!_isloaded) {
                String fullPathFileName = new Common().getFullPathFileName(configureFileName);
                if (fullPathFileName != null) {
                    proprs.load(new FileInputStream(fullPathFileName));
                    Log.Info(String.format("Load Configuration file from : %s", fullPathFileName));
                } else {
                    if (ConfigurationLoader.class.getClassLoader().getResource(configureFileName)!=null) {
                        proprs.load(ConfigurationLoader.class.getClassLoader().getResourceAsStream(configureFileName));
                        Log.Info("Load Configuration file from resource.");
                    }
                    else
                    {
                        Log.Info("Unable to load Configuration file.");
                    }
                }

                //Override System Environment Properties
                ConfigurationLoader.overrideSystemValue();
                _isloaded = true;
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot find configuration file: %s\nCause: %s", e.getMessage(), e.getCause()));
        }
        return proprs;
    }

    public static String getValue(String s) {
        Instant _start = Instant.now();
        String rtn = Current().getProperty(s);
        if (rtn == null) rtn = "";
        _TotalNanoSecond += Duration.between(_start, Instant.now()).toNanos();
        return rtn;
    }

    public static String getValueWithDefault(String s, String defaultValue) {
        Instant _start = Instant.now();
        String rtn = Current().getProperty(s);
        if ((rtn == null) || rtn.isEmpty()) { rtn = defaultValue; }
        _TotalNanoSecond += Duration.between(_start, Instant.now()).toNanos();
        return rtn;
    }

    public static String getSiteValue(String s) {
        return getValue(s);
    }

    public static String getSystemStringValue(String s) {
        try {
            return getValue(String.format("SYSTEM.%s", s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean getSystemBooleanValue(String s) {
        try {
            String rtrn = getValue(String.format("SYSTEM.%s", s));
            if (rtrn.equalsIgnoreCase("TRUE")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDefaultProcessValue(String s) {
        return getValue(String.format("DEFAULT_PROCESS.%s", s));
    }

    public static String getDefaultProcessWithDefaultValue(String s, String defaultValue) {
        String rtrn = getValue(String.format("DEFAULT_PROCESS.%s", s));
        if ((rtrn == null) || rtrn.isEmpty()) {
            rtrn = defaultValue;
        }
        return rtrn;
    }

    public static String getWebSiteURL(String s) throws Exception {
        String _EnvironmentURL = ApplicationSetup.get(Setting.ENVIRONMENT_URL);
        if ((_EnvironmentURL == null) || (_EnvironmentURL.isEmpty())) {
            return getValue(String.format("%s.URL",s));
        } else {
            return _EnvironmentURL;
        }
    }

    public static String getSiteRepository(String s) {
        return getValue(String.format("%s.REPOSITORY_PROPERTIES_FILE", s));
    }

    public static String getSiteExcelRepository(String s) {
        return getValue(String.format("%s.REPOSITORY_EXCEL_FILE", s));
    }

    public static String getSiteXmlRepository(String s) {
        return getValue(String.format("%s.REPOSITORY_XML_FILE", s));
    }

    public static String getExcelTestSuite(String s) {
        return getValue(String.format("%s.TEST_SUITE_EXCEL_FILE", s));
    }

    public static String getExcelTestData(String s) {
        return getValue(String.format("%s.TEST_DATA_EXCEL_FILE", s));
    }

    public static String getXmlTestData(String s) {
        return getValue(String.format("%s.TEST_DATA_XML_FILE", s));
    }

    public static String getWebSiteURL() throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }
        return getValue(String.format("%s.URL", ApplicationSetup.get(Setting.SITE)));
    }

    public static String getRepository() throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }
        return getValue(String.format("%s.REPOSITORY_PROPERTIES_FILE", ApplicationSetup.get(Setting.SITE)));
    }

    public static String getExcelRepository() throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }
        return getValue(String.format("%s.REPOSITORY_EXCEL_FILE", ApplicationSetup.get(Setting.SITE)));
    }

    public static String getXmlRepositoryExcel() throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }
        return getValue(String.format("%s.REPOSITORY_XML_FILE", ApplicationSetup.get(Setting.SITE)));
    }

    public static String getExcelTestSuite() throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }
        return getValue(String.format("%s.TEST_SUITE_EXCEL_FILE", ApplicationSetup.get(Setting.SITE)));
    }

    public static String getTestData() throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }
        return getValue(String.format("%s.TEST_DATA_EXCEL_FILE", ApplicationSetup.get(Setting.SITE)));
    }

    public static String getXmlTestData() throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }
        return getValue(String.format("%s.TEST_DATA_XML_FILE", ApplicationSetup.get(Setting.SITE)));
    }

    public static String getConnectionURL() throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("CONNECTIONURL HAS NOT BEEN SETUP");
        }
        return getValue(String.format("%s.DBCONNECTIONURL",ApplicationSetup.get(Setting.SITE)));
    }

    public static String getDBUser() throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("DB USER HAS NOT BEEN SETUP");
        }
        return getValue(String.format("%s.%s", ApplicationSetup.get(Setting.SITE), "DBUSER"));
    }

    public static String getDBPassword() throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("DB PASSWORD HAS NOT BEEN SETUP");
        }
        return getValue(String.format("%s.DBPASSWORD", ApplicationSetup.get(Setting.SITE)));
    }

    public static void reloadConfig() {
        _isloaded = false;
    }

    public static void NumberOfExecution() {
        Log.Info(String.format("[Configuration.Properties] file has been accessed %d times which took %d ns.", _counter, _TotalNanoSecond));
    }

    public static boolean getModuleToggle(String Name) throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }

        boolean returnStatus = true;  //Default Validate
        String valueInSite = "";
        String valueInModule = "";

        returnStatus = true;  //Default Validate
        valueInSite = Current().getProperty(String.format("%s.%s", ApplicationSetup.get(Setting.SITE), Name));
        valueInModule = Current().getProperty(String.format("%s.%s.%s", ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME), Name));

        if (com.hbcd.utility.helper.Common.isNullOrEmpty(valueInModule))  //No Site
        {
            if (com.hbcd.utility.helper.Common.isNullOrEmpty(valueInSite))  //No Site
            {
                //Default returnStatus;
            } else {
                returnStatus = valueInSite.trim().equalsIgnoreCase("true") ? true : false;
            }
        } else {
            returnStatus = valueInModule.trim().equalsIgnoreCase("true") ? true : false;
        }

        return returnStatus;
    }

    public static String getConfiguration(String Name) throws Exception
    {
        return getConfiguration(Name, "");
    }

    public static String getConfiguration(String Name, String defaultValue) throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }

        String returnStatus = "";
        String valueInSystem = "";
        String valueInProcess = "";
        String valueInSite = "";
        String valueInModule = "";

        returnStatus   = defaultValue;  //Default Validate
        valueInSystem  = getSystemStringValue(Name);
        valueInProcess = getDefaultProcessWithDefaultValue(Name, defaultValue);
        valueInSite    =  Current().getProperty(String.format("%s.%s", ApplicationSetup.get(Setting.SITE), Name));
        valueInModule  = Current().getProperty(String.format("%s.%s.%s", ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME), Name));

        if (com.hbcd.utility.helper.Common.isNullOrEmpty(valueInModule))  //No Module
        {
            if (com.hbcd.utility.helper.Common.isNullOrEmpty(valueInSite)) //No Site
            {
                if (com.hbcd.utility.helper.Common.isNullOrEmpty(valueInProcess)) //No Process
                {
                    if (com.hbcd.utility.helper.Common.isNullOrEmpty(valueInSystem)) //No System
                    {
                        //Default Value
                        returnStatus = defaultValue;
                    }
                    else
                    {
                        //System
                        returnStatus = valueInSystem;
                    }
                }
                else {
                    //Process
                    returnStatus = valueInProcess;
                }
            } else {
                //Site
                returnStatus = valueInSite;
            }
        } else {
            //Module
            returnStatus = valueInModule;
        }

        return returnStatus;
    }

    public static String getDynamicDataUrl(){
        return getValue(String.format("%s.DD_URL", ApplicationSetup.get(Setting.SITE)));
    }
    public static String getDynamicDataUsername(){
        return getValue(String.format("%s.DD_USERNAME", ApplicationSetup.get(Setting.SITE)));
    }
    public static String getDynamicDataPassword(){
        return getValue(String.format("%s.DD_PASSWORD", ApplicationSetup.get(Setting.SITE)));
    }

    public static String getMongoUrl(){
        return getValue(String.format("%s.mongo_url", ApplicationSetup.get(Setting.SITE)));
    }
    public static String getMongoPort() {
        return getValue(String.format("%s.mongo_port", ApplicationSetup.get(Setting.SITE)));
    }
    public static String getMongoUsername(){
        return getValue(String.format("%s.mongo_username", ApplicationSetup.get(Setting.SITE)));
    }
    public static String getMongoPassword(){
        return getValue(String.format("%s.mongo_password", ApplicationSetup.get(Setting.SITE)));
    }
    public static String getMongoDB() {
        return getValue(String.format("%s.mongo_db", ApplicationSetup.get(Setting.SITE)));
    }
}
