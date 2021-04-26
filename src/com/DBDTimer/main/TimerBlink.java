package com.DBDTimer.main;
import java.awt.*;

/**
 * TimerBlink.java.
 * @version 1.0.0
 * This class simply allows the properties of the blinkers to be
 * retrieved or passed as new objects.
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public class TimerBlink {
    /** Colour to set text to .*/
    private Color colour;
    /** Time to set colour at. */
    private int time;
    /** Frequency of text flash (ms), can be 0 to disable flashing text. */
    private int blinkFrequency;

    /**
     * sets the colour of the blinker.
     * @param newColour replaces the colour with new colour
     */
    public void setColour(final Color newColour) {
        this.colour = newColour;
    }

    /**
     * gets the colour of the blinker.
     * @return returns the colour of the blinker
     */
    public Color getColour() {
        return colour;
    }

    /**
     * sets the time of the blinker.
     * @param newTime replaces the time with new time
     */
    public void setTime(final int newTime) {
        this.time = newTime;
    }

    /**
     * gets the time of the blinker.
     * @return returns the time of the blinker
     */
    public int getTime() {
        return time;
    }

    /**
     * sets the frequency of the blinker.
     * @param frequency replaces the frequency with new frequency
     */
    public void setBlinkFrequency(final int frequency) {
        this.blinkFrequency = frequency;
    }

    /**
     * gets the frequency of the blinker.
     * @return returns the frequency of the blinker
     */
    public int getBlinkFrequency() {
        return blinkFrequency;
    }
}
