package com.hbcd.scripting.core;

import com.hbcd.common.utility.ObjectPropertyUtility;
import com.hbcd.scripting.core.fluentInterface.ObjectAction;
import com.hbcd.scripting.core.fluentInterface.ObjectPropertySetting_First;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectProperties;
import com.hbcd.utility.entity.ObjectSearchParameters;

public class New {

    private static Object _getObjectProperties(Object obj, boolean isActionObject) throws Exception {
        ObjectProperties _localObject =  ObjectPropertyUtility.determineAndGetObjectProperties(obj);
        ActionParameters acp = new ActionParameters();
        acp.add(new ObjectSearchParameters().setObjectRepository(_localObject));
        if (isActionObject) {
            return new ApplyObjectAction(acp);
        }
        else //is Set Properties
        {
            return new ApplyObjectPropertySetting_First(acp);
        }
    }

    public static ObjectPropertySetting_First Object(String objNm) throws Exception {
        try {
            return (ApplyObjectPropertySetting_First) _getObjectProperties(new ObjectProperties(objNm), false);
        } catch (Exception e) {
            throw e;
        }
    }

    public static ObjectAction Object(ObjectProperties obj) throws Exception {
        try {
            return (ApplyObjectAction) _getObjectProperties(obj, true);
        } catch (Exception e) {
            throw e;
        }
    }
}
