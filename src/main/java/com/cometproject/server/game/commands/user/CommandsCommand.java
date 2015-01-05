package com.cometproject.server.game.commands.user;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.commands.CommandManager;
import com.cometproject.server.game.commands.development.EntityGridCommand;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.Map;


public class CommandsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        StringBuilder list = new StringBuilder();

        for (Map.Entry<String, ChatCommand> command : CommandManager.getInstance().getChatCommands().entrySet()) {
            if(command.getValue() instanceof EntityGridCommand) continue;

            if (client.getPlayer().getPermissions().hasCommand(command.getValue().getPermission()))
                list.append(":" + command.getKey() + " - " + command.getValue().getDescription() + "\n");
        }

        client.send(MotdNotificationComposer.compose(Locale.get("command.commands.title") + " - Comet " + Comet.getBuild() + "\n================================================\n" + list.toString()));
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
