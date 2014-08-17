package com.cometproject.server.network.messages.incoming.group.settings;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.ManageGroupMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.BadgeUtil;

import java.util.ArrayList;
import java.util.List;

public class ModifyGroupBadgeMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();

        if (!client.getPlayer().getGroups().contains(groupId))
            return;

        Group group = CometManager.getGroups().get(groupId);

        if (group == null || group.getData().getOwnerId() != client.getPlayer().getId())
            return;

        int stateCount = msg.readInt();

        int groupBase = msg.readInt();
        int groupBaseColour = msg.readInt();

        msg.readInt();

        List<Integer> groupItems = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            groupItems.add(msg.readInt());
        }

        String badge = BadgeUtil.generate(groupBase, groupBaseColour, groupItems);

        group.getData().setBadge(badge);
        group.getData().save();

        if (CometManager.getRooms().isActive(group.getData().getRoomId())) {
            Room room = CometManager.getRooms().get(group.getData().getRoomId());

            room.getEntities().broadcastMessage(RoomDataMessageComposer.compose(room));
        }

        client.send(ManageGroupMessageComposer.compose(group));
    }
}
