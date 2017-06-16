package com.hbcd.reporting.controllers;

import com.hbcd.reporting.impl.CaseRecordImpl;
import com.hbcd.reporting.impl.ModuleRecordImpl;
import com.hbcd.reporting.impl.StepRecordImpl;
import com.hbcd.utility.testscriptdata.TestCaseResult;

import java.util.HashMap;

public interface Controller {
    int storeModule(ModuleRecordImpl moduleInpu) throws Exception;

    int storeCase(CaseRecordImpl caseInput) throws Exception;

    void updateCase(long caseId, String status) throws Exception;

    void storeStep(StepRecordImpl stepInput) throws Exception;

    void storeStep(long cId, int sId, StepRecordImpl stepInput) throws Exception;

    void close() throws Exception;

    void init() throws Exception;

    TestCaseResult generateReport(long requestId) throws Exception;

    TestCaseResult generateReport(long requestId, long time) throws Exception;

    TestCaseResult generateReport(long requestId, int caseOrder, long time) throws Exception;

    void createStep(String message, String status, String screenShot) throws Exception;

    void createStep(String action, String message, String status, String screenShot) throws Exception;

    void openConnection() throws Exception;

    void closeConnection() throws Exception;

    void createStep(String action, String expect, String actual, String status,
                    String screenShot) throws Exception;

    void createStep(int type, String action, String message, String status,
                    String screenShot) throws Exception;

    void createStep(int type, String action, String description, String expect,
                    String actual, String status, String screenShot) throws Exception;

    void createStep(int type, String action, String description, String expect,
                    String actual, String status, String screenShot, String pageName, String xml,
                    HashMap<String, Long> data) throws Exception;

    void createPerformanceStep(String action, String pageName, String description, String xml, HashMap<String, Long> data)
            throws Exception;

    void createStepNoSnapShot(String message, String status) throws Exception;


}
