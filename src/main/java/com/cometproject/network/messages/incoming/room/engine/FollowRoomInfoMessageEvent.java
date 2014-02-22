package com.cometproject.network.messages.incoming.room.engine;

import com.cometproject.game.rooms.types.Room;
import com.cometproject.game.wired.types.TriggerType;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.items.FloorItemsMessageComposer;
import com.cometproject.network.messages.outgoing.room.items.WallItemsMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class FollowRoomInfoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {

        Room room = client.getPlayer().getEntity().getRoom();

        client.send(FloorItemsMessageComposer.compose(room));
        client.send(WallItemsMessageComposer.compose(room));

        room.getWired().trigger(TriggerType.ENTER_ROOM, null, client.getPlayer().getEntity());

        //client.send(FollowRoomDataMessageComposer.compose(client.getPlayer().getEntity().getRoom()));
    }
}
