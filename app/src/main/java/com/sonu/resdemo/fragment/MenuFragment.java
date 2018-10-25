package com.sonu.resdemo.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sonu.resdemo.R;
import com.sonu.resdemo.activity.OrderDisplayActivity;
import com.sonu.resdemo.adapter.CategoryAdapter;
import com.sonu.resdemo.baner.SliderUtils;
import com.sonu.resdemo.baner.ViewPagerAdapter;
import com.sonu.resdemo.interfaces.CustomItemClickListener;
import com.sonu.resdemo.model.CategoryModel;
import com.sonu.resdemo.model.TypeModel;
import com.sonu.resdemo.scripts.Scripts;
import com.sonu.resdemo.utils.CommonFunctions;
import com.sonu.resdemo.utils.DatabaseHandler;
import com.sonu.resdemo.utils.Preferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
String type_str="";
  ViewPager viewPager;
  LinearLayout sliderDotspanel;
  private int dotscount;
  private ImageView[] dots;

  List<SliderUtils> sliderImg;
  ViewPagerAdapter viewPagerAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LinearLayout layout;

    private String mParam1;
    private String mParam2;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CategoryModel> data;
    static View.OnClickListener myOnClickListener;
    TextView txt_total_price,txt_items,txt_checkout;
    DatabaseHandler db;
    RelativeLayout relative_layout;
    Preferences pref;
    List<TypeModel> typelist;
    String[] strarey={"Small","Regular"};
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data = new ArrayList<CategoryModel>();
        View v = inflater.inflate(R.layout.fragment_menu, container, false);


typelist=new ArrayList<TypeModel>();
TypeModel type=new TypeModel("cust101","Small");
typelist.add(type);

      TypeModel typeregular=new TypeModel("cust101","Regular");
      typelist.add(typeregular);


      relative_layout=(RelativeLayout)v.findViewById(R.id.rel);

        recyclerView = (RecyclerView) v.findViewById(R.id.cat_recycler_view);
        pref=new Preferences(getActivity());
        layout=(LinearLayout)v.findViewById(R.id.layout);
        txt_total_price=(TextView)v.findViewById(R.id.txt_total_price);
        txt_items=(TextView)v.findViewById(R.id.txt_items_no);
        txt_checkout=(TextView)v.findViewById(R.id.txt_checkout);
        txt_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor=db.getItemcount();
                if(cursor.getCount()>0){
                    switch (pref.getString("open")){
                        case "1":
                            Intent intent=new Intent(getActivity(), OrderDisplayActivity.class);
                            startActivity(intent);
                            break;
                        case "0":
                            CommonFunctions.ShowOpenRestaurant(getActivity());
                            break;
                    }
                }else {
                    CommonFunctions.ShowEmptyCart(getActivity());
                }


            }
        });

        db=new DatabaseHandler(getActivity());

        Cursor c=     db.getItemcount();

        if(c.getCount()>0){
            layout.setVisibility(View.VISIBLE);
            txt_items.setText(""+db.getTotalItem());
            txt_total_price.setText(""+db.getTotalItemprice());
        }else {
            layout.setVisibility(View.GONE);
        }
//
        if (CommonFunctions.isConnected(getActivity())) {
            new CategoryAsynTask().execute();
        } else {
            Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
        }
      baner(v);
        return v;
    }
public  void baner(View v){


  sliderImg = new ArrayList<>();

  viewPager = (ViewPager) v.findViewById(R.id.viewPager);

  sliderDotspanel = (LinearLayout) v.findViewById(R.id.SliderDots);

  if (CommonFunctions.isConnected(getActivity())) {
    new MenuAsyntask().execute();
  } else {
    Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
  }

  viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

      for(int i = 0; i< dotscount; i++){
        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active));
      }

      dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  });
}

    class CategoryAsynTask extends AsyncTask<String, Void, String> {
        String f_id = "", field = "", path;
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Please wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            try{
                result= Scripts.GetCategory(mParam1);
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

                        layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                   Cursor c=     db.getItemcount();

                   if(c.getCount()>0){
                       layout.setVisibility(View.VISIBLE);
                   }else {
                       layout.setVisibility(View.GONE);
                   }
                        adapter = new CategoryAdapter(data,getActivity(), new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {

                                switch (v.getId()){
                                    case R.id.txt_plus:
/*if(type_str.equals("")){
  AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
  mBuilder.setTitle("Choose an item");
  mBuilder.setSingleChoiceItems(strarey, -1, new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
      Toast.makeText(getActivity(),strarey[i],Toast.LENGTH_SHORT).show();
      dialogInterface.dismiss();
    }
  });

  AlertDialog mDialog = mBuilder.create();
  mDialog.show();
}else {

}*/
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
                                                CommonFunctions.ShowOpenRestaurant(getActivity());
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
                                                CommonFunctions.ShowOpenRestaurant(getActivity());
                                                break;
                                        }

                                    break;
                                        }
                            }
                        });
                        recyclerView.setAdapter(adapter);


                        break;
                    case "0":
                        Toast.makeText(getActivity(),"Somthing want wrong!", Toast.LENGTH_LONG).show();
                        break;
                }

            }catch (Exception e){
                Log.e("error","error in branch uploaded data");
            }


        }

    }
    public void StringView(){
        Toast.makeText(getActivity(),"test",Toast.LENGTH_SHORT).show();
    }

  class MenuAsyntask extends AsyncTask<String, Void, String> {
    String f_id = "", field = "", path;
    ProgressDialog progress;
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    /*  progress = new ProgressDialog(getActivity());
      progress.setCanceledOnTouchOutside(false);
      progress.setCancelable(false);
      progress.setMessage("Please wait..");
      progress.show();*/
    }

    @Override
    protected String doInBackground(String... strings) {

      String result="";
      try{
        result= Scripts.GetBaner(CommonFunctions.getDeviceIMEI(getActivity()));
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
     // progress.cancel();
    try{

      JSONObject jsonObject=new JSONObject(s);
      String str=jsonObject.getString("success");
      switch (str){
        case "1":
          relative_layout.setVisibility(View.VISIBLE);
          JSONArray cast = jsonObject.getJSONArray("result");
          SliderUtils sliderUtils = null;
          for (int i=0; i<cast.length(); i++) {
            JSONObject jsonnewsobject = cast.getJSONObject(i);
            sliderUtils=new SliderUtils(jsonnewsobject.getString("image_path"),jsonnewsobject.getString("addlink"),jsonnewsobject.getString("id"));

            sliderImg.add(sliderUtils);
          }
          viewPagerAdapter = new ViewPagerAdapter(sliderImg, getActivity());

          viewPager.setAdapter(viewPagerAdapter);

          dotscount = viewPagerAdapter.getCount();
          dots = new ImageView[dotscount];

          for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

          }

          dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active));
          break;
          default:
            Toast.makeText(getActivity(),"Somthing want wrong.",Toast.LENGTH_SHORT).show();
            relative_layout.setVisibility(View.GONE);
            break;
      }
    }catch (Exception e){
      Log.e("Data error",e.getMessage());
    }



    }
  }
}
