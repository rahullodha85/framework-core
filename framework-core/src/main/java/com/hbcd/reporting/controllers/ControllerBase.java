package com.hbcd.reporting.controllers;

import com.hbcd.logging.log.Log;
import com.hbcd.logging.log.LogReport;
import com.hbcd.reporting.constants.StepConstant;
import com.hbcd.reporting.impl.CaseRecordImpl;
import com.hbcd.reporting.impl.ModuleRecordImpl;
import com.hbcd.reporting.impl.StepRecordImpl;
import com.hbcd.storage.report.SafeReportStorage;
import com.hbcd.storage.data.SafeStorage;
import com.hbcd.utility.configurationsetting.ConfigurationLoader;
import com.hbcd.utility.testscriptdata.TestCaseResult;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class ControllerBase implements Controller {

    protected String _ScenarioName = "";

    @Override
    public int storeModule(ModuleRecordImpl moduleInpu) throws Exception {
        return 0;
    }

    @Override
    public int storeCase(CaseRecordImpl caseInput) throws Exception {
        return 0;
    }

    @Override
    public void updateCase(long caseId, String status) throws Exception {

    }

    @Override
    public void storeStep(StepRecordImpl stepInput) throws Exception {

    }

    public void storeStep(long cId, int sId, StepRecordImpl stepInput)
            throws Exception {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void init() throws Exception {
//		List<StepRecordImpl> stepList = new ArrayList<StepRecordImpl>();
//		SafeInternalStorage.Current().save("threadScenarioStepList", stepList);

        SafeReportStorage.Clear();
    }

    @Override
    public void openConnection() throws Exception {

    }


    @Override
    public void closeConnection() throws Exception {

    }

    @Override
    public TestCaseResult generateReport(long requestId) throws Exception {
        return generateReport(requestId, 0);
    }

    @Override
    public TestCaseResult generateReport(long requestId, long time) throws Exception {
        return generateReport(requestId, 0, time);
    }

    @Override
    public TestCaseResult generateReport(long requestId, int caseOrder, long time) throws Exception {

        Instant _start;
        Instant _end;

        long _caseId = 0L;

        _start = Instant.now();
        TestCaseResult tcr = new TestCaseResult(_caseId, "FAIL");
        CaseRecordImpl caseRow = null;
        try {
            LogReport.Info(String.format("#========================   START R E P O R T   %s===========================#", _ScenarioName));

            _ScenarioName = String.format("[Report for %s]", ((SafeStorage.Current().get("CURRENT_SCENARIO_NAME") == null) ? "" : SafeStorage.Current().get("CURRENT_SCENARIO_NAME").toString()));

            //openConnection();

            //tcr = new TestCaseResult(_caseId, "FAIL");
            caseRow = new CaseRecordImpl();

            caseRow.setReqId(requestId);
            caseRow.setExecutionOrder(caseOrder);
            caseRow.setScenarioName(_ScenarioName); // need classname (scen01)
            caseRow.setScenarioDescription((SafeStorage.Current().get("CURRENT_SCENARIO_DESC") == null) ? "" : SafeStorage.Current().get("CURRENT_SCENARIO_DESC").toString());
            caseRow.setStatus("");
            caseRow.setTotalExecutionTime(time);
            caseRow.setHTMLReport((SafeStorage.Current().get("CURRENT_CASE_HTML_REPORT") == null) ? "" : SafeStorage.Current().get("CURRENT_CASE_HTML_REPORT").toString());
            _caseId = storeCase(caseRow);  //Insert Case into DB

            if (_caseId > 0) {
                //Get Step-List
                //List<StepRecordImpl> stepListFromStorage = SafeInternalStorage.Current().get("threadScenarioStepList");

                List<Object> stepListFromStorage = SafeReportStorage.Get();
                //Insert Steps into DB
                boolean _casePass = false;  //Default to false
                boolean _failData = false;
                boolean _failSystem = false;
                boolean _allPass = true;
                int stepNum = 1;  //Step Count
                StepRecordImpl step = null;
                synchronized (stepListFromStorage) {
                    //ListIterator<StepRecordImpl> stepRecListInterator = stepListFromStorage.listIterator();
                    ListIterator<Object> stepRecListInterator = stepListFromStorage.listIterator();

//					for(StepRecordImpl step : stepListFromStorage)
//					{
                    while (stepRecListInterator.hasNext()) {
                        step = (StepRecordImpl) stepRecListInterator.next();

//						step.setCaseId(_caseId);
//						step.setStepNumber(stepNum);

                        String checkError = (String) step.getReportRow().get(StepConstant.actual);
                        String status = (String) step.getReportRow().get(StepConstant.status);

                        if (checkError.toUpperCase().equals("ERROR")) {

                        }

                        //System.out.println(status);

                        if (status.toUpperCase().equals("PASS_END")) {
                            _casePass = true;
                        }
                        if (status.toUpperCase().equals("FAIL_SYSTEM")) {
                            _failSystem = true;
                        }

                        if (status.toUpperCase().equals("FAIL_DATA")) {
                            _failData = true;
                        }

                        if (status.toUpperCase().contains("FAIL")) {
                            _allPass = false;
                        }

                        //storeStep(step);  //DB Insert Step
                        storeStep(_caseId, stepNum, step);  //DB Insert Step

                        stepNum++; //Next Step
                    }  //end loop
                }
                String myStatus = "FAIL";
                if (_casePass && _allPass) {
                    myStatus = "PASS";
                } else {
                    if (_failData) {
                        myStatus = "FAIL_DATA";
                    } else if (_failSystem) {
                        myStatus = "FAIL_SYSTEM";
                    } else {
                        myStatus = "FAIL";
                    }
                }
                updateCase(_caseId, myStatus);  //DB Update Case
                tcr = new TestCaseResult(_caseId, myStatus);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LogReport.Error("ERROR GENERATE Report", ex);
        } finally {
            _end = Instant.now();
            long _duration_MilliSecond = Duration.between(_start, _end).toMillis();
            LogReport.Info(String.format("#====================  E N D   R E P O R T %s report generated in %s seconds  ========================#", _ScenarioName, (_duration_MilliSecond / 1000)));
            ConfigurationLoader.NumberOfExecution();
        }
        return tcr;
    }

    @Override
    public void createStep(String message, String status, String screenShot) throws Exception {
        String action = "ERROR";
        if (status.toUpperCase().contains("END")) {
            action = "SUCCESSFULLY EXECUTED";
        } else if (status.toUpperCase().equals("PASS")) {
            action = "PASS";
        } else if (status.toUpperCase().contains("DATA")) {
            action = "DATA ISSUE";
        } else if (status.toUpperCase().contains("SYSTEM")) {
            action = "SYSTEM ISSUE";
        } else if (status.toUpperCase().contains("LOG")) {
            action = "LOG INFORMATION";
        }

        if (message == null) {
            message = "NULL EXCEPTION";
        }
        createStep(action, message, status, screenShot);
    }


    @Override
    public void createPerformanceStep(String action, String pageName, String description, String xml, HashMap<String, Long> data) throws Exception {
        createStep(1, action, description, "Measure Performance", "Measure Performance", "PASS", "", pageName, xml, data);
    }

    @Override
    public void createStepNoSnapShot(String message, String status) throws Exception {
        createStep(message, status, "");
    }

    @Override
    public void createStep(String action, String message, String status,
                           String screenShot) throws Exception {
        createStep(0, action, "", message, message, status, screenShot);
    }

    @Override
    public void createStep(int type, String action, String message,
                           String status, String screenShot) throws Exception {
        createStep(type, action, "", message, message, status, screenShot);
    }

    @Override
    public void createStep(String action, String expect, String actual, String status, String screenShot) throws Exception {
        createStep(0, action, "", expect, actual, status, screenShot);
    }

    @Override
    public void createStep(int type, String action, String description, String expect, String actual, String status, String screenShot) throws Exception {

        createStep(type, action, description, expect, actual, status, screenShot, "", "", null);
    }

    @Override
    public void createStep(int type, String action, String description, String expect, String actual, String status, String screenShot, String pageName, String xml, HashMap<String, Long> data) throws Exception {
        StepRecordImpl step = new StepRecordImpl();
        step.setType(type);
        step.setAction(action);
        step.setDesc(description);
        step.setExpected(expect);
        step.setActual(actual);
        step.setSnapShotUrl(screenShot);
        step.setStatus(status);
        if (pageName.length() > 0) {
            step.setPerforamncePageName(pageName);
        }
        if (xml.length() > 0) {
            step.setXmlPerformanceData(xml);
        }
        if (data != null) {
            step.setPerformanceData(data);
        }
        //((List<StepRecordImpl>) SafeInternalStorage.Current().get("threadScenarioStepList")).add(step);
        SafeReportStorage.Store(step);
    }

}
