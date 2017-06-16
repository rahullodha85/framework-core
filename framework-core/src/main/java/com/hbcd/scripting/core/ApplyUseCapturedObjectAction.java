package com.hbcd.scripting.core;

import com.hbcd.common.utility.ObjectPropertyUtility;
import com.hbcd.scripting.core.fluentInterface.MultiObjectsAction;
import com.hbcd.scripting.core.fluentInterface.ObjectAction;
import com.hbcd.scripting.core.fluentInterface.UseCapturedObjectAction;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectProperties;
import com.hbcd.utility.entity.ObjectSearchParameters;

/**
 * Created by ephung on 10/30/2015.
 */
public class ApplyUseCapturedObjectAction implements UseCapturedObjectAction {

    private String _objCapturedName = "";

    public ApplyUseCapturedObjectAction(String captureObjNm) {
        _objCapturedName = captureObjNm;
    }

    //Generic Function
    private Object _getObjectProperties(Object obj, boolean isSingle) throws Exception {
        ObjectProperties _localObject =  ObjectPropertyUtility.determineAndGetObjectProperties(obj);
        ActionParameters acp = new ActionParameters();
        if (isSingle) {
            acp.add(new ObjectSearchParameters().setType(2).setStoredObjectName(_objCapturedName).setObjectRepository(_localObject));
            return new ApplyObjectAction(acp);
        }
        else //is Multiple Object
        {
            acp.add(new ObjectSearchParameters().setType(3).setStoredObjectName(_objCapturedName).setObjectRepository(_localObject));
            return new ApplyMultiObjectsAction(acp);
        }
    }

    @Override
    public ObjectAction FindObject(String objNm) throws Exception {
        try {
            return (ApplyObjectAction)_getObjectProperties(objNm, true /*single*/);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ObjectAction FindObject(ObjectProperties obj) throws Exception {
        try {
            return (ApplyObjectAction)_getObjectProperties(obj, true /*single*/);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MultiObjectsAction FindMultipleObjects(String objNm) throws Exception {
        try {
            return (ApplyMultiObjectsAction)_getObjectProperties(objNm, false /*multiple*/);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MultiObjectsAction FindMultipleObjects(ObjectProperties obj) throws Exception {
        try {
            return (ApplyMultiObjectsAction)_getObjectProperties(obj, false /*multiple*/);
        } catch (Exception e) {
            throw e;
        }
    }

    public ObjectAction AsIs() throws Exception {
        try {
            ActionParameters acp = new ActionParameters();
            acp.add(new ObjectSearchParameters().setType(2).setStoredObjectName(_objCapturedName));
            return new ApplyObjectAction(acp);
        } catch (Exception e) {
            throw e;
        }
    }
}
