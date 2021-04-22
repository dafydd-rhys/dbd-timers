package com.DBDKillerTimer.main;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

/**
 * Dialog.java.
 * @version 1.0
 * This class just adds all components to the dialog which can be
 * displayed the user includes the icons and timers text.
 * @author Dafydd-Rhys Maund
 */
public class Dialog {

    /**
     * This method simply looks at what icon size the user wants
     * and adds them to the dialog to be displayed.
     * @param width the width of the users screen
     * @param height the height of the users screen
     * @param iconSize the icon size the users wants
     * @param main represents this instance of the program
     * @param currentDialog referring to this dialog
     */
    public Dialog(final int width, final int height, final int iconSize,
                  final Main main, final JDialog currentDialog) {
        currentDialog.setAlwaysOnTop(true);
        currentDialog.setPreferredSize(new Dimension(width, height));
        currentDialog.setMaximumSize(new Dimension(width, height));
        currentDialog.setMinimumSize(new Dimension(width, height));

        final int iconSize128px = 128;
        final int iconSize96px = 96;
        final int iconSize64px = 64;

        if (iconSize == iconSize128px || iconSize == iconSize96px
                || iconSize == iconSize64px) {
            JLabel chase = new JLabel(new ImageIcon("images\\deadhard_"
                    + iconSize + ".png"));
            JLabel onShoulder = new JLabel(new ImageIcon("images\\grasp_"
                    + iconSize + ".png"));
            JLabel borrowed = new JLabel(new ImageIcon("images\\borrowed_"
                    + iconSize + ".png"));
            JLabel decisive = new JLabel(new ImageIcon("images\\decisive_"
                    + iconSize + ".png"));

            currentDialog.add(decisive);
            currentDialog.add(borrowed);
            currentDialog.add(chase);
            currentDialog.add(onShoulder);
        } else {
            System.out.println("Not Valid Icon Size!");
            System.exit(1);
        }

        currentDialog.setDefaultCloseOperation(currentDialog.DISPOSE_ON_CLOSE);
        currentDialog.setResizable(false);
        currentDialog.add(main);
        currentDialog.pack();
        currentDialog.setLocationRelativeTo(null);
        currentDialog.setVisible(true);
    }
}
