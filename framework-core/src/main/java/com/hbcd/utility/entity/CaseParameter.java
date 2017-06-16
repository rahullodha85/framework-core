package com.hbcd.utility.entity;

public class CaseParameter {
    private long _requestId = 0;
    private int _caseExecutionOrder = 0;

    /**
     * @return the _requestId
     */
    public long getRequestId() {
        return _requestId;
    }

    /**
     * @param _requestId the _requestId to set
     */
    public void setRequestId(long _requestId) {
        this._requestId = _requestId;
    }

    /**
     * @return the _caseExecutionOrder
     */
    public int getCaseExecutionOrder() {
        return _caseExecutionOrder;
    }

    /**
     * @param _caseExecutionOrder the _caseExecutionOrder to set
     */
    public void setCaseExecutionOrder(int _caseExecutionOrder) {
        this._caseExecutionOrder = _caseExecutionOrder;
    }

}
