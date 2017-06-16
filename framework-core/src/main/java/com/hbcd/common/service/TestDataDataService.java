package com.hbcd.common.service;

import com.hbcd.utility.entity.TestData;
/**
 * Created by ephung on 2/25/2016.
 */
public class TestDataDataService extends DataServiceBase<TestData> {
    public TestDataDataService()
    {
        _allowRepetition = false; //allow
        _startExcelColumnName = "DataName";
    }
}
