package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.api.events.players.OnPlayerLoginEvent;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.modules.ModuleManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.handshake.AuthenticationOKMessageComposer;
import com.cometproject.server.network.messages.outgoing.handshake.HomeRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.handshake.UniqueIDMessageComposer;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolMessageComposer;
import com.cometproject.server.network.messages.outgoing.navigator.FavouriteRoomsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationComposer;
import com.cometproject.server.network.messages.outgoing.user.details.EnableNotificationsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.EnableTradingMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.PlayerSettingsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UnreadMinimailsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.EffectsInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.permissions.FuserightsMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerAccessDao;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class SSOTicketMessageEvent implements Event {
    public static final String TICKET_DELIMITER = ":";

    public void handle(Session client, MessageEvent msg) {
//        if (client.getEncryption() == null) {
//            CometManager.getLogger().warn("Session was disconnected because RC4 was not initialized!");
//            client.disconnect();
//            return;
//        }

//        if (client.getUniqueId().isEmpty() || client.getUniqueId().length() < 10) {
//            client.getLogger().warn("Session was disconnected because it did not have a valid machine ID!");
//            client.disconnect();
//            return;
//        }

        if (BanManager.getInstance().hasBan(client.getUniqueId(), BanType.MACHINE)) {
            client.getLogger().warn("Banned player: " + client.getUniqueId() + " tried logging in");
            return;
        }

        String ticket = msg.readString();

        if (ticket.length() < 10 || ticket.length() > 128) {
            client.getLogger().warn("Session was disconnected because ticket was too long or too short. Length: " + ticket.length());
            client.disconnect();
            return;
        }

        PlayerManager.getInstance().submitLoginRequest(client, ticket);
    }
}
