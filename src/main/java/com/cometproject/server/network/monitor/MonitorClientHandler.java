package com.cometproject.server.network.monitor;

public class MonitorClientHandler {
    /*private Logger log = Logger.getLogger(MonitorClientHandler.class.getName());
    private ByteBuf handshakeMessage;
    private MonitorMessageHandler messageHandler;
    private Gson gson = new Gson();

    public MonitorClientHandler() {
        String message = "Comet Server [" + Comet.getBuild() + "]";

        handshakeMessage = Unpooled.buffer(message.getBytes().length);

        for (int i = 0; i < handshakeMessage.capacity(); i++) {
            handshakeMessage.writeByte(message.getBytes()[i]);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        messageHandler = new MonitorMessageHandler();

        ctx.writeAndFlush(handshakeMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buffer = (ByteBuf) msg;
        String messageJson = buffer.toString(Charset.defaultCharset());
        MonitorPacket message = gson.fromJson(messageJson, MonitorPacket.class);

        this.messageHandler.handle(message, ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Exception caught from MonitorClient", cause);
        ctx.close();
    }*/
}
