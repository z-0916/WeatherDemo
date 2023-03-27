package com.example.weatheractivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.weatheractivity.adapter.CityManagerAdapter;
import com.example.weatheractivity.bean.CityWeatherBean;
import com.example.weatheractivity.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backIV,editCityIm;
    private ListView myListView;
    private CardView addCityCard;
//    显示列表数据源：数据库中存储的数据
    private List<CityWeatherBean>  itemList;
    private CityManagerAdapter cityManagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        bindView();
//        添加点击事件
        backIV.setOnClickListener(this);
        addCityCard.setOnClickListener(this);
        editCityIm.setOnClickListener(this);
        itemList=new ArrayList<>();
        cityManagerAdapter=new CityManagerAdapter(this,itemList);
        myListView.setAdapter(cityManagerAdapter);
    }

//    获取数据库真实数据，添加到原有数据源中，提示适配器更新
    @Override
    protected void onResume() {
        super.onResume();
//        获取数据源
        List<CityWeatherBean> beans= DBManager.queryAllInfo();
//        清空itemList
        itemList.clear();
        itemList.addAll(beans);
//        提示更新
        cityManagerAdapter.notifyDataSetChanged();
    }

    private void bindView() {
        backIV=findViewById(R.id.im_back);
        editCityIm=findViewById(R.id.im_edit_city);
        myListView=findViewById(R.id.city_list_view);
        addCityCard=findViewById(R.id.city_card_add);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.im_back:
               finish();
                break;
            case R.id.city_card_add:
                Intent intent=new Intent(CityManagerActivity.this,AddCityActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.im_edit_city:
                intent=new Intent(CityManagerActivity.this,DeleteCityActivity.class);
                startActivity(intent);
        }
    }
}