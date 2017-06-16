package com.hbcd.scripting.core;

import com.hbcd.common.utility.ObjectPropertyUtility;
import com.hbcd.scripting.core.fluentInterface.WaitForAction;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectProperties;
import com.hbcd.utility.entity.ObjectSearchParameters;
import com.hbcd.utility.entity.SelectMultiObjectConfiguration;

/**
 * Created by ephung on 9/28/16.
 */
public class WaitFor {
    private static Object _getObjectProperties(Object obj, boolean isSingle) throws Exception {
        ObjectProperties _localObject =  ObjectPropertyUtility.determineAndGetObjectProperties(obj);
        ActionParameters acp = new ActionParameters();
        if (isSingle) {
            acp.add(new ObjectSearchParameters().setObjectRepository(_localObject));
            return new WaitForAction_Impl(acp);
        }
        else //is Multiple Object
        {
            acp.add(new ObjectSearchParameters().setType(1).setObjectRepository(_localObject).setMultiObjectConfiguration(new SelectMultiObjectConfiguration()));
            return new ApplyMultiObjectsAction(acp);
        }
    }

    public static WaitForAction Object(String objNm) throws Exception {
        try {
            return (WaitForAction) _getObjectProperties(objNm, true);
        } catch (Exception e) {
            throw e;
        }
    }

    public static WaitForAction Object(ObjectProperties obj) throws Exception {
        try {
            return (WaitForAction) _getObjectProperties(obj, true);
        } catch (Exception e) {
            throw e;
        }
    }
}
