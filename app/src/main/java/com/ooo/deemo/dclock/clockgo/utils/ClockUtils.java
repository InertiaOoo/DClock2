package com.ooo.deemo.dclock.clockgo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ListView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ooo.deemo.dclock.clockgo.bean.ClockBean;
import com.ooo.deemo.dclock.clockgo.data.ConstantValue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author by Deemo, Date on 2019/5/29.
 * Have a good day
 */
public class ClockUtils {


    private static SharedPreferences sp;


    public static void putClockBean(Context ctx, List<ClockBean> clockList) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("clockData", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(clockList);
        editor.putString(ConstantValue.CLOCK_BEAN, json);
       boolean b = editor.commit();
        Log.e("putClockBean",""+b);
    }


    public static List<ClockBean> getClockBean(Context ctx) {

        if (sp == null) {
            sp = ctx.getSharedPreferences("clockData", Context.MODE_PRIVATE);
        }
        Gson gson = new Gson();
        String json = sp.getString(ConstantValue.CLOCK_BEAN, null);
        Type type = new TypeToken<List<ClockBean>>() {
        }.getType();
        List<ClockBean> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

//    public static void removeClockBean(Context ctx, int position,List<ClockBean> clockList){
//
//        if (sp == null) {
//            sp = ctx.getSharedPreferences("clockData", Context.MODE_PRIVATE);
//        }
//
//        clockList.remove(position);
//
//        putClockBean(ctx,clockList);
//
//    }

}
