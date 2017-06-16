package com.hbcd.common.utility;

public enum ServiceName {

    OBJECT_REPOSITORY("com.hbcd.common.service.ObjectRepositoryDataService", "com.hbcd.utility.entity.ObjectProperties"),
    DATA_STORAGE("DataStorage", ""),
    TEST_SUITE("com.hbcd.common.service.TestSuiteDataService", "com.hbcd.utility.entity.ObjectTestScript"),
    GIFT_CARD("com.hbcd.common.service.GiftcardService", ""),
    TEST_DATA("com.hbcd.common.service.TestDataDataService", "com.hbcd.utility.entity.TestData");

    private final String _serviceclassName;
    private final String _entityclassName;

    //private final String _a;
    ServiceName(String scn, String ecn) {
        _serviceclassName = scn;
        _entityclassName = ecn;
    }

    public String getServiceClassName() {
        return _serviceclassName;
    }

    public String getEntityClassName() {
        return _entityclassName;
    }

}
