package com.example.weatheractivity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {
    public   String TAG="WeatherDemo";
    public static final String CREATE_INFO="create table Info(" +
            "id integer primary key autoincrement," +
            "city varchar(30) not null unique ," +
            "content text not null)";
    private Context mContext;
    public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_INFO);
        Log.d(TAG,"数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
