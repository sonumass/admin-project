package com.sonu.resdemo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
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

/**
 * Created by Devesh D on 2/23/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
     String item_code,quantity;
  private  DatabaseHandler db;
    private ArrayList<CategoryModel> dataSet;
    Context context;
    public String param;

    CustomItemClickListener listener;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_veg;
        TextView textViewVersion;
        TextView txt_name,txt_price,txt_des,txt_plus,txt_quantity,txt_min;
        LinearLayout layout_item;
        ImageView imageViewIcon;
        CardView card_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.iv_veg = (ImageView) itemView.findViewById(R.id.iv_veg);
            this.txt_min=(TextView)itemView.findViewById(R.id.txt_min);
            this.txt_quantity=(TextView)itemView.findViewById(R.id.txt_quantity);
            this.txt_plus=(TextView)itemView.findViewById(R.id.txt_plus);
            this.txt_des=(TextView)itemView.findViewById(R.id.txt_des);
            this.txt_name=(TextView)itemView.findViewById(R.id.txt_name);
            this.txt_price=(TextView)itemView.findViewById(R.id.txt_price);
            this.card_item=(CardView)itemView.findViewById(R.id.card_item);


        }
    }

    public CategoryAdapter(ArrayList<CategoryModel> data,Context context,CustomItemClickListener listener) {
        this.dataSet = data;
        this.context=context;
        this.listener=listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row_item, parent, false);



   final     MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {



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

        holder.card_item.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onItemClick(v,listPosition );
          }
        });

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
        //imageView.setImageResource(dataSet.get(listPosition).getPhoto());

       /* holder.txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*Intent intent=new Intent(context, VideoDetailActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*//*


                 int sum=0;
                if(holder.txt_quantity.getText().toString().trim()==""){
                    sum=sum+1;
                    holder.txt_quantity.setText(""+sum);
                    db.insertitem(dataSet.get(listPosition).getItem_code(),holder.txt_quantity.getText().toString(),dataSet.get(listPosition).getPrice());
                }else {

                    String a=holder.txt_quantity.getText().toString();
                  int  summ=Integer.valueOf(a)+1;
                    holder.txt_quantity.setText(""+summ);
                    db.updatecart(dataSet.get(listPosition).getItem_code(),holder.txt_quantity.getText().toString());
                }


            }
        });*/
       /* holder.txt_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  int min=0;
                if((holder.txt_quantity.getText().toString().trim()=="")||(holder.txt_quantity.getText().toString().trim()=="0")){

                    holder.txt_quantity.setText("");

                }else {

                    String a=holder.txt_quantity.getText().toString();
                    if(Integer.valueOf(a)<1){
                        holder.txt_quantity.setText("");
                    }else {
                        min=Integer.valueOf(a)-1;
                        holder.txt_quantity.setText(""+min);
                        db.updatecart(dataSet.get(listPosition).getItem_code(),holder.txt_quantity.getText().toString());
                    }

                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
