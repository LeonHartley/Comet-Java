package com.cometsrv.network.sessions;

import com.cometsrv.game.players.types.Player;
import com.cometsrv.network.messages.types.Composer;
import com.cometsrv.network.security.HabboEncryption;
import org.apache.log4j.Logger;
import io.netty.channel.Channel;

public class Session {
    private Channel channel;
    private Player player;
    private HabboEncryption encryption;

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

    public HabboEncryption getEncryption() {
        return this.encryption;
    }

    public void setEncryption(HabboEncryption encryption) {
        this.encryption = encryption;
    }
}
