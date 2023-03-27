package com.example.weatheractivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weatheractivity.R;
import com.example.weatheractivity.bean.CityWeatherBean;
import com.example.weatheractivity.bean.SevenWeather;
import com.example.weatheractivity.util.Utility;

import java.util.List;

public class CityManagerAdapter extends BaseAdapter {
    Context context;
    List<CityWeatherBean> itemList;
    public CityManagerAdapter(Context context,List<CityWeatherBean> itemList) {
        this.context=context;
        this.itemList = itemList;
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
        ViewHolder holder =null;
        if(view ==null){
            view= LayoutInflater.from(context).inflate(R.layout.city_item,viewGroup,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        CityWeatherBean  bean=itemList.get(i);
        holder.cityName.setText(bean.getCity());
//        解析数据
        SevenWeather weather=Utility.handleSevenWeatherResponse(bean.getContent());
        holder.cityText.setText(weather.getDaily().get(0).getTextDay());
//        TODO：当前温度设置
        holder.cityNowTemp.setText("最高"+weather.getDaily().get(0).getTempMax()+"℃");
        holder.cityMaxMinTemp.setText("最低"+weather.getDaily().get(0).getTempMin()+"℃");

        return view;
    }
    private class ViewHolder{
        TextView cityName,cityText,cityNowTemp,cityMaxMinTemp;

        public ViewHolder(View itemView) {
            cityName=itemView.findViewById(R.id.tv_item_city);
            cityText=itemView.findViewById(R.id.tv_item_text);
            cityNowTemp=itemView.findViewById(R.id.tv_item_now_weather);
            cityMaxMinTemp=itemView.findViewById(R.id.tv_item_max_min_temp);
        }
    }
}
