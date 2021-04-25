package com.DBDKillerTimer.main;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import javax.swing.*;
import com.google.gson.Gson;

/**
 * SettingsManager.java.
 * @version 1.0.1
 * This class simply creates a settings manager and opens it, the
 * user can make many changes here including changing his config, adding
 * timer and disabling timers etc.
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public class SettingsManager {

    /** unused elements of the form. */
    private JPanel mainPanel;
    private JScrollPane survivorPane;
    private JScrollPane killerPane;
    private JPanel killerTimerList;
    private JPanel survivorTimerList;

    /** the tab pain including all panels. */
    private JTabbedPane mainTabPain;
    /** the panel which shows all killer timers. */
    private JPanel survivorPanel;
    /** the panel which shows all survivor timers. */
    private JPanel killerPanel;
    /** the button to save the users custom settings. */
    private JButton saveCustomSettings;
    /** the settings the user has/wants to overwrite. */
    public static Settings settings;
    /** the combo box holding all the possible fonts. */
    private JComboBox<String> fontBox;
    /** the combo box holding all the possible font types. */
    private JComboBox<String> fontTypeBox;
    /** the combo box holding all the possible font sizes. */
    private JComboBox<Integer> fontSizeBox;
    private JSlider iconSlider;
    private JLabel sliderValue;
    private JButton killerAddTimer;
    private JButton survAddTimer;
    private JLabel restartBind;
    private JLabel hideBind;
    private JComboBox<Character> restartBindBox;
    private JComboBox<Character> hideBindBox;

    /** this method creates a new settings manager form
     * with all the elements added to the panel.
     */
    public SettingsManager() {
        JFrame frame = new JFrame("Settings Manager");
        frame.setPreferredSize(new Dimension(390, 450));
        frame.setMaximumSize(new Dimension(390, 450));
        frame.setMinimumSize(new Dimension(390, 450));
        frame.setContentPane(mainTabPain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        //appends config
        saveCustomSettings.addActionListener(e -> {
            try {
                FileWriter fw = new FileWriter("customization\\config.json");
                Gson g = new Gson();
                //this.settings = new Settings();
                assert settings != null;
                settings.setFont(Font.decode(Objects.requireNonNull(fontBox.getSelectedItem()).toString()
                                + " " + Objects.requireNonNull(fontTypeBox.getSelectedItem()).toString()
                                + " " + Objects.requireNonNull(fontSizeBox.getSelectedItem()).toString()));
                settings.iconSize = iconSlider.getValue();
                settings.restartBind = Objects.requireNonNull(restartBindBox.getSelectedItem()).toString();
                settings.hideBind = Objects.requireNonNull(hideBindBox.getSelectedItem()).toString();
                fw.write(g.toJson(settings));
                fw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        iconSlider.addChangeListener(e -> sliderValue.setText(String.valueOf(iconSlider.getValue())));
    }

    /**
     * loads user settings from config.json
     * @return returns the user settings
     */
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

    /**
     * this method simply populates the JLists with all the timers the user has.
     * @param folder the folder containing the timers
     * @throws Exception if the folder doesn't exist
     */
    private void populateLists(final File[] folder) throws Exception {
        killerPanel.setLayout(new GridLayout(0, 3, 0, 5));
        survivorPanel.setLayout(new GridLayout(0, 3, 0, 5));

        for (File file : folder) {
            String jsonString = Files.readString(Path.of(file.getPath()));
            Gson g = new Gson();
            TimerProperties timer = g.fromJson(jsonString, TimerProperties.class);

            if (timer.getTimerMode() == TimerProperties.TimerMode.Killer) {
                createGraphic(killerPanel, timer);
            } else {
                createGraphic(survivorPanel, timer);
            }
        }
        killerAddTimer.addActionListener(e -> new EditTimer(null, "Add Timer"));
        survAddTimer.addActionListener(e -> new EditTimer(null, "Add Timer"));
    }

    /**
     * creates a graphic for each item in the list.
     * @param panel the panel in which the graphic is added to
     * @param timer the timer being added to the list
     */
    private void createGraphic(final JPanel panel, TimerProperties timer) {
        ImageIcon icon = new ImageIcon(timer.getIcon());
        Image image = icon.getImage();
        Image newImg = image.getScaledInstance(64, 64,
                java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);

        ImageIcon settings = new ImageIcon("images\\settings_cog.png");
        Image settingsCog = settings.getImage();
        Image newSettingsCog = settingsCog.getScaledInstance(16, 16,
                java.awt.Image.SCALE_SMOOTH);
        settings = new ImageIcon(newSettingsCog);
        JLabel openSettings = new JLabel(settings);

        panel.add(new JLabel(icon));
        panel.add(new JLabel(timer.getName()));
        panel.add(openSettings);

        openSettings.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new EditTimer(timer, "Edit Timer");
            }
        });
    }

    /** populates all of the combo boxes used on the form. */
    private void populateComboBoxes() {
        populateFontBox();
        populateFontSizeBox();
        populateFontTypeBox();
        populateBindBox(restartBindBox);
        populateBindBox(hideBindBox);
        restartBindBox.setSelectedItem(settings.restartBind.charAt(0));
        hideBindBox.setSelectedItem(settings.hideBind.charAt(0));
        iconSlider.setValue(settings.iconSize);
        sliderValue.setText(String.valueOf(iconSlider.getValue()));
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

    private void populateBindBox(JComboBox<Character> box) {
        for (char c = 'A'; c <= 'Z'; ++c) {
            box.addItem(c);
        }
        for (char num = 48; num <= 57; ++num) {
            box.addItem(num);
        }
    }

}
