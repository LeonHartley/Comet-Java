package com.cometproject.server.game.commands.staff.muting;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;


public class RoomMuteCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (client.getPlayer().getEntity().getRoom().hasRoomMute()) {
            client.getPlayer().getEntity().getRoom().setRoomMute(false);
        } else {
            client.getPlayer().getEntity().getRoom().setRoomMute(true);
        }
    }

    @Override
    public String getPermission() {
        return "roommute_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roommute.description");
    }
}
