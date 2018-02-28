package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang3.StringUtils;

public class GotoRoomCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            return;
        }

        if (!StringUtils.isNumeric(params[0])) {
            return;
        }

        final int roomId = Integer.parseInt(params[0]);

        client.send(new RoomForwardMessageComposer(roomId));
    }

    @Override
    public String getPermission() {
        return "gotoroom_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.roomid", "%roomid%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.gotoroom.description");
    }
}
