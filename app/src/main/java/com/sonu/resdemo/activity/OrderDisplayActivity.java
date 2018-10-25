package com.sonu.resdemo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sonu.resdemo.R;
import com.sonu.resdemo.adapter.OrderDisplayAdapter;
import com.sonu.resdemo.interfaces.CustomItemClickListener;
import com.sonu.resdemo.model.OrderModel;
import com.sonu.resdemo.utils.CommonFunctions;
import com.sonu.resdemo.utils.DatabaseHandler;
import com.sonu.resdemo.utils.Preferences;

import java.util.ArrayList;

public class OrderDisplayActivity extends AppCompatActivity implements View.OnClickListener{

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<OrderModel> data;
   DatabaseHandler db;
    Preferences pref;
    LinearLayout layout;
    TextView txt_total_price,txt_items,txt_buy,txt_apply_coupon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_display);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        data = new ArrayList<OrderModel>();
        pref=new Preferences(OrderDisplayActivity.this);
      txt_apply_coupon=(TextView)findViewById(R.id.txt_apply_coupon);
      txt_apply_coupon.setOnClickListener(this);
        db=new DatabaseHandler(OrderDisplayActivity.this);
        recyclerView = (RecyclerView)findViewById(R.id.order_recycler_view);
        layout=(LinearLayout)findViewById(R.id.layout);
        txt_total_price=(TextView)findViewById(R.id.txt_total_price);
        txt_items=(TextView)findViewById(R.id.txt_items_no);
        txt_buy=(TextView)findViewById(R.id.txt_order_now);
        txt_buy.setOnClickListener(this);
        Cursor cc=     db.getItemcount();

        if(cc.getCount()>0){
            layout.setVisibility(View.VISIBLE);
            txt_items.setText(""+db.getTotalItem());
          if (pref.getString("price").equals("")){
            txt_total_price.setText(""+db.getTotalItemprice());

          }else {
            int coupon_price=Integer.parseInt(pref.getString("price"));
            int price=Integer.parseInt(db.getTotalItemprice());
            int total_price=price-coupon_price;
            txt_total_price.setText(""+total_price);
            txt_apply_coupon.setText("Coupon Applied worth "+pref.getString("price"));
          }
        }else {
            layout.setVisibility(View.GONE);
        }
        Cursor c=db.getOrderItem();
        if(c.getCount()>0){
            if (c.moveToFirst()) {
                do {

                   String quantity = c.getString(c.getColumnIndex("quantity"));
                    String item_code = c.getString(c.getColumnIndex("item_code"));
                    String item_name = c.getString(c.getColumnIndex("item_name"));
                    String price = c.getString(c.getColumnIndex("item_price"));
                    String item_actual_price=c.getString(c.getColumnIndex("actual_price"));
                   data.add(new OrderModel(item_name,item_code,price,quantity,item_actual_price));

                } while (c.moveToNext());
            }
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(OrderDisplayActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            adapter = new OrderDisplayAdapter(data,OrderDisplayActivity.this, new CustomItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {

                    switch (v.getId()){
                        case R.id.txt_plus:

                            switch (pref.getString("open")){
                                case "1":
                                    int sum=0;
                                    //String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
                                    TextView txt_quality=(TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_quantity);
                                    TextView txt_item_total_price=(TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_price);
                                    if(txt_quality.getText().toString().trim()==""){
                                        sum=sum+1;
                                        txt_quality.setText(""+sum);
                                        int quantity=Integer.valueOf(txt_quality.getText().toString());
                                        int price=quantity*Integer.valueOf(data.get(position).getItem_actual_price());
                                        txt_item_total_price.setText(""+price);
                                        db.insertitem(data.get(position).getItem_code(),txt_quality.getText().toString(),String.valueOf(price),data.get(position).getItem_name(),data.get(position).getItem_actual_price());

                                    }else {

                                        String a=txt_quality.getText().toString();
                                        int  summ=Integer.valueOf(a)+1;
                                        txt_quality.setText(""+summ);
                                        int quantity=Integer.valueOf(txt_quality.getText().toString());
                                        int price=quantity*Integer.valueOf(data.get(position).getItem_actual_price());
                                        txt_item_total_price.setText(""+price);
                                        db.updatecart(data.get(position).getItem_code(),txt_quality.getText().toString(),String.valueOf(price));
                                    }

                                   txt_items.setText(""+db.getTotalItem());
                                    if (pref.getString("price").equals("")){
                                      txt_total_price.setText(""+db.getTotalItemprice());
                                    }else {
                                      int coupon_price=Integer.parseInt(pref.getString("price"));
                                      int price=Integer.parseInt(db.getTotalItemprice());
                                      int total_price=price-coupon_price;
                                      txt_total_price.setText(""+total_price);
                                      txt_apply_coupon.setText("Coupon Applied worth "+pref.getString("price"));
                                    }

                                    if(Integer.valueOf(db.getTotalItem())>0){
                                        layout.setVisibility(View.VISIBLE);
                                    }else {
                                        layout.setVisibility(View.GONE);
                                    }
                                    break;
                                case "0":
                                    CommonFunctions.ShowOpenRestaurant(OrderDisplayActivity.this);
                                    break;
                            }

                            break;
                        case R.id.txt_min:

                            switch (pref.getString("open")) {
                                case "1":
                                    TextView txt_qualityy = (TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_quantity);
                                    TextView txt_item_total_pricee=(TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_price);
                                    int min;
                                    if ((txt_qualityy.getText().toString().trim() == "") || (txt_qualityy.getText().toString().trim() == "0")) {

                                        txt_qualityy.setText("");
                                        txt_item_total_pricee.setText("");
                                        db.delete(data.get(position).getItem_code());
                                    } else {

                                        String a = txt_qualityy.getText().toString();
                                        if (Integer.valueOf(a) < 1) {
                                            txt_qualityy.setText("");
                                            db.delete(data.get(position).getItem_code());
                                            txt_item_total_pricee.setText("");
                                        } else {
                                            min = Integer.valueOf(a) - 1;
                                            txt_qualityy.setText("" + min);
                                            int quantity = Integer.valueOf(txt_qualityy.getText().toString());
                                            int price = quantity * Integer.valueOf(data.get(position).getItem_actual_price());
                                            txt_item_total_pricee.setText(""+price);
                                            db.updatecart(data.get(position).getItem_code(), txt_qualityy.getText().toString(), String.valueOf(price));
                                        }
                                        txt_items.setText("" + db.getTotalItem());
                                      if (pref.getString("price").equals("")){
                                        txt_total_price.setText(""+db.getTotalItemprice());
                                      }else {
                                        int coupon_price=Integer.parseInt(pref.getString("price"));
                                        int price=Integer.parseInt(db.getTotalItemprice());
                                        int total_price=price-coupon_price;
                                        txt_total_price.setText(""+total_price);
                                        txt_apply_coupon.setText("Coupon Applied worth "+pref.getString("price"));
                                      }

                                        if (Integer.valueOf(db.getTotalItem()) > 0) {
                                            layout.setVisibility(View.VISIBLE);
                                        } else {
                                            layout.setVisibility(View.GONE);
                                        }



                                    }
                                    break;
                                case "0":
                                    CommonFunctions.ShowOpenRestaurant(OrderDisplayActivity.this);
                                    break;
                            }

                            break;
                    }
                }
            });
            recyclerView.setAdapter(adapter);

        }else {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_order_now:

                Cursor cursor=db.getItemcount();
                if(cursor.getCount()>0){
                    switch (pref.getString("open")){
                        case "1":
                            Intent intent=new Intent(OrderDisplayActivity.this,ConfirmOrderActivity.class);
                            startActivity(intent);
                            break;
                        case "0":
                            CommonFunctions.ShowOpenRestaurant(OrderDisplayActivity.this);
                            break;
                    }
                }else {
                    CommonFunctions.ShowEmptyCart(OrderDisplayActivity.this);
                }

                break;
          case R.id.txt_apply_coupon:
            Intent intent=new Intent(OrderDisplayActivity.this,CouponActivity.class);
            startActivity(intent);
            finish();
            break;
        }
    }
}
