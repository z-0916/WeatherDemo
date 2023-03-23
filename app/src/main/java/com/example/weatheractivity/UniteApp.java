package com.example.weatheractivity;

import android.app.Application;

import com.example.weatheractivity.db.DBManager;

public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        初始化数据库，项目工程一旦被创建，数据库也会被初始化
        DBManager.initDB(this);
    }
}
