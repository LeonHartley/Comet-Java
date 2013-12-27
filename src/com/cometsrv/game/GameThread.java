package com.cometsrv.game;

import com.cometsrv.boot.Comet;
import com.cometsrv.config.CometSettings;
import com.cometsrv.config.Locale;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometsrv.network.sessions.Session;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;

public class GameThread implements Runnable {

    private Thread gameThread;
    private boolean active = false;
    private Logger log = Logger.getLogger(GameThread.class.getName());
    private int interval = Integer.parseInt(Comet.getServer().getConfig().get("comet.game.thread.interval"));

    public GameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
        this.active = true;
    }

    int cycleCount = 0;

    @Override
    public void run() {
        while(this.active) {
            try {
                if(this.gameThread.isInterrupted() || !this.active) {
                    return;
                }

                if(cycleCount >= 15) {
                    synchronized (GameEngine.getRooms().getActiveRooms()) {
                        for(Room room : GameEngine.getRooms().getActiveRooms()) {
                            room.getChatlog().cycle();
                            room.getRights().cycle();
                        }
                    }

                    if(CometSettings.quartlyCreditsEnabled) {
                        for(Session client : Comet.getServer().getNetwork().getSessions().getSessions().values()) {
                            if(client.getPlayer() == null) {
                                continue;
                            }

                            int amountCredits = CometSettings.quartlyCreditsAmount;
                            client.getPlayer().getData().increaseCredits(amountCredits);
                            client.send(AdvancedAlertMessageComposer.compose(Locale.get("game.received.credits.title"), Locale.get("game.received.credits").replace("{$}", amountCredits + "")));

                        }
                    }

                    cycleCount = 0;
                }

                Connection connection = Comet.getServer().getStorage().getConnections().getConnection();
                connection.setAutoCommit(true);

                connection.prepareStatement("UPDATE server_status SET "
                        + "active_players = " + Comet.getServer().getNetwork().getSessions().getUsersOnlineCount() + ","
                        + "active_rooms = " + GameEngine.getRooms().getActiveRooms().size() + ","
                        + "server_version = '" + Comet.getBuild() + "'").executeUpdate();

                connection.close();

                cycleCount++;
                TimeUnit.MINUTES.sleep(interval);
            } catch(Exception e) {
                if(e instanceof InterruptedException) {
                    return;
                }

                log.error("Error during game thread", e);
            }
        }
    }

    public void stop() {
        this.gameThread.interrupt();
        this.active = false;
    }
}
