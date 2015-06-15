package com.cometproject.server.game;

import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.storage.queries.system.StatisticsDao;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.Initializable;
import org.apache.log4j.Logger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class GameThread implements CometTask, Initializable {
    private static final int interval = 1;
    private static final int PLAYER_REWARD_INTERVAL = 15; // minutes

    private static GameThread gameThreadInstance;

    private static Logger log = Logger.getLogger(GameThread.class.getName());

    private ScheduledFuture gameFuture;

    private boolean active = false;

    private int currentOnlineRecord = 0;
    private int onlineRecord = 0;

    public GameThread() {

    }

    @Override
    public void initialize() {
        this.gameFuture = CometThreadManager.getInstance().executePeriodic(this, interval, interval, TimeUnit.MINUTES);
        this.active = true;

        this.onlineRecord = StatisticsDao.getPlayerRecord();
    }

    public static GameThread getInstance() {
        if (gameThreadInstance == null)
            gameThreadInstance = new GameThread();

        return gameThreadInstance;
    }

    @Override
    public void run() {
        try {
            if (!this.active) {
                return;
            }

            BanManager.getInstance().tick();

            final int usersOnline = NetworkManager.getInstance().getSessions().getUsersOnlineCount();
            boolean updateOnlineRecord = false;

            if (usersOnline > this.currentOnlineRecord) {
                this.currentOnlineRecord = usersOnline;
            }

            if (usersOnline > this.onlineRecord) {
                this.onlineRecord = usersOnline;
                updateOnlineRecord = true;
            }

            this.cycleRewards();

            if (!updateOnlineRecord)
                StatisticsDao.saveStatistics(usersOnline, RoomManager.getInstance().getRoomInstances().size(), Comet.getBuild());
            else
                StatisticsDao.saveStatistics(usersOnline, RoomManager.getInstance().getRoomInstances().size(), Comet.getBuild(), this.onlineRecord);
        } catch (Exception e) {
            log.error("Error during game thread", e);
        }
    }

    private void cycleRewards() throws Exception {
        if (CometSettings.quarterlyCreditsEnabled || CometSettings.quarterlyDucketsEnabled) {
            for (ISession client : NetworkManager.getInstance().getSessions().getSessions().values()) {
                try {
                    if (client.getPlayer() == null || client.getPlayer().getData() == null) {
                        continue;
                    }

                    final boolean needsReward = (Comet.getTime() - client.getPlayer().getLastReward()) >= (60 * PLAYER_REWARD_INTERVAL);

                    if (needsReward) {
                        if (CometSettings.quarterlyCreditsEnabled) {
                            client.getPlayer().getData().increaseCredits(CometSettings.quarterlyCreditsAmount);
                        }

                        if (CometSettings.quarterlyDucketsEnabled) {
                            client.getPlayer().getData().increaseActivityPoints(CometSettings.quarterlyDucketsAmount);
                        }

                        client.getPlayer().sendBalance();
                        client.getPlayer().getData().save();

                        client.getPlayer().setLastReward(Comet.getTime());
                    }
                } catch (Exception e) {
                    log.error("Error while cycling rewards", e);
                }
            }
        }
    }

    public void stop() {
        this.active = false;
        this.gameFuture.cancel(false);
    }

    public int getCurrentOnlineRecord() {
        return this.currentOnlineRecord;
    }

    public int getOnlineRecord() {
        return this.onlineRecord;
    }
}
