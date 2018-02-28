package com.cometproject.server.game.commands.development;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;


public class PositionCommand extends ChatCommand {
    private boolean debug;

    public PositionCommand() {
        this.debug = false;
    }

    public PositionCommand(boolean debug) {
        this.debug = debug;
    }

    @Override
    public void execute(Session client, String[] params) {
        sendNotif(("X: " + client.getPlayer().getEntity().getPosition().getX() + "\r\n") +
                        "Y: " + client.getPlayer().getEntity().getPosition().getY() + "\r\n" +
                        "Z: " + client.getPlayer().getEntity().getPosition().getZ() + "\r\n" +
                        "Rotation: " + client.getPlayer().getEntity().getBodyRotation() + "\r\n",
                client);
    }

    @Override
    public String getPermission() {
        return this.debug ? "dev" : "position_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.position.description");
    }
}
