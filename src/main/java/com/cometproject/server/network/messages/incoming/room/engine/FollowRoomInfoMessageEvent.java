package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.game.wired.types.TriggerType;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.FloorItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.WallItemsMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class FollowRoomInfoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int roomId = msg.readInt();
        boolean isInSameRoom = msg.readInt() == 1;

        if (client.getPlayer().getEntity() != null && roomId == client.getPlayer().getEntity().getRoom().getId()) {
            client.send(FloorItemsMessageComposer.compose(client.getPlayer().getEntity().getRoom()));
            client.send(WallItemsMessageComposer.compose(client.getPlayer().getEntity().getRoom()));

            client.getPlayer().getEntity().getRoom().getWired().trigger(TriggerType.ENTER_ROOM, null, client.getPlayer().getEntity());
            return;
        }

        if (roomId != 0 && !isInSameRoom) {
            RoomData roomData = CometManager.getRooms().getRoomData(roomId);

            if (roomData == null) {
                return;
            }

            Composer composer = new Composer(Composers.RoomDataMessageComposer);

            composer.writeBoolean(false);

            RoomWriter.write(roomData, composer);

            composer.writeBoolean(true);
            composer.writeBoolean(false);
            composer.writeBoolean(true);
            composer.writeInt(0);
            composer.writeInt(0);
            composer.writeInt(0);
            composer.writeBoolean(false);
            composer.writeBoolean(true);
            composer.writeBoolean(true);
            composer.writeInt(1);
            composer.writeInt(0);
            composer.writeInt(1);
            composer.writeInt(14);
            composer.writeBoolean(false);
            composer.writeBoolean(false);
            composer.writeBoolean(false);
            composer.writeBoolean(true);

            client.send(composer);
        }
    }
}
