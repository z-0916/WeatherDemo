package com.example.weatheractivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatheractivity.adapter.WeatherAdapter;
import com.example.weatheractivity.bean.SevenWeather;
import com.example.weatheractivity.bean.Weather;
import com.example.weatheractivity.db.DBManager;
import com.example.weatheractivity.util.FormatDate;
import com.example.weatheractivity.util.HttpUtil;
import com.example.weatheractivity.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherFragment  extends Fragment {
    public  String TAG="WeatherDemo";
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<WeatherItem> weatherItems;
    private WeatherAdapter weatherAdapter;
    private TextView currentTemperature,updateTime, currentWeatherText,positionText;

    public String  currentCity,city;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        将fragment布局文件动态加载进来
        View view=inflater.inflate(R.layout.fragment_item,container,false);
        mRecycleView=view.findViewById(R.id.weather_recycle);
        initView(view);
        //        接下来设置recycleView
        initRecycleView();
//        通过Activity获取到当前页面加载哪个城市
        Bundle bundle=getArguments();
        currentCity=bundle.getString("currentCity");
         city=bundle.getString("city");
//         获取并展示天气信息
        String locationId="101010100";
        requestCityWithOkHttp("西安");
        requestWeatherWithOkHttp(city);
        requestSevenWeatherWithOkHttp(city);
        return  view;
    }

    private void initRecycleView() {
        weatherItems = new ArrayList<>();
        mRecycleView.setHasFixedSize(true);
        weatherAdapter = new WeatherAdapter(weatherItems);
        mRecycleView.setAdapter(weatherAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mRecycleView.addItemDecoration(itemDecoration);
    }

    private void initView(View view) {
        positionText=view.findViewById(R.id.current_position);
        mRecycleView=view.findViewById(R.id.weather_recycle);
        currentTemperature=view.findViewById(R.id.current_temperature);
        currentWeatherText=view.findViewById(R.id.current_weather_text);
        updateTime=view.findViewById(R.id.update_time);
    }

    private  void requestWeatherWithOkHttp( String locationId){
        String weatherUrl="https://devapi.qweather.com/v7/weather/now?key=a2e84fe1310d4f79bc79ffe24d70fa2b&location="+locationId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData=response.body().string();
                final Weather weather= Utility.handleWeatherResponse(responseData);
                final  String result;
//                Log.d(TAG,responseData);
               getActivity(). runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ( "200".equals(weather.getCode())){
                            showWeatherInfo(weather);
                        }else {
//                            数据库中查找上一次信息显示在fragment中
//                            String s=DBManager.queryInfoByCity(city);
//                            if (!TextUtils.isEmpty(s)){
//                                showWeatherInfo();
//                            }
                            Toast.makeText(getContext(),"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
    private  void requestSevenWeatherWithOkHttp( String locationId){
        String weatherUrl="https://devapi.qweather.com/v7/weather/7d?key=a2e84fe1310d4f79bc79ffe24d70fa2b&location="+locationId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData=response.body().string();
                final SevenWeather sevenWeather= Utility.handleSevenWeatherResponse(responseData);
//                Log.d(TAG,responseData);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ( "200".equals(sevenWeather.getCode())){
                            showSevenWeatherInfo(sevenWeather);
//                             插入数据库，存在：更新；不存在：插入
                            int i= DBManager.updateInfoByCity(city,responseData);
                            if (i<=0){
//                               更新数据失败，说明没有这个城市，则增加这个城市的记录
                                DBManager.addCityInfo(city,responseData);
                            }
                        }else {
                            Toast.makeText(getContext(),"获取七天天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"获取七天天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
    private  void requestCityWithOkHttp( String city){
        String cityUrl="https://geoapi.qweather.com/v2/city/lookup?key=a2e84fe1310d4f79bc79ffe24d70fa2b&location="+city;
        HttpUtil.sendOkHttpRequest(cityUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String cityResponseData=response.body().string();
//                final SevenWeather sevenWeather= Utility.handleSevenWeatherResponse(cityResponseData);
                Log.d(TAG,cityResponseData);
            }
        });
    }

    private void showWeatherInfo(Weather weather){
        Weather.NowBean nowBean= weather.getNow();
//        顶部当前时间当前定位天气信息
        positionText.setText(city);
        currentTemperature.setText(nowBean.getTemperature()+"℃");
        currentWeatherText.setText(nowBean.getText());
        String time= FormatDate.updateTime(weather.getUpdateTime());
//        updateTime.setText("更新于:"+weather.getUpdateTime());
        updateTime.setText(String.format("最新更新时间：%s%s", FormatDate.showTimeInfo(time),time));
    }
    private  void showSevenWeatherInfo(SevenWeather sevenWeather){
        for (int i = 0; i <7 ; i++) {
            WeatherItem weatherItem=new WeatherItem();
            weatherItem.setDate(sevenWeather.getDaily().get(i).getFxDate());
            weatherItem.setWeather(sevenWeather.getDaily().get(i).getTextDay());
            weatherItem.setMaxTemp(sevenWeather.getDaily().get(i).getTempMax());
            weatherItem.setMinTemp(sevenWeather.getDaily().get(i).getTempMin());
            weatherItems.add(weatherItem);
            Log.d(TAG,"展示七天数据结束");
        }
    }

}
