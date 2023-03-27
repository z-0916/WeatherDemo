package com.example.weatheractivity.util;

import android.util.Log;

import com.example.weatheractivity.bean.CityResponse;
import com.example.weatheractivity.bean.SevenWeather;
import com.example.weatheractivity.bean.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    public static String TAG="WeatherDemo";
    /**
     * 将返回的城市数据解析成类
     */

    public  static Weather handleWeatherResponse(String response)  {
//        JSONObject jsonObject=new JSONObject();
//        JSONArray jsonArray= null;
//            jsonArray = jsonObject.getJSONArray("HeWeather");
//            String weatherContent=jsonArray.getJSONObject(0).toString();
            return  new Gson().fromJson(response,Weather.class);
    }
    public  static SevenWeather handleSevenWeatherResponse(String response)  {

        return  new Gson().fromJson(response,SevenWeather.class);
    }
    public  static CityResponse handleCityResponse(String response)  {

        return  new Gson().fromJson(response,CityResponse.class);
    }
}
