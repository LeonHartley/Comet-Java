package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.game.players.components.types.messenger.MessengerFriend;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;

import java.util.ArrayList;
import java.util.List;


public class AcceptFriendshipMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int amount = msg.readInt();
        List<Integer> requests = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            requests.add(client.getPlayer().getMessenger().getRequestBySender(msg.readInt()));
        }

        for (Integer request : requests) {
            if (request == null) continue;

            MessengerDao.createFriendship(request, client.getPlayer().getId());
            MessengerDao.deleteRequestData(request, client.getPlayer().getId());

            Session friend = NetworkManager.getInstance().getSessions().getByPlayerId(request);

            if (friend != null) {
                friend.getPlayer().getMessenger().addFriend(new MessengerFriend(client.getPlayer().getId()));
                friend.getPlayer().getMessenger().sendStatus(true, friend.getPlayer().getEntity() != null);
            } else {
                client.getPlayer().getMessenger().sendOffline(request, false, false);
            }

            client.getPlayer().getMessenger().addFriend(new MessengerFriend(request));
            client.getPlayer().getMessenger().sendStatus(true, client.getPlayer().getEntity() != null);
        }

        requests.clear();
    }
}
