package com.sonu.resdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import com.sonu.resdemo.R;
import com.sonu.resdemo.ShimmerActivity;
import com.sonu.resdemo.app.Config;
import com.sonu.resdemo.scripts.Scripts;
import com.sonu.resdemo.utils.CommonFunctions;
import com.sonu.resdemo.utils.DatabaseHandler;
import com.sonu.resdemo.utils.NotificationUtils;
import com.sonu.resdemo.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    String regId;DatabaseHandler db;
    private static ArrayList<String> data;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
List<String> str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainn);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
      int version = android.os.Build.VERSION.SDK_INT;
      str=new ArrayList<String>();

      /*for (int i = 0; i < 5000; i++) {
        String  orderno = ""+((int)(Math.random()*9000)+1000);
        if(str.contains(orderno)){
         // Toast.makeText(MainActivity.this,"Duplicate",Toast.LENGTH_SHORT).show();
          Log.e("hhhhhhhhhhhh",orderno);
        }
        str.add(orderno);
       // Log.e("tipe",orderno);


      }*/
      db=new DatabaseHandler(MainActivity.this);
      if (version >= android.os.Build.VERSION_CODES.M) {
        Log.i("onclick 2", "onclick");
        int i = 0;

        int hasNetworkState = checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE);
        int hasWriteExternalPermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasFineLocation = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocation = checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int hasCallPhone = checkSelfPermission(android.Manifest.permission.CALL_PHONE);
        int hasphonestate = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
        int hasgetcontact = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
        List<String> permissions = new ArrayList<String>();

        if (hasNetworkState != PackageManager.PERMISSION_GRANTED) {
          permissions.add(android.Manifest.permission.ACCESS_NETWORK_STATE);

        }

        if (hasWriteExternalPermission != PackageManager.PERMISSION_GRANTED) {
          permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }
        if (hasFineLocation != PackageManager.PERMISSION_GRANTED) {
          permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (hasCoarseLocation != PackageManager.PERMISSION_GRANTED) {
          permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (hasCallPhone != PackageManager.PERMISSION_GRANTED) {
          permissions.add(Manifest.permission.CALL_PHONE);
        }
        if (hasphonestate != PackageManager.PERMISSION_GRANTED) {
          permissions.add(Manifest.permission.READ_PHONE_STATE);

        }
        if (hasgetcontact != PackageManager.PERMISSION_GRANTED) {
          permissions.add(Manifest.permission.GET_ACCOUNTS);


        }
        if(hasphonestate!=PackageManager.PERMISSION_GRANTED &&hasCallPhone != PackageManager.PERMISSION_GRANTED && hasWriteExternalPermission != PackageManager.PERMISSION_GRANTED && hasFineLocation!= PackageManager.PERMISSION_GRANTED ){
          requestPermissions(permissions.toArray(new String[permissions.size()]), 1);
        }else {

        }
        call();



      } else {
     call();
      }

    }

    public void call(){
      txtRegId = (TextView) findViewById(R.id.txt_reg_id);
      txtMessage = (TextView) findViewById(R.id.txt_push_message);
      Log.i("onclick 2", "onclick");
      int i = 0;
      Preferences pref=new Preferences(MainActivity.this);

      db.clearorder();
      pref.storeString("code","");
      pref.storeString("price","");
      pref.storeString("id","");
      mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

          // checking for type intent filter
          if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
            // gcm successfully registered
            // now subscribe to `global` topic to receive app wide notifications
            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

            displayFirebaseRegId();

          } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
            // new push notification is received

            String message = intent.getStringExtra("message");

            Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

            txtMessage.setText(message);
            displayFirebaseRegId();
          }
        }
      };

      displayFirebaseRegId();
    }
  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case 1: {
        for (int i = 0; i < permissions.length; i++) {
          if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

           if (i + 1 == permissions.length) {
              call();

              }
            Log.d("Permissions", "Permission Granted: " + permissions[i]);
          } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
            Log.d("Permissions", "Permission Denied: " + permissions[i]);
          }
        }
      }
      break;
      default: {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      }
    }
  }
    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
         regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {

            data = new ArrayList<String>();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    if(CommonFunctions.isConnected(MainActivity.this)){
                        new MenuAsyntask().execute();

                    }else {
                        Toast.makeText(MainActivity.this,"No Internet!", Toast.LENGTH_LONG).show();
                    }
                }
            }, SPLASH_TIME_OUT);
        }else{
            txtRegId.setText("Firebase Reg Id is not received yet!");
          Toast.makeText(MainActivity.this,"Firebase Reg Id is not received yet!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    class MenuAsyntask extends AsyncTask<String, Void, String> {
        String f_id = "", field = "", path;
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Please wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            try{
                result= Scripts.GetMenu("1",CommonFunctions.getDeviceIMEI(MainActivity.this),regId);
              //result= Scripts.GetMenu("1");
            }catch (Exception e){
                Log.e("error",e.getMessage());
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("script rasponse", s);
            progress.cancel();
            try{
                Preferences pref=new Preferences(MainActivity.this);
                pref.storeString("list",s);
                Log.e("list",s);
                JSONObject jsonObject=new JSONObject(s);

           String str=jsonObject.getString("success");
                switch (str){
                    case "1":
                      pref.storeString("open","1");
                        JSONArray cast = jsonObject.getJSONArray("data");
                        for (int i=0; i<cast.length(); i++) {
                            JSONObject jsonnewsobject = cast.getJSONObject(i);
                            String menu_name =jsonnewsobject.getString("menu_name");
                            String menu_code = jsonnewsobject.getString("menu_code");
                            String coupon = jsonnewsobject.getString("coupon");
                            data.add(menu_name);
                        }
                       Intent i = new Intent(MainActivity.this, HomeActivity.class);
                     i.putStringArrayListExtra("list",data);
                        startActivity(i);

                        // close this activity
                       finish();

                        break;
                    case "0":
                        Toast.makeText(MainActivity.this,"Somthing want wrong!", Toast.LENGTH_LONG).show();
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
