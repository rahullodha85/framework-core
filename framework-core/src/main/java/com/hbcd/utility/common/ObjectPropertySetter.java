package com.hbcd.utility.common;

import com.hbcd.utility.entity.ObjectProperties;

public interface ObjectPropertySetter {
    ObjectProperties setObjectName(String on);

    ObjectProperties setID(String id);

    ObjectProperties setName(String name);

    ObjectProperties setClassName(String cn);

    ObjectProperties setCssSelector(String cs);

    ObjectProperties setLinkText(String lt);

    ObjectProperties setPartialLinkText(String pl);

    ObjectProperties setTagName(String tn);

    ObjectProperties setXPath(String xp);

    ObjectProperties setIsParent(int i);

    ObjectProperties setIth(int i);

    ObjectProperties setContainsText(String ct);

    ObjectProperties setAttributeKey1(String ak1);

    ObjectProperties setAttributeValue1(String av1);

    ObjectProperties setAttributeKey2(String ak2);

    ObjectProperties setAttributeValue2(String av2);

    ObjectProperties setValue(String v);

    ObjectProperties setSQL(String s);

    ObjectProperties setReferenceDataId(String s);

    ObjectProperties setChild(Object o);

    ObjectProperties setUserDefinedExplicitWaitTime(int mt);

    ObjectProperties setUserDefinedThreadWaitTime(int mt);

    ObjectProperties setIsSelectedWithNotVisible(int isSet);

    ObjectProperties setIsSelectedWithDisable(int isSet);

}
