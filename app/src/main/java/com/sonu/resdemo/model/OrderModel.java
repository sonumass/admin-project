package com.sonu.resdemo.model;

/**
 * Created by Devesh D on 3/5/2018.
 */

public class OrderModel {
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

    public OrderModel(String item_name, String item_code, String price, String quantity,String item_actual_price) {
        this.item_name = item_name;
        this.item_code = item_code;
        this.price = price;
        this.quantity = quantity;
        this.item_actual_price=item_actual_price;
    }

    public String item_code;
    public String price;
    public String quantity;
}
