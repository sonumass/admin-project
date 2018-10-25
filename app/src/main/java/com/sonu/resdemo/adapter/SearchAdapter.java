package com.sonu.resdemo.adapter;

/**
 * Created by Devesh D on 3/13/2018.
 */
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sonu.resdemo.R;
import com.sonu.resdemo.interfaces.CustomItemClickListener;
import com.sonu.resdemo.model.CategoryModel;
import com.sonu.resdemo.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Locale;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    String item_code,quantity;
    private  DatabaseHandler db;
    private ArrayList<CategoryModel> dataSet;
    private ArrayList<CategoryModel> searchList;
    Context context;

    CustomItemClickListener listener;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_veg;
        TextView textViewVersion;
        TextView txt_name,txt_price,txt_des,txt_plus,txt_quantity,txt_min;
        LinearLayout layout_item;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.iv_veg = (ImageView) itemView.findViewById(R.id.iv_veg);
            this.txt_min=(TextView)itemView.findViewById(R.id.txt_min);
            this.txt_quantity=(TextView)itemView.findViewById(R.id.txt_quantity);
            this.txt_plus=(TextView)itemView.findViewById(R.id.txt_plus);
            this.txt_des=(TextView)itemView.findViewById(R.id.txt_des);
            this.txt_name=(TextView)itemView.findViewById(R.id.txt_name);
            this.txt_price=(TextView)itemView.findViewById(R.id.txt_price);


        }
    }

    public SearchAdapter(ArrayList<CategoryModel> data,Context context,CustomItemClickListener listener) {
        this.dataSet = data;
        this.context=context;
        this.listener=listener;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(dataSet);
    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row_item, parent, false);



        final SearchAdapter.MyViewHolder myViewHolder = new SearchAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final SearchAdapter.MyViewHolder holder, final int listPosition) {



        ImageView imageView = holder.imageViewIcon;
        holder.txt_name.setText(dataSet.get(listPosition).getItem_name());
        holder.txt_des.setText(dataSet.get(listPosition).getDescription());
        holder.txt_price.setText(dataSet.get(listPosition).getPrice());
        db=new DatabaseHandler(context);
        Cursor cursor= db.getItem(dataSet.get(listPosition).getItem_code());


        if(cursor.getCount()>0){
            if (cursor.moveToFirst()) {
                do {

                    quantity = cursor.getString(cursor.getColumnIndex("quantity"));
                    switch (quantity){
                        case "0":
                            holder.txt_quantity.setText("");
                            break;
                        default:
                            holder.txt_quantity.setText(quantity);
                            break;
                    }

                } while (cursor.moveToNext());
            }

        }else {
            holder.txt_quantity.setText("");
        }

        switch (dataSet.get(listPosition).getVeg_non_veg()){
            case "1":
                holder.iv_veg.setImageResource(R.drawable.veg);
                break;
            case "2":
                holder.iv_veg.setImageResource(R.drawable.non_veg);
                break;
        }

        holder.txt_price.setText("  "+ dataSet.get(listPosition).getPrice());
        holder.txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(v,listPosition );
            }
        });
        holder.txt_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(v,listPosition);
            }
        });

    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataSet.clear();
        if (charText.length() == 0) {
            dataSet.addAll(searchList);
        } else {
            for (int i = 0; i < searchList.size(); i++) {
                if (searchList.get(i).getItem_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataSet.add(new CategoryModel(searchList.get(i).getItem_name(),searchList.get(i).getDescription(),searchList.get(i).getItem_code(),searchList.get(i).getPrice(),searchList.get(i).getDiscount_price(),searchList.get(i).getVeg_non_veg()));
                }
            }

        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
