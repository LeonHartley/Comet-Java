package com.cometproject.server.boot.utils.gui;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.tasks.CometThreadManager;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class CometGui extends JFrame {
    private JPanel mainPanel;
    private JLabel playersOnline;
    private JLabel roomsLoaded;

    public CometGui() {
        super("Comet Server " + Comet.getBuild());

        this.pack();
        this.setContentPane(mainPanel);

        this.setSize(new Dimension(200, 80));
        this.setResizable(false);

        CometThreadManager.getInstance().executePeriodic(this::update, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void update() {
        playersOnline.setText("Players online: " + PlayerManager.getInstance().size());
        roomsLoaded.setText("Rooms loaded: " + RoomManager.getInstance().getRoomInstances().size());
    }
}
