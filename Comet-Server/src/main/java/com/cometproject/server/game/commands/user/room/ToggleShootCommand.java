package com.cometproject.server.game.commands.user.room;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.sessions.Session;

public class ToggleShootCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        final Room room = client.getPlayer().getEntity().getRoom();

        if (room.getData().getOwnerId() != client.getPlayer().getData().getId()) {
            if (!client.getPlayer().getPermissions().getRank().roomFullControl())
                return;
        }

        room.getGame().setShootEnabled(!room.getGame().shootEnabled());
        sendNotif(Locale.get("command.toggleshoot." + (room.getGame().shootEnabled() ? "enabled" : "disabled")), client);
    }

    @Override
    public String getPermission() {
        return "toggleshoot_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.toggleshoot.description");
    }
}
