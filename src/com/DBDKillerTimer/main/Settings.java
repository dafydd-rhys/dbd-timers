package com.DBDKillerTimer.main;
import java.awt.Font;

/**
 * Settings.java.
 * @version 1.0.0
 * This class simply stores the settings the user wants implemented
 * every time they run the program.
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public class Settings {

    /**
     * represents the font of the timers.
     */
    private Font font;
    public int iconSize;
    public int[] windowPosition;

    /**
     * this method simply sets the font settings.
     * @param newFont the passed font
     */
    public void setFont(final Font newFont) {
        this.font = newFont;
    }

    /**
     * this method simply gets the font settings.
     * @return returns the font settings
     */
    public Font getFont() {
        return font;
    }
}
