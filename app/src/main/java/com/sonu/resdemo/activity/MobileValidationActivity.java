package com.sonu.resdemo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sonu.resdemo.R;
import com.sonu.resdemo.model.OrderDataModel;
import com.sonu.resdemo.scripts.Scripts;
import com.sonu.resdemo.utils.CommonFunctions;
import com.sonu.resdemo.utils.DatabaseHandler;
import com.sonu.resdemo.utils.Preferences;

import org.json.JSONObject;

import java.util.ArrayList;

public class MobileValidationActivity extends AppCompatActivity implements View.OnClickListener{
TextView txt_validation_code;
EditText edt_validate;
String validation_code,user_id,validation_code_str;
Preferences pref;
DatabaseHandler db;
String orderno;
    private static ArrayList<OrderDataModel> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_validation);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Mobile Validation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
db=new DatabaseHandler(MobileValidationActivity.this);
        data=new ArrayList<OrderDataModel>();
        init();
    }
public void init(){
    orderno = ""+((int)(Math.random()*9000)+1000);
        pref=new Preferences(MobileValidationActivity.this);
        pref.storeString("edit","1");

    txt_validation_code=(TextView)findViewById(R.id.txt_validation_code);
    txt_validation_code.setOnClickListener(this);
    edt_validate=(EditText)findViewById(R.id.edt_validate);
    validation_code=getIntent().getStringExtra("code");
    user_id=getIntent().getStringExtra("user_id");

    Cursor c=db.getOrderItem();
    if(c.getCount()>0) {
        if (c.moveToFirst()) {
            do {

                String quantity = c.getString(c.getColumnIndex("quantity"));
                String item_code = c.getString(c.getColumnIndex("item_code"));
                String item_name = c.getString(c.getColumnIndex("item_name"));

                String price = c.getString(c.getColumnIndex("item_price"));
                String item_actual_price = c.getString(c.getColumnIndex("actual_price"));
                data.add(new OrderDataModel(item_name, item_code, price, quantity, item_actual_price,orderno,user_id,CommonFunctions.getDateTime()));

            } while (c.moveToNext());
        }
    }
    if(!validation_code.equals("")){
        edt_validate.setText(validation_code);
    }
}
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_validation_code:
                if(edt_validate.getText().toString().equals("")){
                    edt_validate.setError("Please enter code");
                    edt_validate.requestFocus();
                }else {
                    validation_code_str=edt_validate.getText().toString().trim();

if(CommonFunctions.isConnected(MobileValidationActivity.this)){

    new SendOrderAsynTask().execute();
}else {
    Toast.makeText(MobileValidationActivity.this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
}
                }
                break;
        }
    }
    class SendOrderAsynTask extends AsyncTask<String, Void, String> {

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MobileValidationActivity.this);
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Please wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            try{
                result= Scripts.ValidateMobile(validation_code_str,user_id);
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
                        if(CommonFunctions.isConnected(MobileValidationActivity.this)){
                            new OrderdetailSendfromLocalToServer().execute();
                        }else {
                            Toast.makeText(MobileValidationActivity.this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "0":
                        Toast.makeText(MobileValidationActivity.this,"success", Toast.LENGTH_LONG).show();
                        break;
                }

            }catch (Exception e){
                Log.e("error","error in branch uploaded data");
            }


        }

    }

    class OrderdetailSendfromLocalToServer extends AsyncTask<String, Void, String> {

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MobileValidationActivity.this);
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Please wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            try{
                for (int i = 0; i < data.size(); i++) {
if(pref.getString("code").equals("")){
  result= Scripts.OrderData(pref.getString("mobile"),data.get(i).getQuantity(),data.get(i).getItem_code(),data.get(i).getItem_name(),data.get(i).getPrice(),data.get(i).getItem_actual_price(),data.get(i).getOrderno(),CommonFunctions.getDeviceIMEI(MobileValidationActivity.this),data.get(i).getDatetime(),""+db.getTotalItemprice(),"NO","NO");
}else {
  result= Scripts.OrderData(pref.getString("mobile"),data.get(i).getQuantity(),data.get(i).getItem_code(),data.get(i).getItem_name(),data.get(i).getPrice(),data.get(i).getItem_actual_price(),data.get(i).getOrderno(),CommonFunctions.getDeviceIMEI(MobileValidationActivity.this),data.get(i).getDatetime(),""+db.getTotalItemprice(),pref.getString("code"),pref.getString("price"));
}

                    //result= Scripts.OrderData(pref.getString("mobile"),data.get(i).getQuantity(),data.get(i).getItem_code(),data.get(i).getItem_name(),data.get(i).getPrice(),data.get(i).getItem_actual_price(),data.get(i).getOrderno(),CommonFunctions.getDeviceIMEI(MobileValidationActivity.this),data.get(i).getDatetime(),""+db.getTotalItemprice());


                }

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
                        Toast.makeText(MobileValidationActivity.this,jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        pref.storeString("name","");
                        pref.storeString("mobile","");
                        pref.storeString("email","");
                        pref.storeString("user_address","");
                        pref.storeString("edit","2");
                        Intent intent=new Intent(MobileValidationActivity.this,ThankYouActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case "0":
                        Toast.makeText(MobileValidationActivity.this,"Somthing want wrong!", Toast.LENGTH_LONG).show();
                        break;
                }

            }catch (Exception e){
                Log.e("error","error in branch uploaded data");
            }


        }

    }
    @Override
    public void onBackPressed() {

        Intent intent=new Intent(MobileValidationActivity.this,CommonFunctions.class);


       startActivity(intent);
       finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Toast.makeText(UserDetails.this, "1", Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(MobileValidationActivity.this,ConfirmOrderActivity.class);

            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
