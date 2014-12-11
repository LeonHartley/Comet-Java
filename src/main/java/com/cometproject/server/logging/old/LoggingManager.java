//package com.cometproject.server.logging.old;

//
//import com.cometproject.server.boot.Comet;
//
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;


//
//public class LoggingManager {
//    private final int FLUSH_SECONDS = 0;//Integer.parseInt(Comet.getServer().getConfig().get("comet.game.logging.flush.seconds"));
//    private final String TOKEN = "";//Comet.getServer().getConfig().get("comet.game.logging.token");
//
//    private final LoggingQueue queue;
//    private ScheduledFuture process;
//
//    private volatile boolean active = false;
//
//    public LoggingManager() {
//        this.queue = new LoggingQueue(this.TOKEN);
//        //this.start();
//    }
//
//    public void start() {
//        if (this.active) { return; }
//        this.active = true;
//        this.process = Comet.getServer().getThreadManagement().executePeriodic(this.queue, FLUSH_SECONDS, FLUSH_SECONDS, TimeUnit.SECONDS);
//    }
//
//    public void stop() {
//        if (!this.active) { return; }
//        this.active = false;
//        this.process.cancel(false);
//    }
//
//    public void queue(AbstractLogEntry entry) {
//        this.queue.addEntry(entry);
//    }
//}
