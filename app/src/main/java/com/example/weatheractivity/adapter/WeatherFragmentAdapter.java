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
    int childCount=0;

    @Override
    public void notifyDataSetChanged() {
//        表示ViewPager包含的页数
        this.childCount=getCount();
        super.notifyDataSetChanged();
    }

   @Override
    public int getItemPosition(@NonNull Object object) {
        if (childCount>0){
            childCount--;
            return  POSITION_NONE;
        }
     return super.getItemPosition(object);
    }
}
