package com.sonu.resdemo.model;

/**
 * Created by Devesh D on 3/8/2018.
 */

public class OrderDataModel {
    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String orderno;
    public  String user_id;
    public String getItem_actual_price() {
    return item_actual_price;
}

    public void setItem_actual_price(String item_actual_price) {
        this.item_actual_price = item_actual_price;
    }

    public String item_actual_price;
    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String item_name;

    public OrderDataModel(String item_name, String item_code, String price, String quantity,String item_actual_price,String orderno,String user_id,String datetime) {
        this.item_name = item_name;
        this.item_code = item_code;
        this.price = price;
        this.quantity = quantity;
        this.orderno = orderno;
        this.user_id = user_id;
        this.item_actual_price=item_actual_price;
        this.datetime=datetime;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public  String datetime;
    public String item_code;
    public String price;
    public String quantity;
}

