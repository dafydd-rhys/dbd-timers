package com.DBDKillerTimer.main;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.util.Locale;
import javax.swing.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LauncherForm {

    private JPanel mainPanel;
    private JTabbedPane mainTabPain;
    private JPanel killerTimerList;
    private JPanel survivorTimerList;
    private JButton browseButton;
    private JTextField clockName;
    private JButton saveButton;
    private JTextField changeTime;
    private JTextField iconColor;
    private JTextField startTime;
    private JTextField clockBind;
    private JTextField clockType;
    private JLabel clockIcon;
    private JComboBox<String> fontBox;
    private JComboBox<Integer> fontSizeBox;
    private JComboBox<Color> fontColourBox;
    private JComboBox<Integer> fontTypeBox;
    private JButton saveCustom;


    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Settings Manager");

        frame.setPreferredSize(new Dimension(390, 450));
        frame.setMaximumSize(new Dimension(390, 450));
        frame.setMinimumSize(new Dimension(390, 450));
        frame.setContentPane(new LauncherForm(frame).mainTabPain);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public LauncherForm(JFrame frame) throws Exception {
        final File folder = new File("timers\\");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        populateKillerList(listOfFiles);
        populateSurvivorList(listOfFiles);
        populateComboBoxes();
    }

    private void populateKillerList(final File[] folder) throws Exception {
        for (File file : folder) {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader(file));

            String name = (String) data.get("name");
            String location = (String) data.get("icon");
            ImageIcon icon = new ImageIcon(location);
            String mode = (String) data.get("mode");
            boolean enabled = (boolean) data.get("enabled");

            createGraphic(icon, name, enabled);
        }
    }

    private void populateSurvivorList(final File[] folder) {
    }

    private void createGraphic(ImageIcon icon, String name, boolean enabled) {
       
    }

    private void populateComboBoxes() {
        populateFontBox();
        populateFontSizeBox();
        populateFontColourBox();
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

    private void populateFontColourBox() {
        fontColourBox.addItem(Color.WHITE);
        fontColourBox.addItem(Color.BLACK);
        fontColourBox.addItem(Color.LIGHT_GRAY);
        fontColourBox.addItem(Color.GRAY);
        fontColourBox.addItem(Color.DARK_GRAY);
    }

    private void populateFontTypeBox() {
        //0 = plain, 1 = bold, 2 = italic
        fontTypeBox.addItem(0);
        fontTypeBox.addItem(1);
        fontTypeBox.addItem(2);
    }
}
