package com.hbcd.scripting.core;

import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectSearchParameters;

import java.util.ArrayList;
import java.util.List;

public class BaseApplyObjectAction {
    private List<ObjectSearchParameters> _listParams = new ArrayList<>();
    ActionParameters _parameters = new ActionParameters();

    public BaseApplyObjectAction(ActionParameters acp){
        if (acp != null) {
            _parameters.shallowCopy(acp);
        }
    }

    public ObjectSearchParameters getLastParam()
    {
        if (_listParams != null && !_listParams.isEmpty())
        {
            _listParams.add(new ObjectSearchParameters());
        }
        return _listParams.get(_listParams.size() - 1);
    }
}
