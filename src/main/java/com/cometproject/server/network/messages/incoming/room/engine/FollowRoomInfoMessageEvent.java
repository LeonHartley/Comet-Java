package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.game.wired.types.TriggerType;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.FloorItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.WallItemsMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class FollowRoomInfoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int roomId = msg.readInt();
        boolean isInSameRoom = msg.readInt() == 1;

        if(client.getPlayer().getEntity() != null && roomId == client.getPlayer().getEntity().getRoom().getId()) {
            client.getPlayer().getEntity().getRoom().getWired().trigger(TriggerType.ENTER_ROOM, null, client.getPlayer().getEntity());
            return;
        }

        if(roomId != 0 && !isInSameRoom) {
            Room room = GameEngine.getRooms().get(roomId);

            if(room == null) {
                return;
            }

            Composer composer = new Composer(Composers.RoomDataMessageComposer);

            composer.writeBoolean(false);
            composer.writeInt(room.getId());
            composer.writeString(room.getData().getName());
            composer.writeBoolean(true);
            composer.writeInt(room.getData().getOwnerId());
            composer.writeString(room.getData().getOwner());
            composer.writeInt(RoomWriter.roomAccessToNumber(room.getData().getAccess()));
            composer.writeInt(room.getEntities() == null ? 0 : room.getEntities().playerCount());
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

            client.send(composer);
        }
    }
}
