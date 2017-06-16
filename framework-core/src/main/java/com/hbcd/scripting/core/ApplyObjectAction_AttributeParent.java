package com.hbcd.scripting.core;

import com.hbcd.logging.log.Log;
import com.hbcd.scripting.core.fluentInterface.ObjectAction_AttributeChild;
import com.hbcd.scripting.core.fluentInterface.ObjectAction_AttributeParent;
import com.hbcd.utility.entity.ActionParameters;

public class ApplyObjectAction_AttributeParent extends ApplyObjectAction_AttributeChild implements ObjectAction_AttributeParent {

    public ApplyObjectAction_AttributeParent(ActionParameters acp) {
        super(acp);
    }
//    public ApplyObjectAction_AttributeParent(List<ObjectSearchParameters> lst) {
//        super(lst);
//    }
//
//    public ApplyObjectAction_AttributeParent(String objSourceName, ObjectProperties objSearch) {
//        super(objSourceName, objSearch);
//    }

//    public ApplyObjectAction_AttributeParent(List<ObjectSearchParameters> lst, int type, String storageName, ObjectProperties obj, SelectMultiObjectConfiguration selMOConfig) {
//        super(lst, type, storageName, obj, selMOConfig);
//    }

    public ObjectAction_AttributeChild changeAttributeExactValue(String key,
                                                                 String value) throws Exception {
        try {
            if (_parameters.Current().getObjectRepository() != null) {
                _parameters.Current().getObjectRepository().setAttributeKey1(key).setAttributeValue1(value);
                return new ApplyObjectAction(_parameters);
            } else {
                Log.Error("Object is NULL");
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }


    public ObjectAction_AttributeChild changeAttributeContainsValue(
            String key, String value) throws Exception {
        try {
            if (_parameters.Current().getObjectRepository() != null) {
                _parameters.Current().getObjectRepository().setAttributeKey2(key).setAttributeValue2(value);
                return new ApplyObjectAction(_parameters);
            } else {
                Log.Error("Object is NULL");
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }


    public ObjectAction_AttributeChild changeContainsText(String value) throws Exception {
        try {
            if (_parameters.Current().getObjectRepository() != null) {
                _parameters.Current().getObjectRepository().setContainsText(value);
                return new ApplyObjectAction(_parameters);
            } else {
                Log.Error("Object is NULL");
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }


    public ObjectAction_AttributeChild changePartialLinkText(String value) throws Exception {
        try {
            if (_parameters.Current().getObjectRepository() != null) {
                _parameters.Current().getObjectRepository().setPartialLinkText(value);
                return new ApplyObjectAction(_parameters);
            } else {
                Log.Error("Object is NULL");
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }


    public ObjectAction_AttributeChild changeId(String value) throws Exception {
        try {
            if (_parameters.Current().getObjectRepository() != null) {
                _parameters.Current().getObjectRepository().setID(value);
                return new ApplyObjectAction(_parameters);
            } else {
                Log.Error("Object is NULL");
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }
}
