package com.hbcd.utility.testscriptdata;

import javax.xml.bind.annotation.XmlElement;

public class GiftCard {

    private String cardNumber;
    private String pin;

    public GiftCard() {

    }

    public GiftCard(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    @XmlElement(name = "CardNumber")
    public String getGiftCard() {
        return cardNumber;
    }

    public void setGiftCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @XmlElement(name = "PIN")
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

}
