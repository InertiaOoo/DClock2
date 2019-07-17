package com.ooo.deemo.dclock.clockgo.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.ooo.deemo.dclock.R;
import com.ooo.deemo.dclock.clockgo.MainActivity;
import com.ooo.deemo.dclock.clockgo.bean.ClockBean;
import com.ooo.deemo.dclock.clockgo.inter.ClockChangeListener;
import com.ooo.deemo.dclock.clockgo.operater.ChangeClockOperater;
import com.ooo.deemo.dclock.clockgo.utils.ClockUtils;
import com.ooo.deemo.dclock.clockgo.view.AlarmActivity;
import com.ooo.deemo.dclock.clockgo.view.LockScreenActivity;
import com.ooo.deemo.dclock.clockgo.view.TimeChooseView;

import java.io.File;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyForService extends Service implements ClockChangeListener{

    private final String CHANNEL_ID = "TEST_SERVICE_ID";
    private final String CHANNEL_NAME = "clock";

    private final String contentSub = "小标题";
    private final String contentTitle = "Dyyの小吵闹";
    private final String contentText = "起床了";

    Notification notification;
    Notification.Builder builder;
    Calendar clockcalendar = Calendar.getInstance();

    AlarmManager alarmManager = null;


    public MyForService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyForService","onCreate");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel chan = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            chan.enableLights(true);
            chan.setLightColor(Color.RED);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            Intent intentmain = new Intent(this, MainActivity.class);

            PendingIntent pendingIntentm = PendingIntent.getActivity(this,999,intentmain,0);

            builder = new Notification.Builder(this, CHANNEL_ID);
            notification = builder
                    .setSmallIcon(R.mipmap.clockw)
                    .setContentText(contentText)
                    .setSubText(contentSub)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pendingIntentm)
                    .setContentTitle(contentTitle)
                    .build();
        manager.notify(0,notification);
        } else {

            Intent intentmain = new Intent(this, MainActivity.class);

            PendingIntent pendingIntentm = PendingIntent.getActivity(this,999,intentmain,0);
            notification = new Notification.Builder(getApplicationContext())
                    .setContentInfo(contentSub)
                    .setSubText(contentSub)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.clockw)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pendingIntentm)
                    .build();
        }



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("MyForService","onStartCommand");

        notification.flags |= Notification.FLAG_ONGOING_EVENT;



        Intent intent1 = new Intent(MyForService.this,LockScreenActivity.class);

        startActivity(intent1);

//        Intent intent2 = new Intent(MyForService.this, AlarmActivity.class);
////
////        startActivity(intent2);

        startForeground(1, notification);




//
//        ChangeClockOperater operater = new ChangeClockOperater();
//
//        operater.setClockChangeListener(this);


        return START_STICKY;
    }





    //这里设置定时启动闹铃
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void launchClock(){

        Log.e("MyForService","launchClock");
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        List<ClockBean> alarmList = new ArrayList<>();

        alarmList = ClockUtils.getClockBean(this);

        for (int index = 1;index < alarmList.size();index++) {

            Intent i = new Intent(MyForService.this, LockScreenActivity.class);

            PendingIntent sender = PendingIntent.getActivity(
                    MyForService.this, 0, i, 0);



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {


                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmList.get(index).getTimeInMillis(), sender);


            } else {

                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmList.get(0).getTimeInMillis(), sender);

            }
        }
    }




    @Override
    public void onDestroy() {
        Log.e("MyForService","onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyForService","onBind");
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSetNewClock() {
//        launchClock();
    }

    @Override
    public void onDeleteClock() {

    }
}
