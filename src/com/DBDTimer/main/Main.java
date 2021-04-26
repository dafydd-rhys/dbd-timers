package com.DBDTimer.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.awt.FlowLayout;
import java.awt.Canvas;
import java.awt.SystemTray;
import java.awt.PopupMenu;
import java.awt.MenuItem;
import java.awt.Image;
import java.awt.TrayIcon;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main.java.
 * @version 1.0.3
 * This class simply implements and manipulates a JDialog, implementing
 * its features which includes timers, icons and other features.
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public final class Main extends Canvas {

    /** x position for click/drag tracking. */
    private int pointerX;
    /** y position for click/drag tracking. */
    private int pointerY;
    /** x&y positions used to get window position. */
    private final int[] windowPosition = new int[2];
    /** the dialog where all visuals will be outputted. */
    private final JDialog dialog;
    /** the mode in which the user is playing, the default is survivor. */
    private TimerProperties.TimerMode timerMode = TimerProperties.
            TimerMode.Survivor;
    /** where all the timers are stored. */
    private ArrayList<IconTimer> timers;

    /**
     * This method creates a new instance of program.
     * @param args the arguments used in methods
     * @throws NativeHookException there's an issue reading global key presses
     */
    public static void main(final String[] args) throws NativeHookException {
        new Main();
    }

    /**
     * This method creates the UI and implements all of its features,
     * this includes key listener, icons, timers etc.
     * @throws NativeHookException there's an issue reading global key presses
     */
    private Main() throws NativeHookException {
        //the logger: "https://github.com/kwhat/jnativehook".
        Logger logger = Logger.getLogger(GlobalScreen.class.
                getPackage().getName());
        logger.setLevel(Level.OFF);
        GlobalScreen.registerNativeHook();
        logger.setUseParentHandlers(false);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException
                | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        SettingsManager.setSettings(loadSettings());

        //Generate dialog for UI and sets the applications logo in taskbar
        ImageIcon logo = new ImageIcon("images\\icon.png");
        dialog = new JDialog((java.awt.Dialog) null);
        dialog.setTitle("DBD Timer");
        dialog.setIconImage(logo.getImage());

        //Catch window close event and shutdown process
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                assert SettingsManager.getSettings() != null;
                SettingsManager.getSettings().setWindowPosition(windowPosition);
                try {
                    FileWriter fw = new FileWriter(
                            "customization\\config.json");
                    Gson g = new GsonBuilder().setPrettyPrinting().create();
                    fw.write(g.toJson(SettingsManager.getSettings()));
                    fw.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });

        dialog.setUndecorated(true);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(dialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.add(this);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        assert SettingsManager.getSettings() != null;
        dialog.setLocation(SettingsManager.getSettings().getWindowPosition()[0],
                SettingsManager.getSettings().getWindowPosition()[1]);
        this.windowPosition[0] = SettingsManager.getSettings().
                getWindowPosition()[0];
        this.windowPosition[1] = SettingsManager.getSettings().
                getWindowPosition()[1];

        GlobalScreen.addNativeKeyListener(new NativeKeyAdapter() {
            @Override
            public void nativeKeyPressed(final NativeKeyEvent e) {
                if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase(
                        SettingsManager.getSettings().getHideBind())) {
                    dialog.setVisible(!dialog.isVisible());
                }
            }
        });

        if (SystemTray.isSupported()) {
            //settings and toggle mode
            PopupMenu popupMenu = new PopupMenu();
            MenuItem settingsMenuItem = new MenuItem("Settings");
            settingsMenuItem.addActionListener(e -> new SettingsManager(this));
            MenuItem toggleModeMenuItem = new MenuItem("Toggle Mode");

            toggleModeMenuItem.addActionListener(e -> {
                if (this.timerMode == TimerProperties.TimerMode.Killer) {
                    this.timerMode = TimerProperties.TimerMode.Survivor;
                } else {
                    this.timerMode = TimerProperties.TimerMode.Killer;
                }
                reloadTimers();
            });

            //exit
            MenuItem exitMenuItem = new MenuItem("Exit");
            exitMenuItem.addActionListener(e -> dialog.dispose());
            popupMenu.add(settingsMenuItem);
            popupMenu.add(toggleModeMenuItem);
            popupMenu.add(exitMenuItem);

            //create system tray icon
            Image image = logo.getImage();
            Image newImg = image.getScaledInstance(SystemTray.getSystemTray().
                            getTrayIconSize().width, SystemTray.getSystemTray().
                            getTrayIconSize().height,
                    java.awt.Image.SCALE_SMOOTH);
            TrayIcon trayIcon = new TrayIcon(newImg, "DBD Timer", popupMenu);
            SystemTray tray = SystemTray.getSystemTray();

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }

        //creates menu for if user right clicks on program
        JPopupMenu popupMenu = generateRightClickMenu(dialog);
        dialog.add(popupMenu);
        loadTimers();
        dialog.setBackground(new Color(0, 0, 0, 0));
    }

    private Settings loadSettings() {
        try {
            String jsonString = Files.readString(Path.of(
                    "customization\\config.json"));
            Gson g = new Gson();
            return g.fromJson(jsonString, Settings.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /** sets timer mode.
     * @param mode the mode the user is playing in
     */
    public void setTimerMode(final TimerProperties.TimerMode mode) {
        this.timerMode = mode;
    }

    /**
     * This method simple loads all timers from the .json file,
     * if the timer is disabled it isn't added to the dialog.
     */
    public void loadTimers() {
        timers = new ArrayList<>();
        loadTimersFromJSON(getTimerJSONFiles());
        dialog.pack();
        GlobalScreen.addNativeKeyListener(new KeyInput(timers));
    }

    /**
     * This method simple reloads all timers from the .json file,
     * if the timer is disabled it isn't added to the dialog.
     */
    public void reloadTimers() {
        for (IconTimer timer : this.timers) {
            dialog.remove(timer.getUIElement());
        }

        timers = new ArrayList<>();
        loadTimersFromJSON(getTimerJSONFiles());
        dialog.pack();
        dialog.revalidate();
        dialog.repaint();
        GlobalScreen.addNativeKeyListener(new KeyInput(timers));
    }

    /**
     * Load timers from JSON files.
     * @param jsonFiles array of JSON files
     */
    private void loadTimersFromJSON(final File[] jsonFiles) {
        for (File file : jsonFiles) {
            try {
                String jsonString = Files.readString(Path.of(file.getPath()));
                Gson g = new Gson();
                TimerProperties properties = g.fromJson(jsonString,
                        TimerProperties.class);

                if (properties.getTimerMode() == this.timerMode
                        && properties.isEnabled()) {
                    IconTimer timer = new IconTimer(properties);
                    timers.add(timer);
                    dialog.add(timer.getUIElement());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * get list of JSON files from timer folder.
     * @return File array of JSON files
     */
    private File[] getTimerJSONFiles() {
        final File folder = new File("timers\\");
        return folder.listFiles();
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
        settingsMenuItem.addActionListener(e -> new SettingsManager(this));

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
        exitMenuItem.addActionListener(e -> dialog.dispose());
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
                windowPosition[0] = dialog.getLocation().x;
                windowPosition[1] = dialog.getLocation().y;
            }
        });
        contentPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(final MouseEvent me) {
                currDialog.setLocation(currDialog.getLocation().x
                        + me.getX() - pointerX, currDialog.getLocation().y
                        + me.getY() - pointerY);
            }
        });
        return popupMenu;
    }

}
