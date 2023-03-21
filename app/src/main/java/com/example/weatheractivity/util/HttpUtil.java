package com.example.weatheractivity.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
//    发起一条HTTP请求只需要调用sendOkHttpRequest传入一个地址，并注册一个回调来处理服务器响应
    public static void sendOkHttpRequest(String  address, Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
