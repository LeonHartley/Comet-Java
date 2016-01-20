package com.cometproject.server.network.messages.incoming.group.settings;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SendFloorItemMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.BadgeUtil;

import java.util.ArrayList;
import java.util.List;


public class ModifyGroupBadgeMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        if (!client.getPlayer().getGroups().contains(groupId))
            return;

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null || group.getData().getOwnerId() != client.getPlayer().getId())
            return;

        int stateCount = msg.readInt();

        int groupBase = msg.readInt();
        int groupBaseColour = msg.readInt();
        int groupItemsLength = msg.readInt();

        List<Integer> groupItems = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            groupItems.add(msg.readInt());
        }

        String badge = BadgeUtil.generate(groupBase, groupBaseColour, groupItems);

        group.getData().setBadge(badge);
        group.getData().save();

        if (client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            Room room = client.getPlayer().getEntity().getRoom();

            room.getEntities().broadcastMessage(new RoomDataMessageComposer(room));

            for (RoomItemFloor roomItemFloor : room.getItems().getByInteraction("group_item")) {
                if (roomItemFloor instanceof GroupFloorItem) {
                    room.getEntities().broadcastMessage(new RemoveFloorItemMessageComposer(roomItemFloor.getVirtualId(), 0));
                    room.getEntities().broadcastMessage(new SendFloorItemMessageComposer(roomItemFloor));
                }
            }

        }

//        client.send(new ManageGroupMessageComposer(group));
    }
}
