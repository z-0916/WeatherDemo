package com.example.weatheractivity.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

//    API状态码
    private String code;
//    当前api最近更新时间
    private String updateTime;
//    当前数据的响应页面
    private String fxLink;

    private NowBean now;

    private ReferBean refer;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public NowBean getNow() {
        return now;
    }

    public void setNow(NowBean now) {
        this.now = now;
    }

    public ReferBean getRefer() {
        return refer;
    }

    public void setRefer(ReferBean refer) {
        this.refer = refer;
    }

    public class NowBean{
//        数据观测时间
        private String obsTime;
//        温度
        @SerializedName("temp")
        private String temperature;
//        体感温度
        private String feelsLike;
//        天气状况和图标的代码
        private String icon;
//        天气状况文字描述
        private String text;
//        风向360角度
        private String wind360;
//        风向
        private String winDir;
//        风力等级
        private String winScale;
//        风速
        private String winSpeed;
//        相对湿度
        private String humidity;
//        当前小时累计降水量
        @SerializedName("precip")
        private String preCip;
//        大气压强
        private String pressure;
//        能见度
        private  String vis;
//        云量
        private String cloud;
//        露点温度
        private String dew;

        public String getObsTime() {
            return obsTime;
        }

        public void setObsTime(String obsTime) {
            this.obsTime = obsTime;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(String feelsLike) {
            this.feelsLike = feelsLike;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getWind360() {
            return wind360;
        }

        public void setWind360(String wind360) {
            this.wind360 = wind360;
        }

        public String getWinDir() {
            return winDir;
        }

        public void setWinDir(String winDir) {
            this.winDir = winDir;
        }

        public String getWinScale() {
            return winScale;
        }

        public void setWinScale(String winScale) {
            this.winScale = winScale;
        }

        public String getWinSpeed() {
            return winSpeed;
        }

        public void setWinSpeed(String winSpeed) {
            this.winSpeed = winSpeed;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getPreCip() {
            return preCip;
        }

        public void setPreCip(String preCip) {
            this.preCip = preCip;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public String getCloud() {
            return cloud;
        }

        public void setCloud(String cloud) {
            this.cloud = cloud;
        }

        public String getDew() {
            return dew;
        }

        public void setDew(String dew) {
            this.dew = dew;
        }
    }

    public  static class ReferBean{
//        数据原始来源
        private  List<String> sources;
//        数据许可或版权说明
        private  List<String> license;
    }
}
