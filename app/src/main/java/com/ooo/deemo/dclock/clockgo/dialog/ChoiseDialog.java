package com.ooo.deemo.dclock.clockgo.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ooo.deemo.dclock.R;


public class ChoiseDialog extends AlertDialog {
    private Context mContext;

    private Button bt_exit;

    private Button bt_edit;

public ListView lv_mode;

private View mView;

    private String yesStr, noStr;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器

    private AdapterView.OnItemClickListener mOnItemClickListener;

    private OnItemClickListener OnItemClickListener;

    private View.OnClickListener onClickListener;

    public interface onNoOnclickListener {
        public void onNoClick();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听

        //设置取消按钮被点击后，向外界提供监听
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });


        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!=null){
                    onClickListener.onClick(v);
                }
            }
        });

        lv_mode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }


    /**
         * 设置取消按钮的显示内容和监听
         *
         * @param str
         * @param onNoOnclickListener
         */

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

        public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
            if (str != null) {
                noStr = str;
            }
            this.noOnclickListener = onNoOnclickListener;
        }
    public void setChoiceMode(int choiceMode) {
        lv_mode.setChoiceMode(choiceMode);
    }

    public void setItemsCanFocus(boolean b) {
        lv_mode.setItemsCanFocus(b);
    }


    public void setAdapter(Adapter adapter) {

        lv_mode.setAdapter((ListAdapter) adapter);

    }

        public void setmOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){

            this.mOnItemClickListener =  onItemClickListener;
        }

    public interface OnItemClickListener {


        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }



    public ChoiseDialog(Context context) {
        super(context);
        this.mContext = context;

        initView();
        initEvent();

    }


private void initView(){
    LayoutInflater inflater = LayoutInflater.from(mContext);
    mView = inflater.inflate(R.layout.activity_choise_dialog, null);
    lv_mode = (ListView) mView.findViewById(R.id.mode_list);

    bt_exit = mView.findViewById(R.id.bt_exit);



    bt_edit = mView.findViewById(R.id.bt_edit);


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
