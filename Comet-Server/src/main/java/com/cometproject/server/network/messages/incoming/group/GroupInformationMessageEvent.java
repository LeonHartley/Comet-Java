package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GroupInformationMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        boolean flag = msg.readBoolean();

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null)
            return;

        client.send(group.composeInformation(flag, client.getPlayer().getId()));
    }
}
