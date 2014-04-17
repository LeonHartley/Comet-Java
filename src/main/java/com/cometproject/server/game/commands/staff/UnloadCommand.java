package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class UnloadCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        int roomId = client.getPlayer().getEntity().getRoom().getId();

        GameEngine.getRooms().getGlobalProcessor().requestUnload(roomId);
    }

    @Override
    public String getPermission() {
        return "unload_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.unload.description");
    }
}
