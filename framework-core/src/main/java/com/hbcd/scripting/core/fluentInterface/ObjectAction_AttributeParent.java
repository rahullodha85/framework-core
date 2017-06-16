package com.hbcd.scripting.core.fluentInterface;

public interface ObjectAction_AttributeParent extends ObjectAction_CommonAction {
    ObjectAction_AttributeChild changeAttributeExactValue(String key, String value) throws Exception;

    ObjectAction_AttributeChild changeAttributeContainsValue(String key, String value) throws Exception;

    ObjectAction_AttributeChild changePartialLinkText(String value) throws Exception;

    ObjectAction_AttributeChild changeContainsText(String value) throws Exception;

    ObjectAction_AttributeChild changeId(String value) throws Exception;
}
