package com.DBDTimer.main;
import java.awt.*;
import java.util.Objects;
import javax.swing.*;

/**
 * EditBlinker.java.
 * @version 1.0.0
 * This class simply allows the user to edit the blinkers,
 * they can change any property of a blinkers, the blinkers will
 * then be created/appended to their file.
 * @author Dafydd-Rhys Maund
 * @author Morgan Gardner.
 */
public class EditBlinker {

    /** unused GUI elements. */
    private JTabbedPane blinkerPane;
    /** unused GUI elements. */
    private JPanel blinkerProperties;
    /** button allowing user to add blinker. */
    private JPanel addColour;
    /** button allowing user to save there blinker. */
    private JButton saveBlinkerButton;
    /** box containing all possible start blink times. */
    private JComboBox<Integer> startBlink;
    /** checkbox representing whether the blinker is enabled. */
    private JCheckBox blinkEnabled;
    /** button allowing user to choose a colour. */
    private JButton chooseColour;
    /** slider showing the frequency the user wants. */
    private JSlider freqSlider;
    /** the value of the frequency slider. */
    private JLabel sliderValue;
    /** the label representing the currently chosen colour. */
    private JLabel txtColour;

    private TimerProperties timer;
    private TimerBlink newBlinker;
    private Color selectedColor;
    private JFrame frame;

    public TimerBlink editBlinker(final TimerProperties timer, TimerBlink blinker) {
        initUI("Edit Blinker");
        setProperties(blinker);

        chooseColour.addActionListener(e -> {
            blinker.setColour(JColorChooser.showDialog(null, "Pick a Colour",
                blinker.getColour()));

            txtColour.setText("RGB: " + blinker.getColour().getRed()
                    + ", " + blinker.getColour().getGreen() + ", "
                    + blinker.getColour().getBlue());
        });

        saveBlinkerButton.addActionListener(e -> {
            if (blinker.getColour() != null) {

                blinker.setBlinkFrequency(freqSlider.getValue());
                blinker.setTime((int) (Objects.requireNonNull(startBlink.getSelectedItem())));
                JOptionPane.showMessageDialog(null, "Blinker added");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all required properties");
            }
        });

        return blinker;
    }

    /**
     * hub for all the main functionality, adding, editing blinkers etc.
     * @param timer the timer being referred to
     */
    public void addBlinker(final TimerProperties timer) {
        this.timer = timer;
        initUI("Add Blinker");

        this.newBlinker = new TimerBlink();

        chooseColour.addActionListener(e -> {
            newBlinker.setColour(JColorChooser.showDialog(null, "Pick a Colour",
                        Color.BLACK));

            txtColour.setText("RGB: " + newBlinker.getColour().getRed()
                     + ", " + newBlinker.getColour().getGreen() + ", "
                     + newBlinker.getColour().getBlue());
        });

        saveBlinkerButton.addActionListener(e -> {
            if (newBlinker.getColour() != null) {

                newBlinker.setBlinkFrequency(freqSlider.getValue());
                newBlinker.setTime((int) (Objects.requireNonNull(startBlink.getSelectedItem())));

                timer.getTimerBlinks().add(newBlinker);
                JOptionPane.showMessageDialog(null, "Blinker added");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all required properties");
            }
        });
    }

    private void initUI(String windowTitle) {
        frame = new JFrame(windowTitle);
        frame.setPreferredSize(new Dimension(390, 450));
        frame.setMaximumSize(new Dimension(390, 450));
        frame.setMinimumSize(new Dimension(390, 450));
        frame.setContentPane(addColour);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        populateBlinkTime();

        sliderValue.setText(String.valueOf(freqSlider.getValue()));

        freqSlider.addChangeListener(e -> sliderValue.setText(
                String.valueOf(freqSlider.getValue())));
        blinkEnabled.addActionListener(e ->
                freqSlider.setEnabled(blinkEnabled.isSelected()));
    }

    /**
     * this method sets all properties of the current blinker.
     */
    private void setProperties(TimerBlink blinker) {
        freqSlider.setValue(blinker.getBlinkFrequency());
        if (freqSlider.getValue() > 0) {
            blinkEnabled.setSelected(true);
            freqSlider.setEnabled(true);
        }

        startBlink.setSelectedItem(blinker.getTime());
        txtColour.setText(blinker.getColour().getRed() + ", "
                + blinker.getColour().getGreen() + ", "
                + blinker.getColour().getBlue());
    }

    /**
     * this method populates the blink time box with all suitable times.
     */
    private void populateBlinkTime() {
        for (int num = 1; num <= 180; ++num) {
            startBlink.addItem(num);
        }
    }
}
