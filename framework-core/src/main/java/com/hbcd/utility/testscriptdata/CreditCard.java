package com.hbcd.utility.testscriptdata;

import java.util.List;

public class CreditCard {
    private List<String> creditCardIndexNumber;
    private List<String> type;
    private List<String> number;
    private List<String> cvv;
    private List<String> expMonth;
    private List<String> expYear;
    private List<String> name;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getNumber() {
        return number;
    }

    public void setNumber(List<String> number) {
        this.number = number;
    }

    public List<String> getCVV() {
        return cvv;
    }

    public void setCVV(List<String> cvv) {
        this.cvv = cvv;
    }

    public List<String> getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(List<String> expMonth) {
        this.expMonth = expMonth;
    }

    public List<String> getExpYear() {
        return expYear;
    }

    public void setExpYear(List<String> expYear) {
        this.expYear = expYear;
    }


    public List<String> getCreditCardIndexNumber() {
        return creditCardIndexNumber;
    }

    public void setCreditCardIndexNumber(List<String> creditCardIndexNumber) {
        this.creditCardIndexNumber = creditCardIndexNumber;
    }


}
