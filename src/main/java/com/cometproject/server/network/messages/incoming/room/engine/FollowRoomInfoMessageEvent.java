package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.types.TriggerType;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.FloorItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.WallItemsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class FollowRoomInfoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {

        Room room = client.getPlayer().getEntity().getRoom();

        client.send(FloorItemsMessageComposer.compose(room));
        client.send(WallItemsMessageComposer.compose(room));

        room.getWired().trigger(TriggerType.ENTER_ROOM, null, client.getPlayer().getEntity());

        //client.send(FollowRoomDataMessageComposer.compose(client.getPlayer().getEntity().getRoom()));
    }
}
