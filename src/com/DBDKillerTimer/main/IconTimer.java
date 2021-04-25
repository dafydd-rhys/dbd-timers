package com.DBDKillerTimer.main;
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;

/**
 * IconTimer.java.
 * @version 1.0.1
 * This class simply creates the timers and their properties, and they
 * will change depending on their type, status etc
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public class IconTimer {

    /** referring to state of clock. */
    private boolean notRunning = true;
    /** representation of the full time. */
    private JLabel timeLabel;
    /** representation of the seconds on the timer. */
    private String seconds;
    /** representation of the minutes on the timer. */
    private String minutes;
    /** the starting time of this timer. */
    private final int startingTime;
    /** the elapsed time of this timer. */
    private int elapsedTime = 0;
    /** amount of millis in a second. */
    private final int milliseconds = 1000;
    /** second time variable, +1 or -1 every 1000 millis. */
    private int second = 0;
    /** minutes time variable, +1 or -1 every 60000 millis. */
    private int minute = 0;

    /** the enums to separate different timer types. */
    private enum TimerType {
        /** represents increasing timers. */
        CountUp,
        /** represents decreasing timers. */
        CountDown
    }

    /** the binds that starts the timer. */
    private final String startBind;
    /** the binds that restarts the timers. */
    private final String restartBind;
    /** the icon representing the timer. */
    private final JLabel timerIcon;
    /** the timer type, up or down. */
    private TimerType timerType;
    /** the panel that hosts all the timers. */
    private final JPanel hostPanel;

    /**
     * This simply sets the properties of the timers and adds them to the panel.
     * @param iconSize the size of the timers image.
     * @param icon the image representing the timer.
     * @param startTime the starting time of the timer.
     * @param start the bind to start the timer.
     * @param restart the bind to restart timers.
     */
    public IconTimer(final int iconSize, ImageIcon icon, final int startTime,
                     final String start, final String restart) {
        this.startingTime = startTime;
        this.startBind = start;
        this.restartBind = restart;

        if (startingTime == 0) {
            this.timerType = TimerType.CountUp;
        } else {
            this.timerType = TimerType.CountDown;
        }

        Image image = icon.getImage();
        Image newImg = image.getScaledInstance(iconSize, iconSize,
                java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        timerIcon = new JLabel(icon);
        timerIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        hostPanel = new JPanel();
        hostPanel.setLayout(new BoxLayout(hostPanel, BoxLayout.Y_AXIS));
        hostPanel.setPreferredSize(new Dimension(iconSize, iconSize + 30));
        hostPanel.setOpaque(false);

        setString();
        timeLabel = new JLabel(minutes + ":" + seconds);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setBounds(0, 0, iconSize, 30);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, iconSize / 4));

        hostPanel.add(timerIcon);
        hostPanel.add(timeLabel);
    }

    /** this method simply retrieves the start timer bind.
     @return returns the start bind
     */
    public final String getStartBind() {
        return this.startBind;
    }

    /** this method simply retrieves the restart timer bind.
     @return returns the restart bind
     */
    public final String getRestartBind() {
        return this.restartBind;
    }

    /** this method simply retrieves host panel of all the items.
     @return returns the host panel
     */
    public final JPanel getUIElement() {
        return hostPanel;
    }

    /**
     * this method simply represents the timer, it works in intervals
     * of 1000 milliseconds or 1 second. Also, if any specific conditions
     * are met the timer may change colour.
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

        if (second == 0 && minute == 0) {
            timeLabel.setForeground(Color.GREEN);
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
            timeLabel.setForeground(Color.red);
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
