package com.hbcd.reporting.impl;

import com.hbcd.reporting.Record;
import com.hbcd.reporting.constants.StepConstant;

import java.util.HashMap;

public class StepRecordImpl implements Record {
    private HashMap<StepConstant, Object> stepMap = new HashMap<StepConstant, Object>();

    public StepRecordImpl() {

    }

    public StepRecordImpl(String SnapShot) {
        stepMap.put(StepConstant.snapShotUrl, SnapShot);
    }

    public void setCaseId(long id) {
        stepMap.put(StepConstant.caseId, id);
    }

    public void setAction(String action) {
        stepMap.put(StepConstant.action, action);
    }

    public void setStepNumber(int stepNumber) {
        stepMap.put(StepConstant.stepNumber, stepNumber);
    }

    public void setDesc(String desc) {
        stepMap.put(StepConstant.desc, desc);
    }

    public void setExpected(String expected) {
        stepMap.put(StepConstant.expected, expected);
    }

    public void setActual(String actual) {
        stepMap.put(StepConstant.actual, actual);
    }

    public void setStatus(String status) {
        stepMap.put(StepConstant.status, status);
    }

    public void setSnapShotUrl(String url) {
        stepMap.put(StepConstant.snapShotUrl, url);
    }

    public void setType(int type) {
        stepMap.put(StepConstant.type, type);
    }

    public void setPerforamncePageName(String ppn) {
        stepMap.put(StepConstant.performancePageName, ppn);
    }

    public void setXmlPerformanceData(String xml) {
        stepMap.put(StepConstant.xmlPerformanceData, xml);
    }

    public void setPerformanceData(HashMap<String, Long> data) {
        stepMap.put(StepConstant.performanceData, data);
    }

    public HashMap<StepConstant, Object> getReportRow() {
        return stepMap;
    }

    public void print() {
        String str = "";
        for (StepConstant s : StepConstant.values()) {
            str += String.format("|%s: %s", s.toString(), stepMap.get(s));
        }
        System.out.println(str);
    }
}
