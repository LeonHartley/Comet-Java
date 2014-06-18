package com.cometproject.server.network.sessions;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.messages.types.Event;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

public class Session {
    private Logger logger = Logger.getLogger("Session");

    private final ChannelHandlerContext ctx;
    private final SessionEventHandler eventHandler;

    private Player player;

    public Session(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.eventHandler = new SessionEventHandler(this);
    }

    public void setPlayer(Player player) {
        if (player.getData() == null) {
            return;
        }
        String username = player.getData().getUsername();

        this.logger = Logger.getLogger(username);
        this.player = player;

        int channelId = this.ctx.attr(NetworkManager.CHANNEL_ID).get();
        CometManager.getPlayers().put(player.getId(), channelId, username);
    }

    public void onDisconnect() {
        CometManager.getPlayers().remove(player.getId(), player.getData().getUsername());

        this.getPlayer().dispose();
    }

    public void disconnect() {
        this.getChannel().disconnect();
    }

    public void handleMessageEvent(Event msg) {
        this.eventHandler.handle(msg);
    }

    public Session sendQueue(Composer msg) {
        if (msg == null) {
            return this;
        }

        this.ctx.write(msg);
        return this;
    }

    public void send(Composer msg) {
        if (msg == null) {
            return;
        }
        this.ctx.writeAndFlush(msg);
    }

    public void flush() {
        this.ctx.flush();
    }

    public Logger getLogger() {
        return this.logger;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Channel getChannel() {
        return this.ctx.channel();
    }
}
