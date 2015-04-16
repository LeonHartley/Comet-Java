package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.RoomManager;
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

        // TODO: Tell the hotel owners to remove the id:ticket stuff
        Player player = null;
        boolean normalPlayerLoad = false;

        if (ticket.contains(TICKET_DELIMITER)) {
            String[] ticketData = ticket.split(TICKET_DELIMITER);

            if (ticketData.length == 2) {
                String authTicket = ticketData[1];

                player = PlayerDao.getPlayer(authTicket);
            } else {
                normalPlayerLoad = true;
            }
        } else {
            normalPlayerLoad = true;
        }

        if (normalPlayerLoad) {
            player = PlayerDao.getPlayer(ticket);
        }

        if (player == null) {
            player = PlayerDao.getPlayerFallback(ticket);

            if (player == null) {
                client.disconnect(false);
                return;
            }
        }

        Session cloneSession = NetworkManager.getInstance().getSessions().getByPlayerId(player.getId());

        if (cloneSession != null) {
            cloneSession.disconnect(true);
        }

        if (BanManager.getInstance().hasBan(Integer.toString(player.getId()), BanType.USER)) {
            client.getLogger().warn("Banned player: " + player.getId() + " tried logging in");
            client.disconnect("banned");
            return;
        }

        player.setSession(client);
        client.setPlayer(player);

        String ipAddress = client.getIpAddress();

        if (ipAddress != null && !ipAddress.isEmpty()) {
            if (BanManager.getInstance().hasBan(ipAddress, BanType.IP)) {
                client.getLogger().warn("Banned player: " + player.getId() + " tried logging in");
                client.disconnect("banned");
                return;
            }

            client.getPlayer().getData().setIpAddress(ipAddress);
        }

        if (CometSettings.storeAccess)
            PlayerAccessDao.saveAccess(player.getId(), client.getUniqueId(), ipAddress);

        RoomManager.getInstance().loadRoomsForUser(player);

        client.getLogger().debug(client.getPlayer().getData().getUsername() + " logged in");

        PlayerDao.updatePlayerStatus(player, true, true);

        client.sendQueue(new UniqueIDMessageComposer(client.getUniqueId())).
                sendQueue(new AuthenticationOKMessageComposer()).
                sendQueue(new FuserightsMessageComposer(client.getPlayer().getSubscription().exists(), client.getPlayer().getData().getRank())).
                sendQueue(new MotdNotificationComposer()).
                sendQueue(new FavouriteRoomsMessageComposer()).
                sendQueue(new UnreadMinimailsMessageComposer()).
                sendQueue(new EnableTradingMessageComposer(true)).
                sendQueue(new EnableNotificationsMessageComposer())
                .sendQueue(new PlayerSettingsMessageComposer(player.getSettings()));

//        if (player.getSettings().getHomeRoom() > 0) {
        client.sendQueue(new HomeRoomMessageComposer(player.getSettings().getHomeRoom()));
//        }

        if (client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.sendQueue(new ModToolMessageComposer());
        }

        client.send(new EffectsInventoryMessageComposer());

        client.flush();
    }
}
