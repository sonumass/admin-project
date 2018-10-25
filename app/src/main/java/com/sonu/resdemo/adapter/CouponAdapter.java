package com.sonu.resdemo.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sonu.resdemo.R;
import com.sonu.resdemo.activity.ConfirmOrderActivity;
import com.sonu.resdemo.activity.CouponActivity;
import com.sonu.resdemo.activity.OrderDetailActivity;
import com.sonu.resdemo.activity.OrderDisplayActivity;
import com.sonu.resdemo.model.CouponModel;
import com.sonu.resdemo.scripts.Scripts;
import com.sonu.resdemo.utils.CommonFunctions;
import com.sonu.resdemo.utils.DatabaseHandler;
import com.sonu.resdemo.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder>{
  String item_code,quantity;
  private DatabaseHandler db;
  private ArrayList<CouponModel> dataSet;
  Context context;
  Preferences pref;
  String code ,price ,id;

  public static class MyViewHolder extends RecyclerView.ViewHolder {



    TextView txt_price,txt_exp_date,txt_coupon_code,txt_apply;
    LinearLayout item_layout;


    public MyViewHolder(View itemView) {
      super(itemView);

      this.txt_exp_date=(TextView)itemView.findViewById(R.id.txt_exp_date);
      this.txt_coupon_code=(TextView)itemView.findViewById(R.id.txt_coupon_code);
this.txt_apply=(TextView)itemView.findViewById(R.id.txt_apply);
      this.txt_price=(TextView)itemView.findViewById(R.id.txt_price);
      this.item_layout=(LinearLayout)itemView.findViewById(R.id.item_layout);


    }
  }

  public CouponAdapter(ArrayList<CouponModel> data,Context context) {
    this.dataSet = data;
    this.context=context;

  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.coupon_row, parent, false);



    final MyViewHolder myViewHolder = new MyViewHolder(view);

    return myViewHolder;
  }

  @Override
  public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {



pref=new Preferences(context);
    holder.txt_coupon_code.setText("Total Price :"+dataSet.get(listPosition).getCode());
    holder.txt_price.setText("Order NO :"+dataSet.get(listPosition).getPrice());
    holder.txt_exp_date.setText(dataSet.get(listPosition).getDatetime());



    holder.item_layout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent=new Intent(context, OrderDetailActivity.class);
        intent.putExtra("price",dataSet.get(listPosition).getPrice());
        intent.putExtra("datetime",dataSet.get(listPosition).getDatetime());
        intent.putExtra("id",dataSet.get(listPosition).getId());
        intent.putExtra("code",dataSet.get(listPosition).getCode());
        intent.putExtra("des",dataSet.get(listPosition).getDescription());
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

      }
    });
    holder.txt_apply.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
code=dataSet.get(listPosition).getCode();
price=dataSet.get(listPosition).getPrice();
id=dataSet.get(listPosition).getId();
        if (CommonFunctions.isConnected(context)) {
          new CouponApplied().execute();
        } else {
          Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
        }

      }
    });


  }
  class CouponApplied extends AsyncTask<String, Void, String> {

    ProgressDialog progress;
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progress = new ProgressDialog(context);
      progress.setCanceledOnTouchOutside(false);
      progress.setCancelable(false);
      progress.setMessage("Please wait..");
      progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {

      String result="";
      try{
        result= Scripts.GetCouponApplied(CommonFunctions.getDeviceIMEI(context),code);
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

            pref.storeString("code",code);
            pref.storeString("price",price);
            pref.storeString("id",id);
            Intent intent=new Intent(context, OrderDisplayActivity.class);
            context.startActivity(intent);
            ((Activity)context).finish();

            break;
          case "0":
            pref.storeString("code","");
            pref.storeString("price","");
            pref.storeString("id","");
            Intent intentt=new Intent(context, OrderDisplayActivity.class);
            context.startActivity(intentt);
            ((Activity)context).finish();
            Toast.makeText(context,"Sorry you can not apply this coupon because you have exceed coupon used times.", Toast.LENGTH_LONG).show();
            break;
        }

      }catch (Exception e){
        Log.e("error","error in branch uploaded data");
      }


    }

  }
  @Override
  public int getItemCount() {
    return dataSet.size();
  }
}
