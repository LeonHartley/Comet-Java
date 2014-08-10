package com.cometproject.server.network.messages.incoming.group.settings;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupType;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ModifyGroupSettingsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();

        if(!client.getPlayer().getGroups().contains(groupId))
            return;

        Group group = CometManager.getGroups().get(groupId);

        if(group == null || group.getData().getOwnerId() != client.getPlayer().getId())
            return;

        int type = msg.readInt();
        int rightsType = msg.readInt();

        if(GroupType.valueOf(type) != group.getData().getType()) {
            group.getMembershipComponent().clearRequests();
        }

        group.getData().setType(GroupType.valueOf(type));
        group.getData().setAdminDeco(rightsType == 1);

        group.getData().save();

        if(CometManager.getRooms().isActive(group.getData().getRoomId())) {
            Room room = CometManager.getRooms().get(group.getData().getRoomId());

            room.getEntities().broadcastMessage(RoomDataMessageComposer.compose(room));
        }
    }
}
