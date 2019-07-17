package com.ooo.deemo.dclock.clockgo.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.ooo.deemo.dclock.R;
import com.ooo.deemo.dclock.clockgo.bean.ClockBean;
import com.ooo.deemo.dclock.clockgo.dialog.ListDialog;
import com.ooo.deemo.dclock.clockgo.inter.ClockChangeListener;
import com.ooo.deemo.dclock.clockgo.operater.ChangeClockOperater;
import com.ooo.deemo.dclock.clockgo.service.MyForService;
import com.ooo.deemo.dclock.clockgo.utils.ClockUtils;
import com.ooo.deemo.dclock.clockgo.receiver.MyAlarmReceiver;
import com.zyyoona7.wheel.WheelView;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author by Deemo, Date on 2019/7/5.
 * Have a good day
 */

public class TimeChooseView extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private static int hour;
    private static int min;
    private static String name;
    int gethour = 0;
    int getMin = 0;
    private Calendar calendar;

    private static TextView tv_create;
    private  static TextView tv_back;

     WheelView<String> wheelView;
    WheelView<String>  wheelView2;

    private  DecimalFormat df;

    private    SimpleAdapter setAdapter;

    private TextView tv_showhour;
    private TextView tv_showmin;

    private String setName="";

    private long millisCur = 0;

