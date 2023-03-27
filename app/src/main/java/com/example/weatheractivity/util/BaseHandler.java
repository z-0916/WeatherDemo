package com.example.weatheractivity.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.logging.LogRecord;

public abstract class BaseHandler extends Handler {

    protected WeakReference<Activity> activityWeakReference;
    protected WeakReference<Fragment> fragmentWeakReference;

    public BaseHandler() {
    }

    public BaseHandler(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    public BaseHandler(Fragment fragment) {
        this.fragmentWeakReference = new WeakReference<>(fragment);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (activityWeakReference == null || activityWeakReference.get() == null || activityWeakReference.get().isFinishing()) {
//            确认activity是否可用
            Log.i("this", "Activity is gone");
//            handleMessage(msg,Constant.ACTIVITY_GONE );
        } else if (fragmentWeakReference == null || fragmentWeakReference.get() == null || fragmentWeakReference.get().isRemoving()) {
            Log.i("this", "Fragment is gone");
        } else {
            handleMessage(msg, msg.what);
        }
    }
    public abstract void handleMessage (Message msg,int what);
}
