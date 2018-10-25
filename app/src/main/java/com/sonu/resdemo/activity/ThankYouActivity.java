package com.sonu.resdemo.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sonu.resdemo.R;
import com.sonu.resdemo.app.Config;
import com.sonu.resdemo.scripts.Scripts;
import com.sonu.resdemo.utils.CommonFunctions;
import com.sonu.resdemo.utils.DatabaseHandler;
import com.sonu.resdemo.utils.NotificationUtils;
import com.sonu.resdemo.utils.Preferences;

import org.json.JSONObject;

public class ThankYouActivity extends AppCompatActivity implements View.OnClickListener {
TextView txt_thank_you,txt_like,txt_price,txt_address;
    DatabaseHandler db;
    Preferences pref;
  private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Thank You");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        db=new DatabaseHandler(ThankYouActivity.this);
        pref=new Preferences(ThankYouActivity.this);
        init();


    }

  @Override
  protected void onResume() {
    super.onResume();

    LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
      new IntentFilter(Config.PUSH_NOTIFICATION));

    NotificationUtils.clearNotifications(getApplicationContext());
  }

  @Override
  protected void onPause() {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    super.onPause();
  }
    public void init(){
        txt_thank_you=(TextView)findViewById(R.id.txt_thank_you);
        txt_thank_you.setOnClickListener(this);
        txt_like=(TextView)findViewById(R.id.txt_like);
        txt_like.setOnClickListener(this);
        txt_price=(TextView)findViewById(R.id.txt_price);
        if(pref.getString("price").equals("")){
          txt_price.setText("Please pay RS "+db.getTotalItemprice()+" at delivery time.");
        }else {
          int coupon_price=Integer.parseInt(pref.getString("price"));
          int total_pirice=Integer.parseInt(db.getTotalItemprice());
          int paid_price=total_pirice-coupon_price;
          txt_price.setText("Please pay RS "+paid_price);

        }

        txt_address=(TextView)findViewById(R.id.txt_address);
        txt_address.setText(pref.getString("address"));
    }
    @Override
    public void onBackPressed() {

      Intent intent=new Intent(ThankYouActivity.this,HomeActivity.class);
      startActivity(intent);
      finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Toast.makeText(UserDetails.this, "1", Toast.LENGTH_SHORT).show();

          Intent intent=new Intent(ThankYouActivity.this,HomeActivity.class);
          startActivity(intent);
          finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_thank_you:

              if(CommonFunctions.isConnected(ThankYouActivity.this)){
                new SendFeedBackAsynTask().execute();

              }else {
                Toast.makeText(ThankYouActivity.this,"No Internet!", Toast.LENGTH_LONG).show();
              }
                break;
            case R.id.txt_like:
                CommonFunctions.sharelink(ThankYouActivity.this);
                break;
        }
    }

  class SendFeedBackAsynTask extends AsyncTask<String, Void, String> {
    String f_id = "", field = "", path;
    ProgressDialog progress;
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progress = new ProgressDialog(ThankYouActivity.this);
      progress.setCanceledOnTouchOutside(false);
      progress.setCancelable(false);
      progress.setMessage("Please wait..");
      progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {

      String result="";
      try{
        result= Scripts.GetThankyou(CommonFunctions.getDeviceIMEI(ThankYouActivity.this));
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

        JSONObject jsonObject=new JSONObject(s);

        String str=jsonObject.getString("success");
        switch (str){
          case "1":


            Intent intent=new Intent(ThankYouActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
            db.clearorder();
            pref.storeString("code","");
            pref.storeString("price","");
            pref.storeString("id","");

            break;
         default:
            Toast.makeText(ThankYouActivity.this,"Somthing want wrong!", Toast.LENGTH_LONG).show();
            break;
        }

      }catch (Exception e){
        Log.e("error","error in branch uploaded data");
      }


    }

  }
}
