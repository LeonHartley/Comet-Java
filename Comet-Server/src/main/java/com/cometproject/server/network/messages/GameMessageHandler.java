package com.cometproject.server.network.messages;

import com.cometproject.api.networking.messages.IMessageEvent;
import com.cometproject.networking.api.messages.IMessageHandler;
import com.cometproject.networking.api.sessions.INetSession;
import com.cometproject.server.network.sessions.net.NetSession;
import com.cometproject.server.protocol.messages.MessageEvent;

public class GameMessageHandler implements IMessageHandler {

    @Override
    public void handleMessage(IMessageEvent messageEvent, INetSession session) {
        if (!(session instanceof NetSession)) {
            return;
        }

        final NetSession netSession = (NetSession) session;
        netSession.getGameSession().handleMessageEvent((MessageEvent) messageEvent);
    }
}
