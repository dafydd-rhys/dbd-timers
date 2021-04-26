package com.DBDTimer.main;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

/**
 * SettingsManager.java.
 * @version 1.0.2
 * This class simply creates a settings manager and opens it, the
 * user can make many changes here including changing his config, adding
 * timer and disabling timers etc.
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public class SettingsManager {

    /** unused elements of the form. */
    private JPanel mainPanel;
    /** unused elements of the form. */
    private JScrollPane survivorPane;
    /** unused elements of the form. */
    private JScrollPane killerPane;
    /** unused elements of the form. */
    private JPanel killerTimerList;
    /** unused elements of the form. */
    private JPanel survivorTimerList;
    /** unused elements of the form. */
    private JLabel restartBind;
    /** unused elements of the form. */
    private JLabel hideBind;
    /** the tab pain including all panels. */
    private JTabbedPane mainTabPain;
    /** the panel which shows all killer timers. */
    private JPanel survivorPanel;
    /** the panel which shows all survivor timers. */
    private JPanel killerPanel;
    /** the button to save the users custom settings. */
    private JButton saveCustomSettings;
    /** the settings the user has/wants to overwrite. */
    private static Settings settings;
    /** the combo box holding all the possible fonts. */
    private JComboBox<String> fontBox;
    /** the combo box holding all the possible font types. */
    private JComboBox<String> fontTypeBox;
    /** the combo box holding all the possible font sizes. */
    private JComboBox<Integer> fontSizeBox;
    /** the slider setting the icon size. */
    private JSlider iconSlider;
    /** the value of the icon size on the slider. */
    private JLabel sliderValue;
    /** add button on the killers timer list. */
    private JButton killerAddTimer;
    /** add button on the survivor timer list. */
    private JButton survAddTimer;
    /** combo box showing all possible restart bind buttons. */
    private JComboBox<Character> restartBindBox;
    /** combo box showing all possible hide bind buttons. */
    private JComboBox<Character> hideBindBox;
    private JButton chooseInactiveColour;
    private JLabel txtColor;
    /** the width of the frame. */
    private final int width = 450;
    /** the height of the frame. */
    private final int height = 550;
    private Color inactiveColor;

    /** this method creates a new settings manager form
     * with all the elements added to the panel.
     * @param main the instance of the main window
     */
    public SettingsManager(final Main main) {
        JFrame frame = new JFrame("Settings Manager");
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        frame.setContentPane(mainTabPain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                main.reloadTimers();
            }
        });
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        settings = loadSettings();
        final File folder = new File("timers\\");
        File[] listOfFiles = folder.listFiles();
        try {
            assert listOfFiles != null;
            populateLists(listOfFiles);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        populateComboBoxes();

        chooseInactiveColour.addActionListener(e -> {
            inactiveColor = JColorChooser.showDialog(null, "Pick a Color",
                    Color.BLACK);
            txtColor.setText("RGB: " + inactiveColor.getRed() + ", "
                    + inactiveColor.getGreen() + ", "
                    + inactiveColor.getBlue());
        });

        saveCustomSettings.addActionListener(e -> {
            try {
                FileWriter fw = new FileWriter("customization\\config.json");
                Gson g = new GsonBuilder().setPrettyPrinting().create();
                //this.settings = new Settings();
                assert settings != null;
                settings.setFont(Font.decode(Objects.requireNonNull(fontBox.
                        getSelectedItem()).toString()
                                + " " + Objects.requireNonNull(fontTypeBox.
                        getSelectedItem()).toString()
                                + " " + Objects.requireNonNull(fontSizeBox.
                        getSelectedItem()).toString()));
                settings.setIconSize(iconSlider.getValue());
                settings.setRestartBind(Objects.requireNonNull(restartBindBox.
                        getSelectedItem()).toString());
                settings.setHideBind(Objects.requireNonNull(hideBindBox.
                        getSelectedItem()).toString());
                settings.setInactiveColour(inactiveColor);
                fw.write(g.toJson(settings));
                fw.close();
                JOptionPane.showMessageDialog(null,
                        "Custom Settings Updated Successfully.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        iconSlider.addChangeListener(e -> sliderValue.setText(
                String.valueOf(iconSlider.getValue())));
    }

    /**
     * loads user settings from config.json.
     * @return returns the user settings
     */
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

    /**
     * this method simply populates the JLists with all the timers the user has.
     * @param folder the folder containing the timers
     * @throws Exception if the folder doesn't exist
     */
    private void populateLists(final File[] folder) throws Exception {
        killerPanel.setLayout(new BoxLayout(killerPanel, BoxLayout.Y_AXIS));
        killerPanel.setBackground(new Color(255, 255, 255));
        survivorPanel.setLayout(new BoxLayout(survivorPanel, BoxLayout.Y_AXIS));
        survivorPanel.setBackground(new Color(255, 255, 255));

        for (File file : folder) {
            String jsonString = Files.readString(Path.of(file.getPath()));
            Gson g = new Gson();
            TimerProperties timer = g.fromJson(
                    jsonString, TimerProperties.class);

            if (timer.getTimerMode() == TimerProperties.TimerMode.Killer) {
                createGraphic(file, killerPanel, timer);
            } else {
                createGraphic(file, survivorPanel, timer);
            }
        }
        killerAddTimer.addActionListener(e -> new EditTimer(null,
                "Add Timer", width, height));
        survAddTimer.addActionListener(e -> new EditTimer(null,
                "Add Timer", width, height));
    }

    /**
     * creates a graphic for each item in the list.
     * @param panel the panel in which the graphic is added to
     * @param timer the timer being added to the list
     * @param file the file the data was retrieved from
     */
    private void createGraphic(final File file,
                               final JPanel panel,
                               final TimerProperties timer) {
        ImageIcon icon = new ImageIcon(timer.getIcon());
        icon = convertImageSize(icon, 64);
        ImageIcon settingsIcon = new ImageIcon("images\\settings_cog.png");
        JButton openSettings = new JButton(convertImageSize(settingsIcon, 16));
        openSettings.setMaximumSize(new Dimension(16, 16));
        ImageIcon removeIcon = new ImageIcon("images\\remove_icon.png");
        JButton removeTimer = new JButton(convertImageSize(removeIcon, 12));
        removeTimer.setMaximumSize(new Dimension(16, 16));

        JPanel timerPanel = new JPanel();
        timerPanel.setLayout(new GridLayout(0, 4));
        timerPanel.setPreferredSize(new Dimension(panel.getWidth() - 17, 80));
        timerPanel.setMinimumSize(new Dimension(panel.getWidth() - 17, 80));
        timerPanel.setMaximumSize(new Dimension(panel.getWidth() - 17, 80));
        timerPanel.setBackground(new Color(255, 255, 255));
        timerPanel.add(new JLabel(icon));
        timerPanel.add(new JLabel(timer.getName()));
        timerPanel.add(openSettings);
        timerPanel.add(removeTimer);
        panel.add(timerPanel);

        openSettings.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                new EditTimer(timer, "Edit Timer", width, height);
            }
        });

        removeTimer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                int answer = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this timer?",
                        "Warning", JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    if (file.delete()) {
                        panel.remove(timerPanel);
                        panel.updateUI();
                        JOptionPane.showMessageDialog(null,
                                "Timer deleted successfully.");
                    }
                }
            }
        });
    }

    private ImageIcon convertImageSize(final ImageIcon image, final int size) {
        Image imageOfIcon = image.getImage();
        Image newImage = imageOfIcon.getScaledInstance(size, size,
                java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }

    /** populates all of the combo boxes used on the form. */
    private void populateComboBoxes() {
        populateFontBox();
        populateFontSizeBox();
        populateFontTypeBox();
        populateBindBox(restartBindBox);
        populateBindBox(hideBindBox);
        restartBindBox.setSelectedItem(settings.getRestartBind().charAt(0));
        hideBindBox.setSelectedItem(settings.getHideBind().charAt(0));
        iconSlider.setValue(settings.getIconSize());
        sliderValue.setText(String.valueOf(iconSlider.getValue()));
        inactiveColor = settings.getInactiveColour();
        txtColor.setText("RGB: " + inactiveColor.getRed() + ", "
                + inactiveColor.getGreen() + ", "
                + inactiveColor.getBlue());
    }

    /** populates the combo box including fonts. */
    private void populateFontBox() {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        Font[] allFonts = ge.getAllFonts();
        for (Font font : allFonts) {
            if (!font.getFontName(Locale.UK).contains("Dialog")) {
                fontBox.addItem(font.getFontName(Locale.UK));
            }
        }
        fontBox.setSelectedItem(settings.getFont().getFontName(Locale.UK));
    }

    /** populates the combo box including font sizes. */
    private void populateFontSizeBox() {
        fontSizeBox.addItem(16);
        fontSizeBox.addItem(24);
        fontSizeBox.addItem(32);
        fontSizeBox.setSelectedItem(settings.getFont().getSize());
    }

    /** populates the combo box including font types. */
    private void populateFontTypeBox() {
        fontTypeBox.addItem("Plain");
        fontTypeBox.addItem("Bold");
        fontTypeBox.addItem("Italic");
        fontTypeBox.addItem("BoldItalic");
        fontTypeBox.setSelectedIndex(settings.getFont().getStyle());
    }

    private void populateBindBox(final JComboBox<Character> box) {
        for (char c = 'A'; c <= 'Z'; ++c) {
            box.addItem(c);
        }
        for (char num = 48; num <= 57; ++num) {
            box.addItem(num);
        }
    }

    /**
     * retrieves the settings of the user.
     * @return returns the settings of the user
     */
    public static Settings getSettings() {
        return settings;
    }

    /**
     * sets the settings of the user.
     * @param newSettings replaces current settings with new settings
     */
    public static void setSettings(final Settings newSettings) {
        SettingsManager.settings = newSettings;
    }
}
