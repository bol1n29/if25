package fr.utt.ui.CubeUI;

import javax.swing.*;
import java.awt.*;

class JProgressBarThread extends JFrame {

    JProgressBar jp;
    Thread t;

    public JProgressBarThread() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }
        setTitle("JProgressBar with Thread Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setVisible(true);
        jp = new JProgressBar();
        jp.setPreferredSize(new Dimension(350, 30));
        jp.setStringPainted(true);
        add(jp);
        t = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i <= jp.getMaximum(); i++) {
                    // Loop it forever
                    if (i == jp.getMaximum()) {
                        i = 0;
                    }
                    // Update value
                    jp.setValue(i);
                    try {
                        // Get the effect
                        Thread.sleep(200);
                    } catch (Exception e) {
                    }
                }
            }
        });
        // Start thread
        t.start();
        // Set the size now
        setSize(400, 400);
        // Make it center now
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        new JProgressBarThread();
    }
}
