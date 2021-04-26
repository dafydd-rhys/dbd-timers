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

    /** represents the font of the timers. */
    private Font font;
    /** represents the icon size of the timers. */
    private int iconSize;
    /** represents the restart bind of the timers. */
    private String restartBind;
    /** represents the hide bind of the timers. */
    private String hideBind;
    /** represents the position of the timers. */
    private int[] windowPosition;

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

    /**
     * simply sets icon size.
     * @param newIconSize the new icon size
     */
    public void setIconSize(final int newIconSize) {
        this.iconSize = newIconSize;
    }

    /**
     * gets the icon size.
     * @return returns the icon size
     */
    public int getIconSize() {
        return iconSize;
    }

    /**
     * sets the restart bind for this timer.
     * @param newRestartBind the new restart bind
     */
    public void setRestartBind(final String newRestartBind) {
        this.restartBind = newRestartBind;
    }

    /**
     * gets the restart bind for this timer.
     * @return returns the restart bind
     */
    public String getRestartBind() {
        return restartBind;
    }

    /**
     * sets the hide bind for this timer.
     * @param newHideBind the new hind bind
     */
    public void setHideBind(final String newHideBind) {
        this.hideBind = newHideBind;
    }

    /**
     * gets the hide bind for this timer.
     * @return returns the hide bind
     */
    public String getHideBind() {
        return hideBind;
    }

    /**
     * sets the new window position.
     * @param newWindowPosition the new window location
     */
    public void setWindowPosition(final int[] newWindowPosition) {
        this.windowPosition = newWindowPosition;
    }

    /**
     * gets the new window position.
     * @return returns the window position
     */
    public int[] getWindowPosition() {
        return windowPosition;
    }
}
