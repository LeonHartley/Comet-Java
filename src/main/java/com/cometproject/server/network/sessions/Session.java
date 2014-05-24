package com.cometproject.server.network.sessions;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.NetworkEngine;
import com.cometproject.server.network.messages.types.Composer;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;

public class Session {
    private Channel channel;
    private Player player;

    private Logger logger = Logger.getLogger("Session");

    public Session(Channel channel) {
        this.channel = channel;
    }

    public void setPlayer(Player player) {
        if(player.getData() == null) { return; }
        String username = player.getData().getUsername();

        this.logger = Logger.getLogger(username);
        this.player = player;

        int channelId = this.channel.attr(NetworkEngine.CHANNEL_ID).get();
        CometManager.getPlayers().put(player.getId(), channelId, username);
    }

    public void onDisconnect() {
        CometManager.getPlayers().remove(player.getId(), player.getData().getUsername());

        this.getPlayer().dispose();
    }

    public void disconnect() {
        this.getChannel().disconnect();
    }

    public void send(Composer msg) {
        channel.writeAndFlush(msg);

        logger.trace("Data was written to channel");
    }

    public Logger getLogger() {
        return this.logger;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Channel getChannel() {
        return this.channel;
    }
}
