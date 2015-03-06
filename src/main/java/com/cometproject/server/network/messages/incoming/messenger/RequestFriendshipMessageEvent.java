package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.config.Locale;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.FriendRequestMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;


public class RequestFriendshipMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String username = msg.readString();

        if (username.equals(client.getPlayer().getData().getUsername()))
            return;

        Session request = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (request == null || request.getPlayer() == null) return;

        if (!request.getPlayer().getSettings().getAllowFriendRequests()) {
            client.send(new AdvancedAlertMessageComposer(Locale.get("game.messenger.friendrequests.disabled")));
            return;
        }

        if (request.getPlayer().getMessenger().hasRequestFrom(client.getPlayer().getId()))
            return;

        request.getPlayer().getMessenger().addRequest(client.getPlayer().getId());
        request.send(new FriendRequestMessageComposer(client.getPlayer().getData()));

        int userId = PlayerDao.getIdByUsername(username);

        if (userId == 0)
            return;

        MessengerDao.createRequest(client.getPlayer().getId(), userId);
    }
}