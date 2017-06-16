package com.hbcd.scripting.core;

import com.hbcd.scripting.core.fluentInterface.ObjectAction;
import com.hbcd.utility.entity.ActionParameters;

public class ApplyObjectAction extends ApplyObjectAction_AttributeParent implements ObjectAction {


    public ApplyObjectAction(ActionParameters acp) {
        super(acp);
    }

//    public ApplyObjectAction(String objSourceName, ObjectProperties objSearch) {
//        super(objSourceName, objSearch);
//    }

//    public ApplyObjectAction(List<ObjectSearchParameters> lstP, int type, String storageName, ObjectProperties obj, SelectMultiObjectConfiguration selMOConfig) {
//        super(lstP, type, storageName, obj, selMOConfig);
//    }
}
