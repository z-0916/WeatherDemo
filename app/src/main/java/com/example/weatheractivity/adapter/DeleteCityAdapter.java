package com.example.weatheractivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatheractivity.R;
import com.example.weatheractivity.bean.CityWeatherBean;
import com.example.weatheractivity.bean.SevenWeather;
import com.example.weatheractivity.util.Utility;

import java.util.List;

public class DeleteCityAdapter extends BaseAdapter {
    Context context;
    List<String> itemList;
    List<String> deleteCityList;
    String city;

    public DeleteCityAdapter() {
    }

    public DeleteCityAdapter(Context context, List<String> itemList,List<String> deleteCityList) {
        this.context = context;
        this.itemList = itemList;
        this.deleteCityList=deleteCityList;
    }
    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DeleteCityAdapter.ViewHolder holder =null;
        if(view ==null){
            view= LayoutInflater.from(context).inflate(R.layout.delect_city_item,viewGroup,false);
            holder=new DeleteCityAdapter.ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (DeleteCityAdapter.ViewHolder) view.getTag();
        }
        city=itemList.get(i);
        holder.cityName.setText(itemList.get(i));
        if(i==0){
            holder.deleteLeftIm.setVisibility(View.INVISIBLE);
            holder.deleteIm.setVisibility(View.INVISIBLE);
        }else {
            holder.deleteIm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    进行删除操作
                    itemList.remove(city);
                    deleteCityList.add(city);
                    notifyDataSetChanged();
                }
            });
        }

        return view;
    }

    private class ViewHolder{
        TextView cityName;
        ImageView deleteIm,deleteLeftIm;

        public ViewHolder(View itemView) {
            cityName=itemView.findViewById(R.id.tv_delete_item_city);
            deleteIm=itemView.findViewById(R.id.im_delete);
            deleteLeftIm=itemView.findViewById(R.id.im_delete_left);
        }
    }
}
