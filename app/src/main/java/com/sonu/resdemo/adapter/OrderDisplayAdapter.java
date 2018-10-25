package com.sonu.resdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sonu.resdemo.R;
import com.sonu.resdemo.interfaces.CustomItemClickListener;
import com.sonu.resdemo.model.OrderModel;
import com.sonu.resdemo.utils.DatabaseHandler;

import java.util.ArrayList;

/**
 * Created by Devesh D on 3/5/2018.
 */

public class OrderDisplayAdapter extends RecyclerView.Adapter<OrderDisplayAdapter.MyViewHolder> {
    String item_code,quantity;
    private DatabaseHandler db;
    private ArrayList<OrderModel> dataSet;
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

    public OrderDisplayAdapter(ArrayList<OrderModel> data,Context context,CustomItemClickListener listener) {
        this.dataSet = data;
        this.context=context;
        this.listener=listener;
    }

    @Override
    public OrderDisplayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_row, parent, false);



        final OrderDisplayAdapter.MyViewHolder myViewHolder = new OrderDisplayAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final OrderDisplayAdapter.MyViewHolder holder, final int listPosition) {



        ImageView imageView = holder.imageViewIcon;
        holder.txt_name.setText(dataSet.get(listPosition).getItem_name());
       // holder.txt_des.setText(dataSet.get(listPosition).getDescription());
        holder.txt_price.setText(dataSet.get(listPosition).getPrice());
        holder.txt_quantity.setText(dataSet.get(listPosition).getQuantity());
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

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
