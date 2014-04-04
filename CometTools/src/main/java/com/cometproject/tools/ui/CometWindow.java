package com.cometproject.tools.ui;

import javax.swing.*;
import java.awt.*;

public class CometWindow extends JFrame {
    public static final String WINDOW_TITLE = "Comet Tools";
    public static final Dimension WINDOW_DIMENSION = new Dimension(1024, 700);

    private JPanel panel;
    private JTabbedPane tabbedPane1;

    public CometWindow() {
        this.panel.setSize(WINDOW_DIMENSION);
        this.panel.setPreferredSize(WINDOW_DIMENSION);

        this.setSize(WINDOW_DIMENSION);
        this.setPreferredSize(WINDOW_DIMENSION);

        this.setContentPane(panel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.setEnabled(true);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle(WINDOW_TITLE);

        this.pack();

    }
}
