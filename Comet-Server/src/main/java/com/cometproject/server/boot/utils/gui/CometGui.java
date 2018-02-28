package com.cometproject.server.boot.utils.gui;

import com.cometproject.api.stats.CometStats;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.tasks.CometThreadManager;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class CometGui extends JFrame {
    private JPanel mainPanel;
    private JLabel playersOnline;
    private JLabel roomsLoaded;
    private JLabel uptime;

    public CometGui() {
        super("Comet Server - " + Comet.getBuild());

        this.pack();

        if (mainPanel == null) {
            return;
        }

        this.setContentPane(mainPanel);

        this.setSize(new Dimension(350, 120));
        this.setResizable(false);

        CometThreadManager.getInstance().executePeriodic(this::update, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void update() {
        final CometStats stats = Comet.getStats();

        playersOnline.setText("Players online: " + stats.getPlayers());
        roomsLoaded.setText("Rooms loaded: " + stats.getRooms());
        uptime.setText("Uptime: " + stats.getUptime());
    }
}
