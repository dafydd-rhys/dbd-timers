package com.DBDKillerTimer.main;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import javax.swing.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LauncherForm {

    private final String path = "";

    private JPanel mainPanel;
    private JTabbedPane mainTabPain;
    private JPanel killerTimerList;
    private JPanel survivorTimerList;

    private JButton browseButton;
    private JTextField timerName;
    private JTextField timerStartTime;
    private JTextField timerStartColor;
    private JTextField timerType;
    private JTextField timerBind;
    private JTextField timerMode;
    private JTextField timerEnabled;
    private JButton saveTimer;

    private JComboBox<String> fontBox;
    private JComboBox<Integer> fontSizeBox;
    private JComboBox<Integer> fontTypeBox;
    private JButton saveCustomSettings;

    private JScrollPane survivorPane;
    private JScrollPane killerPane;
    private JPanel survivorPanel;
    private JPanel killerPanel;


    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Settings Manager");
        frame.setPreferredSize(new Dimension(390, 450));
        frame.setMaximumSize(new Dimension(390, 450));
        frame.setMinimumSize(new Dimension(390, 450));
        frame.setContentPane(new LauncherForm().mainTabPain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public LauncherForm() throws Exception {
        final File folder = new File("timers\\");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        populateLists(listOfFiles);
        populateComboBoxes();

        //appends config
        browseButton.addActionListener(e -> {
            //browse button code
        });

        //appends/creates new timer
        saveTimer.addActionListener(e -> {
            if (path != "") {
                try {
                    File myObj = new File("timers\\" + timerName.getText() + ".json");
                    FileWriter fw = new FileWriter(myObj);
                    fw.write("{\n" +
                            "\"name\": \"" + timerName.getText() + "\",\n" +
                            "\"start_time\": \"" + timerStartTime.getText() + "\",\n" +
                            "\"start_color\": \"" + timerStartColor.getText() + "\",\n" +
                            "\"timer_type\": \"" + timerType.getText() + "\",\n" +
                            "\"bind\": \"" + timerBind.getText() + "\",\n" +
                            "\"icon\": \"" + path + "\",\n" +
                            "\"mode\": \"" + timerMode.getText() + "\",\n" +
                            "\"enabled\": " + timerEnabled.getText() + "\n" +
                            "}");
                    fw.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        //appends config
        saveCustomSettings.addActionListener(e -> {
            try {
                FileWriter fw = new FileWriter("customization\\config.json");
                fw.write("{\n" +
                        "\"font\": \"" + Objects.requireNonNull(fontBox.getSelectedItem()).toString() + "\",\n" +
                        "\"font_size\": " + Objects.requireNonNull(fontSizeBox.getSelectedItem()).toString() + ",\n" +
                        "\"font_type\": " + Objects.requireNonNull(fontTypeBox.getSelectedItem()).toString() + "\n" +
                        "}");
                fw.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    private void populateLists(final File[] folder) throws Exception {
        killerPanel.setLayout(new GridLayout(0, 3, 5, 5));
        survivorPanel.setLayout(new GridLayout(0, 3, 5, 5));

        for (File file : folder) {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader(file));

            String name = (String) data.get("name");
            String location = (String) data.get("icon");
            ImageIcon icon = new ImageIcon(location);
            String mode = (String) data.get("mode");
            boolean enabled = (boolean) data.get("enabled");

            if (mode.equals("killer")) {
                createGraphic(killerPanel, icon, name, enabled);
            } else if (mode.equals("survivor")){
                createGraphic(survivorPanel, icon, name, enabled);
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
        panel.add(new Checkbox("Enabled", enabled));
    }

    private void populateComboBoxes() {
        populateFontBox();
        populateFontSizeBox();
        populateFontTypeBox();
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
    }

    private void populateFontSizeBox() {
        fontSizeBox.addItem(16);
        fontSizeBox.addItem(24);
        fontSizeBox.addItem(32);
    }

    private void populateFontTypeBox() {
        //0 = plain, 1 = bold, 2 = italic
        fontTypeBox.addItem(0);
        fontTypeBox.addItem(1);
        fontTypeBox.addItem(2);
    }

}
