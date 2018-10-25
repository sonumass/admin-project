package com.sonu.resdemo.scripts;


import java.util.HashMap;
import java.util.Map;

public class Scripts {

	public static final String mainUrl = "http://verbusitech.com/sonu/dev/resdemo/";

	public static final String MENU="menu.php";
	public static final String CATEGORY="category.php";
	public static final String SEARCHDATA="searchdata.php";
	public static final String USER="userdetail.php";
	public static final String MOBILEVALIDATION="mobilevalidate.php";
	public static final String ORDERDATA="orderdata.php";
	public static final String ORDERNO="myorder.php";
	public static final String ORDERDETAIL="myorder_detail.php";
	public static final String FEEDBACK="feedback.php";
  public static final String THANKYOU="notification.php";
  public static final String COUPONLIST="couponlist.php";
  public static final String baner="baner.php";
  public static final String COUPONAPPLIED="coupon_apply.php";


  public static String GetCouponList(String imei) {

    Map<String, Object> params = new HashMap<>();


    params.put("imei",imei);
    //return JSONParser.httpService(mainUrl + "login", params,"post") ;
    return JSONParser.httpService(mainUrl+COUPONLIST, params,"GET") ;
  }
  public static String GetCouponApplied(String imei,String code) {

    Map<String, Object> params = new HashMap<>();


    params.put("imei",imei);
    params.put("code",code);
    //return JSONParser.httpService(mainUrl + "login", params,"post") ;
    return JSONParser.httpService(mainUrl+COUPONAPPLIED, params,"GET") ;
  }
  public static String GetBaner(String imei) {

    Map<String, Object> params = new HashMap<>();


    params.put("imei",imei);
    //return JSONParser.httpService(mainUrl + "login", params,"post") ;
    return JSONParser.httpService(mainUrl+baner, params,"GET") ;
  }

  public static String GetThankyou(String imei) {

    Map<String, Object> params = new HashMap<>();


    params.put("imei",imei);
    //return JSONParser.httpService(mainUrl + "login", params,"post") ;
    return JSONParser.httpService(mainUrl+"/message/"+THANKYOU, params,"GET") ;
  }

	public static String OrderData(String mobile,String quantity,String item_code,String item_name,String item_price,String actual_price,String orderno,String imei,String datetime,String orderprice,String code,String coupon_price) {

		Map<String, Object> params = new HashMap<>();
		params.put("mobile",mobile);
		params.put("item_quantity",quantity);
		params.put("item_code",item_code);
		params.put("item_name",item_name);
		params.put("item_price",item_price);
		params.put("actual_price",actual_price);
		params.put("orderno",orderno);
		params.put("imei",imei);
		params.put("orderprice",orderprice);
		params.put("datetime",datetime);
    params.put("code",code);
    params.put("coupon_price",coupon_price);
		//params.put("reg_id",reg_id);
		//return JSONParser.httpService(mainUrl + "login", params,"post") ;
		return JSONParser.httpService(mainUrl+ORDERDATA, params,"GET") ;
	}

	public static String GetMenu(String status,String imei,String reg_id) {

		Map<String, Object> params = new HashMap<>();

		params.put("status",status);
		params.put("reg_id",reg_id);
		params.put("imei",imei);
		//return JSONParser.httpService(mainUrl + "login", params,"post") ;
		return JSONParser.httpService(mainUrl+MENU, params,"GET") ;
	}

	public static String ValidateMobile(String code,String user_id) {

		Map<String, Object> params = new HashMap<>();

		params.put("code",code);
		params.put("user_id",user_id);
		//params.put("reg_id",reg_id);
		//return JSONParser.httpService(mainUrl + "login", params,"post") ;
		return JSONParser.httpService(mainUrl+MOBILEVALIDATION, params,"GET") ;
	}

	public static String UserSignup(String name,String mobile,String mobileverifiecode,String address,String imei,String email,String datetime) {

		Map<String, Object> params = new HashMap<>();

		params.put("name",name);
		params.put("mobile",mobile);
		params.put("mobileverifiecode",mobileverifiecode);
		params.put("address",address);
		params.put("imei",imei);
		params.put("email",email);
		params.put("datetime",datetime);
		//params.put("reg_id",reg_id);
		//return JSONParser.httpService(mainUrl + "login", params,"post") ;
		return JSONParser.httpService(mainUrl+USER, params,"GET") ;
	}
	public static String GetCategory(String menu_code) {

		Map<String, Object> params = new HashMap<>();

		params.put("menu_code",menu_code);
		//params.put("reg_id",reg_id);
		//return JSONParser.httpService(mainUrl + "login", params,"post") ;
		return JSONParser.httpService(mainUrl +CATEGORY, params,"GET") ;
	}
	public static String GetMyOrder(String imei) {

		Map<String, Object> params = new HashMap<>();

		params.put("imei",imei);
		//params.put("reg_id",reg_id);
		//return JSONParser.httpService(mainUrl + "login", params,"post") ;
		return JSONParser.httpService(mainUrl +ORDERNO, params,"GET") ;
	}

	public static String GetOrderDetail(String orderno) {

		Map<String, Object> params = new HashMap<>();

		params.put("orderno",orderno);
		//params.put("reg_id",reg_id);
		//return JSONParser.httpService(mainUrl + "login", params,"post") ;
		return JSONParser.httpService(mainUrl +ORDERDETAIL, params,"GET") ;
	}

	public static String SendFeedback(String mobile,String message,String date_time) {

		Map<String, Object> params = new HashMap<>();

		params.put("mobile",mobile);
		params.put("message",message);
		params.put("date_time",date_time);
		//params.put("reg_id",reg_id);
		//return JSONParser.httpService(mainUrl + "login", params,"post") ;
		return JSONParser.httpService(mainUrl +FEEDBACK, params,"GET") ;
	}
	public static String GetSearchData(String status) {

		Map<String, Object> params = new HashMap<>();

		params.put("status","1");
		//params.put("reg_id",reg_id);
		//return JSONParser.httpService(mainUrl + "login", params,"post") ;
		return JSONParser.httpService(mainUrl +SEARCHDATA, params,"GET") ;
	}
	public static String GetEntertainment(String status) {

		Map<String, Object> params = new HashMap<>();

		params.put("status",status);
		//params.put("reg_id",reg_id);
		//return JSONParser.httpService(mainUrl + "login", params,"post") ;
		return JSONParser.httpService(mainUrl + "entertaimentnews.php", params,"GET") ;
	}



}
