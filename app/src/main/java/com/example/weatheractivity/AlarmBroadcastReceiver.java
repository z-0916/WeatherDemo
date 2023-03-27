package com.example.weatheractivity;

import static android.app.Notification.DEFAULT_ALL;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("CLOCK_IN")) {
            Log.d("接收广播","null");
             NotificationChannel channel=null;
            Intent mIntent=new Intent(context, MainActivity.class);  //绑定intent，点击图标能够进入某activity
            PendingIntent mPendingIntent=PendingIntent.getActivity(context, 0, mIntent,0);
            //获取状态通知栏管理
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//    通知栏创建渠道
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    channel=new NotificationChannel("001","channel_name",NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(channel);
                }
            Notification notification =new NotificationCompat.Builder(context,"001").
                    setContentTitle("天气") //设置通知栏标题
                    .setContentText("天气情况:今天降温，请注意保暖") //设置通知栏显示内容
                    .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知优先级
                    .setDefaults(DEFAULT_ALL)
                    .setSmallIcon(R.drawable.ic_weather)
                    .setAutoCancel(true)
                    .setContentIntent(mPendingIntent)
                    .build(); //设置这个标志当用户单击面板就可以将通知取消
            manager.notify(0, notification);  //绑定Notification，发送通知请求
        }
    }
}
