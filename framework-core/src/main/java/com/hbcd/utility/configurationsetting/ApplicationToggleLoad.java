package com.hbcd.utility.configurationsetting;

import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.helper.Common;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ApplicationToggleLoad {

    private static String validationFileName = "ValidationToggle.properties";
    private static String perfFileName = "PerformanceToggle.properties";
    private static Properties _valProp = new Properties();
    private static Properties _perfProp = new Properties();
    private static boolean _isValLoaded = false;
    private static boolean _isPerfLoaded = false;
    private static Lock _lockme = new ReentrantLock();

    private static Properties Current() throws Exception {
        _lockme.lock();
        try {
            if (!_isValLoaded) {
                String fullPathFileName = new com.hbcd.common.utility.Common().getFullPathFileName(validationFileName);
                if (fullPathFileName != null) {
                    _valProp.load(new FileInputStream(fullPathFileName));
                } else {
                    _valProp.load(ApplicationToggleLoad.class.getClassLoader().getResourceAsStream(validationFileName));
                }
                _isValLoaded = true;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            _lockme.unlock();
        }
        return _valProp;
    }

    private static Properties CurrentPerf() throws Exception {
        _lockme.lock();
        try {
            if (!_isPerfLoaded) {
                String fullPathFileName = new com.hbcd.common.utility.Common().getFullPathFileName(perfFileName);
                if (fullPathFileName != null) {
                    _perfProp.load(new FileInputStream(fullPathFileName));
                } else {
                    _perfProp.load(ApplicationToggleLoad.class.getClassLoader().getResourceAsStream(perfFileName));
                }
                _isPerfLoaded = true;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            _lockme.unlock();
        }
        return _perfProp;
    }

    public static boolean getSiteToggle(String toggleType, String PageName) throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }
        String valueReturn = "";
        if (toggleType.toUpperCase().equals("VALIDATION")) {
            valueReturn = Current().getProperty(String.format("%s.%s", ApplicationSetup.get(Setting.SITE), PageName));
        } else if (toggleType.toUpperCase().equals("PERFORMANCE")) {
            valueReturn = CurrentPerf().getProperty(String.format("%s.%s", ApplicationSetup.get(Setting.SITE), PageName));
        }
        if ((valueReturn == null) || valueReturn.trim().isEmpty()) {
            return true;  //Default True
        }
        return valueReturn.trim().equalsIgnoreCase("true") ? true : false;
    }

    public static boolean getModuleToggle(String toggleType, String PageName) throws Exception {
        if (ApplicationSetup.get(Setting.SITE).trim().length() == 0) {
            throw new Exception("SITE HAS NOT BEEN SETUP");
        }

        boolean returnStatus = true;  //Default Validate
        String valueAllPageInSite = "";
        String ValueAllPageInModule = "";
        String valueSpecificPageInSite = "";
        String valueSpecificPageInModule = "";
        if (toggleType.toUpperCase().equals("VALIDATION")) {
            returnStatus = true;  //Default Validate
            valueAllPageInSite = Current().getProperty(ApplicationSetup.get(Setting.SITE));
            ValueAllPageInModule = Current().getProperty(String.format("%s.%s", ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME)));
            valueSpecificPageInSite = Current().getProperty(String.format("%s.%s", ApplicationSetup.get(Setting.SITE), PageName));
            valueSpecificPageInModule = Current().getProperty(String.format("%s.%s.%s", ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME), PageName));
        } else if (toggleType.toUpperCase().equals("PERFORMANCE")) {
            returnStatus = true;  //Default Performance
            valueAllPageInSite = CurrentPerf().getProperty(ApplicationSetup.get(Setting.SITE));
            ValueAllPageInModule = CurrentPerf().getProperty(String.format("%s.%s", ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME)));
            valueSpecificPageInSite = CurrentPerf().getProperty(String.format("%s.%s", ApplicationSetup.get(Setting.SITE), PageName));
            valueSpecificPageInModule = CurrentPerf().getProperty(String.format("%s.%s.%s", ApplicationSetup.get(Setting.SITE), ApplicationSetup.get(Setting.MODULE_NAME), PageName));
        }

        if ((valueSpecificPageInModule == null) || valueSpecificPageInModule.trim().isEmpty())  //No Site
        {
            //If Site is Set?
            if ((valueSpecificPageInSite == null) || valueSpecificPageInSite.trim().isEmpty())  //No Site
            {
                if ((ValueAllPageInModule == null) || ValueAllPageInModule.trim().isEmpty())  //No Site
                {
                    if ((valueAllPageInSite == null) || valueAllPageInSite.trim().isEmpty())  //No Site
                    {
                        //Default returnStatus;
                    } else {
                        returnStatus = valueAllPageInSite.trim().equalsIgnoreCase("true") ? true : false;
                    }
                } else {
                    returnStatus = ValueAllPageInModule.trim().equalsIgnoreCase("true") ? true : false;
                }
            } else {
                returnStatus = valueSpecificPageInSite.trim().equalsIgnoreCase("true") ? true : false;
            }
        } else //Module is Set Take Module as Priority
        {
            returnStatus = valueSpecificPageInModule.trim().equalsIgnoreCase("true") ? true : false;
        }
        return returnStatus;
    }

}
