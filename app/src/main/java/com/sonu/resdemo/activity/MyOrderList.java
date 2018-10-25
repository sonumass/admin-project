package com.sonu.resdemo.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.sonu.resdemo.R;
import com.sonu.resdemo.adapter.MyOrderAdapter;
import com.sonu.resdemo.model.MyOrderListModel;
import com.sonu.resdemo.scripts.Scripts;
import com.sonu.resdemo.utils.CommonFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyOrderList extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<MyOrderListModel> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        init();
    }
    public void init(){
        data = new ArrayList<MyOrderListModel>();
        recyclerView = (RecyclerView)findViewById(R.id.order_recycler_view);
        if (CommonFunctions.isConnected(MyOrderList.this)) {
            new MyOrderListAsynTask().execute();
        } else {
            Toast.makeText(MyOrderList.this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }

    }


    class MyOrderListAsynTask extends AsyncTask<String, Void, String> {
        String f_id = "", field = "", path;
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MyOrderList.this);
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Please wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            try{
                result= Scripts.GetMyOrder(CommonFunctions.getDeviceIMEI(MyOrderList.this));
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
                            String mobile = jsonnewsobject.getString("mobile");
                            String order_no = jsonnewsobject.getString("order_no");
                            String order_price = jsonnewsobject.getString("order_price");
                            String datetime = jsonnewsobject.getString("datetime");

                            data.add(new MyOrderListModel(order_price,datetime,order_no,mobile));
                        }
                        MyOrderAdapter adpater=new MyOrderAdapter(data,MyOrderList.this);
                        recyclerView.setHasFixedSize(true);

                        layoutManager = new LinearLayoutManager(MyOrderList.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setAdapter(adpater);
                        break;
                    case "0":
                        Toast.makeText(MyOrderList.this,"Somthing want wrong!", Toast.LENGTH_LONG).show();
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
