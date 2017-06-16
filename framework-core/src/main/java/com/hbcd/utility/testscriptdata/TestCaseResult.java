package com.hbcd.utility.testscriptdata;

import java.io.Serializable;

public class TestCaseResult implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private long _caseId = 0;
    private String _status = "FAIL";
    private Object _testScriptObject = null;

    public TestCaseResult(long cId, String st) {
        _caseId = cId;
        _status = st;
    }

    /**
     * @return the _caseId
     */
    public long getCaseId() {
        return _caseId;
    }

    /**
     * @return the _status
     */
    public String getStatus() {
        return _status;
    }

    public Object getTestScriptObject() {
        return _testScriptObject;
    }

    public void setTestScriptObject(Object obj) {
        _testScriptObject = obj;
    }


    public boolean isPass() {
        return _status.toUpperCase().equals("PASS");
    }

    public boolean isAnyFail() {
        return _status.toUpperCase().contains("FAIL");
    }

    public boolean isFail() {
        return _status.toUpperCase().contains("FAIL") && !_status.toUpperCase().equals("FAIL_DATA") && !_status.toUpperCase().equals("FAIL_SYSTEM");
    }

    public boolean isSystemFail() {
        return _status.toUpperCase().equals("FAIL_SYSTEM");
    }

    public boolean isDataFail() {
        return _status.toUpperCase().equals("FAIL_DATA");
    }

    public void print() {
        System.out.println(String.format("CASE ID: %s | STATUS: %s", _caseId, getStatus()));

    }
}
