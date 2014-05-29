package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.data.BackgroundTonerData;
import com.cometproject.server.game.rooms.items.types.floor.BackgroundTonerFloorItem;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class SaveTonerMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int tonerId = msg.readInt();

        int hue = msg.readInt();
        int saturation = msg.readInt();
        int lightness = msg.readInt();

        RoomItemFloor item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(tonerId);

        if (item == null || !client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().hasPermission("room_full_control") || !(item instanceof BackgroundTonerFloorItem)) {
            return;
        }

        BackgroundTonerData data = new BackgroundTonerData(hue, saturation, lightness);
        String stringData = BackgroundTonerData.get(data);

        item.setExtraData(stringData);

        item.getRoom().getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(item, item.getOwner()));
        item.saveData();
    }
}
