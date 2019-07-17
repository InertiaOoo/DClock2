package com.ooo.deemo.dclock.clockgo;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.ooo.deemo.dclock.R;
import com.ooo.deemo.dclock.clockgo.adapter.ClockListAdapter;
import com.ooo.deemo.dclock.clockgo.bean.ClockBean;
import com.ooo.deemo.dclock.clockgo.receiver.MyAlarmReceiver;
import com.ooo.deemo.dclock.clockgo.service.MyForService;
import com.ooo.deemo.dclock.clockgo.utils.ClockUtils;
import com.ooo.deemo.dclock.clockgo.view.MyGifView;
import com.ooo.deemo.dclock.clockgo.view.TimeChooseView;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Menu menu;
    private Button btSetTime;
    private Button bt_stop;
    private Button bt_showtest;

    private static int NUM = 1;

    private SlideAndDragListView lv_showClock;
    private static Context context;
    AlarmManager alarmManager = null;

    List<ClockBean> clockBeanList = new ArrayList<>();

    List<ClockBean> clockList = new ArrayList<>();

    List<Map<String, String>> items = new ArrayList<>();

    SimpleAdapter clockListAdapter;

    //权限
    private String[] permissions = {Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.WAKE_LOCK
            ,Manifest.permission.DISABLE_KEYGUARD,Manifest.permission.SET_ALARM,Manifest.permission.SYSTEM_ALERT_WINDOW
            ,Manifest.permission.SYSTEM_ALERT_WINDOW
    };
    private AlertDialog dialog;


    @Override
    protected void onDestroy() {
        super.onDestroy();
Log.e("MainActivity","onDestroy");

        ClockUtils.putClockBean(this,clockList);




    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity","onResume");
    initList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity","onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity","onStop");
        ClockUtils.putClockBean(this,clockList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MainActivity","onCreate");
        setContentView(R.layout.activity_main);
//        getPermission();
      initView();


    }

    private void setService(){

        Intent i = new Intent(MainActivity.this, MyForService.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(i);
        } else {
            startService(i);
        }
    }

    private void initList(){
        clockBeanList = ClockUtils.getClockBean(getApplicationContext());
        clockListAdapter = new SimpleAdapter(this,items,R.layout.clocklist_layout,new String[]{"time","name"},new int[]{R.id.tv_clocktime,R.id.tv_clockstatus});

        if (clockBeanList==null){
            Log.e("clockBeanList","null");

//            clockList.add(new ClockBean("1111",0,"1111"));
        }else {
            clockList.clear();
            Log.e("clockBeanList",String.valueOf(clockBeanList.size()));
            items.clear();
            for(int i = 0;i<clockBeanList.size();i++){

                clockList.add(clockBeanList.get(i));
                //导入存储数据
                Map<String, String> map = new HashMap<>();
                map.put("time", clockBeanList.get(i).getClockTime());
                map.put("name", clockBeanList.get(i).getClockName());
                Log.e(clockBeanList.get(i).getClockTime(),clockBeanList.get(i).getClockName());
                items.add(map);

            }

    }
        clockListAdapter.notifyDataSetChanged();
        lv_showClock.setAdapter(clockListAdapter);
    }

    private void initView(){

        btSetTime = findViewById(R.id.setTime);

        lv_showClock = findViewById(R.id.lv_showClock);

        bt_stop = findViewById(R.id.bt_stop);

        bt_showtest = findViewById(R.id.bt_showtest);

        btSetTime.setOnClickListener(this);

        bt_stop.setOnClickListener(this);

        bt_showtest.setOnClickListener(this);

        clockBeanList = ClockUtils.getClockBean(getApplicationContext());
        clockListAdapter = new SimpleAdapter(this,items,R.layout.clocklist_layout,new String[]{"time","name"},new int[]{R.id.tv_clocktime,R.id.tv_clockstatus});

        if (clockBeanList==null){
            Log.e("clockBeanList","null");

//            clockList.add(new ClockBean("1111",0,"1111"));
        }else {
            clockList.clear();
            Log.e("clockBeanList",String.valueOf(clockBeanList.size()));
            items.clear();
            for(int i = 0;i<clockBeanList.size();i++){

                clockList.add(clockBeanList.get(i));
         //导入存储数据
                Map<String, String> map = new HashMap<>();
                map.put("time", clockBeanList.get(i).getClockTime());
                map.put("name", clockBeanList.get(i).getClockName());
                Log.e(clockBeanList.get(i).getClockTime(),clockBeanList.get(i).getClockName());
                items.add(map);

            }

        }

        clockListAdapter.notifyDataSetChanged();
        //列表添加左滑删除
        menu = new Menu(false, 0);
        menu.addItem(new  com.yydcdut.sdlv.MenuItem.Builder().setWidth(220)
                .setBackground(new ColorDrawable(getColor(R.color.back)))
                .setDirection( com.yydcdut.sdlv.MenuItem.DIRECTION_RIGHT)//设置方向 (默认方向为 DIRECTION_LEFT )
                .setIcon(getResources().getDrawable(R.drawable.delete3))// set icon
                .build());

        lv_showClock.setMenu(menu);


        lv_showClock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //增加点击设置
            }
        });

        lv_showClock.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


             myHandler.sendEmptyMessage(01);

                Log.e("onItemLongClick","position:"+position);
                return true;
            }
        });

        lv_showClock.setOnSlideListener(new SlideAndDragListView.OnSlideListener() {
            @Override
            public void onSlideOpen(View view, View parentView, int position, int direction) {
                Log.e("onSlideOpenposition",""+position);
            }

            @Override
            public void onSlideClose(View view, View parentView, int position, int direction) {
        Log.e("onSlideClose",""+position);
            }
        });

        lv_showClock.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                switch (direction) {
                    case com.yydcdut.sdlv.MenuItem.DIRECTION_LEFT:
                        switch (buttonPosition) {
                            case 0://One
                                return Menu.ITEM_SCROLL_BACK;
                        }
                        break;
                    case com.yydcdut.sdlv.MenuItem.DIRECTION_RIGHT:
                        switch (buttonPosition) {
                            case 0://icon
//
                                Log.e("clickitemPosition","item:"+itemPosition);
                                Log.e("clickButtonPosition","button:"+buttonPosition);



                                Intent intent = new Intent(MainActivity.this, MyAlarmReceiver.class);
                                intent.setAction("com.ooo.deemo.DELETE");
                                intent.putExtra("id",clockList.get(itemPosition).getId());
                                sendBroadcast(intent);

                                for(int i = 0;i<clockList.size();i++){

                                    Log.e("MainActivity","id="+clockList.get(i).getId()+"\ttime="+clockList.get(i).getClockTime());

                                }

                                items.remove(itemPosition);

                                clockList.remove(itemPosition);

                                for(int i = itemPosition; i<clockList.size();i++){
                                    clockList.get(i).setId(i);
                                }

                                clockListAdapter.notifyDataSetChanged();
                                lv_showClock.invalidate();

                                for(int i = 0;i<clockList.size();i++){

                                    Log.e("MainActivity","after delete"+"id="+clockList.get(i).getId()+"\ttime="+clockList.get(i).getClockTime());

                                }

                                return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                        }
                        break;
                    default :
                        return Menu.ITEM_NOTHING;
                }
                return Menu.ITEM_NOTHING;
            }
        });

        lv_showClock.setAdapter(clockListAdapter);

    }

