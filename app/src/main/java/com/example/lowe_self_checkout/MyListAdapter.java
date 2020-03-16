package com.example.lowe_self_checkout;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    //private MyListData[] listdata;
    private ArrayList<MyListData> listdata;
    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<MyListData> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.card_view_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyListData myListData = listdata.get(position);
        holder.textView.setText("Barcode: "+listdata.get(position).getBarcode());
        holder.imageView.setText("Name: "+listdata.get(position).getName());
        holder.textView3.setText("Weight "+listdata.get(position).getPrice());
        holder.textView4.setText("Price: "+listdata.get(position).getWeight()+"Rs");
        holder.relativeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+myListData.getName(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return listdata.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView imageView;
        public TextView textView;
        public TextView textView3;
        public TextView textView4;

        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (TextView) itemView.findViewById(R.id.textView2);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.textView3=(TextView)itemView.findViewById(R.id.textView3);
            this.textView4=(TextView)itemView.findViewById(R.id.textView4);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}