//    private EditText et_name;

    ListView lv_set;

    List<Map<String, String>> items = new ArrayList<>();

    private List<ClockBean> clockBeanList = new ArrayList<>();


   String[] setlist ={"ClockName","RepeatMode","RingBell","UnlockMode"};

    AlarmManager alarmManager = null;

    Calendar clockcalendar = Calendar.getInstance();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timechooselayout);

        initView();

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        final String hourStr=df.format(hour);

        tv_showhour.setText(hourStr);
        String minStr=df.format(min);
        tv_showmin.setText(minStr);


        Log.e("hour:",""+hour);
        Log.e("min:",""+min);

        //滚轮设置
        wheelView.setTextSize(36f,true);
        wheelView.setCyclic(true);
        wheelView.setSelectedItemPosition(hour,true);

        wheelView2.setTextSize(36f,true);
        wheelView2.setCyclic(true);
        wheelView2.setSelectedItemPosition(min,true);

        wheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(WheelView<String> wheelView, String data, int position) {

                hour = position;
                Log.e("hour",""+hour);


                myHandler.sendEmptyMessage(001);



            }
        });

        wheelView2.setOnItemSelectedListener(new WheelView.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(WheelView<String> wheelView, String data, int position) {

               min = position;
                Log.e("min",""+min);


                myHandler.sendEmptyMessage(001);




            }
        });




    }

    public class Myhandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 001:
                    Calendar targetTime = Calendar.getInstance();

                    targetTime.setTimeInMillis(System.currentTimeMillis());

                    if(hour>targetTime.get(Calendar.HOUR_OF_DAY)){
                        if(min >= targetTime.get(Calendar.MINUTE)){

                            tv_showhour.setText(df.format(hour-targetTime.get(Calendar.HOUR_OF_DAY)));
                            tv_showmin.setText(df.format(min-targetTime.get(Calendar.MINUTE)));

                        }else {
                            tv_showhour.setText(df.format(hour-targetTime.get(Calendar.HOUR_OF_DAY)-1));
                            tv_showmin.setText(df.format(min+60-targetTime.get(Calendar.MINUTE)));
                        }
                    }
                    else if(hour ==targetTime.get(Calendar.HOUR_OF_DAY)){
                        if(min >= targetTime.get(Calendar.MINUTE)){

                            tv_showhour.setText(df.format(hour-targetTime.get(Calendar.HOUR_OF_DAY)));
                            tv_showmin.setText(df.format(min-targetTime.get(Calendar.MINUTE)));

                        }else {
                            tv_showhour.setText(df.format(24+hour-targetTime.get(Calendar.HOUR_OF_DAY)-1));
                            tv_showmin.setText(df.format(min+60-targetTime.get(Calendar.MINUTE)));
                        }
                    }
                    else {

                        if(min >= targetTime.get(Calendar.MINUTE)){

                            tv_showhour.setText(df.format(24+hour-targetTime.get(Calendar.HOUR_OF_DAY)));
                            tv_showmin.setText(df.format(min-targetTime.get(Calendar.MINUTE)));

                        }else {
                            tv_showhour.setText(df.format(24+hour-targetTime.get(Calendar.HOUR_OF_DAY)-1));
                            tv_showmin.setText(df.format(min+60-targetTime.get(Calendar.MINUTE)));
                        }

                    }




                    break;
                    default:

                        break;

            }
        }
    }

    private Myhandler myHandler = new Myhandler();



    private void initView(){

        calendar = Calendar.getInstance();
        tv_showhour = findViewById(R.id.tv_showhour);
        tv_showmin = findViewById(R.id.tv_showmin);
        tv_back = findViewById(R.id.tv_back);
        tv_create = findViewById(R.id.tv_create);
//et_name = findViewById(R.id.et_name);
lv_set = findViewById(R.id.lv_set);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        tv_back.setOnClickListener(this);
        tv_create.setOnClickListener(this);

        name = "";
        df=new DecimalFormat("00");

        clockBeanList = ClockUtils.getClockBean(this);

        wheelView = findViewById(R.id.wheelview1);

        wheelView2 = findViewById(R.id.wheelview2);
        //初始化数据
        List<String> list = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        for(int i = 0; i < 60; i++) {
            if(i<10){
                String str=df.format(i);
                list.add(str);
                list2.add(str);
            }
            else if (i<24){
                list.add(String.valueOf(i));
                list2.add(String.valueOf(i));
            }else {
                list2.add(String.valueOf(i));
            }
        }
        wheelView.setData(list);
        wheelView2.setData(list2);

for(int i = 0;i<setlist.length;i++){
    Map<String, String> map = new HashMap<>();
    map.put("setit", setlist[i]);
    map.put("setcontent","default");
    map.put("setpic","");

    items.add(map);

}
        lv_set.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("onItemClick","position:"+position);
              createMyDialog(position);
            }
        });

        setAdapter = new SimpleAdapter(this,items,R.layout.listsetlayout,new String[]{"setit","setcontent","setpic"},new int[]{R.id.tv_setit,R.id.tv_setcontent,R.id.iv_setpic});

        setAdapter.notifyDataSetChanged();

        lv_set.setAdapter(setAdapter);

    }

    private void createMyDialog(int position){

        switch (position){
            case 0:

                final ListDialog listDialog = new ListDialog(this);

                listDialog.setOnTextChangeListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        setName = s.toString();
                    }
                });

                listDialog.setNoOnclickListener("bye", new ListDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        listDialog.dismiss();
                    }
                });

                listDialog.setYesOnclickListener("sure", new ListDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {

                        name = setName;
                        if(name!="") {
                            items.get(0).put("setcontent", name);
                        }

                        setAdapter.notifyDataSetChanged();

                        Log.e("setYesOnclickListener","setname:"+name);

                        Log.e("items",""+items.get(0).get("setcontent"));
                        listDialog.dismiss();
                    }
                });

                listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                listDialog.show();

                break;

            case 1:
                //增加设置闹钟循环

                break;

            case 2:
                //增加设置铃声

                break;

            case 3:
                //增加设置解锁类型

                break;

                default:
                    break;

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setClock(){

        String time = df.format(hour)+":"+df.format(min);

        Log.e("clock",""+time);

        Calendar endcalendar = Calendar.getInstance();

        endcalendar.setTimeInMillis(System.currentTimeMillis());

        int currenthour = endcalendar.get(Calendar.HOUR_OF_DAY);
        int currentmin = endcalendar.get(Calendar.MINUTE);
        if(hour>currenthour){

            if(min>=currentmin){

                gethour=hour-currenthour;
                getMin = min-currentmin;

            }else {
                gethour = hour-currenthour-1;
                getMin=60+min-currentmin;
            }

        }else if(hour==currenthour){
            if(min>=currentmin){

                gethour=hour-currenthour;
                getMin = min-currentmin;

            }else {
                gethour = 23;
                getMin=60+min-currentmin;
            }
        }else {
            if(min>=currentmin){
                gethour = 24+hour-currenthour;
                getMin = min-currentmin;
            }else {

                gethour = 23+hour-currenthour;

                getMin= 60+min-currentmin;

            }
        }


        clockcalendar.setTimeInMillis(System.currentTimeMillis());

        Log.e(""+clockcalendar.getTimeInMillis(),""+(clockcalendar.getTimeInMillis()-clockcalendar.getTimeInMillis()%60000));

        long intTime = clockcalendar.getTimeInMillis()-clockcalendar.getTimeInMillis()%60000;

        millisCur = intTime+gethour*60*60*1000+getMin*60*1000;

        ClockBean clock = new ClockBean(name,0,  time ,millisCur,clockBeanList.size());

        clockBeanList.add(clock);

        ClockUtils.putClockBean(getApplicationContext(),clockBeanList);

//        ChangeClockOperater operater = new ChangeClockOperater();
//
//        operater.doSetNewClock();


        Intent intent = new Intent(TimeChooseView.this, MyAlarmReceiver.class);
        intent.setAction("com.ooo.deemo.RING");
        intent.putExtra("msg","new");
        sendBroadcast(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_back:

                finish();

                break;

            case R.id.tv_create:

                setClock();

                finish();

//                Intent intent = new Intent(TimeChooseView.this, MainActivity.class);
//                startActivity(intent);

                break;

        }
    }
}
