package com.hbcd.utility.testscriptdata;

import java.util.ArrayList;
import java.util.List;

public class DSGSmokeTestData {

    public class Item {
        private String _webID = "xxxx";
        private String _price = "5.00";
        private String _quantity = "1";
        private String _size = "";
        private String _color = "";

        public String getWebID() {
            return _webID;
        }

        public void setWebID(String _webID) {
            this._webID = _webID;
        }

        public String getPrice() {
            return _price;
        }

        public void setPrice(String _price) {
            this._price = _price;
        }

        public String getQuantity() {
            return _quantity;
        }

        public void setQuantity(String _quantity) {
            this._quantity = _quantity;
        }
    }

    private static List<Item> _items = new ArrayList<Item>();
    private String _registerUserLogin = "";

    public String getRegisterUserLogin() {
        return _registerUserLogin;
    }

    public void setRegisterUserLogin(String _registerUserLogin) {
        this._registerUserLogin = _registerUserLogin;
    }

    public String getRegisterUserPassword() {
        return _registerUserPassword;
    }

    public void setRegisterUserPassword(String _registerUserPassword) {
        this._registerUserPassword = _registerUserPassword;
    }

    private String _registerUserPassword = "";

    public static Item getItem(int idx) {
        return _items.get(idx);
    }


}
