package com.DBDKillerTimer.main;
import java.util.ArrayList;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * KeyInput.java.
 * @version 1.0.1
 * This class reads the inputs of the users and completes actions
 * based on the bound key.
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public class KeyInput extends NativeKeyAdapter {

    /** this simply stores the timers that are currently being used. */
    private final ArrayList<IconTimer> clocks;

    /**
     * This method simply passes the passed components to the attributes
     * then retrieves binds.
     * @param timers timers
     */
    public KeyInput(final ArrayList<IconTimer> timers) {
        this.clocks = timers;
    }

    /**
     * Runs on every keyboard press. If key matches any binds, runs
     * associated action.
     * @param e KeyEvent from key press.
     */
    public final void nativeKeyPressed(final NativeKeyEvent e) {
        String key = NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
        for (IconTimer clock : clocks) {
            if (key.equals(clock.getStartBind().toLowerCase())) {
                clock.restart();
            }
            if (key.equals(clock.getRestartBind().toLowerCase())) {
                clock.fullReset();
            }
            if (key.equals(clock.getHideBind().toLowerCase())) {
                clock.hide();
            }
        }
    }
}

