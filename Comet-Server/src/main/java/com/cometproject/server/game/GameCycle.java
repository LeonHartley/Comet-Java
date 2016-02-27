package com.cometproject.server.game;

import com.cometproject.api.networking.sessions.BaseSession;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.user.details.UserObjectMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.system.StatisticsDao;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.Initializable;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class GameCycle implements CometTask, Initializable {
    private static final int interval = 1;
    private static final int PLAYER_REWARD_INTERVAL = 15; // minutes

    private static GameCycle gameThreadInstance;

    private static Logger log = Logger.getLogger(GameCycle.class.getName());

    private ScheduledFuture gameFuture;

    private boolean active = false;

    private int currentOnlineRecord = 0;
    private int onlineRecord = 0;

    public GameCycle() {

    }

    @Override
    public void initialize() {
        this.gameFuture = CometThreadManager.getInstance().executePeriodic(this, interval, interval, TimeUnit.MINUTES);
        this.active = true;

        this.onlineRecord = StatisticsDao.getPlayerRecord();
    }

    public static GameCycle getInstance() {
        if (gameThreadInstance == null)
            gameThreadInstance = new GameCycle();

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

            this.processSession();

            if (!updateOnlineRecord)
                StatisticsDao.saveStatistics(usersOnline, RoomManager.getInstance().getRoomInstances().size(), Comet.getBuild());
            else
                StatisticsDao.saveStatistics(usersOnline, RoomManager.getInstance().getRoomInstances().size(), Comet.getBuild(), this.onlineRecord);


        } catch (Exception e) {
            log.error("Error during game thread", e);
        }
    }

    private void processSession() throws Exception {

        final Calendar calendar = Calendar.getInstance();

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        final boolean updateDaily = hour == 0 && minute == 0;
        final int dailyRespects = 3;
        final int dailyScratches = 3;

        if (CometSettings.onlineRewardEnabled || updateDaily) {
            for (BaseSession client : NetworkManager.getInstance().getSessions().getSessions().values()) {
                try {
                    if (!(client instanceof Session) || client.getPlayer() == null || client.getPlayer().getData() == null) {
                        continue;
                    }

                    if (updateDaily) {
                        //  TODO: put this in config.
                        client.getPlayer().getStats().setDailyRespects(dailyRespects);
                        client.getPlayer().getStats().setScratches(dailyScratches);

                        client.send(new UserObjectMessageComposer(((Session) client).getPlayer()));
                    }

                    ((Session) client).getPlayer().getAchievements().progressAchievement(AchievementType.ONLINE_TIME, 1);

                    final boolean needsReward = (Comet.getTime() - client.getPlayer().getLastReward()) >= (60 * PLAYER_REWARD_INTERVAL);

                    if (needsReward) {
                        if (CometSettings.onlineRewardCredits > 0) {
                            client.getPlayer().getData().increaseCredits(CometSettings.onlineRewardCredits);
                        }

                        if (CometSettings.onlineRewardDuckets > 0) {
                            client.getPlayer().getData().increaseActivityPoints(CometSettings.onlineRewardDuckets);
                        }

                        client.getPlayer().sendBalance();
                        client.getPlayer().getData().save();

                        client.getPlayer().setLastReward(Comet.getTime());
                    }
                } catch (Exception e) {
                    log.error("Error while cycling rewards", e);
                }
            }

            if(updateDaily) {
                PlayerDao.dailyPlayerUpdate(dailyRespects, dailyScratches);
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
