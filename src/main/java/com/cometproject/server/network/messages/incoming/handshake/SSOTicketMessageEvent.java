package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.handshake.HomeRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.handshake.LoginMessageComposer;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolMessageComposer;
import com.cometproject.server.network.messages.outgoing.navigator.RoomCategoriesMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationComposer;
import com.cometproject.server.network.messages.outgoing.user.details.LoadVolumeSettingsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.permissions.FuserightsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.net.InetSocketAddress;

public class SSOTicketMessageEvent implements IEvent {
    public static String TICKET_DELIMITER = ":";

    public void handle(Session client, Event msg) {
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

        if (!ipAddress.isEmpty()) {
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

        client.sendQueue(LoginMessageComposer.compose()).
                sendQueue(client.getPlayer().composeCreditBalance()).
                sendQueue(client.getPlayer().composeCurrenciesBalance()).
                sendQueue(FuserightsMessageComposer.compose(client.getPlayer().getSubscription().exists(), client.getPlayer().getData().getRank())).
                sendQueue(MotdNotificationComposer.compose())
                .sendQueue(LoadVolumeSettingsMessageComposer.compose(player));

        if (player.getSettings().getHomeRoom() > 0) {
            client.sendQueue(HomeRoomMessageComposer.compose(player.getSettings().getHomeRoom()));
        }

        if (client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.sendQueue(ModToolMessageComposer.compose());
        }

        client.sendQueue(RoomCategoriesMessageComposer.compose(CometManager.getNavigator().getCategories(), client.getPlayer().getData().getRank()));
        client.flush();
    }
}
