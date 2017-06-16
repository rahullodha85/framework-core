package com.hbcd.scripting.core;

import com.hbcd.core.genericfunctions.GenericFunctions;
import com.hbcd.scripting.core.fluentInterface.CaptureObjectAction;
import com.hbcd.utility.entity.ActionParameters;

/**
 * Created by ephung on 10/30/2015.
 */
public class ApplyCaptureObjectAction extends BaseApplyObjectAction implements CaptureObjectAction {

    public ApplyCaptureObjectAction(ActionParameters acp) {
        super(acp);
    }

    @Override
    public void StoreAs(String objNm) throws Exception {
        //_listParams.add(new ObjectSearchParameters().setType(_type).setStoredObjectName(_localObjectSearchName).setObjectRepository(_localObject).setMultiObjectConfiguration(_selectMultiObjectConfig).setActionParamAsString(objNm));
        _parameters.Current().setStoredObjectName(objNm);
        GenericFunctions.coreStoreCaptureObject(_parameters);
    }

}
