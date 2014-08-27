package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.game.players.components.types.MessengerRequest;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;

import java.util.ArrayList;
import java.util.List;

public class AcceptFriendshipMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int amount = msg.readInt();
        List<MessengerRequest> requests = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            requests.add(client.getPlayer().getMessenger().getRequestBySender(msg.readInt()));
        }

        for (MessengerRequest request : requests) {

            MessengerDao.createFriendship(request.getFromId(), client.getPlayer().getId());
            MessengerDao.deleteRequestData(request.getFromId(), client.getPlayer().getId());

            Session friend = Comet.getServer().getNetwork().getSessions().getByPlayerId(request.getFromId());

            if (friend != null) {
                friend.getPlayer().getMessenger().addFriend(new MessengerFriend(client.getPlayer().getId()));
                friend.getPlayer().getMessenger().sendStatus(true, friend.getPlayer().getEntity() != null);
            } else {
                client.getPlayer().getMessenger().sendOffline(request, false, false);
            }

            client.getPlayer().getMessenger().addFriend(new MessengerFriend(request.getFromId()));
            client.getPlayer().getMessenger().sendStatus(true, client.getPlayer().getEntity() != null);
        }

        requests.clear();
    }
}
