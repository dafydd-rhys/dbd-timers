package com.DBDKillerTimer.main;
import java.io.File;
import java.io.FileNotFoundException;
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

    /** stores the instances of stopwatches. */
    private final Stopwatch decisiveClock;
    private final Stopwatch borrowedClock;
    private final Stopwatch chaseClock;
    private final Stopwatch shoulderClock;

    /** binds used to control stopwatches. */
    private static int decisiveBind = 0;
    private static int chaseBind = 0;
    private static int shoulderBind = 0;
    private static int restartBind = 0;


    /**
     * This method simply passes the passed components to the attributes
     * then retrieves binds.
     * @param decisive decisive strike timer
     * @param borrowed borrowed time timer
     * @param chase chase timer
     * @param shoulder on shoulder timer
     * @throws FileNotFoundException the file binds.txt wasn't found
     */
    public KeyInput(final Stopwatch decisive, final Stopwatch borrowed,
                    final Stopwatch chase, final Stopwatch shoulder)
            throws FileNotFoundException {
        this.decisiveClock = decisive;
        this.borrowedClock = borrowed;
        this.chaseClock = chase;
        this.shoulderClock = shoulder;
        getBinds();
    }

    /**
     * This method simply takes the code of the key the user entered and if
     * it matches to the users bind then complete the action its bound to.
     * @param e the KeyEvent the user committed
     */
    public final void nativeKeyPressed(final NativeKeyEvent e) {
        int key = e.getKeyCode();

        if (key == decisiveBind) {
            decisiveClock.reset();
            decisiveClock.start();
            borrowedClock.reset();
            borrowedClock.start();
        }
        if (key == chaseBind) {
            chaseClock.reset();
            chaseClock.start();
        }
        if (key == shoulderBind) {
            shoulderClock.reset();
            shoulderClock.start();
        }
        if (key == restartBind) {
            decisiveClock.fullReset();
            borrowedClock.fullReset();
            chaseClock.fullReset();
            shoulderClock.fullReset();
        }
    }

    /**
     * this method simply retrieves the binds found in the text file then
     * converts the binds to codes which can be used to bind keys.
     * @throws FileNotFoundException file binds.txt wasn't found
     */
    private void getBinds() throws FileNotFoundException {
        Scanner readFile = new Scanner(new File("customization\\binds.txt"));
        int count = 0;
        while (readFile.hasNextLine()) {
            readFile.next();
            binds[count] = readFile.next().charAt(0);
            count++;
        }
        new ConvertToBinding(binds[0], binds[1], binds[2], binds[3]);
    }

    /**
     * sets the decisive strike bind.
     * @param decisive the code representing the bind
     */
    public static void setBindDecisive(final int decisive) {
        decisiveBind = decisive;
    }

    /**
     * sets the chase bind.
     * @param chase the code representing the bind
     */
    public static void setBindChase(final int chase) {
        chaseBind = chase;
    }

    /**
     * sets the on shoulder bind.
     * @param shoulder the code representing the bind
     */
    public static void setBindShoulder(final int shoulder) {
        shoulderBind = shoulder;
    }

    /**
     * sets the restart bind.
     * @param restart the code representing the bind
     */
    public static void setBindRestart(final int restart) {
        restartBind = restart;
    }
}

