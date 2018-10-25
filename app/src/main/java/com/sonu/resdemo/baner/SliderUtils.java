package com.sonu.resdemo.baner;

public class SliderUtils {


  public String getAddlink() {
    return addlink;
  }

  public void setAddlink(String addlink) {
    this.addlink = addlink;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String sliderImageUrl;

  public SliderUtils(String sliderImageUrl, String addlink, String id) {
    this.sliderImageUrl = sliderImageUrl;
    this.addlink = addlink;
    this.id = id;
  }

  public String addlink;
  public String id;


    public String getSliderImageUrl() {
      return sliderImageUrl;
    }

    public void setSliderImageUrl(String sliderImageUrl) {
      this.sliderImageUrl = sliderImageUrl;
    }
  }
