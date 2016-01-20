package com.cometproject.server.network.messages.incoming.group.settings;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SendFloorItemMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GroupUpdateColoursMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null || client.getPlayer().getId() != group.getData().getOwnerId())
            return;

        int colourA = msg.readInt();
        int colourB = msg.readInt();

        group.getData().setColourA(colourA);
        group.getData().setColourB(colourB);

        group.getData().save();

//        client.send(new ManageGroupMessageComposer(group));

        if (client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            Room room = client.getPlayer().getEntity().getRoom();

            for (RoomItemFloor roomItemFloor : room.getItems().getByInteraction("group_%")) {
                if (roomItemFloor instanceof GroupFloorItem) {
                    room.getEntities().broadcastMessage(new RemoveFloorItemMessageComposer(roomItemFloor.getVirtualId(), 0));
                    room.getEntities().broadcastMessage(new SendFloorItemMessageComposer(roomItemFloor));
                }
            }

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new RoomDataMessageComposer(room));
        }

    }
}
