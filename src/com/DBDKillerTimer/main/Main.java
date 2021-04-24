package com.DBDKillerTimer.main;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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

        //region NativeHook

        // Get the logger for "com.github.kwhat.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        GlobalScreen.registerNativeHook();
        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);

        //endregion

        //Generate dialog for UI
        JDialog dialog = new JDialog((java.awt.Dialog)null);

        //Catch window close event and shutdown process
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        dialog.setUndecorated(true);
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 10, 0);
        dialog.setLayout(layout);
        getProperties();

        dialog.setAlwaysOnTop(true);
        dialog.setPreferredSize(new Dimension(width, height));
        dialog.setMaximumSize(new Dimension(width, height));
        dialog.setMinimumSize(new Dimension(width, height));

        dialog.setDefaultCloseOperation(dialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.add(this);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        ArrayList<Stopwatch> timers = new ArrayList<Stopwatch>();

        //killer
        timers.add(new Stopwatch(iconSize,
                new ImageIcon("images\\decisive_strike.png"), 60, '3', 'r'));

        timers.add(new Stopwatch(iconSize,
                new ImageIcon("images\\borrowed_time.png"), 15, '3', 'r'));

        timers.add(new Stopwatch(iconSize,
                new ImageIcon("images\\chase.png"), 0, '4', 'r'));

        timers.add(new Stopwatch(iconSize,
                new ImageIcon("images\\on_shoulder.png"), 16, 'e', 'r'));

        //survivor
        timers.add(new Stopwatch(iconSize,
                new ImageIcon("images\\unbreakable.png"), 20, 'e', 'r'));

        timers.add(new Stopwatch(iconSize,
                new ImageIcon("images\\generator.png"), 80, '4', 'r'));

        timers.add(new Stopwatch(iconSize,
                new ImageIcon("images\\chase.png"), 0, '3', 'r'));

        int counter = 0;
        for (Stopwatch timer : timers) {
            dialog.add(timer.getUIElement());
            counter++;
        }

        dialog.setBackground(new Color(0, 0, 0, 0));

        //attach key listeners to timer binds
        GlobalScreen.addNativeKeyListener(new KeyInput(timers));
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

}
