package com.DBDKillerTimer.main;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
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
    /** Timer properties object */
    private TimerProperties properties = null;
    /** the elapsed time of this timer. */
    private long elapsedTime;
    /** amount of millis in a second. */
    private final int milliseconds = 1000;
    /** Time formatter for the current time */
    private SimpleDateFormat dateFormat;

    /** the enums to separate different timer types. */
    private enum TimerType {
        /** represents increasing timers. */
        CountUp,
        /** represents decreasing timers. */
        CountDown
    }
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
     * @param properties the properties of the timer.
     */
    public IconTimer(final TimerProperties properties) {
        this.properties = properties;
        this.restartBind = SettingsManager.settings.restartBind;

        dateFormat = new SimpleDateFormat("mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.elapsedTime = properties.getStartTime() * 1000;

        if (properties.getStartTime() == 0) {
            this.timerType = TimerType.CountUp;
        } else {
            this.timerType = TimerType.CountDown;
        }

        Image image = new ImageIcon(properties.getIcon()).getImage();
        Image newImg = image.getScaledInstance(SettingsManager.settings.iconSize, SettingsManager.settings.iconSize,
                java.awt.Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(newImg);
        timerIcon = new JLabel(icon);
        timerIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        hostPanel = new JPanel();
        hostPanel.setLayout(new BoxLayout(hostPanel, BoxLayout.Y_AXIS));
        hostPanel.setPreferredSize(new Dimension(SettingsManager.settings.iconSize, SettingsManager.settings.iconSize + 30));
        hostPanel.setOpaque(false);

        timeLabel = new JLabel(getCurrentTime());
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setBounds(0, 0, SettingsManager.settings.iconSize, 30);
        timeLabel.setFont(SettingsManager.settings.getFont());

        hostPanel.add(timerIcon);
        hostPanel.add(timeLabel);
    }

    /** this method simply retrieves the start timer bind.
     @return returns the start bind
     */
    public final String getStartBind() {
        return this.properties.getStartBind();
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
            if (elapsedTime != 0) {
                elapsedTime -= milliseconds;
            }
        } else {
            elapsedTime += milliseconds;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date(elapsedTime);

        if (date.getTime() == 0) {
            timeLabel.setForeground(properties.getStartColor());
        } else if (date.getTime() == 30000) {   //change implementation of text colour change
            timeLabel.setForeground(Color.red);
        }
        timeLabel.setText(dateFormat.format(date));
    });

    /**
     * Get current time formatted as 'mm:ss'
     * @return Current timer time
     */
    private String getCurrentTime() {
        Date date = new Date(elapsedTime);
        return dateFormat.format(date);
    }

    /**
     * gets the current time for this clock.
     * @param run whether the timers have been ran yet
     */
    private void getTime(final boolean run) {
        if (run) {
            timeLabel.setForeground(Color.red);
            elapsedTime = properties.getStartTime() * milliseconds;
        }
        notRunning = false;
    }

    /**
     * This method simply fully resets the colour and time of the timer.
     */
    private void getOriginalTime() {
        timeLabel.setForeground(Color.white);
        elapsedTime = properties.getStartTime() * milliseconds;
    }

    /**
     * this method restarts the timer (resetting the time).
     */
    public final void restart() {
        timer.stop();
        this.elapsedTime = properties.getStartTime();
        notRunning = true;
        getTime(true);
        timeLabel.setText(getCurrentTime());
        timer.start();
    }

    /**
     * this method fully resets the timers.
     */
    public final void fullReset() {
        timer.stop();
        this.elapsedTime = properties.getStartTime();
        getOriginalTime();
        timeLabel.setText(getCurrentTime());
    }
}
