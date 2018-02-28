package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.server.composers.group.GroupFurnitureWidgetMessageComposer;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class GroupFurnitureWidgetMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int virtualId = msg.readInt();

        long itemId = ItemManager.getInstance().getItemIdByVirtualId(virtualId);

        if (client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            RoomItemFloor floorItem = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

            if (floorItem != null && floorItem instanceof GroupFloorItem) {
                IGroup group = GameContext.getCurrent().getGroupService().getGroup(((GroupFloorItem) floorItem).getGroupId());

                if (group != null) {
                    client.send(new GroupFurnitureWidgetMessageComposer(virtualId, group.getId(), group.getData().getTitle(), group.getData().getRoomId(), client.getPlayer().getGroups().contains(group.getId()), false));
                }
            }
        }
    }
}
