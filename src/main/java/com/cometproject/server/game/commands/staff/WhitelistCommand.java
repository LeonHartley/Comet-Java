package com.cometproject.server.game.commands.staff;


import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class WhitelistCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
    if(params.length < 2)
        return;

        switch (params[1]) {
            case "add":
                GameEngine.getFilter().addwhitelistedWord(params[2]);
                client.send(AdvancedAlertMessageComposer.compose(Locale.get("command.whitelist.title"), Locale.get("command.whitelist.messagewhitelisted")));
                break;

            case "remove":
                GameEngine.getFilter().removewhitelistedWord(params[2]);
                client.send(AdvancedAlertMessageComposer.compose(Locale.get("command.whitelist.title"), Locale.get("command.whitelist.messageunwhitelisted")));
                break;
        }
    }

    @Override
    public String getPermission() {
        return "whitelist_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.whitelist.description");
    }
}
