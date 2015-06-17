package com.cometproject.server.network.sessions;

import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.outgoing.notification.LogoutMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.UUID;


public class Session implements ISession {
    private Logger logger = Logger.getLogger("Session");
    public static int CLIENT_VERSION = 0;

    private final ChannelHandlerContext channel;
    private final SessionEventHandler eventHandler;

    private boolean isClone = false;
    private String uniqueId = "";

    private final UUID uuid = UUID.randomUUID();

    private Player player;

    public Session(ChannelHandlerContext channel) {
        this.channel = channel;

        this.channel.attr(SessionManager.SESSION_ATTR).set(this);
        this.eventHandler = new SessionEventHandler(this);
    }

    public void setPlayer(Player player) {
        if (player == null || player.getData() == null) {
            return;
        }

        String username = player.getData().getUsername();

        this.logger = Logger.getLogger("[" + username + "][" + player.getId() + "]");
        this.player = player;

        int channelId = this.channel.attr(SessionManager.CHANNEL_ID_ATTR).get();

        PlayerManager.getInstance().put(player.getId(), channelId, username, this.getIpAddress());
    }

    public void onDisconnect() {
        if (!isClone && player != null && player.getData() != null)
            PlayerManager.getInstance().remove(player.getId(), player.getData().getUsername(), this.channel.attr(SessionManager.CHANNEL_ID_ATTR).get(), this.getIpAddress());

        this.eventHandler.dispose();

        if (this.player != null) {
            this.getPlayer().dispose();
        }

        this.setPlayer(null);
    }

    public void disconnect(boolean isClone) {
        this.isClone = isClone;
        this.getChannel().disconnect();
    }

    public String getIpAddress() {
        String ipAddress = "0.0.0.0";

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

    public void handleMessageEvent(MessageEvent msg) {
        this.eventHandler.handle(msg);
    }

    public Session sendQueue(final IMessageComposer msg) {
        this.channel.write(msg);
        return this;
    }

    public Session send(IMessageComposer msg) {
        if (msg == null) {
            return this;
        }

        logger.debug("Sent message: " + msg.getClass().getSimpleName() + " / " + msg.getId());

        this.channel.writeAndFlush(msg);
        return this;
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

    public UUID getSessionId() {
        return uuid;
    }
}
