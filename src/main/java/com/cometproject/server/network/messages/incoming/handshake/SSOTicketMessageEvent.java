package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.players.data.PlayerLoader;
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

import java.net.InetSocketAddress;

public class SSOTicketMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String ticket = msg.readString();

        if (ticket.length() < 10 || ticket.length() > 30) {
            client.disconnect();
            return;
        }

        Player player = PlayerLoader.loadPlayerBySSo(ticket);

        if (player == null) {
            client.disconnect();
            return;
        }

        Session cloneSession = Comet.getServer().getNetwork().getSessions().getByPlayerId(player.getId());

        if (cloneSession != null) {
            cloneSession.disconnect();
        }

        if (GameEngine.getBans().hasBan(Integer.toString(player.getId())) || GameEngine.getBans().hasBan(((InetSocketAddress)client.getChannel().getRemoteAddress()).getAddress().getHostAddress())) {
            /*client.send(AdvancedAlertMessageComposer.compose(
                    "You've been banned!",
                    "If you feel you received this in error, please contact the system administrator."
            ));*/

            GameEngine.getLogger().warn("Banned player: " + player.getId() + " tried logging in");

            client.disconnect();
            return;
        }

        player.setSession(client);
        client.setPlayer(player);

        GameEngine.getRooms().loadRoomsForUser(player);

        client.getLogger().info(client.getPlayer().getData().getUsername() + " logged in");

        //Comet.getServer().getStorage().execute("UPDATE players SET last_online = " + Comet.getTime() + " WHERE id = " + player.getId());

        //PlayerDao.updatePlayerStatus(player, true, true);

        client.send(LoginMessageComposer.compose());
        client.send(FuserightsMessageComposer.compose(client.getPlayer().getSubscription().exists(), client.getPlayer().getData().getRank()));
        client.send(MotdNotificationComposer.compose());

        if (player.getSettings().getHomeRoom() > 0) {
            client.send(HomeRoomMessageComposer.compose(player.getSettings().getHomeRoom()));
        }

        /*
            this will be a way of sending the old-style alert notifications
            if you need more info - ask leon

            Header: 2442 (VOUCHER_ERROR)
            #################
            String: AlertId
        */

        if (client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.send(ModToolMessageComposer.compose());
        }

        client.send(RoomCategoriesMessageComposer.compose(GameEngine.getNavigator().getCategories(), client.getPlayer().getData().getRank()));


        //Comet.getServer().getPluginEngine().invokePlayerCommand("test", PluginPlayer.create(player));
    }
}
