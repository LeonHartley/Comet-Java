package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class BlacklistCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
    if(params.length < 2)
        return;

        switch (params[0]) {
            case "add":
                GameEngine.getFilter().addblacklistedWord(params[1]);
                client.send(AdvancedAlertMessageComposer.compose(Locale.get("command.blacklist.title"), Locale.get("command.blacklist.message")));
                break;

            case "remove":
                GameEngine.getFilter().removeblacklistedWord(params[1]);
                client.send(AdvancedAlertMessageComposer.compose(Locale.get("command.blacklist.messageblacklisted"), Locale.get("command.blacklist.messageunblacklisted")));
                break;
        }
    }

    @Override
    public String getPermission() {
        return "blacklist_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.blacklist.description");
    }
}
