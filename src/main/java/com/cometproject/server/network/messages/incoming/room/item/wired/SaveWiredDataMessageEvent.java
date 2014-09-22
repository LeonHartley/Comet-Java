package com.cometproject.server.network.messages.incoming.room.item.wired;

import com.cometproject.server.game.rooms.items.types.floor.wired.AbstractWiredItem;
import com.cometproject.server.game.rooms.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.wired.SaveWiredMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class SaveWiredDataMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int itemId = msg.readInt();

        if(client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) return;

        Room room = client.getPlayer().getEntity().getRoom();

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if ((!isOwner && !hasRights) && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        AbstractWiredItem wiredItem = ((AbstractWiredItem) room.getItems().getFloorItem(itemId));

        if(wiredItem == null) return;

        int paramCount = msg.readInt();

        for (int param = 0; param < paramCount; param++) {
            wiredItem.getWiredData().getParams().put(param, msg.readInt());
        }

        // TODO: Filter txt
        wiredItem.getWiredData().setText(msg.readString());

        wiredItem.getWiredData().getSelectedIds().clear();

        int selectedItemCount = msg.readInt();

        for(int i = 0; i < selectedItemCount; i++) {
            wiredItem.getWiredData().selectItem(msg.readInt());
        }

        if(wiredItem instanceof WiredActionItem) {
            ((WiredActionItem) wiredItem).getWiredData().setDelay(msg.readInt());
        }

        wiredItem.getWiredData().setSelectionType(msg.readInt());
        wiredItem.save();

        client.send(SaveWiredMessageComposer.compose());
        wiredItem.onDataRefresh();
        wiredItem.onDataChange();
    }
}
