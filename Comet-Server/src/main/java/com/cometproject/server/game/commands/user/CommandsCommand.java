package com.cometproject.server.game.commands.user;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.commands.CommandManager;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.Map;


public class CommandsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        StringBuilder list = new StringBuilder();

        for (Map.Entry<String, ChatCommand> command : CommandManager.getInstance().getChatCommands().entrySet()) {
            if(command.getValue().isHidden()) continue;

            if (client.getPlayer().getPermissions().hasCommand(command.getValue().getPermission())) {
//                if(command.getKey().contains(",")) {
//                    final String[] keys = command.getKey().split(",");
//
//                    for(String key : keys) {
//                        list.append(":" + key + " - " + command.getValue().getDescription() + "\n");
//                    }
//                } else {
                    list.append(":" + command.getKey().split(",")[0] + " - " + command.getValue().getDescription() + "\n");
//                }
            }
        }

        client.send(new MotdNotificationComposer(Locale.get("command.commands.title") + "\n================================================\n" + list.toString()));
    }

    @Override
    public String getPermission() {
        return "commands_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.commands.description");
    }
}
