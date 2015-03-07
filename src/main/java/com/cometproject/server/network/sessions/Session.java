package com.cometproject.server.network.sessions;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.outgoing.notification.LogoutMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.storage.queries.player.PlayerDao;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;


public class Session {
    private Logger logger = Logger.getLogger("Session");
    public static int CLIENT_VERSION = 0;

    private final ChannelHandlerContext channel;
    private final SessionEventHandler eventHandler;

    private boolean isClone = false;
    private String uniqueId = "";

    private Player player;
    private Object arc4;

    public Session(ChannelHandlerContext channel) {
        this.channel = channel;

        this.channel.attr(SessionManager.SESSION_ATTR).set(this);
        this.eventHandler = new SessionEventHandler(this);
    }

    public void setPlayer(Player player) {
        if (player.getData() == null) {
            return;
        }

        String username = player.getData().getUsername();

        this.logger = Logger.getLogger("[" + username + "][" + player.getId() + "]");
        this.player = player;

        int channelId = this.channel.attr(SessionManager.CHANNEL_ID_ATTR).get();

        PlayerManager.getInstance().put(player.getId(), channelId, username);
    }

    public void onDisconnect() {
        if (!isClone)
            PlayerManager.getInstance().remove(player.getId(), player.getData().getUsername());

        this.eventHandler.dispose();
        this.getPlayer().dispose();
    }

    public void disconnect(boolean isClone) {
        this.isClone = isClone;
        this.getChannel().disconnect();
    }

    public String getIpAddress() {
        String ipAddress;

        if (!CometSettings.useDatabaseIp) {
            return ((InetSocketAddress) this.getChannel().channel().remoteAddress()).getAddress().getHostAddress();
        } else {
            ipAddress = PlayerDao.getIpAddress(this.getPlayer().getId());
        }

        if (ipAddress == null || ipAddress.isEmpty()) {
            logger.warn("Could not retrieve IP address of player: " + this.getPlayer().getId());
        }

        return ipAddress;
    }

    public void disconnect() {
        this.disconnect(false);
    }

    public void disconnect(String reason) {
        this.send(new LogoutMessageComposer(reason));
        this.disconnect();
    }

    public void handleMessageEvent(Event msg) {
        this.eventHandler.handle(msg);
    }

    public Session sendQueue(final MessageComposer msg) {
        this.send(msg);

        return this;
    }

    public void send(final MessageComposer msg) {
        if (msg == null) {
            logger.debug("Message was null!");
            return;
        }

        logger.debug("Sent message: " + msg.getClass().getSimpleName() + " / " + msg.getId());

        this.channel.writeAndFlush(msg);
    }

    public void flush() {
        this.channel.flush();
    }

    public Logger getLogger() {
        return this.logger;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ChannelHandlerContext getChannel() {
        return this.channel;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
