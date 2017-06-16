package com.hbcd.common.service;

import com.hbcd.utility.testscriptdata.CheckoutDataRow;

import java.util.List;

public class DataCheckoutRepository {

    static List<CheckoutDataRow> _myCheckoutData;
    static String checkoutData;

    public DataCheckoutRepository(String checkoutData) {
        //_myCheckoutData.add();
    }

    public void setList(List<CheckoutDataRow> _myCheckoutData) {
        DataCheckoutRepository._myCheckoutData = _myCheckoutData;
    }

    public List<CheckoutDataRow> getList() {
        return _myCheckoutData;
    }

    public static CheckoutDataRow find(String key) {

        return new CheckoutDataRow();
    }
}
