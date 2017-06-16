package com.hbcd.scripting.core;

import com.hbcd.common.utility.ObjectPropertyUtility;
import com.hbcd.core.genericfunctions.GenericFunctions;
import com.hbcd.scripting.core.fluentInterface.AssertAction;
import com.hbcd.scripting.core.fluentInterface.AssertActionLevel2;
import com.hbcd.scripting.exception.AssertFailException;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectProperties;
import com.hbcd.utility.entity.ObjectSearchParameters;

/**
 * Created by ephung on 5/16/2016.
 */
public class AssertAction_impl implements AssertAction {

    private String _message = "";
    private boolean _isReport = true;
    private boolean _isFailed = false;
    private boolean _checkValue = false;
    private String _expectingMsg = ""; //Pass
    private String _actualMsg = ""; //Failed

    public AssertAction_impl(String msg) {
        _message = msg;
        _isReport = true;
    }

    public AssertAction_impl(String msg, boolean isReport) {
        _message = msg;
        _isReport = isReport;
    }

    @Override
    public AssertActionLevel2 areEqual(String expected, String actual) throws Exception
    {
        String status = "fail";
        if (expected.equals(actual))
        {
            //Report Passed
            status = "pass";
            _isFailed = false;
            _checkValue=true;
        }
        else
        {
            _isFailed = true;
        }
        try {
            Report.log("ASSERT", _message, expected, actual, status, BrowserAction.screenshot());
//            if (_isFailed && !_isReport) //Abandon Script
//                throw new AssertFailException("Condition Check is FAIL.");
        } catch (AssertFailException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AssertActionLevel2_impl(_checkValue, "Condition Check is FAIL.");
    }

    @Override
    public AssertActionLevel2 areEqual(double expected, double actual) throws Exception
    {
        String status = "fail";
        if (expected==actual)
        {
            //Report Passed
            status = "pass";
            _checkValue=true;
            _isFailed = false;
        }
        else
        {
            _isFailed = true;
        }
        try {
            Report.log("ASSERT",_message, String.valueOf(expected), String.valueOf(actual), status, BrowserAction.screenshot());
//            if (_isFailed && !_isReport) //Abandon Script
//                throw new AssertFailException("Condition Check is FAIL.");
        } catch (AssertFailException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AssertActionLevel2_impl(_checkValue, "Condition Check is FAIL.");
    }

    @Override
    public AssertActionLevel2 areEqual(int expected, int actual) throws Exception
    {
        String status = "fail";
        if (expected==actual)
        {
            //Report Passed
            status = "pass";
            _isFailed = false;
            _checkValue=true;
        }
        else
        {
            _isFailed = true;
        }
        try {
            Report.log("ASSERT",_message, String.valueOf(expected), String.valueOf(actual), status, BrowserAction.screenshot());
//            if (_isFailed && !_isReport) //Abandon Script
//                throw new AssertFailException("Condition Check is FAIL.");
        } catch (AssertFailException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AssertActionLevel2_impl(_checkValue, "Condition Check is FAIL.");
    }

    private static ActionParameters _getObjectProperties(Object obj) throws Exception
    {
        ObjectProperties _localObject =  ObjectPropertyUtility.determineAndGetObjectProperties(obj);
        ActionParameters ap = new ActionParameters();
        ap.add(new ObjectSearchParameters().setObjectRepository(_localObject));
        return ap;
    }

    private AssertActionLevel2 _genericCheckStatus(boolean status, String passMsg, String failMsg) throws Exception
    {
        String statusAsText = "fail";
        if (status)
        {
            statusAsText = "pass";
            _isFailed = false;
            _checkValue=true;
        }
        else
        {
            statusAsText = "fail";
            _isFailed = true;
        }
        try {
            Report.log("ASSERT", _message, String.format("%s %s", _message, (_checkValue? passMsg: failMsg)), statusAsText, BrowserAction.screenshot());
//            if (_isFailed) //Abandon Script
//                return new AssertActionLevel2_impl("Condition Check is FAIL.");
        } catch (AssertFailException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AssertActionLevel2_impl(_checkValue, "Condition Check is FAIL.");
    }

    @Override
    public AssertActionLevel2 isTrue (boolean condition) throws Exception
    {
        return _genericCheckStatus(condition, "is true.", "is not true.");
    }

    @Override
    public AssertActionLevel2 isExist(String objName) throws Exception
    {
        return _genericCheckStatus(GenericFunctions.coreIsPresent(_getObjectProperties(objName)), "exists.", "not exists.");
    }

    @Override
    public AssertActionLevel2 isNotExist(String objName) throws Exception
    {
        return _genericCheckStatus(GenericFunctions.coreIsNotPresent(_getObjectProperties(objName)), "not exists.", "exists.");
    }

    @Override
    public AssertActionLevel2 isFound(String objName) throws Exception {
        return _genericCheckStatus(GenericFunctions.coreIsPresent(_getObjectProperties(objName)), "is found.", "is not found.");
    }

    @Override
    public AssertActionLevel2 isExist(ObjectProperties obj) throws Exception
    {
        return _genericCheckStatus(GenericFunctions.coreIsPresent(_getObjectProperties(obj)), "exists.", "not exists.");
    }

    @Override
    public AssertActionLevel2 isNotExist(ObjectProperties obj) throws Exception
    {
        return _genericCheckStatus(GenericFunctions.coreIsNotPresent(_getObjectProperties(obj)), "not exists.", "exists.");
    }

    @Override
    public AssertActionLevel2 isFound(ObjectProperties obj) throws Exception {
        return _genericCheckStatus(GenericFunctions.coreIsPresent(_getObjectProperties(obj)), "is found.", "is not found.");
    }
}
