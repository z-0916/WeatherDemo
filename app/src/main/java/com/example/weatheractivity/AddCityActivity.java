package com.example.weatheractivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
public class AddCityActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etSearch;
    private ImageView imSearch;
    private GridView gvSearch;
    public String TAG = "WeatherDemo";
    String locationId;
    String city;
    private ArrayAdapter<String> arrayAdapter;
    private String[] popularCity = {"北京", "上海", "重庆", "广州", "成都", "深圳", "杭州", "三亚", "西安", "昆明", "长沙", "青岛"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        bindViews();
        imSearch.setOnClickListener(this);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item_gride, popularCity);
        gvSearch.setAdapter(arrayAdapter);
        setListener();
    }

    private void setListener() {
        gvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                city = popularCity[i];
                Intent intent=new Intent("com.example.weatherActivity.MY_BROADCAST");
                intent.putExtra("addCity",city);
                sendBroadcast(intent);
                finish();
            }
        });
    }

    private void bindViews() {
        etSearch = findViewById(R.id.et_search);
        imSearch = findViewById(R.id.im_search);
        gvSearch = findViewById(R.id.gv_search);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_search:
                city = etSearch.getText().toString();
                Log.d(TAG, city);
                if (!TextUtils.isEmpty(city)) {
                    Intent intent=new Intent("com.example.weatherActivity.MY_BROADCAST");
                    intent.putExtra("addCity",city);
                    sendBroadcast(intent);
                    finish();
                }
                break;
        }
    }
}
