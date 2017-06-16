package com.hbcd.scripting.core;

import com.hbcd.logging.log.Log;
import com.hbcd.scripting.core.fluentInterface.ObjectAction_AttributeChild;
import com.hbcd.scripting.core.fluentInterface.ObjectAction_CommonAction;
import com.hbcd.utility.entity.ActionParameters;

public class ApplyObjectAction_AttributeChild extends ApplyObjectAction_Common implements ObjectAction_AttributeChild {

    public ApplyObjectAction_AttributeChild(ActionParameters acp) {
        super(acp);
    }
//    public ApplyObjectAction_AttributeChild(List<ObjectSearchParameters> lst) {
//        super(lst);
//    }

//    public ApplyObjectAction_AttributeChild(String objSourceName, ObjectProperties objSearch) {
//        super(0, objSourceName, objSearch, null);
//    }

//    public ApplyObjectAction_AttributeChild(List<ObjectSearchParameters> lst, int type, String storageName, ObjectProperties obj, SelectMultiObjectConfiguration selMOConfig) {
//        super(lst, type, storageName, obj, selMOConfig);
//    }

    public ObjectAction_CommonAction changeChildAttributeExactValue(String key,
                                                                    String value) throws Exception {
        try {
            if (_parameters.Current().getObjectRepository().child == null) {
                //_parameters.Current().getObjectRepository().setChild(new ObjectProperties()).setAttributeKey1(key).setAttributeValue1(value);
                Log.Error("There is no Child");
                throw new Exception("No Child definition associate to this Object");
            }
            else
            {
                _parameters.Current().getObjectRepository().child.setAttributeValue1(value);
            }

            return new ApplyObjectAction(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }


    public ObjectAction_CommonAction changeChildAttributeContainsValue(
            String key, String value) throws Exception {
        try {
            if (_parameters.Current().getObjectRepository().child == null) {
                //_parameters.Current().getObjectRepository().setChild(new ObjectProperties()).setAttributeKey2(key).setAttributeValue2(value);
                Log.Error("There is no Child");
                throw new Exception("No Child definition associate to this Object");
            }
            else
            {
                _parameters.Current().getObjectRepository().child.setAttributeKey2(key).setAttributeValue2(value);
            }
            return new ApplyObjectAction(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }


    public ObjectAction_CommonAction changeChildContainsText(String value) throws Exception {
        try {
            if (_parameters.Current().getObjectRepository().child == null) {
                //_parameters.Current().getObjectRepository().setChild(new ObjectProperties()).setContainsText(value);
                Log.Error("There is no Child");
                throw new Exception("No Child definition associate to this Object");
            }
            else
            {
                _parameters.Current().getObjectRepository().child.setContainsText(value);
            }
            return new ApplyObjectAction(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    public ObjectAction_CommonAction changeChildPartialLinkText(String value) throws Exception {
        try {
            if (_parameters.Current().getObjectRepository().child == null) {
                //_parameters.Current().getObjectRepository().setChild(new ObjectProperties()).setPartialLinkText(value);
                Log.Error("There is no Child");
                throw new Exception("No Child definition associate to this Object");
            }
            else
            {
                _parameters.Current().getObjectRepository().child.setPartialLinkText(value);
            }
            return new ApplyObjectAction(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ObjectAction_CommonAction changeChildithElement(int value) throws Exception {
        try {
            if (_parameters.Current().getObjectRepository().child == null) {
                //_parameters.Current().getObjectRepository().setChild(new ObjectProperties()).setIth(value);
                Log.Error("There is no Child");
                throw new Exception("No Child definition associate to this Object");
            }
            else
            {
                _parameters.Current().getObjectRepository().child.setIth(value);
            }
           return new ApplyObjectAction(_parameters);
        } catch (Exception e) {
           throw e;
        }
    }
}
