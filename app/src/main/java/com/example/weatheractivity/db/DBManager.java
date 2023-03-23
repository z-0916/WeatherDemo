package com.example.weatheractivity.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.weatheractivity.bean.CityWeatherBean;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
//    初始化数据库内容
    public static SQLiteDatabase database;
    public static void initDB(Context context){
       MyDBHelper dbHelper= new MyDBHelper(context,"Weather.db",null,1);
       database=dbHelper.getWritableDatabase();
    }

//    查找数据库中城市列表
    public static List<String> queryAllCityName(){
        Cursor cursor=database.query("info",null,null,null,null,null,null);
        List<String> cityList=new ArrayList<>();
        while(cursor.moveToNext()){
            @SuppressLint("Range") String city= cursor.getString(cursor.getColumnIndex("city"));
            cityList.add(city);
        }
        return cityList;
    }
//    新增城市记录
    public static long addCityInfo(String city,String content){
        ContentValues values=new ContentValues();
        values.put("content",content);
        values.put("city",city);
        return  database.insert("info",null,values);
    }

//    根据城市名称替换信息内容
    public static int updateInfoByCity(String city,String content){
        ContentValues values=new ContentValues();
        values.put("content",content);
        return database.update("info",values,city="?",new String[]{city});
    }
//    根据城市名查询数据库当中的内容
    public static String queryInfoByCity(String city){
        Cursor cursor=database.query("info",null,"city=?",new String[]{city},null,null,null);
       if ( cursor.getCount()>0){
           cursor.moveToFirst();
           @SuppressLint("Range") String content=cursor.getString(cursor.getColumnIndex("content"));
           return content;
       }
       return  null;
    }
//    查询数据库中所有信息
    public static List<CityWeatherBean> queryAllInfo(){
       Cursor cursor= database.query("info",null,null,null,null,null,null);
        List<CityWeatherBean> beans=new ArrayList<>();
        while(cursor.moveToNext()){
            @SuppressLint("Range") int id=cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String city=cursor.getString(cursor.getColumnIndex("city"));
            @SuppressLint("Range") String content=cursor.getString(cursor.getColumnIndex("content"));
            CityWeatherBean bean=new CityWeatherBean(id,city,content);
            beans.add(bean);
        }
        return  beans;
    }
}
