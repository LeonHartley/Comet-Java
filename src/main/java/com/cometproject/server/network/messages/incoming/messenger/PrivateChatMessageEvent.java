package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.InstantChatMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.FloodFilterMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PrivateChatMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();
        String message = msg.readString();

        if (userId == -1) {
            for (Session user : Comet.getServer().getNetwork().getSessions().getByPlayerPermission("staff_chat")) {
                if (user == client) continue;
                user.send(InstantChatMessageComposer.compose(client.getPlayer().getData().getUsername() + ": " + message, -1));
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

        long time = System.currentTimeMillis();

        if (!client.getPlayer().getPermissions().hasCommand("bypass_flood")) {
            if (time - client.getPlayer().getLastMessageTime() < 750) {
                client.getPlayer().setFloodFlag(client.getPlayer().getFloodFlag() + 1);

                if (client.getPlayer().getFloodFlag() >= 4) {
                    client.getPlayer().setFloodTime(30);
                    client.getPlayer().setFloodFlag(0);

                    client.send(FloodFilterMessageComposer.compose(client.getPlayer().getFloodTime()));
                }
            }

            if (client.getPlayer().getFloodTime() >= 1) {
                return;
            }

            client.getPlayer().setLastMessageTime(time);
        }

        if (!client.getPlayer().getPermissions().hasPermission("bypass_filter")) {
            FilterResult filterResult = CometManager.getRooms().getFilter().filter(message);

            if (filterResult.isBlocked()) {
                client.send(AdvancedAlertMessageComposer.compose(Locale.get("game.message.blocked").replace("%s", filterResult.getChatMessage())));
                return;
            } else if (filterResult.wasModified()) {
                message = filterResult.getChatMessage();
            }
        }

        friendClient.send(InstantChatMessageComposer.compose(message, client.getPlayer().getId()));
    }
}
