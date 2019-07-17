package com.ooo.deemo.dclock.clockgo.bean;

/**
 * Author by Deemo, Date on 2019/5/29.
 * Have a good day
 */
public class ClockBean {

    public String clockName;

    public int clockMode;

    public String clockTime;

    public long timeInMillis;

    public boolean isSet;

    public int id;


    public ClockBean(String clockName, int clockMode, String clockTime , long timeInMillis ,int id) {
        this.clockName = clockName;
        this.clockMode = clockMode;
        this.clockTime = clockTime;
        this.timeInMillis = timeInMillis;
        this.isSet = false;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public String getClockName() {
        return clockName;
    }

    public void setClockName(String clockName) {
        this.clockName = clockName;
    }

    public int getClockMode() {
        return clockMode;
    }

    public void setClockMode(int clockMode) {
        this.clockMode = clockMode;
    }

    public String getClockTime() {
        return clockTime;
    }

    public void setClockTime(String clockTime) {
        this.clockTime = clockTime;
    }


    public long getTimeInMillis(){return timeInMillis;}

    public  void setTimeInMillis(long timeInMillis){ this.timeInMillis = timeInMillis;}
}
