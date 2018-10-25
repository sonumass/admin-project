package com.sonu.resdemo.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sonu.resdemo.R;
import com.sonu.resdemo.scripts.Scripts;
import com.sonu.resdemo.utils.CommonFunctions;

import org.json.JSONArray;
import org.json.JSONObject;
import android.widget.LinearLayout.LayoutParams;

public class OrderDetailActivity extends AppCompatActivity {
String price ,mobile,orderno;
LinearLayout data_layout,datalayoutt,layout_coupon,layout_total;
    TextView txt_name,txt_quantity,txt_price,txt_total_price,txt_coupon_price,txt_total_amount;
    String couon_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Order Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        init();
    }
    public void init(){
      txt_coupon_price=(TextView)findViewById(R.id.txt_coupon_price);
        txt_total_price=(TextView)findViewById(R.id.txt_total_price);
        data_layout=(LinearLayout)findViewById(R.id.data_layout);
      layout_coupon=(LinearLayout)findViewById(R.id.layout_coupon);
      txt_total_amount=(TextView)findViewById(R.id.txt_total_amount);
      layout_total=(LinearLayout)findViewById(R.id.layout_total);
price=getIntent().getStringExtra("price");
mobile=getIntent().getStringExtra("mobile");
orderno=getIntent().getStringExtra("orderno");
        txt_total_price.setText(getResources().getString(R.string.rs)+" " + price);
        if (CommonFunctions.isConnected(OrderDetailActivity.this)) {
            new MyOrderListAsynTask().execute();
        } else {
            Toast.makeText(OrderDetailActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    class MyOrderListAsynTask extends AsyncTask<String, Void, String> {
        String f_id = "", field = "", path;
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(OrderDetailActivity.this);
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Please wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            try{
                result= Scripts.GetOrderDetail(orderno);
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

                        JSONArray cast = jsonObject.getJSONArray("result");
                        for (int i=0; i<cast.length(); i++) {
                            JSONObject jsonnewsobject = cast.getJSONObject(i);
                            String item_name = jsonnewsobject.getString("item_name");
                            String item_quantity = jsonnewsobject.getString("item_quantity");
                            String item_order_price = jsonnewsobject.getString("item_order_price");
                          couon_price=  jsonnewsobject.getString("coupon_price");
                            String datetime = jsonnewsobject.getString("datetime");
                            datalayoutt=new LinearLayout(OrderDetailActivity.this);
                            TextView txt=new TextView(OrderDetailActivity.this);
                            txt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                    2));
                            txt.setBackgroundColor(Color.parseColor("#000000"));
                            datalayoutt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT,3f));
                            datalayoutt.setOrientation(LinearLayout.HORIZONTAL);
                            txt_name=new TextView(OrderDetailActivity.this);
                            txt_name.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT,1.0f));

                            txt_name.setPadding(5,5,0,5);
                            txt_quantity=new TextView(OrderDetailActivity.this);
                            txt_quantity.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT,1.0f));
                            txt_quantity.setGravity(Gravity.CENTER);
                            txt_quantity.setPadding(0,5,0,5);
                            txt_price=new TextView(OrderDetailActivity.this);
                            txt_price.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT,1.0f));
                            txt_price.setGravity(Gravity.END);
                            txt_price.setPadding(0,5,5,5);

txt_name.setText(item_name);
txt_quantity.setText(item_quantity);
txt_price.setText(getResources().getString(R.string.rs)+" "+item_order_price);
                            datalayoutt.addView(txt_name);
                            datalayoutt.addView(txt_quantity);
                            datalayoutt.addView(txt_price);
                            data_layout.addView(txt);
                            data_layout.addView(datalayoutt);

                        }

                        if(couon_price.equals("NO")){
                          layout_coupon.setVisibility(View.GONE);
                          layout_total.setVisibility(View.GONE);
                        }else {
                          layout_total.setVisibility(View.VISIBLE);
                          layout_coupon.setVisibility(View.VISIBLE);
                          int coupon_p=Integer.parseInt(couon_price);
                          int priec=Integer.parseInt(price);
                          int total_p=priec-coupon_p;
                          txt_coupon_price.setText("- "+getResources().getString(R.string.rs)+" "+coupon_p);
                          txt_total_price.setText(getResources().getString(R.string.rs)+" "+total_p);
                          txt_total_amount.setText(getResources().getString(R.string.rs)+" "+priec);
                        }
                        break;
                    case "0":
                        Toast.makeText(OrderDetailActivity.this,"Somthing want wrong!", Toast.LENGTH_LONG).show();
                        break;
                }

            }catch (Exception e){
                Log.e("error","error in branch uploaded data");
            }


        }

    }
    @Override
    public void onBackPressed() {

        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Toast.makeText(UserDetails.this, "1", Toast.LENGTH_SHORT).show();

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
