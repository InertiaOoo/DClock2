package com.ooo.deemo.dclock.clockgo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.ooo.deemo.dclock.R;

/**
 * Author by Deemo, Date on 2019/7/10.
 * Have a good day
 */
public class MyGifView extends android.support.v7.widget.AppCompatImageView {

             private long movieStart;
     private Movie movie;

             // 重写该构造方法

            @SuppressLint("ResourceType")
            public MyGifView(Context context, AttributeSet attributeSet) {
                super(context, attributeSet);

                // 以文件流（InputStream）读取进gif图片资源
                 movie = Movie.decodeStream(getResources().openRawResource(R.drawable.intoast));
            }

            @Override
     protected void onDraw(Canvas canvas) {

                 long curTime = android.os.SystemClock.uptimeMillis();

                 // 第一次播放
                 if (movieStart == 0) {
                        movieStart = curTime;
                     }
               if (movie != null) {
                         int duraction = movie.duration();
                         int relTime = (int) ((curTime - movieStart) % duraction);
                         movie.setTime(relTime);
                         movie.draw(canvas, 0, 0);
                         // 强制重绘
                         invalidate();
                     }

                 super.onDraw(canvas);
            }
 }