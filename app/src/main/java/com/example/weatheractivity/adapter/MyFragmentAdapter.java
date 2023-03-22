package com.example.weatheractivity.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weatheractivity.WeatherFragment;

import java.util.List;

public class MyFragmentAdapter  extends FragmentStateAdapter {

//    定义fragment列表来存放fragment
    private List<WeatherFragment> fragmentLists;

    public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity,List<WeatherFragment> fragmentLists) {
        super(fragmentActivity);
        this.fragmentLists=fragmentLists;
    }


    @NonNull
    @Override
    /**
     * createFragment:用来根据position创建fragment
     */
    public Fragment createFragment(int position) {
        return  fragmentLists.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentLists.size();
    }
}
