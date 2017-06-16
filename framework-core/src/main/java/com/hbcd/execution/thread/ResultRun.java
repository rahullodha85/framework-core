package com.hbcd.execution.thread;

import com.hbcd.utility.entity.ObjectTestScript;
import com.hbcd.utility.testscriptdata.TestCaseResult;

import java.util.ArrayList;
import java.util.List;

public class ResultRun {
    int _passCounter = 0;
    int _failCounter = 0;
    int _dataFailCounter = 0;
    int _systemFailCounter = 0;
    long _requestId = 0;
    long _totalExecutionTime = 0;
    List<ObjectTestScript> _failScripts = new ArrayList<ObjectTestScript>();

    public ResultRun() {

    }

    public ResultRun(long r) {
        _requestId = r;
    }

    public ResultRun(int p, int f, int df, int sf) {
        _passCounter = p;
        _failCounter = f;
        _dataFailCounter = df;
        _systemFailCounter = sf;
    }

    public int getPassCounter() {
        return _passCounter;
    }

    public int getFailCounter() {
        return _failCounter;
    }

    public int getDataFailCounter() {
        return _dataFailCounter;
    }

    public int SystemFailCounter() {
        return _systemFailCounter;
    }

    public int getTotal() {
        return (_passCounter + _failCounter + _dataFailCounter + _systemFailCounter);
    }

    public float getPercentagePass() {
        return ((float) _passCounter / (float) getTotal()) * 100;
    }

    public void AddToResultAnalysis(TestCaseResult r) {
        //Count
        if (r.isPass()) {
            _passCounter++;
        } else {
            //Distinctive Count
            if (r.isSystemFail()) {
                _systemFailCounter++;
                _failScripts.add((ObjectTestScript) r.getTestScriptObject());
            } else if (r.isDataFail()) {
                _dataFailCounter++;
                //Temporary Testing
                _failScripts.add((ObjectTestScript) r.getTestScriptObject());
            } else {
                _failCounter++;  //Faile count regarless
                _failScripts.add((ObjectTestScript) r.getTestScriptObject());
            }

        }
    }

    public List<ObjectTestScript> getFailScript() {
        return _failScripts;
    }

    public long getTotalExecutionTime() {
        return _totalExecutionTime;
    }

    public void setTotalExecutionTime(long t) {
        _totalExecutionTime = t;
    }

    public long getRequestId() {
        return _requestId;
    }


    public int getProcessResultStatus()
    {
        return ((_systemFailCounter > 0) || (_dataFailCounter > 0) || (_failCounter > 0)) ? 1 : 0 ;
    }
}
