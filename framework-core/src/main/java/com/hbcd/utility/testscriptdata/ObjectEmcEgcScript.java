package com.hbcd.utility.testscriptdata;

import javax.xml.bind.annotation.XmlElement;

public class ObjectEmcEgcScript {

    String _type;
    String _id;
    int _pin = 0;
    int _balance = 0;

    @XmlElement(name = "Type")
    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }


    @XmlElement(name = "ID")
    public String getID() {
        return _id;
    }

    public void setID(String id) {
        _id = id;
    }


    @XmlElement(name = "PIN")
    public int getPin() {
        return _pin;
    }

    public void setPin(int pin) {
        _pin = pin;
    }

    @XmlElement(name = "Balance")
    public int getBalance() {
        return _balance;
    }

    public void setBalance(int balance) {
        _balance = balance;
    }


}
