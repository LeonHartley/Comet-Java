package com.cometproject.network.messages.incoming.room.item;

import com.cometproject.boot.Comet;
import com.cometproject.game.GameEngine;
import com.cometproject.game.rooms.items.WallItem;
import com.cometproject.game.rooms.types.Room;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.items.UpdateWallItemMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

import java.sql.PreparedStatement;

public class ChangeWallItemPositionMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();
        String position = msg.readString();

        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null) {
            return;
        }

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if(isOwner || hasRights) {
            WallItem item = room.getItems().getWallItem(itemId);

            if(item == null) {
                return;
            }

            try {
                PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE items SET wall_pos = ? WHERE id = ?");

                statement.setString(1, position);
                statement.setInt(2, itemId);

                statement.executeUpdate();

                item.setPosition(position);
                room.getEntities().broadcastMessage(UpdateWallItemMessageComposer.compose(item, room.getData().getOwnerId(), room.getData().getOwner()));
            } catch(Exception e) {
                GameEngine.getLogger().error("Error while updating wall item position", e);
            }
        }
    }
}
