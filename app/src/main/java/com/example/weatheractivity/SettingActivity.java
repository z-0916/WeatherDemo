package com.example.weatheractivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.util.Calendar;
import java.util.TimeZone;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private ImageView setBack;
    private Switch swMorning;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setBack = findViewById(R.id.im_set_back);
        swMorning = findViewById(R.id.sw_morning);
        final boolean flag = false;
        SharedPreferences preferences;
        preferences=getSharedPreferences("switchStatus",MODE_PRIVATE);
        if (preferences!=null){
            boolean status=preferences.getBoolean("flag",flag);
            swMorning.setChecked(status);
        }
        swMorning.setOnCheckedChangeListener(this);
        setBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    通知栏显示天气
    public void setAlarmMorning(){
        Intent intentMorning = new Intent(this, AlarmBroadcastReceiver.class);
        intentMorning.setAction("CLOCK_IN");
        PendingIntent piMorning = PendingIntent.getBroadcast(this, 0, intentMorning, 0);     //设置事件
        //获取到AlarmManager对象
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        设置提醒时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
//            设置时区，不然有时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY,15);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long systemTime = System.currentTimeMillis();
        long firstTime = SystemClock.elapsedRealtime();    // 开机之后到现在的运行时间(包括睡眠时间)
        // 选择的定时时间
        long selectTime = calendar.getTimeInMillis();
        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {
            calendar.add(Calendar.DATE, 1);
            selectTime = calendar.getTimeInMillis();
        }
        // 计算设定时间到现在时间的时间差
        Long time = selectTime-systemTime;
        Long triggerTime=firstTime+time;
        //获取到PendingIntent的意图对象
//        参数分别为：闹钟类型，闹钟首次执行时间，执行间隔，响应动作
        manager.setRepeating(AlarmManager.RTC_WAKEUP,triggerTime, AlarmManager.INTERVAL_DAY,piMorning); //提交事件，发送给 广播接收器
    }
    public void cancelAlarmMorning() {
        Intent intentMorning = new Intent(this, AlarmBroadcastReceiver.class);
        intentMorning.setAction("CLOCK_IN");
        PendingIntent piMorning = PendingIntent.getBroadcast(this, 0, intentMorning, 0);     //设置事件
        //获取到AlarmManager对象
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.cancel(piMorning);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.sw_morning:
                if (isChecked){
//                    开启定时推送
                    setAlarmMorning();
                    SharedPreferences preferences = getSharedPreferences("switchStatus", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("flag", true);
                    editor.commit();
                }else {
                    cancelAlarmMorning();
                    SharedPreferences preferences = getSharedPreferences("switchStatus", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("flag", false);
                    editor.commit();
                }
                break;
        }
    }
}