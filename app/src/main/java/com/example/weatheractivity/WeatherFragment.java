package com.example.weatheractivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatheractivity.adapter.WeatherAdapter;
import com.example.weatheractivity.bean.CityResponse;
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
    private  Context mContext;
    private ImageView imPosition;
    private TextView currentTemperature,updateTime, currentWeatherText,positionText;
    public String  city;
    public List<String> citySelected;
    String locationId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        将fragment布局文件动态加载进来
        View view=inflater.inflate(R.layout.fragment_item,container,false);
        mRecycleView=view.findViewById(R.id.weather_recycle);
        initView(view);
        initRecycleView();
//        通过Activity传过来的city获取到当前页面加载哪个城市
        Bundle bundle=getArguments();
         city=bundle.getString("city");
         citySelected=new ArrayList<>();
         citySelected.add(city);
//         获取并展示天气信息
        requestDataWithOkHttp(city);
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
        imPosition=view.findViewById(R.id.im_fragment_position);
        positionText=view.findViewById(R.id.current_position);
        mRecycleView=view.findViewById(R.id.weather_recycle);
        currentTemperature=view.findViewById(R.id.current_temperature);
        currentWeatherText=view.findViewById(R.id.current_weather_text);
        updateTime=view.findViewById(R.id.update_time);
    }

    public   void  requestDataWithOkHttp( String city){
        String cityUrl="https://geoapi.qweather.com/v2/city/lookup?key=a2e84fe1310d4f79bc79ffe24d70fa2b&location="+city;

        HttpUtil.sendOkHttpRequest(cityUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"发送城市请求失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String cityResponseData = response.body().string();
                Log.d(TAG, cityResponseData);
                final CityResponse cityResponse = Utility.handleCityResponse(cityResponseData);
               locationId = cityResponse.getLocation().get(0).getId();
                String weatherUrl="https://devapi.qweather.com/v7/weather/now?key=a2e84fe1310d4f79bc79ffe24d70fa2b&location="+locationId;
                String weatherUrl7="https://devapi.qweather.com/v7/weather/7d?key=a2e84fe1310d4f79bc79ffe24d70fa2b&location="+locationId;
               if("200".equals(cityResponse.getCode())){
                   HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                       @Override
                       public void onResponse(Call call, Response response) throws IOException {
                           final String responseData=response.body().string();
                           final Weather weather= Utility.handleWeatherResponse(responseData);
                           Log.d(TAG,responseData);
                           if ( "200".equals(weather.getCode())){
                               HttpUtil.sendOkHttpRequest(weatherUrl7, new Callback() {
                                   @Override
                                   public void onResponse(Call call, Response response) throws IOException {
                                       final String responseData=response.body().string();
                                       final SevenWeather sevenWeather= Utility.handleSevenWeatherResponse(responseData);
                                       Log.d(TAG,responseData);
                                       getActivity().runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                               if ( "200".equals(sevenWeather.getCode())){
                                                   showWeatherInfo(weather);
                                                   showSevenWeatherInfo(sevenWeather);
//                             插入数据库，存在：更新；不存在：插入
                                                   List<String> cityDB= DBManager.queryAllCityName();
                                                   if(cityDB.contains(city)){
                                                       DBManager.updateInfoByCity(city,responseData);
                                                   }else {
                                                       DBManager.addCityInfo(city,responseData);
                                                   }
                                               }else {
                                                   Toast.makeText(getActivity(),"七天数据获取失败",Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });
                                   }
                                   @Override
                                   public void onFailure(Call call, IOException e) {
                                       e.printStackTrace();
                                       Log.d(TAG,"获取七天天气数据失败");
                                   }
                               });
                           }else {
                               Log.d(TAG,"获取实时天气数据失败");
                           }
                       }
                       @Override
                       public void onFailure(Call call, IOException e) {
                           e.printStackTrace();
                           Log.d(TAG,"发送实时天气请求失败");
                       }
                   });

               }else{
                   Log.d(TAG,"获取城市请求失败");
               }
            }
        });
    }

    private void showWeatherInfo(Weather weather){
        Weather.NowBean nowBean= weather.getNow();
//        顶部当前时间当前定位天气信息
        positionText.setText(city);
        currentTemperature.setText(weather.getNow().getTemperature()+"℃");
        currentWeatherText.setText(weather.getNow().getText());
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
