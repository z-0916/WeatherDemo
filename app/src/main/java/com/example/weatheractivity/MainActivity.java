package com.example.weatheractivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<WeatherItem> weatherItems;
    private  MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        mRecycleView.setHasFixedSize(true);
        setWeatherItem();
        mAdapter=new MyAdapter(weatherItems);
        mRecycleView.setAdapter(mAdapter);
        //Log.d("TAG","  setWAdapter()执行结束");
        mLayoutManager=new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);
       // Log.d("TAG","  setLayoutManager执行结束");
        DividerItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecycleView.addItemDecoration(itemDecoration);
    }

    private void bindView() {
        mRecycleView=findViewById(R.id.weather_recycle);
    }
     private void setWeatherItem(){
       weatherItems=new ArrayList<>();
         for (int i = 0; i <20 ; i++) {
             WeatherItem weatherItem=new WeatherItem();
             weatherItem.setDate("3月"+i+"日");
             weatherItem.setWeather("晴朗");
             weatherItem.setMaxTemp("20");
             weatherItem.setMinTemp("3");
             weatherItems.add(weatherItem);
         }
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

}