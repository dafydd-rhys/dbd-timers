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

public class LauncherForm {

    private String iconPath = "";

    private JPanel mainPanel;
    private JTabbedPane mainTabPain;
    private JPanel killerTimerList;
    private JPanel survivorTimerList;

    private JButton browseButton;
    private JTextField timerName;
    private JTextField timerStartTime;
    private JComboBox<TimerClass.TimerType> timerTypeBox;
    private JComboBox<TimerClass.TimerMode> timerModeBox;
    private JTextField timerBind;
    private JCheckBox timerEnabled;
    private JButton saveTimer;
    private Color timerStartColor;

    private JComboBox<String> fontBox;
    private JComboBox<Integer> fontSizeBox;
    private JComboBox<String> fontTypeBox;
    private JButton saveCustomSettings;

    private JScrollPane survivorPane;
    private JScrollPane killerPane;
    private JPanel survivorPanel;
    private JPanel killerPanel;
    private JLabel txtPath;
    private JButton chooseColour;
    private JLabel txtColor;

    private final Settings settings;

    public LauncherForm() {
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

        final File folder = new File("timers\\");
        File[] listOfFiles = folder.listFiles();
        this.settings = loadSettings();
        try {
            assert listOfFiles != null;
            populateLists(listOfFiles);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        populateComboBoxes();

        //appends config
        browseButton.addActionListener(e -> {
            FileDialog fd = new FileDialog(new JFrame());
            fd.setVisible(true);
            
            File[] f = fd.getFiles();
            if(f.length > 0){
                iconPath = fd.getFiles()[0].getAbsolutePath();
                txtPath.setText(fd.getFiles()[0].getName());
            }
        });

        //lets you choose colour
        chooseColour.addActionListener(e -> {
            timerStartColor = JColorChooser.showDialog(null, "Pick a Color", Color.BLACK);
            if (timerStartColor != null) {
                txtColor.setText("RGB: " + timerStartColor.getRed() + ", " + timerStartColor.getGreen()
                        + ", " + timerStartColor.getBlue());
            }
        });

        //appends/creates new timer
        saveTimer.addActionListener(e -> {
            if (!iconPath.equals("")) {
                try {
                    FileWriter fw = new FileWriter("timers\\" + timerName.getText() + ".json");
                    Gson g = new Gson();

                    TimerClass newTimer = new TimerClass();
                    newTimer.name = timerName.getText();
                    newTimer.startTime = Integer.parseInt(timerStartTime.getText());
                    newTimer.startColor = timerStartColor;
                    newTimer.timerType = (TimerClass.TimerType)timerTypeBox.getSelectedItem();
                    newTimer.icon = iconPath;
                    newTimer.timerMode = (TimerClass.TimerMode)timerModeBox.getSelectedItem();
                    newTimer.enabled = timerEnabled.isSelected();
                    newTimer.startBind = timerBind.getText();

                    fw.write(g.toJson(newTimer));
                    fw.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //appends config
        saveCustomSettings.addActionListener(e -> {
            try {
                FileWriter fw = new FileWriter("customization\\config.json");
                Gson g = new Gson();
                //this.settings = new Settings();
                assert this.settings != null;
                this.settings.font = Font.decode(
                        Objects.requireNonNull(fontBox.getSelectedItem()).toString()
                                + " " + Objects.requireNonNull(fontTypeBox.getSelectedItem()).toString()
                                + " " + Objects.requireNonNull(fontSizeBox.getSelectedItem()).toString());

                fw.write(g.toJson(this.settings));
                fw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
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

    private void populateLists(final File[] folder) throws Exception {
        killerPanel.setLayout(new GridLayout(0, 3, 5, 5));
        survivorPanel.setLayout(new GridLayout(0, 3, 5, 5));

        for (File file : folder) {
            String jsonString = Files.readString(Path.of(file.getPath()));
            Gson g = new Gson();
            TimerClass timer = g.fromJson(jsonString, TimerClass.class);

            if (timer.timerMode == TimerClass.TimerMode.Killer) {
                createGraphic(killerPanel, new ImageIcon(timer.icon), timer.name, timer.enabled);
            } else {
                createGraphic(survivorPanel, new ImageIcon(timer.icon), timer.name, timer.enabled);
            }
        }
    }

    private void createGraphic(JPanel panel, ImageIcon icon, String name, boolean enabled) {
        //resize image
        Image image = icon.getImage();
        Image newImg = image.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH);
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

    private void populateComboBoxes() {
        populateFontBox();
        populateFontSizeBox();
        populateFontTypeBox();
        populateTimerTypeBox();
        populateTimerModeBox();
    }

    private void populateFontBox() {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        Font[] allFonts = ge.getAllFonts();
        for (Font font : allFonts) {
            if (!font.getFontName(Locale.UK).contains("Dialog")) {
                fontBox.addItem(font.getFontName(Locale.UK));
            }
        }
        fontBox.setSelectedItem(this.settings.font.getFontName(Locale.UK));
    }

    private void populateFontSizeBox() {
        fontSizeBox.addItem(16);
        fontSizeBox.addItem(24);
        fontSizeBox.addItem(32);
        fontSizeBox.setSelectedItem(this.settings.font.getSize());
    }

    private void populateFontTypeBox() {
        fontTypeBox.addItem("Plain");
        fontTypeBox.addItem("Bold");
        fontTypeBox.addItem("Italic");
        fontTypeBox.addItem("BoldItalic");
        fontTypeBox.setSelectedIndex(this.settings.font.getStyle());
    }

    private void populateTimerTypeBox() {
        timerTypeBox.addItem(TimerClass.TimerType.CountUp);
        timerTypeBox.addItem(TimerClass.TimerType.CountDown);
    }

    private void populateTimerModeBox() {
        timerModeBox.addItem(TimerClass.TimerMode.Killer);
        timerModeBox.addItem(TimerClass.TimerMode.Survivor);
    }

}
