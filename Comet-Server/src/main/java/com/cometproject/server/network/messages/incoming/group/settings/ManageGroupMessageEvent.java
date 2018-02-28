package com.cometproject.server.network.messages.incoming.group.settings;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.server.composers.catalog.groups.GroupElementsMessageComposer;
import com.cometproject.server.composers.group.ManageGroupMessageComposer;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class ManageGroupMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null || client.getPlayer().getId() != group.getData().getOwnerId())
            return;

        client.send(new GroupElementsMessageComposer(GameContext.getCurrent().getGroupService().getItemService())); // TODO: Send this once
        client.send(new ManageGroupMessageComposer(group));
    }
}
