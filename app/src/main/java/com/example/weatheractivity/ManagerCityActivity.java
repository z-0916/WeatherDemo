package com.example.weatheractivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ManagerCityActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<CityItem> cityItems;
    private CityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cityItems=new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_city);
        mRecycleView=findViewById(R.id.city_recycle);
        setCityItem();
        mAdapter=new CityAdapter(cityItems);
        mRecycleView.setAdapter(mAdapter);
        mLayoutManager=new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);
    }

    private void setCityItem() {
        for (int i = 0; i < 20; i++) {
            CityItem cityItem=new CityItem();
            cityItem.setCity("城市"+i);
            cityItems.add(cityItem);
        }
    }
}