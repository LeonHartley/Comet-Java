package com.cometsrv.network.messages.incoming.room.engine;

import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.wired.types.TriggerType;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.items.FloorItemsMessageComposer;
import com.cometsrv.network.messages.outgoing.room.items.WallItemsMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class FollowRoomInfoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {

        Room room = client.getPlayer().getAvatar().getRoom();

        client.send(FloorItemsMessageComposer.compose(room));
        client.send(WallItemsMessageComposer.compose(room));

        room.getWired().trigger(TriggerType.ENTER_ROOM, null, client.getPlayer().getAvatar());

        //client.send(FollowRoomDataMessageComposer.compose(client.getPlayer().getAvatar().getRoom()));
    }
}
