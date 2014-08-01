package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.GroupInformationMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GroupInformationMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();
        boolean flag = msg.readBoolean();

        Group group = CometManager.getGroups().get(groupId);

        if(group == null)
            return;

        client.send(GroupInformationMessageComposer.compose(group, CometManager.getRooms().getRoomData(group.getData().getRoomId()), flag, true, false));
    }
}
