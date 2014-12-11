package com.cometproject.server.network.messages.incoming.group.favourite;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.GroupBadgesMessageComposer;
import com.cometproject.server.network.messages.outgoing.group.UpdateFavouriteGroupMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class SetFavouriteGroupMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();

        if (!client.getPlayer().getGroups().contains(groupId)) {
            return;
        }

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null)
            return;

        client.getPlayer().getData().setFavouriteGroup(groupId);
        client.getPlayer().getData().save();

        if (client.getPlayer().getEntity() != null) {
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(GroupBadgesMessageComposer.compose(groupId, group.getData().getBadge()));

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(LeaveRoomMessageComposer.compose(client.getPlayer().getEntity().getId()));
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(AvatarsMessageComposer.compose(client.getPlayer().getEntity()));
        } else {
            client.send(GroupBadgesMessageComposer.compose(groupId, group.getData().getBadge()));
        }

        client.send(UpdateFavouriteGroupMessageComposer.compose(client.getPlayer().getId()));
    }
}
