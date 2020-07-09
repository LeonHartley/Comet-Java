package com.cometproject.server.game.commands.user.room;

import com.cometproject.api.game.rooms.models.RoomTileState;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.mapping.RoomMapping;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.room.engine.UpdateStackMapMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;

import java.util.List;

public class HeightCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        double height;

        try {
            height = Double.parseDouble(params[0]);
        } catch (Exception e) {
            height = -1;
        }

        if (height < -1 || height > 64) {
            sendNotif(Locale.get("command.height.invalid"), client);
            return;
        }

        client.getPlayer().setItemPlacementHeight(height);
        sendNotif(Locale.get("command.height.set").replace("%height%", "" + height), client);

        final RoomMapping roomMapping = client.getPlayer().getEntity().getRoom().getMapping();
        for (int x = 0; x < roomMapping.getModel().getSizeX(); x++) {
            for (int y = 0; y < roomMapping.getModel().getSizeY(); y++) {
                final RoomTile roomTile = roomMapping.getTile(x, y);
                if (roomTile != null && roomTile.getState() != RoomTileState.INVALID) {
                    client.sendQueue(new UpdateStackMapMessageComposer(roomTile, height));
                }
            }
        }

        client.flush();
    }

    @Override
    public String getPermission() {
        return "height_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.height.param", "%height%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.height.description");
    }
}

