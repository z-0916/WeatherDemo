package com.example.weatheractivity.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.weatheractivity.WeatherFragment;

import java.util.List;

public class WeatherFragmentAdapter  extends FragmentStatePagerAdapter {
    //    定义fragment列表来存放fragment
    private List<WeatherFragment> fragmentList;
    public WeatherFragmentAdapter(@NonNull FragmentManager fm,List<WeatherFragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
