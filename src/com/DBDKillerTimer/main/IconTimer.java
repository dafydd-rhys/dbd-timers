package com.DBDKillerTimer.main;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private TimerProperties properties;
    /** the elapsed time of this timer. */
    private long elapsedTime;
    /** amount of millis in a second. */
    private final int milliseconds = 1000;
    /** Time formatter for the current time */
    private SimpleDateFormat dateFormat;

    private final Timer blinkTimer;
    private Color blinkColour;

    /** the enums to separate different timer types. */
    private enum TimerType {
        /** represents increasing timers. */
        CountUp,
        /** represents decreasing timers. */
        CountDown
    }
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

        dateFormat = new SimpleDateFormat("mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.elapsedTime = properties.getStartTime() * 1000L;

        blinkTimer = new Timer(1000, f -> {
            if (timeLabel.getForeground() == properties.getStartColor()) {
                timeLabel.setForeground(blinkColour);
            } else {
                timeLabel.setForeground(properties.getStartColor());
            }
        });

        if (properties.getStartTime() == 0) {
            this.timerType = TimerType.CountUp;
        } else {
            this.timerType = TimerType.CountDown;
        }

        Image image = new ImageIcon(properties.getIcon()).getImage();
        Image newImg = image.getScaledInstance(SettingsManager.settings.iconSize, SettingsManager.settings.iconSize,
                java.awt.Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(newImg);
        JLabel timerIcon = new JLabel(icon);
        timerIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        hostPanel = new JPanel();
        hostPanel.setLayout(new BoxLayout(hostPanel, BoxLayout.Y_AXIS));
        hostPanel.setPreferredSize(new Dimension(SettingsManager.settings.iconSize, SettingsManager.settings.iconSize + 30));
        hostPanel.setOpaque(false);

        timeLabel = new JLabel(getCurrentTime());
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setForeground(properties.getStartColor());
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

        Date date = new Date(elapsedTime);

        ArrayList<TimerBlink> timerBlinks = properties.getTimerBlinks();
        if(timerBlinks != null) {
            for (TimerBlink timerBlink : timerBlinks) {
                if ((date.getTime() / 1000) == timerBlink.time) {
                    if (timerBlink.blinkFrequency != 0) {
                        this.blinkColour = timerBlink.colour;
                        restartTimer(timerBlink.blinkFrequency);
                    }
                }
            }
        }
        timeLabel.setText(dateFormat.format(date));
    });

    /**
     * Restart blink timer with new delay
     * @param delay millis to wait between ticks
     */
    private void restartTimer(int delay) {
        blinkTimer.stop();
        blinkTimer.setDelay(delay);
        blinkTimer.start();
    }

    /**
     * Stop blink timer (used for main timer resets)
     */
    private void stopTimer() {
        blinkTimer.stop();
        timeLabel.setForeground(properties.getStartColor());
    }

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
            //timeLabel.setForeground(Color.red);
            elapsedTime = (long) properties.getStartTime() * milliseconds;
        }
        notRunning = false;
    }

    /**
     * This method simply fully resets the colour and time of the timer.
     */
    private void getOriginalTime() {
        timeLabel.setForeground(Color.white);
        elapsedTime = (long) properties.getStartTime() * milliseconds;
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
        stopTimer();
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
        stopTimer();
    }
}
