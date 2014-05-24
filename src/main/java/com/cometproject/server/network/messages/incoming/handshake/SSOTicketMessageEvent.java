package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.data.PlayerLoader;
import com.cometproject.server.game.players.queue.PlayerLoginQueueEntry;
import com.cometproject.server.game.players.queue.StaticPlayerQueue;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.handshake.HomeRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.handshake.LoginMessageComposer;
import com.cometproject.server.network.messages.outgoing.misc.MotdNotificationComposer;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolMessageComposer;
import com.cometproject.server.network.messages.outgoing.navigator.RoomCategoriesMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.permissions.FuserightsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.net.InetSocketAddress;

public class SSOTicketMessageEvent implements IEvent {
    public static String TICKET_DELIMITER = ":";

    public void handle(Session client, Event msg) {
        String ticket = msg.readString();

        if (ticket.length() < 10 || ticket.length() > 64) {
            CometManager.getLogger().warn("Session was disconnected because ticket was too long or too short. Length: " + ticket.length());
            client.disconnect();
            return;
        }

        /*boolean normalPlayerLoad = false;

        if(ticket.contains(TICKET_DELIMITER)) {
            String[] ticketData = ticket.split(TICKET_DELIMITER);

            if(ticketData.length == 2) {
                int playerId = Integer.parseInt(ticket.split(TICKET_DELIMITER)[0]);
                String authTicket = ticketData[1];

                StaticPlayerQueue.getQueueManager().queue(new PlayerLoginQueueEntry(client, playerId, authTicket));
            } else {
                normalPlayerLoad = true;
            }
        } else {
            normalPlayerLoad = true;
        }

        if (normalPlayerLoad) {
            StaticPlayerQueue.getQueueManager().queue(new PlayerLoginQueueEntry(client, -1, ticket));
        }*/

        Player player = null;
        boolean normalPlayerLoad = false;

        if(ticket.contains(TICKET_DELIMITER)) {
            String[] ticketData = ticket.split(TICKET_DELIMITER);

            if(ticketData.length == 2) {
                int playerId = Integer.parseInt(ticket.split(TICKET_DELIMITER)[0]);
                String authTicket = ticketData[1];

                player = PlayerLoader.loadPlayerByIdAndTicket(playerId, authTicket);
            } else {
                normalPlayerLoad = true;
            }
        } else {
            normalPlayerLoad = true;
        }

        if(normalPlayerLoad) {
            player = PlayerLoader.loadPlayerBySSo(ticket);
        }

        if (player == null) {
            client.disconnect();
            return;
        }

        Session cloneSession = Comet.getServer().getNetwork().getSessions().getByPlayerId(player.getId());

        if (cloneSession != null) {
            cloneSession.disconnect();
        }

        if (CometManager.getBans().hasBan(Integer.toString(player.getId())) || CometManager.getBans().hasBan(((InetSocketAddress)client.getChannel().remoteAddress()).getAddress().getHostAddress())) {
            CometManager.getLogger().warn("Banned player: " + player.getId() + " tried logging in");

            client.disconnect();
            return;
        }

        player.setSession(client);
        client.setPlayer(player);

        CometManager.getRooms().loadRoomsForUser(player);

        client.getLogger().info(client.getPlayer().getData().getUsername() + " logged in");

        PlayerDao.updatePlayerStatus(player, true, true);

        client.send(LoginMessageComposer.compose());
        client.getPlayer().sendBalance();
        client.send(FuserightsMessageComposer.compose(client.getPlayer().getSubscription().exists(), client.getPlayer().getData().getRank()));
        client.send(MotdNotificationComposer.compose());

        if (player.getSettings().getHomeRoom() > 0) {
            client.send(HomeRoomMessageComposer.compose(player.getSettings().getHomeRoom()));
        }

        if (client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.send(ModToolMessageComposer.compose());
        }

        client.send(RoomCategoriesMessageComposer.compose(CometManager.getNavigator().getCategories(), client.getPlayer().getData().getRank()));
    }
}
