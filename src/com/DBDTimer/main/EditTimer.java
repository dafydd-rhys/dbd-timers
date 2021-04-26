package com.DBDTimer.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.SwingConstants;

/**
 * EditTimer.java.
 * @version 1.0.2
 * This class simply allows the user to edit the timers,
 * they can change any property of a timer, the timer will
 * then be created/appended to the folder.
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public class EditTimer {

    /** unused GUI elements. */
    private JPanel mainPanel;
    /** unused GUI elements. */
    private JPanel timerPanel;
    /** unused GUI elements. */
    private JPanel coloursPanel;
    /** unused GUI elements. */
    private JScrollPane coloursScrollPane;
    /** the properties of the current timer. */
    private final TimerProperties timer;
    /** the tabbed pane displaying the properties of this timer. */
    private JTabbedPane timerProperties;
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
    /** the button used to point to image. */
    private JButton browseButton;
    /** the button used to open a colour chooser. */
    private JButton chooseColour;
    /** the button to save the timer. */
    private JButton saveTimer;
    /** the path of the icons image. */
    private JCheckBox timerEnabled;
    /** the label which shows the path of the selected image. */
    private JLabel txtPath;
    /** the label which shows the color selected by the user.*/
    private JLabel txtColor;
    /** the panels displaying all colour blinkers. */
    private JPanel coloursList;
    /** button to initiate adding a blinker. */
    private JButton addBlinkerTimer;
    private JPanel rgbVisual;
    /** colour representing the starting colour of this clock. */
    private Color timerStartColor;
    /** ArrayList containing all blinkers for this timer. */
    private ArrayList<TimerBlink> timerBlinks;
    /** the width of the frame. */
    private final int width;
    /** the height of the frame. */
    private final int height;

    /**
     * this method is a host for all functionality allowing users to edit
     * this timer, they can add, remove blinkers etc.
     * @param properties the properties of the timer
     * @param title the title for the frame
     * @param width the width of frame
     * @param height the height of frame
     */
    public EditTimer(final TimerProperties properties, final String title,
                     final int width, final int height) {
        this.timer = properties;
        this.width = width;
        this.height = height;
        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        frame.setContentPane(timerProperties);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        populateComboBoxes();
        if (properties != null) {
            populateColoursPanel();
            setTimerProperties();
        }

        browseButton.addActionListener(e -> {
            FileDialog fd = new FileDialog(new JFrame());
            fd.setVisible(true);
            File[] f = fd.getFiles();
            if (f.length > 0) {
                iconPath = fd.getFiles()[0].getAbsolutePath();
                txtPath.setText(fd.getFiles()[0].getName());
            }
        });

        chooseColour.addActionListener(e -> {
            timerStartColor = JColorChooser.showDialog(null, "Pick a Colour",
                    Color.BLACK);
            txtColor.setText("RGB: " + timerStartColor.getRed() + ", "
                    + timerStartColor.getGreen() + ", "
                    + timerStartColor.getBlue());
            rgbVisual.setBackground(timerStartColor);
        });

        addBlinkerTimer.addActionListener(e -> new EditBlinker().addBlinker(
                width, height, properties));

        saveTimer.addActionListener(e -> {
            if (!iconPath.equals("") && timerStartColor != null) {
                try {
                    FileWriter fw = new FileWriter("timers\\"
                            + timerName.getText() + ".json");
                    Gson g = new GsonBuilder().setPrettyPrinting().create();

                    TimerProperties newTimer = new TimerProperties();
                    newTimer.setName(timerName.getText());
                    newTimer.setStartColor(timerStartColor);
                    newTimer.setTimerBlinks(timerBlinks);
                    newTimer.setTimerType((TimerProperties.TimerType)
                            timerTypeBox.getSelectedItem());
                    if (newTimer.getTimerType() == TimerProperties.
                            TimerType.CountUp) {
                        newTimer.setStartTime(0);
                    } else {
                        newTimer.setStartTime((int) (Objects.requireNonNull(
                                startTime.getSelectedItem())));
                    }
                    newTimer.setIcon(iconPath);
                    newTimer.setTimerMode((TimerProperties.TimerMode)
                            timerModeBox.getSelectedItem());
                    newTimer.setEnabled(timerEnabled.isSelected());
                    newTimer.setStartBind(Objects.requireNonNull(
                            startBind.getSelectedItem()).toString());

                    fw.write(g.toJson(newTimer));
                    //create new timer
                    if (timer == null) {
                        JOptionPane.showMessageDialog(null, "Added timer");
                    } else {
                        JOptionPane.showMessageDialog(null, "Updated timer");
                    }

                    fw.close();
                    frame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Please fill in all required properties");
            }
        });
    }

    /**
     * this method simply populates the panel with all blinkers.
     */
    private void populateColoursPanel() {
        coloursList.setLayout(new BoxLayout(coloursList, BoxLayout.Y_AXIS));
        if (timer.getTimerBlinks() != null) {
            int counter = 1;
            for (TimerBlink blinkColour : timer.getTimerBlinks()) {
                addColour(blinkColour, counter, blinkColour.getColour(),
                        blinkColour.getTime(), blinkColour.getBlinkFrequency());
                counter++;
            }
        }
    }

    /**
     * This method simply adds all blinkers for this timer.
     *
     * @param timerBlink the blinker being referred to
     * @param index      the current blinker
     * @param timerColor the current colour of the timer
     * @param time       the time the blinker instantiates
     * @param frequency  the speed at which the blinker blinks
     */
    private void addColour(final TimerBlink timerBlink,
                           final int index,
                           final Color timerColor,
                           final int time,
                           final int frequency) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 0, 0, 10));

        JPanel rgbPanel = new JPanel();
        rgbPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel rgbLabel = new JLabel("RGB: " + timerColor.getRed() + ", "
                + timerColor.getGreen() + ", " + timerColor.getBlue());
        rgbPanel.add(rgbLabel);

        JPanel hGap = new JPanel();
        hGap.setVisible(true);

        hGap.setMaximumSize(new Dimension(5, 10));
        hGap.setMinimumSize(new Dimension(5, 10));
        hGap.setPreferredSize(new Dimension(5, 10));

        rgbPanel.add(hGap);

        JPanel rgbVisual = new JPanel();
        rgbVisual.setBackground(timerColor);

        rgbVisual.setMaximumSize(new Dimension(10, 10));
        rgbVisual.setMinimumSize(new Dimension(10, 10));
        rgbVisual.setPreferredSize(new Dimension(10, 10));

        rgbPanel.add(rgbVisual);
        infoPanel.add(rgbPanel);

        infoPanel.add(new JLabel("Blink time: " + time + "s"));
        infoPanel.add(new JLabel("Frequency: " + frequency + "ms"));

        ImageIcon settings = new ImageIcon("images\\settings_cog.png");
        JLabel openSettings = new JLabel(convertImageSize(settings, 16));
        ImageIcon removeIcon = new ImageIcon("images\\remove_icon.png");
        JLabel removeTimer = new JLabel(convertImageSize(removeIcon, 12));

        JPanel blinkerPanel = new JPanel();
        blinkerPanel.setLayout(new GridLayout(0, 4));
        blinkerPanel.setMaximumSize(new Dimension(coloursList.getWidth(), 60));
        blinkerPanel.setPreferredSize(new Dimension(coloursList.getWidth(), 60));
        blinkerPanel.setMinimumSize(new Dimension(coloursList.getWidth(), 60));
        JLabel blinkerName = new JLabel("Colour: " + index);
        blinkerName.setHorizontalAlignment(SwingConstants.CENTER);
        blinkerPanel.add(blinkerName);
        blinkerPanel.add(infoPanel);
        blinkerPanel.add(openSettings);
        blinkerPanel.add(removeTimer);
        coloursList.add(Box.createRigidArea(new Dimension(0, 20)));
        coloursList.add(blinkerPanel);

        openSettings.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                new EditBlinker().editBlinker(width, height, timerBlink);
            }
        });

        removeTimer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                int answer = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want " + "to delete this timer?",
                        "Warning", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    timer.getTimerBlinks().remove(timerBlink);
                    coloursList.remove(blinkerPanel);
                    coloursList.updateUI();
                    JOptionPane.showMessageDialog(null,
                            "Blinker deleted successfully.");
                }
            }
        });
    }

    /**
     * this method converts images to different sizes.
     *
     * @param image the image that wants to be converted
     * @param size  the size in which the images wants to be resized to.
     * @return returns the resized image.
     */
    private ImageIcon convertImageSize(final ImageIcon image, final int size) {
        Image imageOfIcon = image.getImage();
        Image newImage = imageOfIcon.getScaledInstance(size, size,
                java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }

    /**
     * sets all properties for this timer.
     */
    private void setTimerProperties() {
        timerName.setText(timer.getName());
        iconPath = timer.getIcon();
        timerTypeBox.setSelectedItem(timer.getTimerType());
        timerStartColor = timer.getStartColor();
        timerBlinks = timer.getTimerBlinks();
        startTime.setSelectedItem(timer.getStartTime());
        startBind.setSelectedItem(timer.getStartBind().charAt(0));
        timerModeBox.setSelectedItem(timer.getTimerMode());
        timerEnabled.setSelected(timer.isEnabled());
        String[] pathFolders = timer.getIcon().split("\\\\");
        txtPath.setText(pathFolders[pathFolders.length - 1]);
        txtColor.setText("RGB: " + timer.getStartColor().getRed() + ", "
                + timer.getStartColor().getGreen()
                + ", " + timer.getStartColor().getBlue());
        rgbVisual.setBackground(timer.getStartColor());
    }

    /**
     * populates all combo boxes in this GUI.
     */
    private void populateComboBoxes() {
        populateTimerTypeBox();
        populateTimerModeBox();
        populateStartBind();
        populateStartTime();
    }

    /**
     * populates the combo box including timer types.
     */
    private void populateTimerTypeBox() {
        timerTypeBox.addItem(TimerProperties.TimerType.CountUp);
        timerTypeBox.addItem(TimerProperties.TimerType.CountDown);
        startTime.setEnabled(false);
        timerTypeBox.addActionListener(e -> startTime.setEnabled(
                Objects.requireNonNull(timerTypeBox.
                        getSelectedItem()).toString().equals("CountDown")));
    }

    /**
     * populates the combo box including timer modes.
     */
    private void populateTimerModeBox() {
        timerModeBox.addItem(TimerProperties.TimerMode.Killer);
        timerModeBox.addItem(TimerProperties.TimerMode.Survivor);
    }

    /**
     * populates the combo box including start timer binds.
     */
    private void populateStartBind() {
        for (char c = 'A'; c <= 'Z'; ++c) {
            startBind.addItem(c);
        }
        for (char num = 48; num <= 57; ++num) {
            startBind.addItem(num);
        }
    }

    /**
     * populates the combo box including timer start times.
     */
    private void populateStartTime() {
        for (int num = 1; num <= 180; ++num) {
            startTime.addItem(num);
        }
    }

}
