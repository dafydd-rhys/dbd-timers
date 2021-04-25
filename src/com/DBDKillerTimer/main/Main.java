package com.DBDKillerTimer.main;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.google.gson.Gson;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Main.java.
 * @version 1.0.1
 * This class simply implements and manipulates a JDialog, implementing
 * its features which includes timers, icons and other features.
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public final class Main extends Canvas {

    /** x&y position for click/drag tracking */
    int pointerX,pointerY;
    int[] windowPosition = new int[2];

    private final JDialog dialog;
    /** the mode in which the user is playing, the default is survivor. */
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

        SettingsManager.settings = loadSettings();

        //Generate dialog for UI and sets the applications logo in taskbar
        ImageIcon logo = new ImageIcon("images\\icon.png");
        dialog = new JDialog((java.awt.Dialog) null);
        dialog.setTitle("DBD Timer");
        dialog.setIconImage(logo.getImage());

        //Catch window close event and shutdown process
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                assert SettingsManager.settings != null;
                SettingsManager.settings.windowPosition = windowPosition;
                try {
                    FileWriter fw = new FileWriter("customization\\config.json");
                    Gson g = new Gson();
                    fw.write(g.toJson(SettingsManager.settings));
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
        assert SettingsManager.settings != null;
        dialog.setLocation(SettingsManager.settings.windowPosition[0], SettingsManager.settings.windowPosition[1]);

        GlobalScreen.addNativeKeyListener(new NativeKeyAdapter() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e)
            {
                if(NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase().equals("h")) {
                    dialog.setVisible(!dialog.isVisible());
                }
            }
        });

        //creates menu for if user right clicks on program
        JPopupMenu popupMenu = generateRightClickMenu(dialog);
        dialog.add(popupMenu);
        loadTimers();
        dialog.setBackground(new Color(0, 0, 0, 0));
    }

    private Settings loadSettings() {
        try {
            String jsonString = Files.readString(Path.of("customization\\config.json"));
            Gson g = new Gson();
            return g.fromJson(jsonString, Settings.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void setTimerMode(TimerProperties.TimerMode mode) {
        this.timerMode = mode;
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
                    IconTimer timer = new IconTimer(properties);
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
                    IconTimer timer = new IconTimer(timerProperties);
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
        exitMenuItem.addActionListener(e -> dialog.dispose());

        popupMenu.add(settingsMenuItem);
        popupMenu.add(toggleModeMenuItem);
        popupMenu.add(exitMenuItem);

        Container contentPane = currDialog.getContentPane();
        contentPane.addMouseListener(new MouseAdapter() {
            public void mousePressed(final MouseEvent me)
            {
                pointerX = me.getX();
                pointerY = me.getY();
            }

            public void mouseReleased(final MouseEvent me) {
                if (me.isPopupTrigger())
                {
                    popupMenu.show(me.getComponent(), me.getX(), me.getY());
                }
                windowPosition[0] = dialog.getLocation().x;
                windowPosition[1] = dialog.getLocation().y;
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
    };

}
