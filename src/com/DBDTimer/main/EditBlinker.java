package com.DBDTimer.main;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Objects;

/**
 * EditBlinker.java.
 * @version 1.0.2
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
    private JLabel txtColor;
    private JPanel rgbVisual;
    /** the label representing the currently chosen colour. */
    private JLabel txtColour;
    /** represents the newBlinker being created. */
    private TimerBlink newBlinker;
    /** the frame which represents the UI. */
    private JFrame frame;

    /**
     * This method simply replaces the blinker with the newBlinkers properties.
     * @param blinker the blinker being edited
     * @param width the width of frame
     * @param height the height of frame
     */
    public void editBlinker(final int width, final int height,
                            final TimerBlink blinker) {
        initUI(width, height, "Edit Blinker");
        setProperties(blinker);

        chooseColour.addActionListener(e -> {
            blinker.setColour(JColorChooser.showDialog(null, "Pick a Colour",
                blinker.getColour()));

            txtColor.setText("RGB: " + blinker.getColour().getRed()
                    + ", " + blinker.getColour().getGreen() + ", "
                    + blinker.getColour().getBlue());
            rgbVisual.setBackground(blinker.getColour());
        });

        saveBlinkerButton.addActionListener(e -> {
            if (blinker.getColour() != null) {
                blinker.setBlinkFrequency(freqSlider.getValue());
                blinker.setTime((int) (Objects.requireNonNull(
                        startBlink.getSelectedItem())));

                JOptionPane.showMessageDialog(
                        null, "Blinker added");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Please fill in all required properties");
            }
        });
    }

    /**
     * hub for all the main functionality, adding, editing blinkers etc.
     * @param timer the timer being referred to
     * @param width the width of frame
     * @param height the height of frame
     */
    public void addBlinker(final int width, final int height,
                           final TimerProperties timer) {
        initUI(width, height, "Add Blinker");

        this.newBlinker = new TimerBlink();
        chooseColour.addActionListener(e -> {
            newBlinker.setColour(JColorChooser.showDialog(null, "Pick a Colour",
                        Color.BLACK));

            txtColor.setText("RGB: " + newBlinker.getColour().getRed()
                     + ", " + newBlinker.getColour().getGreen() + ", "
                     + newBlinker.getColour().getBlue());
            rgbVisual.setBackground(newBlinker.getColour());
        });

        saveBlinkerButton.addActionListener(e -> {
            if (newBlinker.getColour() != null) {

                newBlinker.setBlinkFrequency(freqSlider.getValue());
                newBlinker.setTime((int) (Objects.requireNonNull(
                        startBlink.getSelectedItem())));

                timer.getTimerBlinks().add(newBlinker);
                JOptionPane.showMessageDialog(null, "Blinker added");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Please fill in all required properties");
            }
        });
    }

    private void initUI(final int width, final int height,
                        final String windowTitle) {
        frame = new JFrame(windowTitle);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
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
     * @param blinker sets the properties for the existing blinker
     */
    private void setProperties(final TimerBlink blinker) {
        freqSlider.setValue(blinker.getBlinkFrequency());
        if (freqSlider.getValue() > 0) {
            blinkEnabled.setSelected(true);
            freqSlider.setEnabled(true);
        }
        startBlink.setSelectedItem(blinker.getTime());
        txtColor.setText("RGB: " + blinker.getColour().getRed() + ", "
                + blinker.getColour().getGreen() + ", "
                + blinker.getColour().getBlue());
        rgbVisual.setBackground(blinker.getColour());
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
