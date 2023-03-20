package com.example.weatheractivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public List<WeatherItem>  weatherItems=new ArrayList<>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dateText;
        public TextView infoText;
        public TextView  maxText;
        public TextView  minText;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText=itemView.findViewById(R.id.date_text);
            infoText=itemView.findViewById(R.id.info_text);
            maxText=itemView.findViewById(R.id.max_text);
            minText=itemView.findViewById(R.id.min_text);
        }
    }

    public MyAdapter(List<WeatherItem> weatherItems){
        this.weatherItems=weatherItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item,parent,false);
       MyViewHolder myViewHolder=new MyViewHolder(itemView);
       return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WeatherItem weatherItem=weatherItems.get(position);
        holder.dateText.setText(weatherItem.date);
        holder.infoText.setText(weatherItem.weather);
        holder.maxText.setText(weatherItem.maxTemp);
        holder.minText.setText(weatherItem.minTemp);

    }



    @Override
    public int getItemCount() {
        return weatherItems.size();
    }
}
