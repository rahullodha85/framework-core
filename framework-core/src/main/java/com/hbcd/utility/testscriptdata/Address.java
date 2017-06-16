package com.hbcd.utility.testscriptdata;

import java.util.List;

public class Address {


    private List<String> address1;
    private List<String> city;
    private List<String> state;
    private List<String> country;
    private List<String> zipCode;
    private List<String> phone;
    private List<String> email;
    private List<String> province;


    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public List<String> getAddress1() {
        return address1;
    }

    public void setAddress1(List<String> address1) {
        this.address1 = address1;
    }

    public List<String> getCity() {
        return city;
    }

    public void setCity(List<String> city) {
        this.city = city;
    }

    public List<String> getProvince(){
        return province;
    }

    public void setProvince(List<String> province){
        this.province = province;
    }

    public List<String> getState() {
        return state;
    }

    public void setState(List<String> state) {
        this.state = state;
    }

    public List<String> getCountry() {
        return country;
    }

    public void setCountry(List<String> country) {
        this.country = country;
    }

    public List<String> getZipCode() {
        return zipCode;
    }

    public void setZipCode(List<String> zipCode) {
        this.zipCode = zipCode;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }


}
