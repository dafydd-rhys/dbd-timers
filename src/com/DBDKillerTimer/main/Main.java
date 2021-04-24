package com.DBDKillerTimer.main;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import com.google.gson.Gson;
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
    int pX,pY;

    /** the size and location of the icon its text representation. */
    private static int iconSize = 0;

    private TimerClass.TimerMode timerMode = TimerClass.TimerMode.Survivor;
    private JDialog dialog;
    private ArrayList<Stopwatch> timers;

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
        // Get the logger for "https://github.com/kwhat/jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        GlobalScreen.registerNativeHook();
        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);

        //Generate dialog for UI
        dialog = new JDialog((java.awt.Dialog)null);
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
        //dialog.setPreferredSize(new Dimension(width, height));
        //dialog.setMaximumSize(new Dimension(width, height));
        //dialog.setMinimumSize(new Dimension(width, height));

        dialog.setDefaultCloseOperation(dialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.add(this);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        //Generate right click menu
        JPopupMenu popupMenu = generateRightClickMenu(dialog);
        dialog.add(popupMenu);

        loadTimers();

        dialog.setBackground(new Color(0, 0, 0, 0));
    }

    public void setTimerMode(TimerClass.TimerMode mode) {
        this.timerMode = mode;
    }

    /**
     * Load timers from JSON files
     */
    public void loadTimers() {
        final File folder = new File("timers\\");
        File[] listOfTimers = folder.listFiles();

        timers = new ArrayList<>();

        for (File file : listOfTimers) {
            try
            {
                //read timer info from json file
                String jsonString = Files.readString(Path.of(file.getPath()));
                Gson g = new Gson();
                TimerClass timerClass = g.fromJson(jsonString, TimerClass.class);

                //only add timer if mode matches current software timer mode
                if (timerClass.timerMode == this.timerMode)
                {
                    Stopwatch timer = new Stopwatch(iconSize, new ImageIcon(timerClass.icon),
                            timerClass.startTime, timerClass.startBind, 'r');

                    timers.add(timer);

                    //add timer to UI
                    dialog.add(timer.getUIElement());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        dialog.pack();

        //attach key listeners to timer binds
        GlobalScreen.addNativeKeyListener(new KeyInput(timers));
    }

    /**
     * Reload timers from JSON files
     */
    public void reloadTimers() {
        final File folder = new File("timers\\");
        File[] listOfTimers = folder.listFiles();

        //remove old timers
        for (Stopwatch timer : this.timers) {
            dialog.remove(timer.getUIElement());
        }

        timers = new ArrayList<>();

        for (File file : listOfTimers) {
            try
            {
                //read timer info from json file
                String jsonString = Files.readString(Path.of(file.getPath()));
                Gson g = new Gson();
                TimerClass timerClass = g.fromJson(jsonString, TimerClass.class);

                //only add timer if mode matches current software timer mode
                if (timerClass.timerMode == this.timerMode)
                {
                    Stopwatch timer = new Stopwatch(iconSize, new ImageIcon(timerClass.icon),
                            timerClass.startTime, timerClass.startBind, 'r');

                    timers.add(timer);

                    //add timer to UI
                    dialog.add(timer.getUIElement());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        dialog.pack();
        dialog.revalidate();
        dialog.repaint();

        //attach key listeners to timer binds
        GlobalScreen.addNativeKeyListener(new KeyInput(timers));
    }

    private JPopupMenu generateRightClickMenu(JDialog dialog) {
        JPopupMenu popupMenu = new JPopupMenu();

        //Settings
        JMenuItem settingsMenuItem = new JMenuItem("Settings");
        settingsMenuItem.addActionListener(e -> new LauncherForm());

        //Toggle Mode
        JMenuItem toggleModeMenuItem = new JMenuItem("Toggle Mode");
        toggleModeMenuItem.addActionListener(e -> {
            if (this.timerMode == TimerClass.TimerMode.Killer) {
                this.timerMode = TimerClass.TimerMode.Survivor;
            } else {
                this.timerMode = TimerClass.TimerMode.Killer;
            }
            reloadTimers();
        });

        //Exit
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));

        popupMenu.add(settingsMenuItem);
        popupMenu.add(toggleModeMenuItem);
        popupMenu.add(exitMenuItem);

        Container contentPane = dialog.getContentPane();
        contentPane.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    // Get x,y and store them
                    pX=me.getX();
                    pY=me.getY();
                }
            public void mouseReleased(MouseEvent me) {
                if(me.isPopupTrigger())
                    popupMenu.show(me.getComponent(), me.getX(), me.getY());
            }
        });
        contentPane.addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent me)
            {
                dialog.setLocation(dialog.getLocation().x+me.getX()-pX,dialog.getLocation().y+me.getY()-pY);
            }
        });

        return popupMenu;
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
