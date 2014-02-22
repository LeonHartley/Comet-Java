package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class BuyRoomCommand extends ChatCommand {

    @Override
    public void execute(Session client, String message[]) {

    }

    @Override
    public String getPermission() {
        return "buyroom_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.buyroom.description");
    }
}
