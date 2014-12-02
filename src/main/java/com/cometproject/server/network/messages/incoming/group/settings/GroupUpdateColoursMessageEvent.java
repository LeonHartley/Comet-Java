package com.cometproject.server.network.messages.incoming.group.settings;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.ManageGroupMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SendFloorItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GroupUpdateColoursMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();

        Group group = CometManager.getGroups().get(groupId);

        if (group == null || client.getPlayer().getId() != group.getData().getOwnerId())
            return;

        int colourA = msg.readInt();
        int colourB = msg.readInt();

        group.getData().setColourA(colourA);
        group.getData().setColourB(colourB);

        group.getData().save();

//        client.send(ManageGroupMessageComposer.compose(group));

        if(client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            Room room = client.getPlayer().getEntity().getRoom();

            for(RoomItemFloor roomItemFloor : room.getItems().getByInteraction("group_%")) {
                if(roomItemFloor instanceof GroupFloorItem) {
                    room.getEntities().broadcastMessage(RemoveFloorItemMessageComposer.compose(roomItemFloor.getId(), 0));
                    room.getEntities().broadcastMessage(SendFloorItemMessageComposer.compose(roomItemFloor, room));
                }
            }

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(RoomDataMessageComposer.compose(room));
        }

    }
}
