package com.cometproject.server.network.sessions;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.security.RC4;
import org.apache.log4j.Logger;
import io.netty.channel.Channel;

public class Session {
    private Channel channel;
    private Player player;
    private RC4 rc4;

    private Logger logger = Logger.getLogger("Unknown session");

    public Session(Channel channel) {
        this.channel = channel;
    }

    public void setPlayer(Player player) {
        this.logger = Logger.getLogger(player.getData().getUsername());
        this.player = player;
    }

    public void onDisconnect() {
        this.getPlayer().dispose();
    }

    public void disconnect() {
        this.getChannel().disconnect();
    }

    public void send(Composer msg) {
        if(msg == null)
            return;

        channel.writeAndFlush(msg);
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

    public RC4 getRC4() {
        return rc4;
    }

    public void setRC4(RC4 rc4) {
        this.rc4 = rc4;
    }
}
