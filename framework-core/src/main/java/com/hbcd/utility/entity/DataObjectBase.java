package com.hbcd.utility.entity;

import com.hbcd.utility.common.GenericDataInterface;

/**
 * Created by ephung on 9/6/16.
 */
public class DataObjectBase implements GenericDataInterface {

    protected String _startColumnName = "";

    @Override
    public String getStartColumnName() {
        return _startColumnName;
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public void print() {

    }
}
