package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.game.players.components.types.messenger.MessengerFriend;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class FollowFriendMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int friendId = msg.readInt();

        MessengerFriend friend = client.getPlayer().getMessenger().getFriendById(friendId);

        if (friend == null || !friend.isInRoom())
            return;

        Room room = friend.getSession().getPlayer().getEntity().getRoom();

        if (room == null) {
            // wtf?
            return;
        }

        client.send(new RoomForwardMessageComposer(room.getId()));
    }
}