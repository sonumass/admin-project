package com.sonu.resdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sonu.resdemo.R;
import com.sonu.resdemo.activity.OrderDetailActivity;
import com.sonu.resdemo.model.MyOrderListModel;
import com.sonu.resdemo.utils.DatabaseHandler;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Devesh D on 3/22/2018.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {
    String item_code,quantity;
    private DatabaseHandler db;
    private ArrayList<MyOrderListModel> dataSet;
    Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView txt_price,txt_date,txt_orderno,txt_mobile;
        LinearLayout item_layout;


        public MyViewHolder(View itemView) {
            super(itemView);

            this.txt_date=(TextView)itemView.findViewById(R.id.txt_date);
            this.txt_mobile=(TextView)itemView.findViewById(R.id.txt_mobile);
            this.txt_orderno=(TextView)itemView.findViewById(R.id.txt_orderno);
            this.txt_price=(TextView)itemView.findViewById(R.id.txt_price);
            this.item_layout=(LinearLayout)itemView.findViewById(R.id.item_layout);


        }
    }

    public MyOrderAdapter(ArrayList<MyOrderListModel> data,Context context) {
        this.dataSet = data;
        this.context=context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorder_list_row, parent, false);



        final MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {




        holder.txt_price.setText("Total Price :"+dataSet.get(listPosition).getPrice());
        holder.txt_orderno.setText("Order NO :"+dataSet.get(listPosition).getOrderno());
        holder.txt_date.setText(dataSet.get(listPosition).getDate());
        holder.txt_mobile.setText("Mobile NO :"+dataSet.get(listPosition).getMobile());


        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OrderDetailActivity.class);
                intent.putExtra("price",dataSet.get(listPosition).getPrice());
                intent.putExtra("mobile",dataSet.get(listPosition).getMobile());
                intent.putExtra("orderno",dataSet.get(listPosition).getOrderno());
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}