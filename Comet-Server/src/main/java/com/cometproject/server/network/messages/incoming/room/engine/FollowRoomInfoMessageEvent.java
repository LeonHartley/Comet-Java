package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.FollowRoomDataMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class FollowRoomInfoMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int roomId = msg.readInt();
        boolean isLoading = msg.readInt() == 1;
        boolean checkEntry = msg.readInt() == 1;

//        if (roomId != 0 && !isInSameRoom) {
            Room room = RoomManager.getInstance().get(roomId);
//
            if (room == null || room.getData() == null) {
                return;
            }
//
//            boolean checkEntry = true;
//
//            if (room.getRights().hasRights(client.getPlayer().getId())) {
//                checkEntry = false;
//            } else if (client.getPlayer().isTeleporting() || client.getPlayer().isBypassingRoomAuth()) {
//                checkEntry = false;
//            }

            client.send(new FollowRoomDataMessageComposer(room.getData(), isLoading, checkEntry, room.getRights().hasRights(client.getPlayer().getId()) || client.getPlayer().getPermissions().getRank().roomFullControl()));
//        }
    }
}
