package com.ooo.deemo.dclock.clockgo.operater;

import com.ooo.deemo.dclock.clockgo.inter.ClockChangeListener;

/**
 * Author by Deemo, Date on 2019/7/12.
 * Have a good day
 */
public class ChangeClockOperater {

    private ClockChangeListener clockChangeListener;


    public void setClockChangeListener(ClockChangeListener clockChangeListener){
        this.clockChangeListener =  clockChangeListener;
    }

    public void doSetNewClock(){
        if(clockChangeListener != null){
            clockChangeListener.onSetNewClock();
        }
    }

    public void doDeleteClock(){
        if(clockChangeListener != null){
            clockChangeListener.onDeleteClock();
        }
    }
}
