package com.ooo.deemo.dclock.clockgo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ooo.deemo.dclock.R;
import com.ooo.deemo.dclock.clockgo.bean.ClockBean;
import com.ooo.deemo.dclock.clockgo.utils.ClockUtils;

import java.util.List;
import java.util.Random;

public class LockScreenActivity extends AppCompatActivity {
    private final String TAG = "LockScreenActivity";

    private MediaPlayer mediaPlayer;

    private Button bt_exit;

    private TextView tv_num1;

    private TextView tv_num2;

    private  TextView tv_operator;

    private EditText et_answer;

    private  Integer randomNum1=99;

    private  Integer randomNum2=99;

    private TextView bt_out;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        Log.e(TAG,"onCreate");

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
win.addFlags(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY );


//        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        params.screenOrientation = Configuration.ORIENTATION_PORTRAIT;
//        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
//                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;



        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");

        mWakelock.acquire(10*60*1000L);

        mediaPlayer = MediaPlayer.create(this,R.raw.bitter_sweet_symphony);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        mediaPlayer.setVolume((float) 0.4,(float) 0.4);
        mediaPlayer.start();


        bt_exit = findViewById(R.id.bt_closeclock);
        et_answer = findViewById(R.id.et_answer);
        tv_num1 = findViewById(R.id.tv_num1);
        tv_num2 = findViewById(R.id.tv_num2);
        tv_operator = findViewById(R.id.tv_operator);
        bt_out = findViewById(R.id.bt_out);


        bt_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });
//
//        Bundle bundle = new Bundle();
//
//        String name =   bundle.getString("name");
//
//        int index0 = bundle.getInt("index");
//
//        Log.e("AlarmNotifyService:","name:"+name+"index:"+index0);

//        SharedPreferences data = getSharedPreferences("data",MODE_PRIVATE);
//
//
//       int index = data.getInt("MODE",0);
//
//       Log.e(TAG,"MODE value:"+index);


        List<ClockBean> clockBeanList = ClockUtils.getClockBean(getApplicationContext());

        int mode = clockBeanList.get(0).getClockMode();

        if(mode==0) {
            randomNum1 = (new Random().nextInt(89))+10;

            randomNum2 = (new Random().nextInt(89))+10;
        }else if(mode==1){
            randomNum1 = (new Random().nextInt(899))+100;

            randomNum2 = (new Random().nextInt(899))+100;
        }else {
            randomNum1 = (new Random().nextInt(9));

            randomNum2 = (new Random().nextInt(9));
        }
        tv_num1.setText(randomNum1.toString());

        tv_num2.setText(randomNum2.toString());
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        et_answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Integer ans = randomNum1*randomNum2;

                if(s.toString().equals(ans.toString())){

                    mediaPlayer.stop();
                    Toast.makeText(LockScreenActivity.this, "good job!", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });



//        new AlertDialog.Builder(AlarmAlert.this)
//                .setIcon(R.drawable.ic_launcher_foreground)
//                .setTitle("闹钟响了")
//                .setMessage("时间到了！")
//                .setPositiveButton("关掉"
//                        , new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                AlarmAlert.this.finish();
//                                mediaPlayer.stop();
//                            }
//                        }).show();

    }


    private void initNum(){
        randomNum1 =  new Random().nextInt(20);

        randomNum2 =  new Random().nextInt(20);

        tv_num1.setText(randomNum1);

        tv_num2.setText(randomNum2);


    }


}