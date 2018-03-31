package com.example.android.batteryalert;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.batteryalert.MainActivity.mContext;
import static com.example.android.batteryalert.MainActivity.notiApp;

/**
 * Created by 300 on 1/9/2018.
 */

public class MyService extends Service
{
    @Override
    public void onCreate()
    {
        Log.i("MyService.java","Service Started");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here

        BatteryThread t=new BatteryThread("Battery");

        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;

    }

    @Override
    public void onDestroy()
    {
        Log.i("MyService.java","Service Destroyed");}
}

