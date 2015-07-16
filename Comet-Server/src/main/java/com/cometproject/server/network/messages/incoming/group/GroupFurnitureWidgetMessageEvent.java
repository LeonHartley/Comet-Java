package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.GroupFurnitureWidgetMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GroupFurnitureWidgetMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int itemId = msg.readInt();

        if (client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            RoomItemFloor floorItem = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

            if (floorItem != null && floorItem instanceof GroupFloorItem) {
                Group group = GroupManager.getInstance().get(((GroupFloorItem) floorItem).getGroupId());

                if (group != null) {
                    client.send(new GroupFurnitureWidgetMessageComposer(itemId, group.getId(), group.getData().getTitle(), group.getData().getRoomId(), client.getPlayer().getGroups().contains(group.getId()), false));
                }
            }
        }
    }
}
