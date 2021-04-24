package com.DBDKillerTimer.main;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;


/**
 * KeyInput.java.
 * @version 1.0
 * This class reads the inputs of the users and completes actions
 * based on the bound key.
 * @author Dafydd-Rhys Maund
 */
public class KeyInput extends NativeKeyAdapter {

    /** creates array for binds. */
    private final int bindNum = 4;
    private final char[] binds = new char[bindNum];

    /** binds used to control stopwatches. */
    private static int startBind = 0;
    private static int restartBind = 0;

    private ArrayList<Stopwatch> clocks;


    /**
     * This method simply passes the passed components to the attributes
     * then retrieves binds.
     * @param timers timers
     * @throws FileNotFoundException the file binds.txt wasn't found
     */
    public KeyInput(ArrayList<Stopwatch> timers) throws FileNotFoundException {
        this.clocks = timers;
    }

    /**
     * Runs on every keyboard press. If key matches any binds, runs
     * associated action.
     * @param e KeyEvent from key press.
     */
    public final void nativeKeyPressed(final NativeKeyEvent e) {
        char key = Character.toLowerCase(e.getKeyText(e.getKeyCode()).charAt(0));

        for (Stopwatch clock : clocks) {
            if (key == clock.getStartBind()) {
                clock.restart();
            }
            if (key == clock.getRestartBind()) {
                clock.fullReset();
            }
        }

    }

    /**
     * sets the start timer bind
     * @param startBind the character representing the bind
     */
    public void setStartBind(int startBind) {
        this.startBind = startBind;
    }

    /**
     * sets the restart timer bind
     * @param restartBind the character representing the bind
     */
    public void setRestartBind(int restartBind) {
        this.restartBind = restartBind;
    }
}

