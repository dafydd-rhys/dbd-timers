package com.DBDKillerTimer.main;
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
    /** the new blinker created by the user. */
    private final TimerBlink newBlinker;
    /** whether the user is adding or editing. */
    private boolean adding = false;

    /**
     * hub for all the main functionality, adding, editing blinkers etc.
     * @param timer the timer being referred to
     * @param blinker the blinker that could possibly getting edited
     */
    public EditBlinker(final TimerProperties timer, final TimerBlink blinker) {
        JFrame frame = new JFrame("Add Blinker");
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

        if (blinker != null) {
            frame.setTitle("Edit Blinker");
            newBlinker = blinker;
            setProperties();
        } else {
            frame.setTitle("Add Blinker");
            newBlinker = new TimerBlink();
            adding = true;
        }
        sliderValue.setText(String.valueOf(freqSlider.getValue()));

        freqSlider.addChangeListener(e -> sliderValue.setText(
                String.valueOf(freqSlider.getValue())));
        blinkEnabled.addActionListener(e ->
                freqSlider.setEnabled(blinkEnabled.isSelected()));
        chooseColour.addActionListener(e -> {
            newBlinker.setColour(JColorChooser.showDialog(null, "Pick a Color",
                    newBlinker.getColour()));

            if (newBlinker.getColour() != null) {
                txtColour.setText("RGB: " + newBlinker.getColour().getRed()
                        + ", " + newBlinker.getColour().getGreen() + ", "
                        + newBlinker.getColour().getBlue());
            }
        });

        saveBlinkerButton.addActionListener(e -> {
            if (newBlinker.getColour() != null) {
                try {
                    newBlinker.setBlinkFrequency(freqSlider.getValue());
                    newBlinker.setTime((int) (Objects.requireNonNull(
                            startBlink.getSelectedItem())));
                    if (adding) {
                        timer.getTimerBlinks().add(newBlinker);
                    }
                    JOptionPane.showMessageDialog(null, "blinker created.");
                    frame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "cant create blinker.");
            }
        });
    }

    /**
     * this method sets all properties of the current blinker.
     */
    private void setProperties() {
        freqSlider.setValue(newBlinker.getBlinkFrequency());
        if (freqSlider.getValue() > 0) {
            blinkEnabled.setSelected(true);
            freqSlider.setEnabled(true);
        }
        startBlink.setSelectedItem(newBlinker.getTime());
        txtColour.setText(newBlinker.getColour().getRed() + ", "
                + newBlinker.getColour().getGreen() + ", "
                + newBlinker.getColour().getBlue());
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
