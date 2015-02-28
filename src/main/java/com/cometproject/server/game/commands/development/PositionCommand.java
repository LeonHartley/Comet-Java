package com.cometproject.server.game.commands.development;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;


public class PositionCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("X: " + client.getPlayer().getEntity().getPosition().getX() + "\r\n");
        stringBuilder.append("Y: " + client.getPlayer().getEntity().getPosition().getY() + "\r\n");
        stringBuilder.append("Z: " + client.getPlayer().getEntity().getPosition().getZ() + "\r\n");
        stringBuilder.append("Rotation: " + client.getPlayer().getEntity().getBodyRotation() + "\r\n");

        sendNotif(stringBuilder.toString(), client);
    }

    @Override
    public String getPermission() {
        return "position_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.position.description");
    }
}
