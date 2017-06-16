package com.hbcd.scripting.core;

import com.hbcd.core.genericfunctions.GenericFunctions;
import com.hbcd.scripting.core.fluentInterface.MultiObjectsAction;
import com.hbcd.scripting.core.fluentInterface.ObjectAction_CommonAction;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.SelectMultiObjectConfiguration;

import java.util.Arrays;
import java.util.List;

public class ApplyMultiObjectsAction extends BaseApplyObjectAction implements MultiObjectsAction {

//    public ApplyMultiObjectsAction(List<ObjectSearchParameters> lst) {
//        super(lst);
//    }

    public ApplyMultiObjectsAction(ActionParameters ap) {
        super(ap);
    }

//    public ApplyMultiObjectsAction(List<ObjectSearchParameters> lst, ObjectProperties obj, SelectMultiObjectConfiguration selMOConfig) {
//        super(lst, obj, selMOConfig);
//        _type = 1;
//    }
//
//    public ApplyMultiObjectsAction(List<ObjectSearchParameters> lst, int type, String objStorageSourceName, ObjectProperties obj, SelectMultiObjectConfiguration selMOConfig) {
//        super(lst, type, objStorageSourceName, obj, selMOConfig);
//    }


    @Override
    public List<String> getAttributes(String... attribList) throws Exception {
        try {
            _parameters.Current().setListActionParamAsString(Arrays.asList(attribList)).getMultiObjectConfiguration().setIsMultiObjectFilter(false);
            return GenericFunctions.coreGetAttributes(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<String> getText(String... attribList) throws Exception {
        try {
            _parameters.Current().setListActionParamAsString(Arrays.asList(attribList)).getMultiObjectConfiguration().setIsMultiObjectFilter(false);
            return GenericFunctions.coreGetTexts(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

//    @Override
//    public void click(int ithIndex) throws Exception {
//        try {
//            int size = GenericFunctions.coreGetReturnSize(_localObjectSearchName, _localObject);
//            if ((ithIndex <= 0) && (ithIndex > size)) {
//                GenericFunctions.coreClickOnIth(new ObjectSearchParameters().setType(_type).setStoredObjectName(_localObjectSearchName).setObjectRepository(_localObject), ithIndex);
//                return;
//            }
//            //GenericFunctions.core;
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    @Override
    public int size() throws Exception {
        if (_parameters.Current().getMultiObjectConfiguration()==null) {_parameters.Current().setMultiObjectConfiguration(new SelectMultiObjectConfiguration());}
        return GenericFunctions.coreGetReturnSize(_parameters);
    }


    @Override
    public ObjectAction_CommonAction select(int index) throws Exception {

        try {
            if (_parameters.Current().getMultiObjectConfiguration()==null) {_parameters.Current().setMultiObjectConfiguration(new SelectMultiObjectConfiguration());}
            _parameters.Current().getMultiObjectConfiguration().setIsMultiObjectFilter(true).setIndex(index);
            return new ApplyObjectAction_Common(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ObjectAction_CommonAction selectRandom() throws Exception {
        //not very efficient since search for multiple objects twice.   1. find size  2.apply action to object index
        try {
            if (_parameters.Current().getMultiObjectConfiguration()==null) {_parameters.Current().setMultiObjectConfiguration(new SelectMultiObjectConfiguration());}
            getLastParam().getMultiObjectConfiguration().setIsMultiObjectFilter(true).setRandom(true);
            return new ApplyObjectAction_Common(_parameters);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ObjectAction_CommonAction selectItemContainText(String text) throws Exception {
        try {
            if (_parameters.Current().getMultiObjectConfiguration()==null) {_parameters.Current().setMultiObjectConfiguration(new SelectMultiObjectConfiguration());}
            _parameters.Current().getMultiObjectConfiguration().setIsMultiObjectFilter(true).setContainText(text);
            return new ApplyObjectAction_Common(_parameters);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int findItemIndexContainText(String text) throws Exception {
        int index = 0;
        try {
            if (_parameters.Current().getMultiObjectConfiguration()==null) {_parameters.Current().setMultiObjectConfiguration(new SelectMultiObjectConfiguration());}
            _parameters.Current().getMultiObjectConfiguration().setIsMultiObjectFilter(true).setContainText(text);
            index = GenericFunctions.getContainTextIndex(_parameters);
        } catch (Exception e) {
            throw e;
        }
        return index;
    }
}
