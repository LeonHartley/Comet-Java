package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.components.types.ChatMessageColour;
import com.cometproject.server.network.sessions.Session;

public class ColourCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            client.getPlayer().setChatMessageColour(null);

            sendNotif(Locale.getOrDefault("command.colour.reset", "Your chat has been returned to normal!"), client);
            return;
        }

        String colourString = "";

        if (params[0].toLowerCase().equals(Locale.getOrDefault("command.colour.red", "red"))) {
            params[0] = "red";
            colourString = (Locale.getOrDefault("command.colour.red", "red"));
        }

        if (params[0].toLowerCase().equals(Locale.getOrDefault("command.colour.blue", "blue"))) {
            params[0] = "blue";
            colourString = (Locale.getOrDefault("command.colour.blue", "blue"));
        }

        if (params[0].toLowerCase().equals(Locale.getOrDefault("command.colour.green", "green"))) {
            params[0] = "green";
            colourString = (Locale.getOrDefault("command.colour.green", "green"));
        }

        if (params[0].toLowerCase().equals(Locale.getOrDefault("command.colour.purple", "purple"))) {
            params[0] = "purple";
            colourString = (Locale.getOrDefault("command.colour.purple", "purple"));
        }

        if (params[0].toLowerCase().equals(Locale.getOrDefault("command.colour.cyan", "cyan"))) {
            params[0] = "cyan";
            colourString = (Locale.getOrDefault("command.colour.cyan", "cyan"));
        }

        if (params[0].toLowerCase().equals(Locale.getOrDefault("command.colour.reset.word", "normal"))) {
            client.getPlayer().setChatMessageColour(null);

            sendNotif(Locale.getOrDefault("command.colour.reset", "Your chat colour has been returned to normal!"), client);
            return;
        }

        final String colourName = params[0];

        try {
            final ChatMessageColour colour = ChatMessageColour.valueOf(colourName);

            client.getPlayer().setChatMessageColour(colour);

            sendNotif(Locale.getOrDefault(
                    "command.colour.done",
                    "Your chat messages are now %colour%").replace("%colour%", colourString), client);
        } catch (Exception e) {
            sendNotif(Locale.getOrDefault(
                    "command.colour.invalid",
                    "Invalid colour, available colours: %colours%").replace("%colours%", ChatMessageColour.getAllAvailable()), client);
        }
    }

    @Override
    public String getPermission() {
        return "colour_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.colour", "%colour%");
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.colour.description", "Allows you to change the colour of your chat messages");
    }
}
