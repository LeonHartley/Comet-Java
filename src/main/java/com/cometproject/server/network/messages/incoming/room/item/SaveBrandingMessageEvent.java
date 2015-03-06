package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class SaveBrandingMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int brandingId = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || (!room.getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        int length = msg.readInt();
        String data = "state" + (char) 9 + "0";

        for (int i = 1; i <= length; i++) {
            data = data + (char) 9 + msg.readString();
        }

        RoomItemFloor item = room.getItems().getFloorItem(brandingId);
        item.setExtraData(data);

        item.getRoom().getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(item, item.getOwner()));
        item.saveData();
    }
}
