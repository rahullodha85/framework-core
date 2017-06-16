package com.hbcd.scripting.core;

import com.hbcd.common.utility.ObjectPropertyUtility;
import com.hbcd.scripting.core.fluentInterface.MultiObjectsAction;
import com.hbcd.scripting.core.fluentInterface.ObjectAction;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectProperties;
import com.hbcd.utility.entity.ObjectSearchParameters;
import com.hbcd.utility.entity.SelectMultiObjectConfiguration;

public class Find {


    private static Object _getObjectProperties(Object obj, boolean isSingle) throws Exception {
        ObjectProperties _localObject =  ObjectPropertyUtility.determineAndGetObjectProperties(obj);
        ActionParameters acp = new ActionParameters();
        if (isSingle) {
            acp.add(new ObjectSearchParameters().setObjectRepository(_localObject));
            return new ApplyObjectAction(acp);
        }
        else //is Multiple Object
        {
            acp.add(new ObjectSearchParameters().setType(1).setObjectRepository(_localObject).setMultiObjectConfiguration(new SelectMultiObjectConfiguration()));
            return new ApplyMultiObjectsAction(acp);
        }
    }

    public static ObjectAction Object(String objNm) throws Exception {
        try {
            return (ApplyObjectAction) _getObjectProperties(objNm, true);
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

    public static MultiObjectsAction MultipleObjects(String objNm) throws Exception {
        try {
            return (ApplyMultiObjectsAction) _getObjectProperties(objNm, false);
        } catch (Exception e) {
            throw e;
        }
    }

    public static MultiObjectsAction MultipleObjects(ObjectProperties obj) throws Exception {
        try {
            return (ApplyMultiObjectsAction) _getObjectProperties(obj, false);
        } catch (Exception e) {
            throw e;
        }
    }

    public static ObjectAction ObjectToHighlight(String objNm) throws Exception {
        try {
            return Object(objNm);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static ObjectAction ObjectWith(String parentClassName,
                                          String keyType, String keyId, String propertyClassWanted) {
        ObjectProperties _localObject = new ObjectProperties().setClassName(parentClassName)
                .setIsParent(1)
                .setAttributeKey1(keyType)
                .setAttributeValue1(keyId);
        if (_localObject.child == null)
            _localObject.child = new ObjectProperties();
        _localObject.child.setCssSelector(propertyClassWanted);
        ActionParameters acp = new ActionParameters();
        acp.add(new ObjectSearchParameters().setObjectRepository(_localObject));
        return new ApplyObjectAction(acp);
    }

    public static ObjectAction ObjectWith(String parentClassNm, String keyType,
                                          String keyId, String propertyClassWanted, int location) {
        ObjectProperties _localObject = new ObjectProperties("", "", parentClassNm)
                .setIsParent(1)
                .setAttributeKey1(keyType)
                .setAttributeValue1(keyId);

        if (_localObject.child == null)
            _localObject.child = new ObjectProperties();

        _localObject.child.setClassName(propertyClassWanted);
        _localObject.child.setIth(location);

        ActionParameters acp = new ActionParameters();
        acp.add(new ObjectSearchParameters().setObjectRepository(_localObject));
        return new ApplyObjectAction(acp);
    }

    public static ObjectAction LinkWithText(boolean hasParent,
                                            String cssSelectParent, String partialLinkText) {
        ObjectProperties _localObject = new ObjectProperties(
                "LinkWithText_CommonFunction");
        if (hasParent) {
            _localObject.setIsParent(1)
                    .setCssSelector(cssSelectParent) // div[class='sbHolder cclist']
                    .setIth(1);
            if (_localObject.child == null) {
                _localObject.child = new ObjectProperties();
            }
            _localObject.child.setPartialLinkText(partialLinkText);
        } else {
            _localObject.setIth(1)
                    .setPartialLinkText(partialLinkText);
        }

        ActionParameters acp = new ActionParameters();
        acp.add(new ObjectSearchParameters().setObjectRepository(_localObject));
        return new ApplyObjectAction(acp);
    }
}
