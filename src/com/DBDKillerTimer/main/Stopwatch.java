package com.DBDKillerTimer.main;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Stopwatch.java.
 * @version 1.0
 * This class simply creates the stopwatches and changes them based on
 * their type, status etc
 * @author Dafydd-Rhys Maund
 */
public class Stopwatch {

    /** referring to state of clock. */
    private boolean notRunning = true;

    /** referring to this clock. */
    private final CLOCK clock;
    private boolean shoulderClock = false;

    /** test representation for the time. */
    private final JLabel timeLabel = new JLabel();
    private int textFontSize = 0;
    private String seconds;
    private String minutes;

    /** starting times of clocks. */
    private final int decisiveStartingTime = 1;
    private final int borrowedStartingTime = 15;
    private final int chaseStartingTime = 0;
    private final int shoulderStartingTime = 16;

    /** time variables. */
    private int elapsedTime = 0;
    private final int milliseconds = 1000;
    private final int millisecondsPerMin = 60000;
    private int second = 0;
    private int minute = 0;

    /** stores the type of clock. */
    public enum CLOCK {
        DecisiveClock(),
        BorrowedClock(),
        ChaseClock(),
        OnShoulderClock(),
    }

    /**
     * This method simply creates the stopwatches and their format then add them
     * to the dialog.
     * @param dialog represents the dialog the stopwatches are being added to
     * @param x represents the width of the dialog
     * @param y represents the height of the dialog
     * @param stopwatchType represents the type of stopwatch we're referring too
     * @param iconSize represents the desired icon size
     */
    Stopwatch(final JDialog dialog, final int x, final int y,
              final CLOCK stopwatchType, final int iconSize) {
        this.clock = stopwatchType;
        if (clock == CLOCK.OnShoulderClock) {
            shoulderClock = true;
        }
        setString();

        dialog.setLayout(null);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setText(minutes + ":" + seconds);
        setTextFontSize(iconSize);

        final int textWidth = 100;
        final int textHeight = 30;
        timeLabel.setBounds(x, y, textWidth, textHeight);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, textFontSize));
        dialog.add(timeLabel);
    }

    /**
     * This method simply calculates the font size based on the
     * icon size.
     * @param iconSize the icon size the user wants
     */
    private void setTextFontSize(final int iconSize) {
        final int iconSize1 = 128;
        final int iconSize2 = 96;
        final int iconSize3 = 64;
        final int quarter = 4;

        if (iconSize == iconSize1) {
            textFontSize = iconSize / quarter;
        } else if (iconSize == iconSize2) {
            textFontSize = iconSize / quarter;
        } else if (iconSize == iconSize3) {
            textFontSize = iconSize / quarter;
        }
    }

    /**
     * This method simply makes a new timer and identifies the starting time
     * and whether the timer will increase on decrease based on the type.
     * Also, this method sets the format and changes the color of the text
     * based on the type.
     */
    private final Timer timer = new Timer(1000, e -> {
        getTime(notRunning);
        if (getType()) {
            if (!(elapsedTime == 0)) {
                elapsedTime = elapsedTime - milliseconds;
            }
        } else {
            elapsedTime = elapsedTime + milliseconds;
        }

        int secondsPerMin = millisecondsPerMin / milliseconds;
        minute = (elapsedTime / millisecondsPerMin) % secondsPerMin;
        second = (elapsedTime / milliseconds) % secondsPerMin;
        seconds = String.format("%02d", second);
        minutes = String.format("%02d", minute);

        if (second == 0 && minute < 1) {
            if (!shoulderClock) {
                timeLabel.setForeground(Color.GREEN);
            } else {
                timeLabel.setForeground(Color.red);
            }
        } else if (second >= secondsPerMin / 2 || minute > 0) {
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
     * this method simply identifies whether this clock is supposed to
     * decrease in timer rather than increase.
     * @return true if the the clock is supposed to count down
     */
    private boolean getType() {
        return clock == CLOCK.DecisiveClock || clock == CLOCK.BorrowedClock
                || clock == CLOCK.OnShoulderClock;
    }

    /**
     * gets the starting times for each clock.
     */
    private void getStartTime() {
        if (clock == CLOCK.DecisiveClock) {
            minute = decisiveStartingTime;
            second = 0;
        } else if (clock == CLOCK.BorrowedClock) {
            minute = 0;
            second = borrowedStartingTime;
        } else if (clock == CLOCK.OnShoulderClock) {
            minute = 0;
            second = shoulderStartingTime;
        } else {
            timeLabel.setForeground(Color.white);
            minute = chaseStartingTime;
            second = chaseStartingTime;
        }
    }

    /**
     * gets the current time for this clock.
     * @param run whether the timers have been ran yet
     */
    private void getTime(final boolean run) {
        if (run) {
            if (clock == CLOCK.DecisiveClock) {
                timeLabel.setForeground(Color.red);
                elapsedTime = decisiveStartingTime * millisecondsPerMin;
            } else if (clock == CLOCK.BorrowedClock) {
                timeLabel.setForeground(Color.red);
                elapsedTime = borrowedStartingTime * milliseconds;
            } else if (clock == CLOCK.OnShoulderClock) {
                timeLabel.setForeground(Color.GREEN);
                elapsedTime = shoulderStartingTime * milliseconds;
            } else {
                elapsedTime = chaseStartingTime;
            }
        }
        notRunning = false;
    }

    /**
     * This method simply fully resets the timers, color and time.
     */
    private void getOriginalTime() {
        timeLabel.setForeground(Color.white);
        if (clock == CLOCK.DecisiveClock) {
            elapsedTime = decisiveStartingTime * millisecondsPerMin;
        } else if (clock == CLOCK.BorrowedClock) {
            elapsedTime = borrowedStartingTime * milliseconds;
        } else if (clock == CLOCK.OnShoulderClock) {
            elapsedTime = shoulderStartingTime * milliseconds;
        } else {
            elapsedTime = chaseStartingTime;
        }
    }

    /**
     * this method starts the timer.
     */
    public final void start() {
        timer.start();
    }

    /**
     * this method resets the timer.
     */
    public final void reset() {
        timer.stop();
        setString();
        notRunning = true;
        getTime(true);
        timeLabel.setText(minutes + ":" + seconds);
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
