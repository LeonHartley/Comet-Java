package com.cometproject.server.game.commands.user.muting;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class MuteBotsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        client.getPlayer().setBotsMuted(!client.getPlayer().botsMuted());
        sendWhisper(Locale.get("command.mutebots." + client.getPlayer().botsMuted()), client);
    }

    @Override
    public String getPermission() {
        return "mutebots_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.mutebots.description");
    }
}
