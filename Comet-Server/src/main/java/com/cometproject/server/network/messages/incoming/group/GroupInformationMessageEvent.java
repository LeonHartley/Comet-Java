package com.cometproject.server.network.messages.incoming.group;


import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.server.composers.group.GroupInformationMessageComposer;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class GroupInformationMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        boolean flag = msg.readBoolean();

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null)
            return;

        client.send(new GroupInformationMessageComposer(group, GameContext.getCurrent().getRoomService().getRoomData(group.getData().getRoomId()), flag,
                client.getPlayer().getId() == group.getData().getOwnerId(), group.getMembers().getAdministrators().contains(client.getPlayer().getId()),
                group.getMembers().getAll().containsKey(client.getPlayer().getId()) ? 1 : group.getMembers().getMembershipRequests().contains(client.getPlayer().getId()) ? 2 : 0));

    }
}
