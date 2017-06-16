package com.hbcd.reporting.impl;

import com.hbcd.reporting.Record;
import com.hbcd.reporting.constants.CaseConstant;

import java.util.HashMap;

public class CaseRecordImpl implements Record {

    private HashMap<CaseConstant, Object> caseMap = new HashMap<CaseConstant, Object>();

    public void setReqId(long id) {
        caseMap.put(CaseConstant.moduleId, id);
    }

    public void setExecutionOrder(int ith) {
        caseMap.put(CaseConstant.executionOrder, ith);
    }

    public void setScenarioId(int id) {
        caseMap.put(CaseConstant.caseId, id);
    }

    public void setScenarioName(String name) {
        caseMap.put(CaseConstant.caseName, name);
    }

    public void setScenarioDescription(String desc) {
        caseMap.put(CaseConstant.caseDescription, desc);
    }

    public void setStatus(String status) {
        caseMap.put(CaseConstant.caseStatus, status);
    }

    public void setTotalExecutionTime(long inMs) {
        caseMap.put(CaseConstant.totalExecutionTimeMs, inMs);
    }

    public void setHTMLReport(String htmlReport) {
        caseMap.put(CaseConstant.caseHTMLReport, htmlReport);
    }

    public HashMap<CaseConstant, Object> getCaseRow() {
        return caseMap;
    }

}
