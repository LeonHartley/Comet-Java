package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.api.game.quests.QuestType;
import com.cometproject.server.config.Locale;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.messenger.FriendRequestMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;


public class RequestFriendshipMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String username = msg.readString();

        if (username.equals(client.getPlayer().getData().getUsername()))
            return;

        Session request = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (request == null || request.getPlayer() == null || request.getPlayer().getMessenger() == null) {
            int userId = PlayerDao.getIdByUsername(username);

            if(userId != 0) {
                MessengerDao.createRequest(client.getPlayer().getId(), userId);
            }

            return;
        }

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

        client.getPlayer().getQuests().progressQuest(QuestType.SOCIAL_FRIEND);
        MessengerDao.createRequest(client.getPlayer().getId(), userId);
    }
}