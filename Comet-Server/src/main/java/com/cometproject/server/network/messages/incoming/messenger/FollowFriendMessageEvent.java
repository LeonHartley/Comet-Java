package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.api.game.players.data.components.messenger.IMessengerFriend;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class FollowFriendMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int friendId = msg.readInt();

        IMessengerFriend friend = client.getPlayer().getMessenger().getFriendById(friendId);

        if (friend == null || !friend.isInRoom())
            return;

        Room room = (Room) friend.getSession().getPlayer().getEntity().getRoom();

        if (room == null) {
            // wtf?
            return;
        }

        if (client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            Room roomInstance = client.getPlayer().getEntity().getRoom();

            if (roomInstance.getId() == room.getId()) {
                client.getPlayer().getEntity().leaveRoom(false, false, false);
            }
        }

        client.send(new RoomForwardMessageComposer(room.getId()));
    }
}