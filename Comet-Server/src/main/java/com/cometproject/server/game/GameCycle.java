package com.cometproject.server.game;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.achievements.types.AchievementType;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.user.details.UserObjectMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.system.StatisticsDao;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class GameCycle implements CometTask, Initialisable {
    private static final int interval = 1;

    private static GameCycle gameThreadInstance;

    private static Logger log = Logger.getLogger(GameCycle.class.getName());

    private ScheduledFuture gameFuture;

    private boolean active = false;

    private int currentOnlineRecord = 0;
    private int onlineRecord = 0;

    public GameCycle() {

    }

    public static GameCycle getInstance() {
        if (gameThreadInstance == null)
            gameThreadInstance = new GameCycle();

        return gameThreadInstance;
    }

    @Override
    public void initialize() {
        this.gameFuture = CometThreadManager.getInstance().executePeriodic(this, interval, interval, TimeUnit.MINUTES);
        this.active = true;

        this.onlineRecord = StatisticsDao.getPlayerRecord();
    }

    @Override
    public void run() {
        try {
            if (!this.active) {
                return;
            }

            BanManager.getInstance().processBans();

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

    private void processSession() {

        final LocalDate date = LocalDate.now();
        final Calendar calendar = Calendar.getInstance();

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        final boolean doubleRewards = CometSettings.onlineRewardDoubleDays.contains(date.getDayOfWeek());
        final boolean updateDaily = hour == 0 && minute == 0;
        final int dailyRespects = 3;
        final int dailyScratches = 3;

        if (CometSettings.onlineRewardEnabled || updateDaily) {
            for (ISession client : NetworkManager.getInstance().getSessions().getSessions().values()) {
                try {
                    if (!(client instanceof Session) || client.getPlayer() == null || client.getPlayer().getData() == null) {
                        continue;
                    }

                    if ((Comet.getTime() - ((Session) client).getLastPing()) >= 300) {
                        client.disconnect();
                        continue;
                    }

                    if (updateDaily) {
                        //  TODO: put this in config.
                        client.getPlayer().getStats().setDailyRespects(dailyRespects);
                        client.getPlayer().getStats().setScratches(dailyScratches);

                        client.send(new UserObjectMessageComposer(((Session) client).getPlayer()));
                    }

                    ((Session) client).getPlayer().getAchievements().progressAchievement(AchievementType.ONLINE_TIME, 1);

                    final boolean needsReward = (Comet.getTime() - client.getPlayer().getLastReward()) >= (60 * CometSettings.onlineRewardInterval);
                    final boolean needsDiamondsReward = (Comet.getTime() - client.getPlayer().getLastDiamondReward()) >= (60 * CometSettings.onlineRewardDiamondsInterval);

                    if (needsReward || needsDiamondsReward) {
                        if(needsReward) {
                            if (CometSettings.onlineRewardCredits > 0) {
                                client.getPlayer().getData().increaseCredits(CometSettings.onlineRewardCredits * (doubleRewards ? 2 : 1));
                            }

                            if (CometSettings.onlineRewardDuckets > 0) {
                                client.getPlayer().getData().increaseActivityPoints(CometSettings.onlineRewardDuckets * (doubleRewards ? 2 : 1));
                            }
                        }

                        if(needsDiamondsReward) {
                            if(CometSettings.onlineRewardDiamonds > 0) {
                                client.getPlayer().getData().increaseVipPoints(CometSettings.onlineRewardDiamonds * (doubleRewards ? 2 : 1));
                            }

                            client.getPlayer().setLastDiamondReward(Comet.getTime());
                        }

                        client.getPlayer().sendBalance();
                        client.getPlayer().getData().save();

                        client.getPlayer().setLastReward(Comet.getTime());
                    }
                } catch (Exception e) {
                    log.error("Error while cycling rewards", e);
                }
            }

            if (updateDaily) {
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
