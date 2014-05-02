package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.types.MessengerRequest;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.FriendRequestMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;

public class RequestFriendshipMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String username = msg.readString();

        if (username.equals(client.getPlayer().getData().getUsername()))
            return;

        Session request = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        MessengerRequest req = new MessengerRequest(client.getPlayer().getId(), client.getPlayer().getData().getUsername(), client.getPlayer().getData().getFigure(), client.getPlayer().getData().getMotto());

        if (request != null) {
            request.getPlayer().getMessenger().addRequest(req);
            request.send(FriendRequestMessageComposer.compose(req));
        }

        int userId = PlayerDao.getIdByUsername(username);

        if(userId == 0)
            return;

        MessengerDao.createRequest(client.getPlayer().getId(), userId);
    }
}
