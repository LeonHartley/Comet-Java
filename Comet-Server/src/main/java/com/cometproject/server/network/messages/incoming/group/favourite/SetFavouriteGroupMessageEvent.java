package com.cometproject.server.network.messages.incoming.group.favourite;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.server.composers.group.GroupBadgesMessageComposer;
import com.cometproject.server.composers.group.UpdateFavouriteGroupMessageComposer;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class SetFavouriteGroupMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        if (!client.getPlayer().getGroups().contains(groupId)) {
            return;
        }

        IGroupData group = GameContext.getCurrent().getGroupService().getData(groupId);

        if (group == null)
            return;

        if (client.getPlayer().getData().getFavouriteGroup() == groupId) {
            // Already the favourite group, why set it again?
            return;
        }

        client.getPlayer().getData().setFavouriteGroup(groupId);
        client.getPlayer().getData().save();

        if (client.getPlayer().getEntity() != null) {
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new GroupBadgesMessageComposer(groupId, group.getBadge()));

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new LeaveRoomMessageComposer(client.getPlayer().getEntity().getId()));
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new AvatarsMessageComposer(client.getPlayer().getEntity()));

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new AvatarUpdateMessageComposer(client.getPlayer().getEntity()));
        } else {
            client.send(new GroupBadgesMessageComposer(groupId, group.getBadge()));
        }

        client.send(new UpdateFavouriteGroupMessageComposer(client.getPlayer().getId()));
    }
}
