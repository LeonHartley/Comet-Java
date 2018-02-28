package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.server.composers.catalog.groups.GroupElementsMessageComposer;
import com.cometproject.server.composers.catalog.groups.GroupPartsMessageComposer;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Lists;

import java.util.List;


public class BuyGroupDialogMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        final List<IRoomData> roomData = Lists.newArrayList();

        for (Integer roomId : client.getPlayer().getRooms()) {
            final IRoomData room = GameContext.getCurrent().getRoomService().getRoomData(roomId);

            if (room.getGroupId() < 1) {
                roomData.add(room);
            }
//            if (GroupManager.getInstance().getGroupByRoomId(room) == null)
//                roomData.add(RoomManager.getInstance().getRoomData(room));
        }

        client.send(new GroupPartsMessageComposer(roomData));
        client.send(new GroupElementsMessageComposer(GameContext.getCurrent().getGroupService().getItemService()));
    }
}
