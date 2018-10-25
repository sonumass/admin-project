package com.sonu.resdemo.model;

/**
 * Created by Devesh D on 2/23/2018.
 */

public class CategoryModel {
    public CategoryModel(String item_name, String description, String item_code, String price, String discount_price, String veg_non_veg) {
        this.item_name = item_name;
        this.description = description;
        this.item_code = item_code;
        this.price = price;
        this.discount_price = discount_price;
        this.veg_non_veg = veg_non_veg;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getVeg_non_veg() {
        return veg_non_veg;
    }

    public void setVeg_non_veg(String veg_non_veg) {
        this.veg_non_veg = veg_non_veg;
    }

    public String item_name;
    public String description;
    public String item_code;
    public String price;
    public String discount_price;
    public String veg_non_veg;
}
