package com.hbcd.utility.testscriptdata;

import java.util.HashMap;

public class ServiceRequestResult {

    boolean _status = false;
    String _response = "";  //response JOSN Message
    String _message1 = "";
    String _message2 = "";
    String _errorResponse = "";
    HashMap<String, String> _returnValues = new HashMap<String, String>();
    HashMap<String, String> _returnJSONObjs = new HashMap<String, String>();

    public ServiceRequestResult(String er, boolean s, String m1, String m2) {
        _errorResponse = er;
        _status = s;
        _message1 = m1;
        _message2 = m2;
    }

    public ServiceRequestResult(String er, boolean s, String rm, String m1, String m2) {
        _errorResponse = er;
        _status = s;
        _response = rm;
        _message1 = m1;
        _message2 = m2;
    }

    public boolean isPass() {
        return _status;
    }

    public String getStatus() {
        if (isErrorFound()) {
            return "fail_error";
        } else {
            return _status ? "pass" : "fail";
        }
    }

    public String getResponse() {
        return _response;
    }

    public String getMessage1() {
        return _message1;
    }

    public String getMessage2() {
        return _message2;
    }

    public String getErrorResponse() {
        return _errorResponse;
    }

    public void setErrorResponse(String er) {
        _errorResponse = er;
    }

    public boolean isErrorFound() {
        return ((_errorResponse == null) || (_errorResponse.isEmpty())) ? false : true;
    }

    public HashMap<String, String> getReturnValues() {
        return _returnValues;
    }

    public HashMap<String, String> getReturnObjects() {
        return _returnJSONObjs;
    }

    public void setReturnValues(String k, String v) {
        _returnValues.put(k, v);
//		if (_returnValues.containsKey(k))
//		{
//			_returnValues.
//		}
//		else
//		{
//			
//		}
    }

    public void setReturnObjects(String k, String v) {
        _returnJSONObjs.put(k, v);
    }

    public void print() {
        System.out.println(
                String.format("Status: %s\nResponse: %s\nMessage 1: %s\nMessage 2: %s\nError: %s\n"
                        , _status
                        , _response
                        , _message1
                        , _message2
                        , _errorResponse));
    }
}
