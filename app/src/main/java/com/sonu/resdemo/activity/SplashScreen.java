package com.sonu.resdemo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.sonu.resdemo.R;
import com.sonu.resdemo.ShimmerActivity;
import com.sonu.resdemo.scripts.Scripts;
import com.sonu.resdemo.utils.CommonFunctions;
import com.sonu.resdemo.utils.Preferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SplashScreen extends AppCompatActivity {

    private static ArrayList<String> data;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        data = new ArrayList<String>();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
               /* Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(i);

                // close this activity
                finish();*/
                if(CommonFunctions.isConnected(SplashScreen.this)){
                    new MenuAsyntask().execute();
                  //  String strr=getLocationFromAddress("Khalid Bin Waleed rd. P.O.Box- 5868 Bur Dubai,Dubai");
                   // Toast.makeText(SplashScreen.this,strr,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SplashScreen.this,"No Internet!", Toast.LENGTH_LONG).show();
                }
            }
        }, SPLASH_TIME_OUT);
    }
    class MenuAsyntask extends AsyncTask<String, Void, String> {
        String f_id = "", field = "", path;
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(SplashScreen.this);
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Please wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            try{
                result= Scripts.GetMenu("1",CommonFunctions.getDeviceIMEI(SplashScreen.this),"1");
            }catch (Exception e){
                Log.e("error","errroe");
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("script rasponse", s);
            progress.cancel();
            try{
                Preferences pref=new Preferences(SplashScreen.this);
                pref.storeString("list",s);
                Log.e("list",s);
              JSONObject jsonObject=new JSONObject(s);

                String str=jsonObject.getString("success");
                switch (str){
                    case "1":


                        Intent i = new Intent(SplashScreen.this, HomeActivity.class);

                        startActivity(i);

                        // close this activity
                        finish();

                        pref.storeString("open","1");
           /* String str=jsonObject.getString("success");
                switch (str){
                    case "1":

                        JSONArray cast = jsonObject.getJSONArray("data");
                        for (int i=0; i<cast.length(); i++) {
                            JSONObject jsonnewsobject = cast.getJSONObject(i);
                            String menu_name =jsonnewsobject.getString("menu_name");
                            String menu_code = jsonnewsobject.getString("menu_code");
                            String coupon = jsonnewsobject.getString("coupon");
                            data.add(menu_name);
                        }*//*
                        Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                        i.putStringArrayListExtra("list",data);
                        startActivity(i);

                        // close this activity
                        finish();
*/
                        break;
                    case "0":
                        Toast.makeText(SplashScreen.this,"Somthing want wrong!", Toast.LENGTH_LONG).show();
                        break;
                }

            }catch (Exception e){
                Log.e("error","error in branch uploaded data");
            }
        }
    }
    public String getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
     String str ="";

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();
str=""+ location.getLatitude()+location.getLongitude();

        }catch (Exception e){
            Log.e("ERROR","YES");
        }
        return str;
    }
}
