package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class QuickPollCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length == 0) {
            return;
        }

        String question = this.merge(params);

        if (params[0].equals("end")) {
            client.getPlayer().getEntity().getRoom().endQuestion();
        } else {
            client.getPlayer().getEntity().getRoom().startQuestion(question);
        }
    }

    @Override
    public String getPermission() {
        return "quickpoll_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.quickpoll.description");
    }
}
