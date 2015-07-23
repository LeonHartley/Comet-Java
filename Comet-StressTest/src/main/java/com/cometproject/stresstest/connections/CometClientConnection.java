package com.cometproject.stresstest.connections;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.codec.MessageDecoder;
import com.cometproject.server.protocol.codec.MessageEncoder;
import com.cometproject.stresstest.CometStressTest;
import com.cometproject.stresstest.connections.messages.SSOTicketMessageComposer;
import com.cometproject.stresstest.connections.messages.WalkMessageComposer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class CometClientConnection {
    private boolean isConnected = false;

    private boolean isOnline = false;
    private boolean isInRoom = false;
    private boolean isWalk = true;

    private Channel channel;
    public CometClientConnection(CometClientConfig config, EventLoopGroup loopGroup) {
        final Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast("encoder", new MessageEncoder());
                socketChannel.pipeline().addLast("decoder", new MessageDecoder());
            }
        });

        bootstrap.remoteAddress(config.getHostName(), config.getPort());

        ChannelFuture connectFuture = bootstrap.connect();

        connectFuture.addListener((future) -> {
            if (!future.isSuccess()) {
                System.out.println("[" + config.getSsoTicket() + "] Failed to connect to server!");
            } else {
                System.out.println("[" + config.getSsoTicket() + "] Connected to the server.");

                // we can do shit! :D
                this.isConnected = true;
                this.channel = connectFuture.channel();

                this.channel.writeAndFlush(new SSOTicketMessageComposer(config.getSsoTicket()));
                this.isOnline = true;
            }
        });
    }

    public void tick() {
        if(this.isOnline() && this.isInRoom() && this.isWalk()) {
            int x = CometStressTest.getRandom(1, 32);
            int y = CometStressTest.getRandom(0, 32);

            this.send(new WalkMessageComposer(x, y));
        }
    }

    public void send(MessageComposer msg) {
        this.channel.writeAndFlush(msg);
    }

    public void disconnect() {
        if(!this.isConnected) {
            channel.disconnect();
        }
    }

    public boolean isOnline() {
        return isOnline;
    }

    public boolean isConnected() {
        return isConnected;
    }


    public boolean isInRoom() {
        return isInRoom;
    }

    public void setIsInRoom(boolean isInRoom) {
        this.isInRoom = isInRoom;
    }

    public boolean isWalk() {
        return isWalk;
    }

    public void setIsWalk(boolean isWalk) {
        this.isWalk = isWalk;
    }

}
