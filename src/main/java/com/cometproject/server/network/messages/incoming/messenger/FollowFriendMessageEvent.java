package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.FollowFriendMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class FollowFriendMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int friendId = msg.readInt();

        MessengerFriend friend = client.getPlayer().getMessenger().getFriendById(friendId);

        if(friend == null || friend.updateClient() == null || friend.getClient().getPlayer() == null || friend.getClient().getPlayer().getEntity() == null)
            return;

        Room room = friend.getClient().getPlayer().getEntity().getRoom();

        client.send(FollowFriendMessageComposer.compose(room.getId()));

        /*Composer composer = new Composer(Composers.RoomDataMessageComposer);

        composer.writeBoolean(false);
        composer.writeInt(room.getId());
        composer.writeString(room.getData().getName());
        composer.writeBoolean(true);
        composer.writeInt(room.getData().getOwnerId());
        composer.writeString(room.getData().getOwner());
        composer.writeInt(RoomWriter.roomAccessToNumber(room.getData().getAccess()));
        composer.writeInt(room.getEntities().playerCount());
        composer.writeInt(room.getData().getMaxUsers());
        composer.writeString(room.getData().getDescription());
        composer.writeInt(0);
        composer.writeInt(0);
        composer.writeInt(room.getData().getScore());
        composer.writeInt(0);
        composer.writeInt(room.getData().getCategory().getId());
        composer.writeInt(0); // group shit here!!
        composer.writeInt(0); // group shit here!!
        composer.writeString("");
        composer.writeInt(room.getData().getTags().length);

        for(String tag : room.getData().getTags()) {
            composer.writeString(tag);
        }

        composer.writeInt(0);
        composer.writeInt(0);
        composer.writeInt(0);
        composer.writeBoolean(false);
        composer.writeBoolean(true);
        composer.writeInt(0);
        composer.writeInt(0);
        composer.writeBoolean(true);
        composer.writeBoolean(false);
        composer.writeBoolean(true);
        composer.writeBoolean(true);
        composer.writeInt(0);
        composer.writeInt(0);
        composer.writeInt(0);
        composer.writeBoolean(true);

        client.send(composer);*/
    }
}