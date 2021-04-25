package com.DBDKillerTimer.main;
import java.awt.Color;

/**
 * TimerProperties.java.
 * @version 1.0.0
 * This class simply is used to store and retrieve the properties
 * of the current icon timer.
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public final class TimerProperties {

    public enum TimerType {
        /** represents the timer increases. */
        CountUp,
        /** represents the timer decreases. */
        CountDown
    }
    public enum TimerMode {
        /** represents the user is using survivor timers. */
        Survivor,
        /** represents the user is using killer timers. */
        Killer
    }

    /** represents the timers name.*/
    private String name;
    /** represents the timers start time.*/
    private int startTime;
    /** represents the timers start colour.*/
    private Color startColor;
    /** represents the timers type.*/
    private TimerType timerType;
    /** represents the timers icon path.*/
    private String icon;
    /** represents the timers mode.*/
    private TimerMode timerMode;
    /** represents whether the timer is enabled or disabled.*/
    private boolean enabled;
    /** represents the timers bind to start it.*/
    private String startBind;

    /**
     * sets the timers name.
     * @param timersName the name of the timer
     */
    public void setName(final String timersName) {
        this.name = timersName;
    }

    /**
     * gets the timers name.
     * @return returns the name of the timer
     */
    public String getName() {
        return name;
    }

    /**
     * sets the timers start time.
     * @param timersStartTime the start time of the timer
     */
    public void setStartTime(final int timersStartTime) {
        this.startTime = timersStartTime;
    }

    /**
     * gets the timers start time.
     * @return returns the start time of the timer
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * sets the timers starting colour.
     * @param timersStartColor the name of the timer
     */
    public void setStartColor(final Color timersStartColor) {
        this.startColor = timersStartColor;
    }

    /**
     * gets the timers starting colour.
     * @return returns the starting colour of the timer
     */
    public Color getStartColor() {
        return startColor;
    }

    /**
     * sets the timers type.
     * @param timersType the type of the timer
     */
    public void setTimerType(final TimerType timersType) {
        this.timerType = timersType;
    }

    /**
     * gets the timers type.
     * @return returns the type of the timer
     */
    public TimerType getTimerType() {
        return timerType;
    }

    /**
     * sets the timers icon path.
     * @param timersImage the icon path of the timer
     */
    public void setIcon(final String timersImage) {
        this.icon = timersImage;
    }

    /**
     * gets the timers icon path.
     * @return returns the path of the timers icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * sets the timers mode.
     * @param timersMode the mode of the timer
     */
    public void setTimerMode(final TimerMode timersMode) {
        this.timerMode = timersMode;
    }

    /**
     * gets the timers mode.
     * @return returns the mode of the timer
     */
    public TimerMode getTimerMode() {
        return timerMode;
    }

    /**
     * sets whether the timer is enabled.
     * @param timerIsEnabled the status of the timer
     */
    public void setEnabled(final boolean timerIsEnabled) {
        this.enabled = timerIsEnabled;
    }

    /**
     * gets the timers status.
     * @return returns the status of the timer
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * sets the timers start bind.
     * @param timersStartBind the start bind of the timer
     */
    public void setStartBind(final String timersStartBind) {
        this.startBind = timersStartBind;
    }

    /**
     * gets the timers start bind.
     * @return returns the bind for the timer
     */
    public String getStartBind() {
        return startBind;
    }
}
