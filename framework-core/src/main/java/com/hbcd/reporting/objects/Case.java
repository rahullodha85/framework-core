package com.hbcd.reporting.objects;

import com.hbcd.reporting.constants.CaseConstant;

import java.util.HashMap;

public class Case {
    private int caseId;
    private long requestId;
    private String caseName;
    private String caseStatus;
    private String caseDescription;
    private String caseHTMLReport = "";
    private long totalExecutionTimeMs = 0;
    private int executionOrder = 0;

    public Case(HashMap<CaseConstant, Object> input) {
        this.requestId = (long) input.get(CaseConstant.moduleId);
        this.caseName = (String) input.get(CaseConstant.caseName);
        this.caseDescription = (String) input.get(CaseConstant.caseDescription);
        this.caseStatus = (String) input.get(CaseConstant.caseStatus);
        this.caseHTMLReport = (String) input.get(CaseConstant.caseHTMLReport);
        this.totalExecutionTimeMs = (long) input.get(CaseConstant.totalExecutionTimeMs);
        this.executionOrder = (int) input.get(CaseConstant.executionOrder);
    }

    public String toString() {
        return String.format("Case Id : %s | Module : %s | Case Name : %s | Case Description : %s |Case Status : %s | Total Execution Time: %s ms |Execution Order: %s"
                , this.caseId
                , this.requestId
                , this.caseName
                , this.caseDescription
                , this.caseStatus
                , this.totalExecutionTimeMs
                , this.executionOrder);
    }

    public long getRequestId() {
        return requestId;
    }

    public int getCaseId() {
        return caseId;
    }

    public int getExecutionOrder() {
        return executionOrder;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public long getTotalExecutionTime() {
        return totalExecutionTimeMs;
    }

    public String getCaseHTMLReport() {
        return caseHTMLReport;
    }
}
