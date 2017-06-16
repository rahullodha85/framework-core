package com.hbcd.reporting.objects;

import com.hbcd.reporting.constants.StepConstant;

import java.util.HashMap;

public class Step {
    private String action;
    private String desc;
    private String expected;
    private String actual;
    private String status;
    private String snapShotUrl;
    private long caseRowId;
    private long stepId;
    private int stepNumber;
    private int type = 0; /* 0: regular log;  1: html log; 2: performance log*/
    private String performancePageName;
    private String xmlPerformanceData;
    private HashMap<String, Long> perfomanceData = null;

    public Step(HashMap<StepConstant, Object> input) {
        this.caseRowId = (input.containsKey(StepConstant.caseId) ? (Long) input.get(StepConstant.caseId) : 0L);
        this.stepNumber = (Integer) (input.containsKey(StepConstant.stepNumber) ? input.get(StepConstant.stepNumber) : 0);
        init(input);
    }

    public Step(long cId, int sId, HashMap<StepConstant, Object> input) {
        this.caseRowId = cId;
        this.stepNumber = sId;
        init(input);
    }

    private void init(HashMap<StepConstant, Object> input) {
        if (input.containsKey(StepConstant.type)) {
            this.type = (Integer) input.get(StepConstant.type);
        } else {
            this.type = 0;
        }

        this.action = (String) input.get(StepConstant.action);
        this.desc = (String) input.get(StepConstant.desc);
        this.expected = (String) input.get(StepConstant.expected);
        this.actual = (String) input.get(StepConstant.actual);
        this.status = (String) input.get(StepConstant.status);
        this.snapShotUrl = (String) input.get(StepConstant.snapShotUrl);
        this.performancePageName = (String) input.get(StepConstant.performancePageName);
        this.xmlPerformanceData = (String) input.get(StepConstant.xmlPerformanceData);
        this.perfomanceData = (HashMap<String, Long>) input.get(StepConstant.performanceData);
    }

    public String toString() {
        return String.format("Case Id : %s | type %s:  | StepNumber : %s | Action : %s | Description : %s | Expected : %s | Actual : %s | ScreenShot : %s | Status : [%s]"
                , this.caseRowId
                , ((this.type == 1) ? "Performance" : "Regular")
                , this.stepNumber
                , this.action
                , this.desc
                , this.expected
                , this.actual
                , (this.snapShotUrl.length() > 0 ? "YES" : "NO")
                , this.status) ;
    }

    public long getCaseRowId() {
        return this.caseRowId;
    }

    public String getAction() {
        return action;
    }

    public long getStepId() {
        return stepId;
    }

    public String getDesc() {
        return desc;
    }

    public String getExpected() {
        return expected;
    }

    public String getActual() {
        return actual;
    }

    public String getStatus() {
        return status;
    }

    public String getSnapShotUrl() {
        return snapShotUrl;
    }

    public int getStepNumber() {
        return this.stepNumber;
    }

    public int getType() {
        return this.type;
    }

    public String getPerformancePageName() {
        return this.performancePageName;
    }

    public String getXmlPerformanceData() {
        return this.xmlPerformanceData;
    }

    public HashMap<String, Long> getPerformanceData() {
        return this.perfomanceData;
    }
}
