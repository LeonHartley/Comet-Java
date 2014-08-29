package com.cometproject.server.network.monitor;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.NetworkManager;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;

public class MonitorClientHandler extends SimpleChannelInboundHandler {
    private Logger log = Logger.getLogger(MonitorClientHandler.class.getName());
    private ByteBuf handshakeMessage;
    private MonitorMessageHandler messageHandler;
    private Gson gson = new Gson();

    private ChannelHandlerContext context;

    public MonitorClientHandler() {
        String message = "{\"name\":\"hello\", \"message\": {\"version\": \"Comet Server [" + Comet.getBuild() + "]\", \"port\": " + NetworkManager.serverPort + "}}";

        this.handshakeMessage = Unpooled.buffer(message.getBytes().length);

        for (int i = 0; i < this.handshakeMessage.capacity(); i++) {
            this.handshakeMessage.writeByte(message.getBytes()[i]);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.context = ctx;
        this.messageHandler = new MonitorMessageHandler();

        ctx.writeAndFlush(this.handshakeMessage);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        String messageJson = buffer.toString(Charset.defaultCharset());

        MonitorPacket message = this.gson.fromJson(messageJson, MonitorPacket.class);

        this.messageHandler.handle(message, channelHandlerContext);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Exception caught from MonitorClient", cause);
        ctx.close();
    }

    public ChannelHandlerContext getContext() {
        return context;
    }
}
