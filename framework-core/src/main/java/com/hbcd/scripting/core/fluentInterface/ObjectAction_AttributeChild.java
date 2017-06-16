package com.hbcd.scripting.core.fluentInterface;

public interface ObjectAction_AttributeChild extends ObjectAction_CommonAction {
    ObjectAction_CommonAction changeChildAttributeExactValue(String key, String value) throws Exception;

    ObjectAction_CommonAction changeChildAttributeContainsValue(String key, String value) throws Exception;

    ObjectAction_CommonAction changeChildContainsText(String value) throws Exception;

    ObjectAction_CommonAction changeChildPartialLinkText(String value) throws Exception;

    ObjectAction_CommonAction changeChildithElement(int value) throws Exception;
}
