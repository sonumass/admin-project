package com.sonu.resdemo.utils;


import java.util.HashMap;
import java.util.Map;

public class Scripts {

	public static final String mainUrl = "http://salasarservices.com/cropinsurance/api_v2/";
	public static final String cce_upload="cce_photo_upload";
	public static final String nonloanee_photo_upload="nonloanee_photo_upload";
	public static final String claimsurvey_photo_upload="claim_survey_photo";
	public static final String loanee_photo_upload="loanee_photo";
	public static final String loanee_excel="loanee_excel";
	public static final String crophealthsurvey_photo_upload="health_survey_photo";
	public static String login(String email,String password,String IMEI) {

		Map<String, Object> params = new HashMap<>();
		params.put("user_name", email);
		params.put("user_pass", password);
		params.put("imei",IMEI);
		return JSONParser.httpService(mainUrl + "login", params,"post") ;
	}
	public static String Tracking(Map<String, Object> params) {


		return JSONParser.httpService(mainUrl + "tracking", params,"post") ;
	}
	public static String cceSend(Map<String, Object> params) {


		return JSONParser.httpService(mainUrl + "cce", params,"post") ;
	}

	public static String getMultipleCCE(String random_number, String id, String imei) {

		Map<String, Object> params = new HashMap<>();
		params.put("rand_no_cce", random_number);
		params.put("id", id);

		params.put("imei",imei);
		return JSONParser.httpService(mainUrl + "pre_cce_data", params,"post") ;
	}

	//nonloanee
	public static String nonloaneeSend(Map<String, Object> params) {

	return JSONParser.httpService(mainUrl + "nonloanee", params,"post") ;
	}
	public static String PremiumCalcSend(Map<String, Object> params) {
		return JSONParser.httpService(mainUrl + "get_premium", params,"post") ;
	}

	public static String nonloaneeSendImage(Map<String, Object> params) {


		return JSONParser.httpService(mainUrl + "nonloanee_photo_upload", params,"post") ;

	}

	//
	public static String claimsurveySend(Map<String, Object> params) {


		return JSONParser.httpService(mainUrl + "claim_survey", params,"post") ;
	}

	public static String LoneeSend(Map<String, Object> params) {


		return JSONParser.httpService(mainUrl + "loanee_text", params,"post") ;
	}

	public static String crophealthsurveySend(Map<String, Object> params) {


		return JSONParser.httpService(mainUrl + "health_survey", params,"post") ;
	}

	/*
	expense data method
	 */

	public static String expensedatasenttoserver(Map<String, Object> params) {


		return JSONParser.httpService(mainUrl + "set_expences_data", params,"post") ;
	}
}
