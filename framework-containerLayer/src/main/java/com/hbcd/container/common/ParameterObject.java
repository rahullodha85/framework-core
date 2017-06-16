package com.hbcd.container.common;

import java.util.Map;

/**
 * Created by ephung on 2/4/2016.
 */
public class ParameterObject {
    private String _propertyType = "";
    private String _propertyName = "";
    private boolean _isFirstTime = true;
    private int _maxUserDefinedWaitTime = 0;
    private int _isNotDisplayed = 0;
    private int _isDisEnabled = 0;

    //Constructor
    public ParameterObject(Map<String, Object> params)
    {
        _propertyType = params.get("PropertyType").toString();
        _propertyName = params.get("PropertyName").toString();
        _isFirstTime = (Boolean)params.get("IsFirstTime");
        _maxUserDefinedWaitTime = (Integer)params.get("MaxUserDefinedWaitTime");
        _isNotDisplayed = (Integer)params.get("IsElementNotDisplay");
        _isDisEnabled = (Integer)params.get("IsElementDisabled");
    }

    public boolean isElementDisplayed()
    {
        return (_isNotDisplayed == 0);
    }

    public boolean isElementEnabled()
    {
        return (_isDisEnabled == 0);
    }

    public boolean isFirstTime()
    {
        return _isFirstTime;
    }

    public int getMaxWaitTime()
    {
        return _maxUserDefinedWaitTime;
    }

    public String getPropertyType()
    {
        return _propertyType;
    }

    public String getPropertyName()
    {
        return _propertyName;
    }
}
