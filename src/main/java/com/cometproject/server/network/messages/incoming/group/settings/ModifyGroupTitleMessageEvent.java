package com.cometproject.server.network.messages.incoming.group.settings;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ModifyGroupTitleMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();
        String title = msg.readString();
        String description = msg.readString();

        if(!client.getPlayer().getGroups().contains(groupId))
            return;

        Group group = CometManager.getGroups().get(groupId);

        if(group == null)
            return;

        GroupMember groupMember = group.getMembershipComponent().getMembers().get(client.getPlayer().getId());

        if(!groupMember.getAccessLevel().isAdmin())
            return;

        group.getData().setTitle(title);
        group.getData().setDescription(description);
        group.getData().save();

        if(CometManager.getRooms().isActive(group.getData().getRoomId())) {
            Room room = CometManager.getRooms().get(group.getData().getRoomId());

            room.getEntities().broadcastMessage(RoomDataMessageComposer.compose(room));
        }
    }
}
