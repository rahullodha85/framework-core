package com.hbcd.scripting.core;

import com.hbcd.core.genericfunctions.GenericFunctions;
import com.hbcd.scripting.core.fluentInterface.ObjectActionLevel2;
import com.hbcd.utility.common.Type.Level2Type;
import com.hbcd.utility.entity.ActionParameters;

public class ApplyObjectActionLevel2 extends BaseApplyObjectAction implements ObjectActionLevel2 {

    private Level2Type _lookingFor = Level2Type.TEXT;
    private String _attrbKey = "";

    public ApplyObjectActionLevel2(ActionParameters acp)
    {
        super(acp);
    }

    public ApplyObjectActionLevel2(ActionParameters acp, Level2Type t) {
        this(acp, t, "");
    }
//
    public ApplyObjectActionLevel2(ActionParameters acp, Level2Type t, String attributeKey) {
        super(acp);
        _lookingFor = t;
        _attrbKey = attributeKey;
    }

//    public ApplyObjectActionLevel2(ObjectProperties obj) {
//        super(obj);
//        _lookingFor = Level2Type.TEXT;
//    }


    public String value() throws Exception {
        try {
            if (_parameters.Current().isSearchable()) {
                if (_lookingFor == Level2Type.TEXT) {
//                    return GenericFunctions.coreGetText(new ObjectSearchParameters().setType(_type).setStoredObjectName(_localObjectSearchName).setObjectRepository(_localObject).setMultiObjectConfiguration(_selectMultiObjectConfig));
                    return GenericFunctions.coreGetText(_parameters);
                } else if (_lookingFor == Level2Type.ATTRIBUTE) {
//                    return GenericFunctions.coreGetAttribute(new ObjectSearchParameters().setType(_type).setStoredObjectName(_localObjectSearchName).setObjectRepository(_localObject).setMultiObjectConfiguration(_selectMultiObjectConfig).setActionParamAsString(_attrbKey));
                    _parameters.Current().setActionParamAsString(_attrbKey);
                    return GenericFunctions.coreGetAttribute(_parameters);
                } else return "";

            } else {

            }
        } catch (Exception e) {
            throw e;
        }
        return "";
    }


    public boolean isEqual(String cmpTxt) throws Exception {
        try {
            if (_parameters.Current().isSearchable()) {
                if (_lookingFor == Level2Type.TEXT) {
//                    String txt = GenericFunctions.coreGetText(new ObjectSearchParameters().setType(_type).setStoredObjectName(_localObjectSearchName).setObjectRepository(_localObject).setMultiObjectConfiguration(_selectMultiObjectConfig));
                    String txt = GenericFunctions.coreGetText(_parameters);
                    return (cmpTxt.equals(txt));
                } else if (_lookingFor == Level2Type.ATTRIBUTE) {
                    _parameters.Current().setActionParamAsString(_attrbKey);
//                    String txt = GenericFunctions.coreGetAttribute(new ObjectSearchParameters().setType(_type).setStoredObjectName(_localObjectSearchName).setObjectRepository(_localObject).setMultiObjectConfiguration(_selectMultiObjectConfig).setActionParamAsString(_attrbKey));
                    String txt = GenericFunctions.coreGetAttribute(_parameters);
                    return (cmpTxt.equals(txt));
                } else return false;
            } else {

            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }


}
