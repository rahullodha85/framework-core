package com.hbcd.utility.common;

public interface ObjectPropertyGetter {
    String getProperty(String subPro);

    String getObjectName();

    String getID();

    String getName();

    String getClassName();

    String getCssSelector();

    String getLinkText();

    String getPartialLinkText();

    String getTagName();

    String getXPath();

    int getIsParent();

    int getIth();

    String getContainsText();

    String getAttributeKey1();

    String getAttributeValue1();

    String getAttributeKey2();

    String getAttributeValue2();

    String getValue();

    String getSQL();

    String getReferenceDataId();

    int getUserDefinedExplicitWaitTime();

    int getUserDefinedThreadWaitTime();

    int getIsSelectedWithNotVisible();

    int getIsSelectedWithDisable();

}
