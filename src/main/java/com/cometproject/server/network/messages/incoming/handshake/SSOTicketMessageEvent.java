package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.handshake.AuthenticationOKMessageComposer;
import com.cometproject.server.network.messages.outgoing.handshake.HomeRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.handshake.UniqueIDMessageComposer;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolMessageComposer;
import com.cometproject.server.network.messages.outgoing.navigator.FavouriteRoomsMessageComposer;
import com.cometproject.server.network.messages.outgoing.navigator.RoomCategoriesMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.EnableNotificationsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.EnableTradingMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.LoadVolumeSettingsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UnreadMinimailsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.EffectsInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.permissions.FuserightsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class SSOTicketMessageEvent implements IEvent {
    public static String TICKET_DELIMITER = ":";

    public void handle(Session client, Event msg) {
        if(client.getEncryption() == null) {
            CometManager.getLogger().warn("Session was disconnected because RC4 was not initialized!");
            client.disconnect();
            return;
        }

        if(client.getUniqueId().isEmpty() || client.getUniqueId().length() < 10) {
            CometManager.getLogger().warn("Session was disconnected because it did not have a valid machine ID!");
            client.disconnect();
            return;
        }

        // check for machine id ban

        String ticket = msg.readString();

        if (ticket.length() < 10 || ticket.length() > 128) {
            CometManager.getLogger().warn("Session was disconnected because ticket was too long or too short. Length: " + ticket.length());
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

        Session cloneSession = Comet.getServer().getNetwork().getSessions().getByPlayerId(player.getId());

        if (cloneSession != null) {
            cloneSession.disconnect(true);
        }

        if (CometManager.getBans().hasBan(Integer.toString(player.getId()))) {
            CometManager.getLogger().warn("Banned player: " + player.getId() + " tried logging in");
            client.disconnect();
            return;
        }

        player.setSession(client);
        client.setPlayer(player);


        String ipAddress = client.getIpAddress();

        if (ipAddress != null && !ipAddress.isEmpty()) {
            if (CometManager.getBans().hasBan(ipAddress)) {
                CometManager.getLogger().warn("Banned player: " + player.getId() + " tried logging in");
                client.disconnect();
                return;
            }

            client.getPlayer().getData().setIpAddress(ipAddress);
        }

        CometManager.getRooms().loadRoomsForUser(player);

        client.getLogger().debug(client.getPlayer().getData().getUsername() + " logged in");

        PlayerDao.updatePlayerStatus(player, true, true);

        client.sendQueue(UniqueIDMessageComposer.compose(client.getUniqueId())).
                sendQueue(AuthenticationOKMessageComposer.compose()).
                sendQueue(FuserightsMessageComposer.compose(client.getPlayer().getSubscription().exists(), client.getPlayer().getData().getRank())).
                sendQueue(MotdNotificationComposer.compose()).
                sendQueue(FavouriteRoomsMessageComposer.compose()).
                sendQueue(UnreadMinimailsMessageComposer.compose(0)).
                sendQueue(EnableTradingMessageComposer.compose(true)).
                sendQueue(EnableNotificationsMessageComposer.compose());
//                .sendQueue(LoadVolumeSettingsMessageComposer.compose(player));

//        if (player.getSettings().getHomeRoom() > 0) {
            client.sendQueue(HomeRoomMessageComposer.compose(player.getSettings().getHomeRoom()));
//        }

        if (client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.sendQueue(ModToolMessageComposer.compose());
        }

        client.send(EffectsInventoryMessageComposer.compose());

        client.flush();
    }
}
