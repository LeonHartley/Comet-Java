package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;


public class DeclineFriendshipMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        final boolean allRequests = msg.readBoolean();
        final int mode = msg.readInt();

        if (allRequests) {
            MessengerDao.deleteRequestDataByRecieverId(client.getPlayer().getId());
            client.getPlayer().getMessenger().clearRequests();
            return;
        }

        final int sender = msg.readInt();
        final Integer messengerRequest = client.getPlayer().getMessenger().getRequestBySender(sender);

        if (messengerRequest != null) {
            MessengerDao.deleteRequestData(messengerRequest, client.getPlayer().getId());
            client.getPlayer().getMessenger().removeRequest(messengerRequest);
        }
    }
}
