package com.DBDKillerTimer.main;
import java.awt.*;
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
    /** the path of the icons image. */
    private String iconPath = "";
    /** the TextField where the timers name is entered. */
    private JTextField timerName;
    /** the combo box including all timer types. */
    private JComboBox<TimerProperties.TimerType> timerTypeBox;
    /** the combo box including all timers modes. */
    private JComboBox<TimerProperties.TimerMode> timerModeBox;
    /** the combo box including all possible starting times. */
    private JComboBox<Integer> startTime;
    /** the combo box including all the possible binds. */
    private JComboBox<Character> startBind;
    /** the starting color of the timer. */
    private Color timerStartColor;
    /** the button used to point to image. */
    private JButton browseButton;
    /** the button used to open a colour chooser. */
    private JButton chooseColour;
    /** the button to save the timer. */
    private JButton saveTimer;
    /** the button to save the users custom settings. */
    private JButton saveCustomSettings;
    /** the path of the icons image. */
    private JCheckBox timerEnabled;
    /** the label which shows the path of the selected image. */
    private JLabel txtPath;
    /** the label which shows the color selected by the user. */
    private JLabel txtColor;
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

        this.settings = loadSettings();
        final File folder = new File("timers\\");
        File[] listOfFiles = folder.listFiles();
        try {
            assert listOfFiles != null;
            populateLists(listOfFiles);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        populateComboBoxes();

        //allows you to browse for image
        browseButton.addActionListener(e -> {
            FileDialog fd = new FileDialog(new JFrame());
            fd.setVisible(true);
            File[] f = fd.getFiles();
            if (f.length > 0) {
                iconPath = fd.getFiles()[0].getAbsolutePath();
                txtPath.setText(fd.getFiles()[0].getName());
            }
        });

        //lets you choose colour
        chooseColour.addActionListener(e -> {
            timerStartColor = JColorChooser.showDialog(null, "Pick a Color",
                    Color.BLACK);
            if (timerStartColor != null) {
                txtColor.setText("RGB: " + timerStartColor.getRed() + ", "
                        + timerStartColor.getGreen() + ", "
                        + timerStartColor.getBlue());
            }
        });

        //appends/creates new timer
        saveTimer.addActionListener(e -> {
            if (!iconPath.equals("") && timerStartColor != null) {
                try {
                    FileWriter fw = new FileWriter("timers\\"
                            + timerName.getText() + ".json");
                    Gson g = new Gson();

                    TimerProperties newTimer = new TimerProperties();
                    newTimer.setName(timerName.getText());
                    newTimer.setStartColor(timerStartColor);
                    newTimer.setTimerType((TimerProperties.TimerType) timerTypeBox.getSelectedItem());
                    if (newTimer.getTimerType() == TimerProperties.TimerType.CountUp) {
                        newTimer.setStartTime(0);
                    } else {
                        newTimer.setStartTime((int) startTime.getSelectedItem());
                    }
                    newTimer.setIcon(iconPath);
                    newTimer.setTimerMode((TimerProperties.TimerMode) timerModeBox.getSelectedItem());
                    newTimer.setEnabled(timerEnabled.isSelected());
                    newTimer.setStartBind(Objects.requireNonNull(startBind.getSelectedItem()).toString());

                    fw.write(g.toJson(newTimer));
                    fw.close();
                    JOptionPane.showMessageDialog(null, "Timer created.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "cant create timer.");
            }
        });

        //appends config
        saveCustomSettings.addActionListener(e -> {
            try {
                FileWriter fw = new FileWriter("customization\\config.json");
                Gson g = new Gson();
                //this.settings = new Settings();
                assert this.settings != null;
                this.settings.setFont(Font.decode(Objects.requireNonNull(fontBox.getSelectedItem()).toString()
                                + " " + Objects.requireNonNull(fontTypeBox.getSelectedItem()).toString()
                                + " " + Objects.requireNonNull(fontSizeBox.getSelectedItem()).toString()));
                this.settings.iconSize = iconSlider.getValue();
                fw.write(g.toJson(this.settings));
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
        killerPanel.setLayout(new GridLayout(0, 3, 5, 5));
        survivorPanel.setLayout(new GridLayout(0, 3, 5, 5));

        for (File file : folder) {
            String jsonString = Files.readString(Path.of(file.getPath()));
            Gson g = new Gson();
            TimerProperties timer = g.fromJson(jsonString, TimerProperties.class);

            if (timer.getTimerMode() == TimerProperties.TimerMode.Killer) {
                createGraphic(killerPanel, new ImageIcon(timer.getIcon()),
                        timer.getName(), timer.isEnabled());
            } else {
                createGraphic(survivorPanel, new ImageIcon(timer.getIcon()),
                        timer.getName(), timer.isEnabled());
            }
        }
    }

    /**
     * creates a graphic for each item in the list.
     * @param panel the panel in which the graphic is added to
     * @param icon the image representing the icon
     * @param name the name of the icon/timer
     * @param enabled whether the timer is enabled
     */
    private void createGraphic(final JPanel panel, ImageIcon icon,
                               final String name, final boolean enabled) {
        Image image = icon.getImage();
        Image newImg = image.getScaledInstance(64, 64,
                java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        panel.add(new JLabel(icon));
        panel.add(new JLabel(name));
        Checkbox box = new Checkbox("Enabled", enabled);
        panel.add(box);

        box.addItemListener(e -> {
            boolean wanted = box.getState();
            if (wanted) {
                //edit json to true
            } else {
                //edit json to false
            }
        });
    }

    /** populates all of the combo boxes used on the form. */
    private void populateComboBoxes() {
        populateFontBox();
        populateFontSizeBox();
        populateFontTypeBox();
        populateTimerTypeBox();
        populateTimerModeBox();
        populateStartBind();
        populateStartTime();
        iconSlider.setValue(this.settings.iconSize);
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
        fontBox.setSelectedItem(this.settings.getFont().getFontName(Locale.UK));
    }

    /** populates the combo box including font sizes. */
    private void populateFontSizeBox() {
        fontSizeBox.addItem(16);
        fontSizeBox.addItem(24);
        fontSizeBox.addItem(32);
        fontSizeBox.setSelectedItem(this.settings.getFont().getSize());
    }

    /** populates the combo box including font types. */
    private void populateFontTypeBox() {
        fontTypeBox.addItem("Plain");
        fontTypeBox.addItem("Bold");
        fontTypeBox.addItem("Italic");
        fontTypeBox.addItem("BoldItalic");
        fontTypeBox.setSelectedIndex(this.settings.getFont().getStyle());
    }

    /** populates the combo box including timer types. */
    private void populateTimerTypeBox() {
        timerTypeBox.addItem(TimerProperties.TimerType.CountUp);
        timerTypeBox.addItem(TimerProperties.TimerType.CountDown);
        startTime.setEnabled(false);
        timerTypeBox.addActionListener(e -> {
            //disable start time if timer type is count up
            startTime.setEnabled(Objects.requireNonNull(timerTypeBox.
                    getSelectedItem()).toString().equals("CountDown"));
        });
    }

    /** populates the combo box including timer modes. */
    private void populateTimerModeBox() {
        timerModeBox.addItem(TimerProperties.TimerMode.Killer);
        timerModeBox.addItem(TimerProperties.TimerMode.Survivor);
    }

    /** populates the combo box including start timer binds. */
    private void populateStartBind() {
        for (char c = 'A'; c <= 'Z'; c++) {
            startBind.addItem(c);
        }
        for (char num = 48; num <= 57; num++) {
            startBind.addItem(num);
        }
    }

    /** populates the combo box including timer start times. */
    private void populateStartTime() {
        for (int num = 1; num <= 180; ++num) {
            startTime.addItem(num);
        }
    }

}
