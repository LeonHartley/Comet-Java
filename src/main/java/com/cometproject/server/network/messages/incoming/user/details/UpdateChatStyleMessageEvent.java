package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class UpdateChatStyleMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        boolean useOldChat = msg.readBoolean();

        if (client.getPlayer() == null) { return; }

        client.getPlayer().getSettings().setUseOldChat(useOldChat);
        PlayerDao.saveChatStyle(useOldChat, client.getPlayer().getId());
    }
}
