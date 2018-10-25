package com.sonu.resdemo.model;

/**
 * Created by Devesh D on 2/23/2018.
 */

public class MenuModel {
    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_code() {
        return menu_code;
    }

    public void setMenu_code(String menu_code) {
        this.menu_code = menu_code;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String menu_name;

    public MenuModel(String menu_name, String menu_code, String coupon) {
        this.menu_name = menu_name;
        this.menu_code = menu_code;
        this.coupon = coupon;
    }

    public String menu_code;
    public String coupon;
}
