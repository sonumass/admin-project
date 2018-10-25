package com.sonu.resdemo.model;

public class CouponModel {
  public CouponModel(String id, String code, String description, String count, String price, String datetime) {
    this.id = id;
    this.code = code;
    this.description = description;
    this.count = count;
    this.price = price;
    this.datetime = datetime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCount() {
    return count;
  }

  public void setCount(String count) {
    this.count = count;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getDatetime() {
    return datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  public String id;
  public String code;
  public String description;
  public String count;
  public String price;
  public String datetime;


}
