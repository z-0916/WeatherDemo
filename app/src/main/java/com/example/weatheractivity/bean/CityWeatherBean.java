package com.example.weatheractivity.bean;

import androidx.annotation.Nullable;

public class CityWeatherBean {
    private int id;
    private String  city;
    private String content;

    public CityWeatherBean() {
    }

    public CityWeatherBean(int id, String city, String content) {
        this.id = id;
        this.city = city;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this==obj){
            return  true;
        }
        if (obj == null || getClass() != obj.getClass()) return false;
        CityWeatherBean bean= (CityWeatherBean) obj;
        if (id==bean.id){
            return false;
        }
        return  city.equals(bean.city);
    }
}
