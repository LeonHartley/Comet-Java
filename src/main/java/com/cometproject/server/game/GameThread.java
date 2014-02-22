package com.cometproject.server.game;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManagement;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameThread implements CometTask {
    private static final boolean MULTITHREADED_CYCLE = false;
    private static Logger log = Logger.getLogger(GameThread.class.getName());

    private CometThreadManagement threadManagement;

    private ScheduledFuture myFuture;

    private boolean active = false;

    public GameThread(CometThreadManagement mgr) {
        this.threadManagement = mgr;

        int interval = Integer.parseInt(Comet.getServer().getConfig().get("comet.game.thread.interval"));
        this.myFuture = mgr.executePeriodic(this, interval, interval, TimeUnit.MINUTES);
        this.active = true;
    }

    int cycleCount = 0;

    @Override
    public void run() {
        try {
            if(!this.active) {
                return;
            }

            if(cycleCount >= 15) {
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

            Connection connection = Comet.getServer().getStorage().getConnections().getConnection();
            connection.setAutoCommit(true);

            connection.prepareStatement("UPDATE server_status SET "
                    + "active_players = " + Comet.getServer().getNetwork().getSessions().getUsersOnlineCount() + ","
                    + "active_rooms = " + GameEngine.getRooms().getActiveRooms().size() + ","
                    + "server_version = '" + Comet.getBuild() + "'").executeUpdate();

            connection.close();

            cycleCount++;
        } catch(Exception e) {
            if(e instanceof InterruptedException) {
                return;
            }

            log.error("Error during game thread", e);
        }
    }

    private void cycle() throws Exception {
        synchronized (GameEngine.getRooms().getActiveRooms()) {
            for(Room room : GameEngine.getRooms().getActiveRooms()) {
                room.getChatlog().cycle();
                room.getRights().cycle();
            }
        }

        if(CometSettings.quartlyCreditsEnabled) {
            for(Session client : Comet.getServer().getNetwork().getSessions().getSessions().values()) {
                if(client.getPlayer() == null || client.getPlayer().getData() == null) {
                    continue;
                }

                int amountCredits = CometSettings.quartlyCreditsAmount;
                client.getPlayer().getData().increaseCredits(amountCredits);
                client.send(AdvancedAlertMessageComposer.compose(Locale.get("game.received.credits.title"), Locale.get("game.received.credits").replace("{$}", amountCredits + "")));

            }
        }

        cycleCount = 0;
    }

    public void stop() {
        this.active = false;
        this.myFuture.cancel(false);
    }
}
