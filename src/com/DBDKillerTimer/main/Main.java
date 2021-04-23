package com.DBDKillerTimer.main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 * Main.java.
 * @version 1.0
 * This class creates and displays 4 clocks that can be run by the user,
 * these clocks represent in game mechanics and timings which will allow
 * the user to get perfect timing on hits, hooks etc.
 * This class also reads properties such as resolution and icon sizes
 * which will allow the user to customize the appearance of there
 * instance and the program will adapt and optimize to these changes.
 * @author Dafydd-Rhys Maund
 */
public final class Main extends Canvas {

    /** properties used when creating JDialog. */
    private final int amountOfProperties = 3;
    private final int[] properties = new int[amountOfProperties];

    /** captures users resolution. */
    private static int width = 0;
    private static int height = 0;

    /** the size and location of the icon its text representation. */
    private static int iconSize = 0;
    private int yPositionOfText = 0;

    /** position of clocks. */
    private int decisiveClockXPosition;
    private int borrowedClockXPosition;
    private int chaseClockXPosition;
    private int shoulderClockXPosition;

    /**
     * This method creates a new instance of the main method, which
     * creates an instance of this program.
     * @param args the arguments used in methods
     * @throws FileNotFoundException if the properties file doesn't exist
     * @throws NativeHookException there's an issue reading global key presses
     */
    public static void main(final String[] args) throws
            FileNotFoundException, NativeHookException {
        new Main();
    }

    /**
     * This method creates the UI and stopwatches used to go with it and
     * begins reading inputs from the user which are used to manipulate
     * the timers.
     * @throws FileNotFoundException if the properties file doesn't exist
     * @throws NativeHookException there's an issue reading global key presses
     */
    private Main() throws FileNotFoundException, NativeHookException {
        GlobalScreen.registerNativeHook();

        //Generate dialog for UI
        JDialog dialog = new JDialog((java.awt.Dialog)null);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        dialog.setUndecorated(true);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT));
        getProperties();
        new Dialog(width, height, iconSize, this, dialog);
        getStopwatchTextPosition();

        ArrayList<Timer> timers = new ArrayList<Timer>();

        Timer decisiveTimer = new Timer(
                new ImageIcon("images\\decisive_" + iconSize + ".png"));

        Timer decisiveTimer = new Timer(
                new ImageIcon("images\\decisive_" + iconSize + ".png"));

        Timer decisiveTimer = new Timer(
                new ImageIcon("images\\decisive_" + iconSize + ".png"));

        Timer decisiveTimer = new Timer(
                new ImageIcon("images\\decisive_" + iconSize + ".png"));


        Stopwatch decisiveClock = new Stopwatch(dialog, decisiveClockXPosition,
                yPositionOfText, Stopwatch.CLOCK.DecisiveClock, iconSize);
        Stopwatch borrowedClock = new Stopwatch(dialog, borrowedClockXPosition,
                yPositionOfText, Stopwatch.CLOCK.BorrowedClock, iconSize);
        Stopwatch chaseClock = new Stopwatch(dialog, chaseClockXPosition,
                yPositionOfText, Stopwatch.CLOCK.ChaseClock, iconSize);
        Stopwatch shoulderClock = new Stopwatch(dialog, shoulderClockXPosition,
                yPositionOfText, Stopwatch.CLOCK.OnShoulderClock, iconSize);
        dialog.setBackground(new Color(0, 0, 0, 0));

        GlobalScreen.addNativeKeyListener(new KeyInput(decisiveClock,
                borrowedClock, chaseClock, shoulderClock));
    }

    /**
     * This method gets the properties that are found within a .txt, then
     * implements these into the program. It optimizes the location of time
     * labels to the resolution to insure they are placed correctly.
     * @throws FileNotFoundException if the properties file doesn't exist
     */
    private void getProperties() throws FileNotFoundException {
        Scanner file = new Scanner(new File("customization\\properties.txt"));
        int count = 0;
        while (file.hasNextLine()) {
            file.next();
            properties[count] = file.nextInt();
            count++;
        }

        width = properties[0];
        height = properties[1];
        iconSize = properties[2];

        final int maximumHeight = 1080;
        final int minimumHeight = 600;
        final int maximumWidth = 1920;
        final int minimumWidth = 800;
        if (width < minimumWidth || width > maximumWidth
                || height > maximumHeight || height < minimumHeight) {
            System.exit(0);
        }
    }

    /**
     * This method gets all the text locations using a formula that places
     * them in correct positions even after considering different resolution
     * and icon sizes.
     */
    private void getStopwatchTextPosition() {
        final int textSpacing = -5;
        final int formulaVariable = -6;
        final int smallestIconSize = 64;
        final int xSpacingValue = iconSize - textSpacing;
        final double decisiveStartingXPosition = (int) (((iconSize / (double)
                smallestIconSize) * 10) + 8);

        yPositionOfText = (int) (iconSize - (iconSize
                / (double) smallestIconSize) * formulaVariable);
        decisiveClockXPosition = (int) decisiveStartingXPosition;
        borrowedClockXPosition = decisiveClockXPosition + xSpacingValue;
        chaseClockXPosition = borrowedClockXPosition + xSpacingValue;
        shoulderClockXPosition = chaseClockXPosition + xSpacingValue;
    }

}
