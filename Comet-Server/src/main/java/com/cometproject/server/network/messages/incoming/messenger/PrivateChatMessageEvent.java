package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.players.components.types.messenger.MessengerFriend;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.entries.MessengerChatLogEntry;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.messenger.InstantChatMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class PrivateChatMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int userId = msg.readInt();
        String message = msg.readString();

        if (userId == -1 && client.getPlayer().getPermissions().hasPermission("staff_chat")) {
            for (ISession user : NetworkManager.getInstance().getSessions().getByPlayerPermission("staff_chat")) {
                if (user == client) continue;
                user.send(new InstantChatMessageComposer(client.getPlayer().getData().getUsername() + ": " + message, -1));
            }
            return;
        }

        MessengerFriend friend = client.getPlayer().getMessenger().getFriendById(userId);

        if (friend == null) {
            return;
        }

        Session friendClient = friend.getSession();

        if (friendClient == null) {
            return;
        }

        final long time = System.currentTimeMillis();

        if (!client.getPlayer().getPermissions().hasPermission("bypass_flood")) {
            if (time - client.getPlayer().getMessengerLastMessageTime() < 750) {
                client.getPlayer().setMessengerFloodFlag(client.getPlayer().getMessengerFloodFlag() + 1);

                if (client.getPlayer().getMessengerFloodFlag() >= 4) {
                    client.getPlayer().setMessengerFloodTime(30);
                    client.getPlayer().setMessengerFloodFlag(0);

                }
            }

            if (client.getPlayer().getMessengerFloodTime() >= 1) {
                return;
            }

            client.getPlayer().setMessengerLastMessageTime(time);
        }

        if (!client.getPlayer().getPermissions().hasPermission("bypass_filter")) {
            FilterResult filterResult = RoomManager.getInstance().getFilter().filter(message);

            if (filterResult.isBlocked()) {
                client.send(new AdvancedAlertMessageComposer(Locale.get("game.message.blocked").replace("%s", filterResult.getMessage())));
                return;
            } else if (filterResult.wasModified()) {
                message = filterResult.getMessage();
            }
        }

        try {
            if (LogManager.ENABLED && CometSettings.logMessengerMessages)
                LogManager.getInstance().getStore().getLogEntryContainer().put(new MessengerChatLogEntry(client.getPlayer().getEntity().getRoom().getId(), userId, message));
        } catch (Exception ignored) {

        }


        friendClient.send(new InstantChatMessageComposer(message, client.getPlayer().getId()));
    }
}
