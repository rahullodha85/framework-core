package com.hbcd.utility.common;

public class Type {
    public enum PropertyType {
        ID("setID"),
        NAME("setName"),
        CLASS("setClassName"),
        CSS_SELECTOR("setCssSelector"),
        CSS_SELECTOR2("setCssSelector"),
        CSS_SELECTOR3("setCssSelector"),
        LINK_TEXT("setLinkText"),
        PARTIAL_LINK_TEXT("setPartialLinkText"),
        TAG_NAME("setTagName"),
        XPATH("setXPath"),
        CONTAINS_TEXT("setContainsText"),
        ATTRIBUTE_KEY1("setAttributeKey1"),
        ATTRIBUTE_VALUE1("setAttributeValue1"),
        ATTRIBUTE_KEY2("setAttributeKey2"),
        ATTRIBUTE_VALUE2("setAttributeValue2"),
        VALUE("setValue"),
        SQL("setSQL"),
        REFERENCE_DATA_ID("setReferenceDataId"),
        UNUSED_COLUMN1("setUnUsedColumn1"),
        UNUSED_COLUMN2("setUnUsedColumn2"),
        UNUSED_COLUMN3("setUnUsedColumn3");

        PropertyType(final String value) {
            this.value = value;
        }

        public String getvalue() {
            return value;
        }

        private String value;
    }

    public enum PropertyIntType {
        iTH("setIth"),
        IS_PARENT("setIsParent"),
        USER_DEFINED_EXPLICIT_WAIT_TIME("setUserDefinedExplicitWaitTime"),
        USER_DEFINED_THREAD_WAIT_TIME("getUserDefinedThreadWaitTime");

        PropertyIntType(final String value) {
            this.value = value;
        }

        public String getvalue() {
            return value;
        }

        private String value;
    }

    public enum Level2Type {TEXT, ATTRIBUTE}

}