Handler myHandler = new MyHandler();

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 01:
                    ToastMessage("","左滑删除，短按（点击）修改设置，长按这个o.O,右滑Nothing");
                    break;

                    default:

                        break;
            }
        }
    }

    private void ToastMessage(String titles, String messages) {
                 //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
                LayoutInflater inflater = getLayoutInflater();//调用Activity的getLayoutInflater()
                View view = inflater.inflate(R.layout.toastlayout, null); //加載layout下的布局
        GifImageView  iv = view.findViewById(R.id.iv_pic);
        iv.setImageResource(R.drawable.intoast);//显示的图片
                TextView title = view.findViewById(R.id.tv_title);
                 title.setText(titles); //toast的标题
                TextView text = view.findViewById(R.id.tv_info);
                text.setText(messages); //toast内容
                 Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
                 toast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
                toast.setView(view); //添加视图文件
               toast.show();
            }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setTime:

                ClockUtils.putClockBean(this,clockList);

                Intent intent = new Intent(MainActivity.this, TimeChooseView.class);

                startActivity(intent);

                break;


            case R.id.bt_stop:

               Intent stopIntent = new Intent(this,MyForService.class);

               stopService(stopIntent);

                break;


            case R.id.bt_showtest:

                if (NUM%2==0){
                    Toast.makeText(this, ""+ClockUtils.getClockBean(this).get(0).clockTime, Toast.LENGTH_SHORT).show();

                    Log.e("ClockUtils",""+ClockUtils.getClockBean(this).get(0).clockTime);

                }else {

                    Calendar c = Calendar.getInstance();
                    Toast.makeText(this, "" + c.getTimeInMillis(), Toast.LENGTH_SHORT).show();

                    Log.e("Calendar",""+c.getTimeInMillis());
                }

                NUM++;
                break;
        }
    }




    /*
 获取权限
  */
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            for (int i = 0; i < permissions.length; i++) {


                int j = ContextCompat.checkSelfPermission(this, permissions[i]);


                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (j != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有授予该权限，就去提示用户请求
                    showDialogTipUserRequestPermission(i);
                }
            }
        }

    }

    /*
          权限申请
           */
    private void showDialogTipUserRequestPermission(int i) {
        String str_1 = "";
        String str_2 = "";
        switch (i) {
            case 0:
                str_1 = "获权限不可用";
                str_2 = "由于需要获取；\n否则，您将无法正常使用";
                break;
            case 1:
                str_1 = "获取唤醒权限不可用";
                str_2 = "由于需要唤醒；\n否则，您将无法正常使用";
                break;
            case 2:
                str_1 = "在SDCard中创建与删除文件权限不可用";
                str_2 = "在SDCard中创建与删除文件权限；\n否则，您将无法正常使用";
                break;

            case 3:
                str_1 = "往SDCard读出数据权限不可用";
                str_2 = "往SDCard读出数据权限；\n否则，您将无法正常使用";
                break;
            case 4:
                str_1 = "创建删除文件夹权限不可用";
                str_2 = "创建删除文件夹权限；\n否则，您将无法正常使用";
                break;
            default:
                break;
        }


        new AlertDialog.Builder(this)
                .setTitle(str_1)
                .setMessage(str_2)
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }


    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSetting();
                    } else
                        finish();
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSetting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许使用存储权限来保存用户数据")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();

    }


    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSetting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
}
