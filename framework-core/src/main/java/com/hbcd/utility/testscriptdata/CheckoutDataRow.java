package com.hbcd.utility.testscriptdata;

import java.util.ArrayList;
import java.util.List;

public class CheckoutDataRow {

    private Login loginInfo;
    private GiftDetails giftInfo;
    private CreditCard creditCardInfo;
    private Address addressInfo;
    private SkuIDs skuListInfo;
    private URL urlInfo;
    private Promo promoInfo;

    public List<String> getSkuListInfo() {
        return skuListInfo.getSkuList();
    }


    public String getLoginPassword() {
        return loginInfo.getPassword();
    }

    public String getEmailID() {
        return loginInfo.getEmail();
    }

    public String getPromo() {
        return promoInfo.getPromoList().get(0);
    }

    public List<String> getPromoList() {
        return promoInfo.getPromoList();
    }

    public int getPercentage() {
        return Integer.parseInt(promoInfo.getPercentage().get(0));
    }

    public List<Integer> getPromoPercentageList() {
        List<Integer> r = new ArrayList<Integer>();
        for (String s : promoInfo.getPercentage())
            r.add(Integer.parseInt(s));
        return r;
    }

    public String getGiftCardNumber() {
        return giftInfo.getGiftCardNumber();
    }

    public String getPin() {
        return giftInfo.getPin();
    }

    public String getCreditCardIndexNumber() {
        return creditCardInfo.getCreditCardIndexNumber().get(0);
    }

    public String getAddress1() {
        return addressInfo.getAddress1().get(0);
    }

    public List<String> getAddress1List() {
        return addressInfo.getAddress1();
    }

    public String getCity() {
        return addressInfo.getCity().get(0);
    }

    public List<String> getAddress1City() {
        return addressInfo.getCity();
    }


    public String getState() {
        return addressInfo.getState().get(0);
    }

    public String getProvince(){
        return addressInfo.getProvince().get(0);
    }

    public List<String> getProvinceList(){
        return addressInfo.getProvince();
    }

    public List<String> getStateList() {
        return addressInfo.getState();
    }

    public List<String> getCountryList() {
        return addressInfo.getCountry();
    }

    public String getCountry() {
        return addressInfo.getCountry().get(0);
    }

    public String getZipCode() {
        return addressInfo.getZipCode().get(0);
    }

    public String getCCNumber() {
        return creditCardInfo.getNumber().get(0);
    }

    public String getName() {
        return creditCardInfo.getName().get(0);
    }

    public String getCCType() {
        return creditCardInfo.getType().get(0);
    }

    public String getCVV() {
        return creditCardInfo.getCVV().get(0);
    }

    public String getExpMonth() {
        return creditCardInfo.getExpMonth().get(0);
    }

    public String getExpYear() {
        return creditCardInfo.getExpYear().get(0);
    }

    public String getURL() {
        return urlInfo.getURL();
    }


    public List<String> getCityList() {
        return addressInfo.getCity();

    }

    public List<String> getZipCodeList() {
        return addressInfo.getZipCode();

    }

}
