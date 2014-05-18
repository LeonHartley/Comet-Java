package com.cometproject.server.game.players.queue;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.data.PlayerLoader;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.outgoing.handshake.HomeRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.handshake.LoginMessageComposer;
import com.cometproject.server.network.messages.outgoing.misc.MotdNotificationComposer;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolMessageComposer;
import com.cometproject.server.network.messages.outgoing.navigator.RoomCategoriesMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.permissions.FuserightsMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.tasks.CometTask;

import java.net.InetSocketAddress;
import java.util.ArrayDeque;

public class PlayerLoginQueue implements CometTask {
    private final int MAX_QUEUE_SIZE = 1000;
    private final ArrayDeque<PlayerLoginQueueEntry> queue = new ArrayDeque<>();

    @Override
    public void run() {
        if (this.queue.isEmpty()) { return; }
        PlayerLoginQueueEntry entry = this.queue.pop();
        this.processQueueItem(entry);
    }

    private void processQueueItem(PlayerLoginQueueEntry entry) {
        Session client = entry.getClient();

        int id = entry.getPlayerId();
        String sso = entry.getSsoTicket();

        Player player = null;

        if (id == -1) {
            player = PlayerLoader.loadPlayerBySSo(sso);
        } else {
            player = PlayerLoader.loadPlayerByIdAndTicket(id, sso);
        }

        if (player == null) {
            client.disconnect();
            return;
        }

        Session cloneSession = Comet.getServer().getNetwork().getSessions().getByPlayerId(player.getId());

        if (cloneSession != null) {
            cloneSession.disconnect();
        }

        if (CometManager.getBans().hasBan(Integer.toString(player.getId())) || CometManager.getBans().hasBan(((InetSocketAddress)client.getChannel().getRemoteAddress()).getAddress().getHostAddress())) {
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

    public boolean queue(PlayerLoginQueueEntry entry) {
        if (this.queue.size() >= MAX_QUEUE_SIZE) {
            return false;
        }

        return this.queue.add(entry);
    }
}
