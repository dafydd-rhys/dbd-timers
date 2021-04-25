package com.DBDKillerTimer.main;

import java.awt.*;
import java.util.Objects;
import javax.swing.*;

public class EditBlinker {

    private JPanel addColour;
    private JTabbedPane blinkerPane;
    private JPanel blinkerProperties;
    private JButton saveBlinkerButton;
    private JComboBox<Integer> startBlink;
    private JCheckBox blinkEnabled;
    private JButton chooseColour;
    private JSlider freqSlider;
    private JLabel sliderValue;
    private JLabel txtColour;

    public EditBlinker(TimerProperties timer) {
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

        TimerBlink newBlinker = new TimerBlink();
        populateBlinkTime();
        sliderValue.setText(String.valueOf(freqSlider.getValue()));
        freqSlider.addChangeListener(e -> sliderValue.setText(String.valueOf(freqSlider.getValue())));

        //lets you choose colour
        chooseColour.addActionListener(e -> {
            newBlinker.colour = JColorChooser.showDialog(null, "Pick a Color",
                    Color.BLACK);

            if (newBlinker.colour != null) {
                txtColour.setText("RGB: " + newBlinker.colour.getRed() + ", "
                        + newBlinker.colour.getGreen() + ", "
                        + newBlinker.colour.getBlue());
            }
        });

        saveBlinkerButton.addActionListener(e-> {
            if (newBlinker.colour != null) {
                try {
                    newBlinker.blinkFrequency = freqSlider.getValue();
                    newBlinker.time = (int) (Objects.requireNonNull(startBlink.getSelectedItem()));
                    timer.getTimerBlinks().add(newBlinker);
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

    private void populateBlinkTime() {
        for (int num = 1; num <= 180; ++num) {
            startBlink.addItem(num);
        }
    }
}
