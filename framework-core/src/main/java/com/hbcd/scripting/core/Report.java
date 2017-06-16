package com.hbcd.scripting.core;


import com.hbcd.common.utility.Common;
import com.hbcd.container.common.SafeDriver;
import com.hbcd.logging.log.Log;
import com.hbcd.reporting.controllers.SafeReport;
import com.hbcd.scripting.exception.DataException;
import com.hbcd.scripting.exception.ScriptErrorException;
import com.hbcd.scripting.exception.ScriptFailException;
import com.hbcd.scripting.exception.SystemException;
import com.hbcd.utility.common.ApplicationSetup;
import com.hbcd.utility.common.Setting;
import com.hbcd.utility.testscriptdata.TestCaseResult;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class Report {

    public static TestCaseResult generate() throws Exception {
        if (ApplicationSetup.isSetup()) {
            return SafeReport.Current().generateReport(ApplicationSetup.getLong(Setting.REQUEST_ID));
        }
        return null;
    }

    public static TestCaseResult generate(long reqId, int executionOrder, long totalTime) throws Exception {
        return SafeReport.Current().generateReport(reqId, executionOrder, totalTime);
    }

    public static void init() throws Exception {
        SafeReport.Current().init();
    }

    private static String getScenarioName()
    {
        return (Storage.get("CURRENT_SCENARIO_NAME")==null)?"":Storage.get("CURRENT_SCENARIO_NAME").toString();
    }
    public static void close() throws Exception {
        SafeReport.Current().close();
    }

    public static void fail(String message, String screenShot) throws Exception {
        SafeReport.Current().createStep(message, "fail", screenShot);
        throw new ScriptFailException("Script Fail.");
    }

    public static void fail(String message) throws Exception
    {
        SafeReport.Current().createStep(message, "fail", BrowserAction.screenshot());
        throw new ScriptFailException("Script Fail.");
    }

    public static void error(String message, String screenShot) throws Exception {
        SafeReport.Current().createStep(message, "fail_error", screenShot);
        throw new ScriptErrorException("Script Error.");
    }


    public static void dataFail(String message, String screenShot) throws Exception {
        SafeReport.Current().createStep(message, "fail_data", screenShot);
        throw new DataException("Data Error.");
    }

    public static void dataFail(String message) throws Exception {
        SafeReport.Current().createStep(message, "fail_data", BrowserAction.screenshot());
        throw new DataException("Data Error.");
    }

    public static void pass(String message, String screenShot) throws Exception {
        SafeReport.Current().createStep(message, "pass", screenShot);
        Log.Info(String.format("[%s] : %s", getScenarioName(), message));
    }

    public static void pass(String action, String message, String screenShot) throws Exception {
        SafeReport.Current().createStep(action, message, "pass", screenShot);
        Log.Info(String.format("[%s] : Successfully Executed %s", getScenarioName(), action));
    }
    public static void pass(String message) throws Exception
    {
        SafeReport.Current().createStep(message, "pass", BrowserAction.screenshot());
        Log.Info(String.format("[%s] : %s", getScenarioName(), message));
    }

    public static void log(String action, String message, String status, String screenShot) throws Exception {
        status = (status.equalsIgnoreCase("PASS"))? "pass" : "fail";
        SafeReport.Current().createStep(action, message, status, screenShot);
        Log.Info(String.format("[%s] : %s - %s", getScenarioName(), action, message));
    }

    public static void log(String action, String message1, String message2, String status, String screenShot) throws Exception {
        status = (status.equalsIgnoreCase("PASS"))? "pass" : "fail";
        SafeReport.Current().createStep(action, message1, message2, status, screenShot);
        Log.Info(String.format("[%s] : %s - %s", getScenarioName(), action, message1));
    }

    public static void log(String action, String description, String message1, String message2, String status, String screenShot) throws Exception {
        status = (status.equalsIgnoreCase("PASS"))? "pass" : "fail";
        SafeReport.Current().createStep(0, action, description, message1, message2, status, screenShot);
        Log.Info(String.format("[%s] : %s - %s", getScenarioName(), action, message1));
    }

    public static void info(String message) throws Exception
    {
        SafeReport.Current().createStep(message, "log_info", BrowserAction.screenshot());
        Log.Info(String.format("[%s] : %s", getScenarioName(), message));
    }

    public static void performance(String action, String pageName, String description, String xml, HashMap<String, Long> data) throws Exception {
        SafeReport.Current().createPerformanceStep(action, pageName, description, xml, data);
    }

    public static void end_script(String message, String screenShot) throws Exception {
        SafeReport.Current().createStep(message, "pass_end", screenShot);
        Log.Info(String.format("[%s] : THE SCRIPT IS SUCCESSFULLY EXECUTED", getScenarioName()));
    }

    public static void systemFail(String message, String screenShot) throws Exception {
        SafeReport.Current().createStep(message, "fail_system", screenShot);
        throw new SystemException("System Fail.");
    }

    public static void errorAnalysis(Exception e) {
        if (e == null) return;
        Log.Info("<< START EXCEPTION ERROR-ANALYSIS >>");
        //Analyze Exception.
        //Null Exception
        //
    }

    public static boolean errorAnalysis(boolean isAnalyze, String site)
    {

        boolean _statusFoundCause = false;

        if (!isAnalyze) return false;
        Log.Info("<< START SCREEN TEXT ERROR-ANALYSIS >>");

        String htmlSource = SafeDriver.Current().fw_getPageSource();
        String pageUrl = SafeDriver.Current().fw_getCurrentUrl();

        String _fileName = "FailAnalysis.properties";
        //Analyze Screen Text
        Properties _pro = new Properties();
        FileInputStream in = null;
        try {
            String fullPathFileName = new Common().getFullPathFileName(_fileName);
            if (fullPathFileName != null) {
                _pro.load(new FileInputStream(fullPathFileName));
            } else {
                _pro.load(Report.class.getClassLoader().getResourceAsStream(_fileName));
            }
//            Enumeration em = _pro.keys();
//            while(em.hasMoreElements()){
//                String key = (String)em.nextElement();
            for (Enumeration e = _pro.propertyNames(); e.hasMoreElements(); ) {
                String msg = "";

                String key = (String) e.nextElement();
                String value = _pro.getProperty(key);
                if (key.startsWith(site) || key.startsWith("ALL_SITES")){
                    //System.out.println(key + ": " + _pro.get(key));
                    if (com.hbcd.utility.helper.Common.isNotNullAndNotEmpty(value) && htmlSource.contains(value)) {
                        try {
                            if (key.contains("SYSTEM_FAIL")) {
                                msg = "SYSTEM IS DOWN.";
                                Report.systemFail(String.format("System is DOWN [URL: %s]", pageUrl), "");
                            } else if (key.contains("DATA_FAIL")) {
                                msg = "Data Fail.";
                                Report.dataFail(String.format("DATA FAILED [URL: %s]", pageUrl), "");
                            }
                            _statusFoundCause = true;
                        } catch (Exception ex) {
                            Log.Info(msg);
                        }
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        try {
//            String htmlSource = SafeDriver.Current().fw_getPageSource();
//            String pageUrl = SafeDriver.Current().fw_getCurrentUrl();
//            if (htmlSource.contains("DataService Temporarily Unavailable")) {
//                try {
//                    Report.systemFail("System is DOWN [URL: " +  pageUrl + "]", "");
//                    _statusFoundCause = true;
//                } catch (Exception ex) { Log.Info("SYSTEM IS DOWN."); }
//            }
//        } catch (Exception ex)
//        {
//            Log.Error("Error", ex);
//            Log.Info("SYSTEM IS DOWN.");
//        }
        return _statusFoundCause;
    }
}
