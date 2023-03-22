package com.example.weatheractivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatheractivity.adapter.WeatherAdapter;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment  extends Fragment {
    public  String TAG="WeatherDemo";
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<WeatherItem> weatherItems;
    private WeatherAdapter weatherAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        将fragment布局文件动态加载进来
        View view=inflater.inflate(R.layout.fragment_item,container,false);
        mRecycleView=view.findViewById(R.id.weather_recycle);
        return  view;
    }
}
