package com.DBDKillerTimer.main;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
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
 * @version 1.0.1
 * This class simply implements and manipulates a JDialog, implementing
 * its features which includes timers, icons and other features.
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public final class Main extends Canvas {

    /** this is where the JDialog properties are stored.*/
    private final int[] dialogProperties = new int[3];
    /** gets current X location of mouse. */
    private int pointerX;
    /** gets current Y location of mouse. */
    private int pointerY;
    /** the window in which the icons and timers are outputted. */
    private final JDialog dialog;
    /** the size of the icon being displayed. */
    private static int iconSize = 0;
    /** the mode in which the user is playing, be default is survivor. */
    private TimerProperties.TimerMode timerMode = TimerProperties.TimerMode.Survivor;
    /** where all the timers are stored. */
    private ArrayList<IconTimer> timers;

    /**
     * This method creates a new instance of program.
     * @param args the arguments used in methods
     * @throws FileNotFoundException if the properties file doesn't exist
     * @throws NativeHookException there's an issue reading global key presses
     */
    public static void main(final String[] args) throws
            FileNotFoundException, NativeHookException {
        new Main();
    }

    /**
     * This method creates the UI and implements all of its features,
     * this includes key listener, icons, timers etc.
     * @throws FileNotFoundException if the properties file doesn't exist
     * @throws NativeHookException there's an issue reading global key presses
     */
    private Main() throws FileNotFoundException, NativeHookException {
        //the logger: "https://github.com/kwhat/jnativehook".
        Logger logger = Logger.getLogger(GlobalScreen.class.
                getPackage().getName());
        logger.setLevel(Level.OFF);
        GlobalScreen.registerNativeHook();
        logger.setUseParentHandlers(false);

        //Generate dialog for UI and sets the applications logo in taskbar
        ImageIcon logo = new ImageIcon("images\\icon.png");
        dialog = new JDialog((java.awt.Dialog) null);
        dialog.setTitle("DBD Timer");
        dialog.setIconImage(logo.getImage());

        //Catch window close event and shutdown process
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                System.exit(0);
            }
        });

        getProperties();
        dialog.setUndecorated(true);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(dialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.add(this);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        //creates menu for if user right clicks on program
        JPopupMenu popupMenu = generateRightClickMenu(dialog);
        dialog.add(popupMenu);
        loadTimers();
        dialog.setBackground(new Color(0, 0, 0, 0));
    }

    /**
     * This method simple loads all timers from the .json file,
     * if the timer is disabled it isn't added to the dialog.
     */
    public void loadTimers() {
        timers = new ArrayList<>();
        final File folder = new File("timers\\");
        File[] listOfTimers = folder.listFiles();
        assert listOfTimers != null;

        for (File file : listOfTimers) {
            try {
                String jsonString = Files.readString(Path.of(file.getPath()));
                Gson g = new Gson();
                TimerProperties properties = g.fromJson(jsonString, TimerProperties.class);

                if (properties.getTimerMode() == this.timerMode) {
                    IconTimer timer = new IconTimer(iconSize, new ImageIcon(properties.getIcon()),
                            properties.getStartTime(), properties.getStartBind(), "R", "H");
                    timers.add(timer);
                    dialog.add(timer.getUIElement());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        dialog.pack();
        GlobalScreen.addNativeKeyListener(new KeyInput(timers));
    }

    /**
     * This method simple reloads all timers from the .json file,
     * if the timer is disabled it isn't added to the dialog.
     */
    public void reloadTimers() {
        final File folder = new File("timers\\");
        File[] listOfTimers = folder.listFiles();
        assert listOfTimers != null;

        for (IconTimer timer : this.timers) {
            dialog.remove(timer.getUIElement());
        }
        timers = new ArrayList<>();

        for (File file : listOfTimers) {
            try {
                String jsonString = Files.readString(Path.of(file.getPath()));
                Gson g = new Gson();
                TimerProperties timerProperties = g.fromJson(jsonString, TimerProperties.class);

                if (timerProperties.getTimerMode() == this.timerMode) {
                    IconTimer timer = new IconTimer(iconSize, new ImageIcon(timerProperties.getIcon()),
                            timerProperties.getStartTime(), timerProperties.getStartBind(), "R", "H");
                    timers.add(timer);
                    dialog.add(timer.getUIElement());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        dialog.pack();
        dialog.revalidate();
        dialog.repaint();
        GlobalScreen.addNativeKeyListener(new KeyInput(timers));
    }

    /**
     * this method simply generates a right click menu, if the dialog is
     * right-clicked it displays the menu.
     * @param currDialog refers to this dialog
     * @return displays the pop up menu
     */
    private JPopupMenu generateRightClickMenu(final JDialog currDialog) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem settingsMenuItem = new JMenuItem("Settings");
        settingsMenuItem.addActionListener(e -> new SettingsManager());

        //toggle
        JMenuItem toggleModeMenuItem = new JMenuItem("Toggle Mode");
        toggleModeMenuItem.addActionListener(e -> {
            if (this.timerMode == TimerProperties.TimerMode.Killer) {
                this.timerMode = TimerProperties.TimerMode.Survivor;
            } else {
                this.timerMode = TimerProperties.TimerMode.Killer;
            }
            reloadTimers();
        });

        //exit
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        popupMenu.add(settingsMenuItem);
        popupMenu.add(toggleModeMenuItem);
        popupMenu.add(exitMenuItem);

        Container contentPane = currDialog.getContentPane();
        contentPane.addMouseListener(new MouseAdapter() {
            public void mousePressed(final MouseEvent me) {
                pointerX = me.getX();
                pointerY = me.getY();
            }
            public void mouseReleased(final MouseEvent me) {
                if (me.isPopupTrigger()) {
                    popupMenu.show(me.getComponent(), me.getX(), me.getY());
                }
            }
        });
        contentPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(final MouseEvent me) {
                currDialog.setLocation(currDialog.getLocation().x + me.getX() - pointerX,
                        currDialog.getLocation().y + me.getY() - pointerY);
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
            dialogProperties[count] = file.nextInt();
            count++;
        }

        int width = dialogProperties[0];
        int height = dialogProperties[1];
        iconSize = dialogProperties[2];
        if (width < 800 || width > 1920
                || height > 1080 || height < 600) {
            System.exit(0);
        }
    }

    /**
     * this method simply sets the mode in which the user
     * is playing, this will be killer or survivor.
     * @param mode the mode which the user is playing.
     */
    public void setTimerMode(final TimerProperties.TimerMode mode) {
        this.timerMode = mode;
    }
}
