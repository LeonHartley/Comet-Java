package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.data.BackgroundTonerData;
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

        FloorItem item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(tonerId);

        if(item == null || !item.getRoom().getRights().hasRights(client.getPlayer().getId())) {
            // Item doesn't exist, gtfo
            return;
        }

        BackgroundTonerData data = new BackgroundTonerData(hue, saturation, lightness);
        item.setExtraData(BackgroundTonerData.get(data));

        item.getRoom().getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(item, item.getOwner()));
        item.saveData();
    }
}
