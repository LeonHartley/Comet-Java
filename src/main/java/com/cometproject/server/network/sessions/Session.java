package com.cometproject.server.network.sessions;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.security.hurlant.ARC4;
import com.cometproject.server.storage.queries.player.PlayerDao;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

import java.net.InetSocketAddress;

public class Session {
    private Logger logger = Logger.getLogger("Session");

    private final Channel channel;
    private final SessionEventHandler eventHandler;

    private boolean isClone = false;
    private String uniqueId = "";

    private Player player;
    private ARC4 arc4;

    public Session(Channel channel) {
        this.channel = channel;
        this.eventHandler = new SessionEventHandler(this);
    }

    public void setPlayer(Player player) {
        if (player.getData() == null) {
            return;
        }

        String username = player.getData().getUsername();

        this.logger = Logger.getLogger("[" + username + "][" + player.getId() + "]");
        this.player = player;

        int channelId = this.channel.getId();

        CometManager.getPlayers().put(player.getId(), channelId, username);
    }

    public void onDisconnect() {
        if (!isClone)
            CometManager.getPlayers().remove(player.getId(), player.getData().getUsername());

        this.eventHandler.dispose();
        this.getPlayer().dispose();
    }

    public void disconnect(boolean isClone) {
        this.isClone = isClone;
        this.getChannel().disconnect();
    }

    public String getIpAddress() {
        String ipAddress;

        if(!CometSettings.useDatabaseIp) {
            return ((InetSocketAddress)this.getChannel().getRemoteAddress()).getAddress().getHostAddress();
        } else {
            ipAddress = PlayerDao.getIpAddress(this.getPlayer().getId());
        }

        if(ipAddress == null || ipAddress.isEmpty()) {
            logger.warn("Could not retrieve IP address of player: " + this.getPlayer().getId());
        }

        return ipAddress;
    }
    public void disconnect() {
        this.disconnect(false);
    }

    public void handleMessageEvent(Event msg) {
        this.eventHandler.handle(msg);
    }

    public Session sendQueue(Composer msg) {
        this.send(msg);
        return this;
    }

    public void send(Composer msg) {
        if (msg == null) {
            return;
        }

        logger.debug("Sending composer: " + Composers.valueOfId(msg.getId()));
        this.getChannel().write(msg);
    }

    public void flush() {
       // todo: bundling of packets
    }

    public ARC4 getEncryption() {
        return this.arc4;
    }

    public void initEncryption(byte[] key) {
        this.arc4 = new ARC4(key);
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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
