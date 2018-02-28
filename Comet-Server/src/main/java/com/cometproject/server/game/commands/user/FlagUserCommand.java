package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UserObjectMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class FlagUserCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.flaguser.none", "Who do you want to change his name?"), client);
            return;
        }

        String username = params[0];

        Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null || user.getPlayer() == null || user.getPlayer().getData() == null) {
            return;
        }

        user.getPlayer().getData().setFlaggingUser(true);
        user.getPlayer().getData().setChangingName(true);
        user.send(new AdvancedAlertMessageComposer(Locale.getOrDefault("command.flaguser.title", "Grrrr!"), Locale.getOrDefault("command.flaguser.message", "Your name is inappropriate! You can change your name by clicking on yourself. Do this within a minute or you will be banned!")));
        user.send(new UserObjectMessageComposer(user.getPlayer()));
    }

    @Override
    public String getPermission() {
        return "flaguser_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.flaguser.description");
    }
}