package com.DBDKillerTimer.main;
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;

/**
 * Stopwatch.java.
 * @version 1.0
 * This class simply creates the stopwatches and changes them based on
 * their type, status etc
 * @author Dafydd-Rhys Maund & Morgan Gardner
 */
public class Stopwatch {

    /** referring to state of clock. */
    private boolean notRunning = true;

    /** representation for the time label. */
    private JLabel timeLabel;
    private String seconds;
    private String minutes;

    private final int startingTime;

    /** time variables. */
    private int elapsedTime = 0;
    private final int milliseconds = 1000;
    private int second = 0;
    private int minute = 0;

    private enum TimerType {
        CountUp, CountDown
    }

    private final String startBind;
    private final String restartBind;

    private TimerType timerType;
    private final JPanel hostPanel;

    /**
     *
     * @param iconSize the size of the icon
     * @param icon the icon image
     * @param startingTime the starting time of the clock
     * @param startBind the bind to start timer
     * @param restartBind the bind to restart timer
     */
    public Stopwatch(final int iconSize, ImageIcon icon, final int startingTime,
                     final String startBind, final String restartBind) {
        this.startingTime = startingTime;
        this.startBind = startBind;
        this.restartBind = restartBind;

        if(startingTime == 0) {
            this.timerType = TimerType.CountUp;
        } else {
            this.timerType = TimerType.CountDown;
        }

        // transforms icon to different size smoothly
        Image image = icon.getImage();
        Image newImg = image.getScaledInstance(iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);

        JLabel timerIcon = new JLabel(icon);
        timerIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        hostPanel = new JPanel();
        hostPanel.setPreferredSize(new Dimension(iconSize, iconSize + 30));
        hostPanel.setOpaque(false);
        BoxLayout hostLayout = new BoxLayout(hostPanel, BoxLayout.Y_AXIS);
        hostPanel.setLayout(hostLayout);

        //generate timer label
        setString();
        timeLabel = new JLabel(minutes + ":" + seconds);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setForeground(Color.WHITE);

        timeLabel.setBounds(0, 0, iconSize, 30);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, iconSize / 4));
        hostPanel.add(timerIcon);
        hostPanel.add(timeLabel);
    }

    public final String getStartBind() {
        return this.startBind;
    }

    public final String getRestartBind() {
        return this.restartBind;
    }

    public final JPanel getUIElement() {
        return hostPanel;
    }

    /**
     * Timer
     */
    private final Timer timer = new Timer(1000, e -> {

        getTime(notRunning);
        if (timerType == TimerType.CountDown) {
            if (!(elapsedTime == 0)) {
                elapsedTime = elapsedTime - milliseconds;
            }
        } else {
            elapsedTime = elapsedTime + milliseconds;
        }

        minute = (elapsedTime / 60000) % 60;
        second = (elapsedTime / 1000) % 60;
        seconds = String.format("%02d", second);
        minutes = String.format("%02d", minute);

        //clock is stopped
        if (second == 0 && minute == 0) {
            //clock is stopped
            timeLabel.setForeground(Color.GREEN);   //timeLabel.setForeground(timer.stopColour);
        } else if (second >= 60 / 2 || minute > 0) {
            timeLabel.setForeground(Color.red);
        }
        timeLabel.setText(minutes + ":" + seconds);
    });

    /**
     * this method simply sets the string for the timer.
     */
    private void setString() {
        getStartTime();
        seconds = String.format("%02d", second);
        minutes = String.format("%02d", minute);
    }

    /**
     * gets the starting times for each clock.
     */
    private void getStartTime() {
        minute = 0;
        second = startingTime;
    }

    /**
     * gets the current time for this clock.
     * @param run whether the timers have been ran yet
     */
    private void getTime(final boolean run) {
        if (run) {
            timeLabel.setForeground(Color.red); //timeLabel.setForeground(timer.startColour);
            elapsedTime = startingTime * milliseconds;
        }
        notRunning = false;
    }

    /**
     * This method simply fully resets the colour and time of the timer.
     */
    private void getOriginalTime() {
        timeLabel.setForeground(Color.white);
        elapsedTime = startingTime * milliseconds;
    }

    /**
     * this method restarts the timer (resetting the time).
     */
    public final void restart() {
        timer.stop();
        setString();
        notRunning = true;
        getTime(true);
        timeLabel.setText(minutes + ":" + seconds);
        timer.start();
    }

    /**
     * this method fully resets the timers.
     */
    public final void fullReset() {
        timer.stop();
        setString();
        getOriginalTime();
        timeLabel.setText(minutes + ":" + seconds);
    }
}
