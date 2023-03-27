package com.example.weatheractivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.weatheractivity.adapter.DeleteCityAdapter;
import com.example.weatheractivity.bean.CityWeatherBean;
import com.example.weatheractivity.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DeleteCityActivity extends AppCompatActivity  implements View.OnClickListener {
    private ImageView trueIm,falseIm;
    private ListView deleteCityLv;
    private List<String> itemList;
    private List<String> deleteCity;
    private DeleteCityAdapter deleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_city);
        bindView();
        trueIm.setOnClickListener(this);
        falseIm.setOnClickListener(this);
        itemList=new ArrayList<>();
        deleteCity=new ArrayList<>();
        deleteAdapter=new DeleteCityAdapter(this,itemList,deleteCity);
        deleteCityLv.setAdapter(deleteAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> cityList= DBManager.queryAllCityName();
//        清空itemList
        itemList.clear();
        itemList.addAll(cityList);
//        提示更新
        deleteAdapter.notifyDataSetChanged();

    }

    private void bindView() {
        trueIm=findViewById(R.id.im_true);
        falseIm=findViewById(R.id.im_error);
        deleteCityLv=findViewById(R.id.delete_city_lv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.im_true:
                for (int i = 0; i <deleteCity.size(); i++) {
                    String city=deleteCity.get(i);
//                  数据库中删除城市
                    DBManager.deleteInfoByCity(city);
                    Intent intent=new Intent("com.example.weatherActivity.DELETE_BROADCAST");
                    intent.putExtra("deleteCity",city);
                    sendBroadcast(intent);
                    finish();
                }
                finish();
                break;
            case R.id.im_error:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("提示信息").setMessage("确定放弃修改吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
                break;
        }
    }
}