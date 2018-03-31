package com.example.android.batteryalert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import static com.example.android.batteryalert.MainActivity.mContext;
import static com.example.android.batteryalert.MainActivity.notiApp;

public class MainActivity extends AppCompatActivity {

    public static Context mContext;

    public NotificationManager mNotificationManager;
    public static NotificationManager nm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        nm=mNotificationManager;

        mContext=getApplicationContext();

        startService(new Intent(this, MyService.class));



    }



    public static void notiApp(String data,Uri sound)
    {

        //* * * * * NOTIFICATION - APP RUNNING * * * * *//

//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        // The id of the channel.
        String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.batteryblack)
                        .setContentTitle("Battery Alert")
                        .setContentText(data)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVibrate(new long[] {1000,1000})
                        .setSound(sound)
                        .setAutoCancel(true);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        String[] events = new String[1];
        events[0]=data;

// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Battery Alert");
//...
// Moves events into the expanded layout
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }
// Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);




// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(mContext, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
//        NotificationManager mNotificationManager =
        //              (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


// mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
        // mNotificationManager.notify(00001, mBuilder.build());

        nm.notify(00001,mBuilder.build());

        //* * * * * NOTIFICATION - APP RUNNING * * * * *//


    }



    public static void doTask()
    {
        try {


            while(true) {

                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = mContext.registerReceiver(null, ifilter);
                int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                float batteryPct = level / (float)scale;





                boolean flag1 = false, flag2 = false;

                while (batteryPct<1.0&&isCharging==true) {

                    level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                    batteryPct = level / (float)scale;


                    Log.i("MainActivity.java","Charging "+batteryPct);

                    ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    batteryStatus = mContext.registerReceiver(null, ifilter);
                    status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    isFull = status == BatteryManager.BATTERY_STATUS_FULL;
                    isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

                    if (!flag1) {
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        notiApp("Charging",alarmSound);
                    }


                    flag1 = true;

                }

                while (batteryPct==1.0) {

                    level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                    batteryPct = level / (float)scale;



                    Log.i("MainActivity.java","Full");

                    ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    batteryStatus = mContext.registerReceiver(null, ifilter);
                    status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    isFull = status == BatteryManager.BATTERY_STATUS_FULL;
                    isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

                    if (!flag2) {

                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

                        notiApp("Phone Charged",alarmSound);

                 /*  Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                   Ringtone r = RingtoneManager.getRingtone(mContext, notification);
                   r.play();*/


                    }

                    flag2 = true;


                }

            }
        }
        catch(Exception e)
        {e.printStackTrace();}


    }







}

class BatteryThread extends Thread
{
    BatteryThread(String name)
    {super(name);
    start();}

    public void run()
    {try {


       while(true) {

           IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
           Intent batteryStatus = mContext.registerReceiver(null, ifilter);
           int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
           boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;
           boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

           int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
           int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

           float batteryPct = level / (float)scale;





           boolean flag1 = false, flag2 = false;

           while (batteryPct<1.0&&isCharging==true) {

                level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                batteryPct = level / (float)scale;


               Log.i("MainActivity.java","Charging "+batteryPct);

               ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
               batteryStatus = mContext.registerReceiver(null, ifilter);
               status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
               isFull = status == BatteryManager.BATTERY_STATUS_FULL;
               isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

               if (!flag1) {
                   Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                   notiApp("Charging",alarmSound);
               }


               flag1 = true;

           }

           while (batteryPct==1.0) {

               level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
               scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

               batteryPct = level / (float)scale;



               Log.i("MainActivity.java","Full");

               ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
               batteryStatus = mContext.registerReceiver(null, ifilter);
               status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
               isFull = status == BatteryManager.BATTERY_STATUS_FULL;
               isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

               if (!flag2) {

                   Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

                   notiApp("Phone Charged",alarmSound);

                 /*  Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                   Ringtone r = RingtoneManager.getRingtone(mContext, notification);
                   r.play();*/


               }

               flag2 = true;


           }

       }
    }
    catch(Exception e)
    {e.printStackTrace();}
    }

}