package com.ooo.deemo.dclock.clockgo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ooo.deemo.dclock.R;

/**
 * Author by Deemo, Date on 2019/7/10.
 * Have a good day
 */
public class ListDialog extends Dialog {
    private Context mContext;
    private EditText et_setname;
    private Button bt_setname;
    private Button bt_cancel;
    private View mView;
    private String yesStr, noStr;

    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private View.OnClickListener onClickListener;
    private TextWatcher textWatcher;

    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

    private void initEvent(){
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });

        bt_setname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });


        et_setname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(textWatcher != null){
                    textWatcher.afterTextChanged(s);
                }


            }
        });


    }

    public void setOnTextChangeListener(TextWatcher textWatcher){
        this.textWatcher = textWatcher;
    }




    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }


    public ListDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;

        initView();
        initEvent();
    }


    private void initView(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = inflater.inflate(R.layout.mydialoglayout, null);
        et_setname = mView.findViewById(R.id.et_setname);

        bt_setname = mView.findViewById(R.id.bt_setname);

        bt_cancel = mView.findViewById(R.id.bt_cancel);


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        setCanceledOnTouchOutside(false);
        wl.x = 0;
        wl.y = 0;
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);


    }

}
