package com.hbcd.scripting.core;

import com.hbcd.scripting.core.fluentInterface.AssertActionLevel2;
import com.hbcd.scripting.exception.AssertFailException;

/**
 * Created by ephung on 6/27/2016.
 */
public class AssertActionLevel2_impl implements AssertActionLevel2
{
    private String _message = "Condition Check is FAIL.";
    private boolean _value = false;
    public AssertActionLevel2_impl(boolean v, String msg)
    {
        _message = msg;
        _value = v;
    }

    @Override
    public void stopIfFalse() throws Exception {
        if (!_value) {
            throw new AssertFailException(_message);
        }
    }

    @Override
    public boolean getValue() throws Exception {
        return _value;
    }
}
