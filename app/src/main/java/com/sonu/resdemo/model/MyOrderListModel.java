package com.sonu.resdemo.model;

/**
 * Created by Devesh D on 3/22/2018.
 */

public class MyOrderListModel {
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String price;
    public String date;

    public MyOrderListModel(String price, String date, String orderno, String mobile) {
        this.price = price;
        this.date = date;
        this.orderno = orderno;
        this.mobile = mobile;
    }

    public String orderno;
    public String mobile;
}
