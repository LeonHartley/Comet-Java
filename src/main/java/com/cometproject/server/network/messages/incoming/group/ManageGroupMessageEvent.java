package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.ManageGroupMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ManageGroupMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();

        Group group = CometManager.getGroups().get(groupId);

        if(group == null)
            return;

        client.send(ManageGroupMessageComposer.compose(group));
    }
}
