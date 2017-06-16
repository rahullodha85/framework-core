package com.hbcd.utility.testscriptdata;

import java.util.List;

public class PromoItemRow {
    private SkuIDs skuListInfo;
    List<data> promos;

    public class data {
        String brand;
        double prices;
        int quantity;
        int checkout;
        String item;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public double getPrices() {
            return prices;
        }

        public void setPrices(double prices) {
            this.prices = prices;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getCheckout() {
            return checkout;
        }

        public void setCheckout(int checkout) {
            this.checkout = checkout;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

    }

    public List<data> getPromos() {
        return promos;
    }

    public void setPromos(List<data> promos) {
        this.promos = promos;
    }

    public List<String> getSkuListInfo() {
        return skuListInfo.getSkuList();
    }
}
