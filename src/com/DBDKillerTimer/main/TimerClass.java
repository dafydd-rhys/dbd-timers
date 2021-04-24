package com.DBDKillerTimer.main;

import java.awt.*;

public class TimerClass {

    public enum TimerType {
        CountUp, CountDown
    }
    public enum TimerMode {
        Survivor, Killer
    }

    public String name;
    public int startTime;
    public Color startColor;
    public TimerType timerType;
    public String icon;
    public TimerMode timerMode;
    public boolean enabled;
    public String startBind;
}
