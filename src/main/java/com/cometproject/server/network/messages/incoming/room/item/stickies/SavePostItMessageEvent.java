package com.cometproject.server.network.messages.incoming.room.item.stickies;

import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.objects.items.types.wall.PostItWallItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class SavePostItMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int itemId = msg.readInt();

        if(client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        String colour = msg.readString();
        String message = msg.readString();

        RoomItemWall wallItem = room.getItems().getWallItem(itemId);

        if(wallItem == null || !(wallItem instanceof PostItWallItem)) return;

        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        wallItem.setExtraData(colour + " " + message);
        wallItem.sendUpdate();

        wallItem.saveData();
    }
}
