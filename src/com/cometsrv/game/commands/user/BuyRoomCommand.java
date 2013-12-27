package com.cometsrv.game.commands.user;

import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.network.sessions.Session;

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
