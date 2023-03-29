package com.example.weatheractivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.weatheractivity.adapter.WeatherAdapter;
import com.example.weatheractivity.adapter.WeatherFragmentAdapter;
import com.example.weatheractivity.bean.CityResponse;
import com.example.weatheractivity.bean.SevenWeather;
import com.example.weatheractivity.bean.Weather;
import com.example.weatheractivity.db.DBManager;
import com.example.weatheractivity.util.FormatDate;
import com.example.weatheractivity.util.HttpUtil;
import com.example.weatheractivity.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//   LocationClient类必须在主线程中声明，需要Context参数
    public LocationClient mLocationClient;

    public MLocationListener myListener;
    private ViewPager myViewPager;
//    ViewPager数据源
    private List<WeatherFragment> fragmentList;
//    城市列表,选中的城市的集合
    private  List<String > cityList;
//    表示页数
    private  List<ImageView> imgList;
    private WeatherFragmentAdapter myWfAdapter;

    public TextView positionText;
    private ImageView imMore,imAddCity;
    public  String district,addCity,deleteCity;

    public   String TAG="WeatherDemo";

    private BroadcastReceiver cityBroadCastReceiver,deleteCityBroadCastReceiver;
    private IntentFilter intentFilter,deleteIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imAddCity=findViewById(R.id.add_city);
        imMore=findViewById(R.id.more);
        imAddCity.setOnClickListener(this);
        myViewPager=findViewById(R.id.view_pager);
        imMore.setOnClickListener(this);

        /**
         * 定位当前城市
         */
        LocationClient.setAgreePrivacy(true);
        try {
//            声明LocationClient类实例
            mLocationClient = new LocationClient(getApplicationContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        注册监听函数：当获取到位置信息的时候就会回调这个定位监听器
        myListener = new MLocationListener();
        mLocationClient.registerLocationListener(myListener);
        applyPermissions();
//        ViewPager添加fragment数据
        fragmentList=new ArrayList<>();
        cityList= DBManager.queryAllCityName();
        imgList=new ArrayList<>();
        if (cityList.size()==0){
//            如果没有数据，先将当前定位 的城市数据加载
            cityList.add("长安区");
        }
//        初始化ViewPager页面
        initViewPager();
        myWfAdapter=new WeatherFragmentAdapter(getSupportFragmentManager(),fragmentList);
        myViewPager.setAdapter(myWfAdapter);

//        创建广播接收器
        intentFilter=new IntentFilter();
        deleteIntentFilter=new IntentFilter();
        deleteIntentFilter.addAction("com.example.weatherActivity.DELETE_BROADCAST");
        intentFilter.addAction("com.example.weatherActivity.MY_BROADCAST");
        cityBroadCastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                addCity=intent.getExtras().getString("addCity");
                cityList.add(addCity);
                fragmentList.clear();
                initViewPager();
                myWfAdapter.notifyDataSetChanged();
            }
        };
        deleteCityBroadCastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String deleteCity=intent.getExtras().getString("deleteCity");
                cityList.remove(deleteCity);
                fragmentList.clear();
                initViewPager();
                myWfAdapter.notifyDataSetChanged();
            }
        };
        registerReceiver(cityBroadCastReceiver,intentFilter);
        registerReceiver(deleteCityBroadCastReceiver,deleteIntentFilter);
    }

    private void initViewPager(){
//        创建Fragment对象添加到集合
        for( int i=0;i<cityList.size();i++){
            WeatherFragment weatherFragment=new WeatherFragment();
            Bundle bundle=new Bundle();
            bundle.putString("city",cityList.get(i));
            weatherFragment.setArguments(bundle);
            fragmentList.add(weatherFragment);
        }
    }

    /**
     * 定位相关部分：sdk设置
     *
     * @return
     */

    private void initLocation(){
            LocationClientOption option=new LocationClientOption();
//            设置每5秒更新一次位置信息
//            option.setScanSpan(5000);
            option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
            option.setIsNeedAddress(true);
            mLocationClient.setLocOption(option);
    }

    /**
     * 权限申请：运行时一次性申请三个权限
     */

    private void applyPermissions(){
        List<String> permissionList=new ArrayList<>();
//        ContextCompat.checkSelfPermission()：此方法返回 PERMISSION_GRANTED 或PERMISSION_DENIED，具体取决于您的应用是否具有权限，用于检测用户是否授权了某个权限。
//        PackageManager.PERMISSION_GRANTED 表示授权成功
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String [] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else {
            startLocation();
        }
    }
    /**
     * 何时调用：当用户响应系统权限对话框后，系统就会调用应用的 onRequestPermissionsResult() 实现。系统会传入用户对权限对话框的响应以及您定义的请求代码，
     *  该方法主要用于对申请的权限进行判断，如果有任何一个权限被用户拒绝，直接调用finish关闭当前程序，只有所有权限都被用户同意了，才会开始地理位置定位
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults) {
                        if (result!= PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用本程序",Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    startLocation();
                }
                else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    private  void startLocation() {
        initLocation();
        mLocationClient.start();
        Log.d(TAG, "start方法启动");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cityBroadCastReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_city:
                Intent intent=new Intent(MainActivity.this,CityManagerActivity.class);
                startActivity(intent);
                break;
            case  R.id.more:
             intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
    private  class MLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
           district=bdLocation.getDistrict();
           Log.d(TAG,district);
        }
    }
}