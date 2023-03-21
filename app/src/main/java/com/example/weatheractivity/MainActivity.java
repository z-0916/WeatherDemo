package com.example.weatheractivity;

import static com.example.weatheractivity.util.Utility.handleWeatherResponse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.weatheractivity.bean.SevenWeather;
import com.example.weatheractivity.bean.Weather;
import com.example.weatheractivity.util.HttpUtil;
import com.example.weatheractivity.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

//   LocationClient类必须在主线程中声明，需要Context参数
    public LocationClient mLocationClient;

    public MLocationListener myListener;
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<WeatherItem> weatherItems;
    private  MyAdapter mAdapter;

    public TextView positionText;
    private TextView currentTemperature;
    private  TextView currentWeatherText;
    private  TextView updateTime;

    public  String TAG="WeatherDemo";

    public TextView getPositionText() {
        return positionText;
    }

    public void setPositionText(TextView positionText) {
        this.positionText = positionText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        weatherItems=new ArrayList<>();
        mRecycleView.setHasFixedSize(true);
        mAdapter=new MyAdapter(weatherItems);
        mRecycleView.setAdapter(mAdapter);
        mLayoutManager=new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecycleView.addItemDecoration(itemDecoration);
        /**
         * 定位服务
         */
        initLocation();
        applyPermissions();

        /**
         * 展示天气
         */
            String locationId="101010100";
            requestWeatherWithOkHttp(locationId);
            requestSevenWeatherWithOkHttp(locationId);
    }

    private void bindView() {
        positionText=findViewById(R.id.current_position);
        mRecycleView=findViewById(R.id.weather_recycle);
        currentTemperature=findViewById(R.id.current_temperature);
        currentWeatherText=findViewById(R.id.current_weather_text);
        updateTime=findViewById(R.id.update_time);
    }

    /**
     *将天气信息展示到界面上
     */
    private void showWeatherInfo(Weather weather){
//        顶部当前时间当前定位天气信息
        currentTemperature.setText("当前温度："+weather.getNow().getTemperature());
        currentWeatherText.setText("天气："+weather.getNow().getText());
        updateTime.setText("更新于:"+weather.getUpdateTime());
    }
     private  void showSevenWeatherInfo(SevenWeather sevenWeather){
         for (int i = 0; i <7 ; i++) {
             WeatherItem weatherItem=new WeatherItem();
             weatherItem.setDate(sevenWeather.getDaily().get(i).getFxDate());
             weatherItem.setWeather(sevenWeather.getDaily().get(i).getTextDay());
             weatherItem.setMaxTemp(sevenWeather.getDaily().get(i).getTempMax());
             weatherItem.setMinTemp(sevenWeather.getDaily().get(i).getTempMin());
             weatherItems.add(weatherItem);
         }
     }
//     private void setWeatherItem(){
//         weatherItems=new ArrayList<>();
//         for (int i = 0; i <20 ; i++) {
//             WeatherItem weatherItem=new WeatherItem();
//             weatherItem.setDate("3月"+i+"日");
//             weatherItem.setWeather("晴朗");
//             weatherItem.setMaxTemp("20");
//             weatherItem.setMinTemp("3");
//             weatherItems.add(weatherItem);
//         }
//        }


    /**
     * 定位相关部分：初始化位置
     *
     * @return
     */

    private void initLocation(){
        try {
    //  声明LocationClient类
           LocationClient.setAgreePrivacy(true);
            mLocationClient=new LocationClient(getApplicationContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (mLocationClient!=null){
            // 注册监听函数：当获取到位置信息的时候就会回调这个定位监听器
            myListener=new MLocationListener();
            mLocationClient.registerLocationListener(myListener);
            LocationClientOption option=new LocationClientOption();
//            设置每5秒更新一次位置信息
//            option.setScanSpan(5000);
            option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
            option.setIsNeedAddress(true);
            mLocationClient.setLocOption(option);
        }
    }

    /**
     * 权限申请
     */

    private void applyPermissions(){
        List<String> permissionList=new ArrayList<>();
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
            Log.d(TAG,"开始定位");
            startLocation();
        }
    }

    private  void startLocation(){
        mLocationClient.start();
        Log.d(TAG,"start方法启动");
    }


    /**
     *
     * @param requestCode The request code
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
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

    /**
     * 接下来为获取天气数据部分
     */


    private  void requestWeatherWithOkHttp( String locationId){
        String weatherUrl="https://devapi.qweather.com/v7/weather/now?key=a2e84fe1310d4f79bc79ffe24d70fa2b&location="+locationId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData=response.body().string();
                final  Weather weather= Utility.handleWeatherResponse(responseData);
                Log.d(TAG,responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ( "200".equals(weather.getCode())){
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(MainActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
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
                final SevenWeather  sevenWeather= Utility.handleSevenWeatherResponse(responseData);
                Log.d(TAG,responseData);
                Log.d(TAG, sevenWeather.getDaily().get(0).getTextDay());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ( "200".equals(sevenWeather.getCode())){
                            showSevenWeatherInfo(sevenWeather);
                        }else {
                            Toast.makeText(MainActivity.this,"获取七天天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"获取七天天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    /**
     * 以下为设置图标部分
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.weather_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_share:
                Toast.makeText(this,"分享",Toast.LENGTH_LONG).show();
                return  true;
            case R.id.item_manager:
                Intent intent=new Intent(MainActivity.this,ManagerCityActivity.class);
                startActivity(intent);

                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    private  class MLocationListener extends BDAbstractLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder currentPosition = new StringBuilder();
//            currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
//            currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
//            currentPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
//            currentPosition.append("省：").append(bdLocation.getProvince()).append("\n");
//            currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
//            currentPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
            currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
//            currentPosition.append("定位方式：");
//            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
//                currentPosition.append("GPS");
//            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
//                currentPosition.append("网络");
//            }
            positionText.setText(currentPosition);
            Log.d("WeatherDemo", String.valueOf(currentPosition));
        }
    }

}