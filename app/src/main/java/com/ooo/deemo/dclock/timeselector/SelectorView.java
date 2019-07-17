package com.ooo.deemo.dclock.timeselector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ooo.deemo.dclock.R;
import com.ooo.deemo.dclock.timeselector.interf.IPickerViewOperation;
import com.ooo.deemo.dclock.timeselector.util.ScreenUtil;


/**
 * Author by Deemo, Date on 2019/7/4.
 * Have a good day
 */
public class SelectorView extends RecyclerView {

    private Runnable mSmoothScrollTask;
    private Paint mBgPaint;
    private int mItemHeight;
    private int mItemWidth;
    private int mInitialY;
    private int mFirstLineY;
    private int mSecondLineY;
    private boolean mFirstAmend;

    public SelectorView(@NonNull Context context) {
        this(context,null);
    }

    public SelectorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelectorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTask();
    }

    private void initTask() {
        mSmoothScrollTask = new Runnable() {
            @Override
            public void run() {
                int newY = getScrollYDistance();
                if (mInitialY != newY) {
                    mInitialY = getScrollYDistance();
                    postDelayed(mSmoothScrollTask, 30);
                } else if (mItemHeight > 0) {
                    final int offset = mInitialY % mItemHeight;//离选中区域中心的偏移量
                    if (offset == 0) {
                        return;
                    }
                    if (offset >= mItemHeight / 2) {//滚动区域超过了item高度的1/2，调整position的值
                        smoothScrollBy(0, mItemHeight - offset);
                    } else if (offset < mItemHeight / 2) {
                        smoothScrollBy(0, -offset);
                    }
                }
            }
        };
    }
    private int getScrollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        if (layoutManager == null) {
            return 0;
        }
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        if (firstVisibleChildView == null) {
            return 0;
        }
        int itemHeight = firstVisibleChildView.getHeight();
        return (position) * itemHeight - firstVisibleChildView.getTop();
    }

    public void doDraw(Canvas canvas) {
        if (mItemHeight > 0) {
            int screenX = getWidth();
            int startX = screenX / 2 - mItemWidth / 2 - ScreenUtil.dpToPx(5);
            int stopX = mItemWidth + startX + ScreenUtil.dpToPx(5);

            canvas.drawLine(startX, mFirstLineY, stopX, mFirstLineY, mBgPaint);
            canvas.drawLine(startX, mSecondLineY, stopX, mSecondLineY, mBgPaint);
        }
    }


    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        doDraw(c);
        if (!mFirstAmend) {
            mFirstAmend = true;
            ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(getItemSelectedOffset(), 0);
        }
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        freshItemView();
    }

    private void initPaint() {
        if (mBgPaint == null) {
            mBgPaint = new Paint();
            mBgPaint.setColor(getLineColor());
            mBgPaint.setStrokeWidth(ScreenUtil.dpToPx(1f));
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            processItemOffset();
        }
        return super.onTouchEvent(e);
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {

        widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        heightSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);

        super.onMeasure(widthSpec, heightSpec);

        measureSize();

        setMeasuredDimension(mItemWidth, mItemHeight * getVisibleItemNumber());
    }

    private void measureSize() {
        if (getChildCount() > 0) {
            if (mItemHeight == 0) {
                mItemHeight = getChildAt(0).getMeasuredHeight();
            }
            if (mItemWidth == 0) {
                mItemWidth = getChildAt(0).getMeasuredWidth();
            }

            if (mFirstLineY == 0 || mSecondLineY == 0) {
                mFirstLineY = mItemHeight * getItemSelectedOffset();
                mSecondLineY = mItemHeight * (getItemSelectedOffset() + 1);
            }
        }
    }

    private void processItemOffset() {
        mInitialY = getScrollYDistance();
        postDelayed(mSmoothScrollTask, 30);
    }



    private int getVisibleItemNumber() {
        IPickerViewOperation operation = (IPickerViewOperation) getAdapter();
        if (operation != null) {
            return operation.getVisibleItemNumber();
        }
        return 3;
    }

    private int getItemSelectedOffset() {
        IPickerViewOperation operation = (IPickerViewOperation) getAdapter();
        if (operation != null) {
            return operation.getSelectedItemOffset();
        }
        return 1;
    }

    private int getLineColor() {
        IPickerViewOperation operation = (IPickerViewOperation) getAdapter();
        if (operation != null && operation.getLineColor() != 0) {
            return operation.getLineColor();
        }
        return getResources().getColor(R.color.colorPrimary);
    }

    private void updateView(View itemView, boolean isSelected) {
        IPickerViewOperation operation = (IPickerViewOperation) getAdapter();
        if (operation != null) {
            operation.updateView(itemView, isSelected);
        }
    }

    private void freshItemView() {
        for (int i = 0; i < getChildCount(); i++) {
            float itemViewY = getChildAt(i).getTop() + mItemHeight / 2;
            updateView(getChildAt(i), mFirstLineY < itemViewY && itemViewY < mSecondLineY);
        }
    }

//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        //获取子控件的数量
//        int count = getChildCount();
//        //设置默认起始值
//        int startWidth = 0;
//        int startHeight = 0;
//        //循环遍历子控件
//        for (int j = 0; j < count; j++) {
//            //取出每一条子视图
//            View v = getChildAt(j);
//            v.layout(startWidth,startHeight,startWidth+v.getMeasuredWidth(),startHeight+v.getMeasuredHeight());
//            startWidth+=v.getMeasuredWidth();
//            startHeight+=v.getMeasuredHeight();
//        }
//
//    }

//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initPaint();
    }
}


