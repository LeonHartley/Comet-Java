package com.cometsrv.network.messages.incoming.room.item;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.rooms.items.WallItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.items.UpdateWallItemMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

import java.sql.PreparedStatement;

public class ChangeWallItemPositionMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();
        String position = msg.readString();

        Room room = client.getPlayer().getAvatar().getRoom();

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
                room.getAvatars().broadcast(UpdateWallItemMessageComposer.compose(item, room.getData().getOwnerId(), room.getData().getOwner()));
            } catch(Exception e) {
                GameEngine.getLogger().error("Error while updating wall item position", e);
            }
        }
    }
}
