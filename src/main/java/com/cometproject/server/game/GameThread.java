package com.cometproject.server.game;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.StatisticsDao;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManagement;
import com.cometproject.server.utilities.TimeSpan;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameThread implements CometTask {
    private static final boolean MULTITHREADED_CYCLE = false;
    private static Logger log = Logger.getLogger(GameThread.class.getName());

    private CometThreadManagement threadManagement;

    private ScheduledFuture gameFuture;
    private ScheduledFuture dailyCycleFuture;

    private boolean active = false;

    public GameThread(CometThreadManagement mgr) {
        this.threadManagement = mgr;

        int interval = Integer.parseInt(Comet.getServer().getConfig().get("comet.game.thread.interval"));
        this.gameFuture = mgr.executePeriodic(this, interval, interval, TimeUnit.MINUTES);
        this.active = true;

        this.configureDailyCycle();
    }

    private void configureDailyCycle() {
        Calendar c = Calendar.getInstance();
        Date now = new Date();

        c.setTime(now);
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long msTillMidnight = (c.getTimeInMillis() - now.getTime());

        this.dailyCycleFuture = this.threadManagement.executePeriodic(new CometTask() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();

                Comet.getServer().getStorage().execute("UPDATE player_stats SET daily_respects = 3 WHERE daily_respects < 3");

                TimeSpan span = new TimeSpan(start, System.currentTimeMillis());

                log.info("Daily task has cycled. Took: " + span.toMilliseconds() + "ms to execute.");
            }
        }, msTillMidnight, 86400000, TimeUnit.MILLISECONDS);
    }

    private int cycleCount = 0;

    @Override
    public void run() {
        try {
            if (!this.active) {
                return;
            }

            if (cycleCount >= 15) {
                if (MULTITHREADED_CYCLE) {
                    this.threadManagement.executeOnce(new CometTask() {
                        @Override
                        public void run() {
                            // Need to do some changes here to allow cycling to run multi threaded...
                        }
                    });
                } else {
                    this.cycle();
                }
            }

            StatisticsDao.updateStats(0, 0, "0.1");
            cycleCount++;
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                return;
            }

            log.error("Error during game thread", e);
        }
    }

    private void cycle() throws Exception {
        synchronized (GameEngine.getRooms().getActiveRooms()) {
            for (Room room : GameEngine.getRooms().getActiveRooms()) {
                room.getChatlog().cycle();
                room.getRights().cycle();
            }
        }

        if (CometSettings.quartlyCreditsEnabled) {
            for (Session client : Comet.getServer().getNetwork().getSessions().getSessions().values()) {
                if (client.getPlayer() == null || client.getPlayer().getData() == null) {
                    continue;
                }

                int amountCredits = CometSettings.quartlyCreditsAmount;
                client.getPlayer().getData().increaseCredits(amountCredits);

                client.getPlayer().sendBalance();
                client.getPlayer().getData().save();
            }
        }

        cycleCount = 0;
    }

    public void stop() {
        this.active = false;
        this.gameFuture.cancel(false);
        this.dailyCycleFuture.cancel(false);
    }
}
