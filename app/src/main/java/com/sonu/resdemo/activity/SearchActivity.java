package com.sonu.resdemo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sonu.resdemo.R;
import com.sonu.resdemo.adapter.SearchAdapter;
import com.sonu.resdemo.interfaces.CustomItemClickListener;
import com.sonu.resdemo.model.CategoryModel;
import com.sonu.resdemo.scripts.Scripts;
import com.sonu.resdemo.utils.CommonFunctions;
import com.sonu.resdemo.utils.DatabaseHandler;
import com.sonu.resdemo.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapterr;
    private static SearchAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CategoryModel> data;
    static View.OnClickListener myOnClickListener;
    TextView txt_total_price,txt_items,txt_checkout;
    DatabaseHandler db;
    Preferences pref;
    LinearLayout layout;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Search here");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        data = new ArrayList<CategoryModel>();
        relativeLayout=(RelativeLayout)findViewById(R.id.rel);
      relativeLayout.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.cat_recycler_view);
        pref=new Preferences(SearchActivity.this);
        layout=(LinearLayout)findViewById(R.id.layout);
        txt_total_price=(TextView)findViewById(R.id.txt_total_price);
        txt_items=(TextView)findViewById(R.id.txt_items_no);
        txt_checkout=(TextView)findViewById(R.id.txt_checkout);
        txt_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor=db.getItemcount();
                if(cursor.getCount()>0){
                    switch (pref.getString("open")){
                        case "1":
                            Intent intent=new Intent(SearchActivity.this, OrderDisplayActivity.class);
                            startActivity(intent);
                            break;
                        case "0":
                            CommonFunctions.ShowOpenRestaurant(SearchActivity.this);
                            break;
                    }
                }else {
                    CommonFunctions.ShowEmptyCart(SearchActivity.this);
                }


            }
        });

        db=new DatabaseHandler(SearchActivity.this);
        Cursor c=     db.getItemcount();

        if(c.getCount()>0){
            layout.setVisibility(View.VISIBLE);
            txt_items.setText(""+db.getTotalItem());
            txt_total_price.setText(""+db.getTotalItemprice());
        }else {
            layout.setVisibility(View.GONE);
        }
//
        if (CommonFunctions.isConnected(SearchActivity.this)) {
            new CategoryAsynTask().execute();
        } else {
            Toast.makeText(SearchActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter.filter("");
                    //listView.clearTextFilter();
                } else {
                    adapter.filter(newText);
                }
                return true;
            }
        });

        return true;
    }
    @Override
    public void onBackPressed() {

       Intent intent=new Intent(SearchActivity.this,HomeActivity.class);
       startActivity(intent);
       finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Toast.makeText(UserDetails.this, "1", Toast.LENGTH_SHORT).show();

          Intent intent=new Intent(SearchActivity.this,HomeActivity.class);
          startActivity(intent);
          finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class CategoryAsynTask extends AsyncTask<String, Void, String> {
        String f_id = "", field = "", path;
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(SearchActivity.this);
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Please wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            try{
                result= Scripts.GetSearchData("1");
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

                        JSONArray cast = jsonObject.getJSONArray("data");
                        for (int i=0; i<cast.length(); i++) {
                            JSONObject jsonnewsobject = cast.getJSONObject(i);
                            String item_name = jsonnewsobject.getString("item_name");
                            String discount_price = jsonnewsobject.getString("discount_price");
                            String item_code = jsonnewsobject.getString("item_code");
                            String description = jsonnewsobject.getString("description");
                            String price = jsonnewsobject.getString("price");
                            String veg_non_veg = jsonnewsobject.getString("veg_non_veg");
                            data.add(new CategoryModel(item_name,description,item_code,price,discount_price,veg_non_veg));
                        }

                        recyclerView.setHasFixedSize(true);

                        layoutManager = new LinearLayoutManager(SearchActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        Cursor c=     db.getItemcount();

                        if(c.getCount()>0){
                            layout.setVisibility(View.VISIBLE);
                        }else {
                            layout.setVisibility(View.GONE);
                        }
                        adapter = new SearchAdapter(data,SearchActivity.this, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {

                                switch (v.getId()){
                                    case R.id.txt_plus:

                                        switch (pref.getString("open")){
                                            case "1":
                                                int sum=0;
                                                //String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
                                                TextView txt_quality=(TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_quantity);
                                                if(txt_quality.getText().toString().trim()==""){
                                                    sum=sum+1;
                                                    txt_quality.setText(""+sum);
                                                    int quantity=Integer.valueOf(txt_quality.getText().toString());
                                                    int price=quantity*Integer.valueOf(data.get(position).getPrice());
                                                    db.insertitem(data.get(position).getItem_code(),txt_quality.getText().toString(),String.valueOf(price),data.get(position).getItem_name(),data.get(position).getPrice());

                                                }else {

                                                    String a=txt_quality.getText().toString();
                                                    int  summ=Integer.valueOf(a)+1;
                                                    txt_quality.setText(""+summ);
                                                    int quantity=Integer.valueOf(txt_quality.getText().toString());
                                                    int price=quantity*Integer.valueOf(data.get(position).getPrice());
                                                    db.updatecart(data.get(position).getItem_code(),txt_quality.getText().toString(),String.valueOf(price));
                                                }

                                                txt_items.setText(""+db.getTotalItem());
                                                txt_total_price.setText(""+db.getTotalItemprice());
                                                if(Integer.valueOf(db.getTotalItem())>0){
                                                    layout.setVisibility(View.VISIBLE);
                                                }else {
                                                    layout.setVisibility(View.GONE);
                                                }
                                                break;
                                            case "0":
                                                CommonFunctions.ShowOpenRestaurant(SearchActivity.this);
                                                break;
                                        }

                                        break;
                                    case R.id.txt_min:

                                        switch (pref.getString("open")) {
                                            case "1":
                                                TextView txt_qualityy = (TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_quantity);
                                                int min;
                                                if ((txt_qualityy.getText().toString().trim() == "") || (txt_qualityy.getText().toString().trim() == "0")) {

                                                    txt_qualityy.setText("");
                                                    db.delete(data.get(position).getItem_code());
                                                } else {

                                                    String a = txt_qualityy.getText().toString();
                                                    if (Integer.valueOf(a) < 1) {
                                                        txt_qualityy.setText("");
                                                        db.delete(data.get(position).getItem_code());
                                                    } else {
                                                        min = Integer.valueOf(a) - 1;
                                                        txt_qualityy.setText("" + min);
                                                        int quantity = Integer.valueOf(txt_qualityy.getText().toString());
                                                        int price = quantity * Integer.valueOf(data.get(position).getPrice());
                                                        db.updatecart(data.get(position).getItem_code(), txt_qualityy.getText().toString(), String.valueOf(price));
                                                    }
                                                    txt_items.setText("" + db.getTotalItem());
                                                    txt_total_price.setText("" + db.getTotalItemprice());

                                                    if (Integer.valueOf(db.getTotalItem()) > 0) {
                                                        layout.setVisibility(View.VISIBLE);
                                                    } else {
                                                        layout.setVisibility(View.GONE);
                                                    }



                                                }
                                                break;
                                            case "0":
                                                CommonFunctions.ShowOpenRestaurant(SearchActivity.this);
                                                break;
                                        }

                                        break;
                                }
                            }
                        });
                        recyclerView.setAdapter(adapter);

                        break;
                    case "0":
                        Toast.makeText(SearchActivity.this,"Somthing want wrong!", Toast.LENGTH_LONG).show();
                        break;
                }

            }catch (Exception e){
                Log.e("error","error in branch uploaded data");
            }


        }

    }
}
