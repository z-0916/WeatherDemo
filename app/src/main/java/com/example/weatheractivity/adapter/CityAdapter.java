package com.example.weatheractivity.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatheractivity.CityItem;
import com.example.weatheractivity.R;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {

    public List<CityItem>  cityItems=new ArrayList<>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView city;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            city=itemView.findViewById(R.id.city_text);
        }
    }

    public CityAdapter(List<CityItem>cityItems){
        this.cityItems=cityItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CityItem cityItem=cityItems.get(position);
        holder.city.setText(cityItem.city);
    }
    @Override
    public int getItemCount() {
        return cityItems.size();
    }
}

