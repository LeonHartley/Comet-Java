package com.cometproject.server.network.monitor;

public class MonitorMessageLibrary {
    /*private static Logger log = Logger.getLogger(MonitorMessageLibrary.class.getName());

    public static String request;
    public static ChannelHandlerContext ctx;

    // Hello message
    public static void hello() {
        log.debug(request);

        heartbeat();
    }

    public static void heartbeat() {
        Runtime runtime = Runtime.getRuntime();

        ServerStatus status = new ServerStatus(
                Comet.getServer().getNetwork().getSessions().getUsersOnlineCount(),
                GameEngine.getRooms().getActiveRooms().size(),
                TimeSpan.millisecondsToDate(System.currentTimeMillis() - Comet.start),
                ((runtime.totalMemory() / 1024) / 1024),
                ((runtime.totalMemory() / 1024) / 1024) - ((runtime.freeMemory() / 1024) / 1024),
                System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")",
                runtime.availableProcessors(),
                Comet.getServer().getStorage().getConnectionCount()
        );

        sendMessage(new Gson().toJson(status));
    }

    private static void sendMessage(String json) {
        ByteBuf msg = Unpooled.buffer(json.getBytes().length);

        for (int i = 0; i < msg.capacity(); i++) {
            msg.writeByte(json.getBytes()[i]);
        }

        ctx.writeAndFlush(msg);
    }*/
}
