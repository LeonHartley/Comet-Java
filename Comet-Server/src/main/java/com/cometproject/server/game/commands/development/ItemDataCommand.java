package com.cometproject.server.game.commands.development;

import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class ItemDataCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        final PlayerEntity playerEntity = client.getPlayer().getEntity();

        RoomItemFloor floorItem = playerEntity.getTile().getTopItemInstance();

        if (floorItem == null) {
            return;
        }

        client.send(new MotdNotificationMessageComposer(String.format("Item Data (%s)\n\nRotation: %s\nType: %s\nPosition: %s", floorItem.getId(), floorItem.getRotation(), floorItem.getClass().getSimpleName(), floorItem.getPosition())));
    }

    @Override
    public String getPermission() {
        return "dev";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Gets the data for the item you're currently standing on";
    }
}
