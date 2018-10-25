package com.sonu.resdemo.model;

public class TypeModel {

  public String getItemcode() {
    return itemcode;
  }

  public void setItemcode(String itemcode) {
    this.itemcode = itemcode;
  }

  public String getType_name() {
    return type_name;
  }

  public void setType_name(String type_name) {
    this.type_name = type_name;
  }

  public TypeModel(String itemcode, String type_name) {
    this.itemcode = itemcode;
    this.type_name = type_name;
  }

  public String itemcode;
  public String type_name;
}
