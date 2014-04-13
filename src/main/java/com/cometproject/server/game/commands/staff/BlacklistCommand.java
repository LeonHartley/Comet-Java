package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class BlacklistCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {

        switch (params[0]) {
            case "add":
                GameEngine.getFilter().addblacklistedWord(params[1]);
                client.send(AdvancedAlertMessageComposer.compose("Alert", "The word is now blacklisted"));
                break;

            case "remove":
                GameEngine.getFilter().removeblacklistedWord(params[1]);
                client.send(AdvancedAlertMessageComposer.compose("Alert", "The word is now unblacklisted"));
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
