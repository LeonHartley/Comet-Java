package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.game.players.components.types.MessengerRequest;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;

import java.util.ArrayList;
import java.util.List;

public class DeclineFriendshipMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int mode = msg.readInt();
        int count = msg.readInt();

        if(mode == 0) {
            for (MessengerRequest request : client.getPlayer().getMessenger().getRequests()) {
                if (request != null) {
                    MessengerDao.deleteRequestData(request.getFromId(), client.getPlayer().getId());
                    client.getPlayer().getMessenger().removeRequest(request);
                }
            }
        } else {
            List<MessengerRequest> requests = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                requests.add(client.getPlayer().getMessenger().getRequestBySender(msg.readInt()));
            }

            for (MessengerRequest request : requests) {
                if (request != null) {
                    MessengerDao.deleteRequestData(request.getFromId(), client.getPlayer().getId());
                    client.getPlayer().getMessenger().removeRequest(request);
                }
            }

            requests.clear();
        }
    }
}
