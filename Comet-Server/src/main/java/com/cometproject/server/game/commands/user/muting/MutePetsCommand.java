package com.cometproject.server.game.commands.user.muting;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class MutePetsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        client.getPlayer().setPetsMuted(!client.getPlayer().petsMuted());
        sendWhisper(Locale.get("command.mutepets." + client.getPlayer().petsMuted()), client);
    }

    @Override
    public String getPermission() {
        return "mutepets_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.mutepets.description");
    }
}
