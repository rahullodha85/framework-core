package com.hbcd.scripting.core;

import com.hbcd.core.genericfunctions.GenericFunctions;
import com.hbcd.scripting.core.fluentInterface.WaitForAction;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectSearchParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ephung on 9/28/16.
 */

public class WaitForAction_Impl implements WaitForAction {
    private List<ObjectSearchParameters> _listParams = new ArrayList<>();
    ActionParameters _parameters = new ActionParameters();

    public WaitForAction_Impl(ActionParameters acp)
    {
        if (acp != null) {
            _parameters.shallowCopy(acp);
        }
    }

    @Override
    public int untilDisappear() throws Exception {
        int rtrnStatus = 1;
        try {
            rtrnStatus = GenericFunctions.coreWaitTillElementDisappear(_parameters);
        } catch (Exception e) {
            throw e;
        }
        return rtrnStatus;
    }

    @Override
    public int untilContainTextChange(String originalText) throws Exception {
        int rtrnStatus = 1;
        try {
            if (_parameters.Current().isSearchable()) {
                _parameters.Current().setActionParamAsString(originalText);
                rtrnStatus = GenericFunctions.coreWaitTillTextChange(_parameters);
            }
        } catch (Exception e) {
            throw e;
        }
        return rtrnStatus;
    }

    @Override
    public int untilAttribueValueChangeContainText(String key, String changeToValue) throws Exception {
        int rtrnStatus = 1;
        try {
            if (_parameters.Current().isSearchable()) {
                //_parameters.Current().setActionParamAsString(key);
                _parameters.Current().getListActionParamAsHashMap().put(key,changeToValue);
                rtrnStatus = GenericFunctions.coreWaitTillAttribueValueChangeContainText(_parameters);
            }
        } catch (Exception e) {
            throw e;
        }
        return rtrnStatus;
    }

    @Override
    public int untilAttribueValueNotContainText(String key, String changeToValue) throws Exception {
        int rtrnStatus = 1;
        try {
            if (_parameters.Current().isSearchable()) {
                //_parameters.Current().setActionParamAsString(key);
                _parameters.Current().getListActionParamAsHashMap().put(key,changeToValue);
                rtrnStatus = GenericFunctions.coreWaitTillAttribueNotContainText(_parameters);
            }
        } catch (Exception e) {
            throw e;
        }
        return rtrnStatus;
    }

    @Override
    public int untilContainTextHasText(String newText) throws Exception {
        int rtrnStatus = 1;
        try {
            if (_parameters.Current().isSearchable()) {
                _parameters.Current().setActionParamAsString(newText);
                rtrnStatus = GenericFunctions.coreWaitTillHasText(_parameters);
            }
        } catch (Exception e) {
            throw e;
        }
        return rtrnStatus;
    }
}
