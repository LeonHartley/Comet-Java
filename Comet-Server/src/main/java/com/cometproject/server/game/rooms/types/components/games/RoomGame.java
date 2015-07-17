package com.cometproject.server.game.rooms.types.components.games;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.GameComponent;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import org.apache.log4j.Logger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public abstract class RoomGame implements CometTask {
    private GameType type;

    protected int timer;
    protected int gameLength;
    protected boolean active;
    protected boolean finished = false;
    protected Room room;

    private ScheduledFuture future;
    private Logger log;

    public RoomGame(Room room, GameType gameType) {
        this.type = gameType;
        this.log = Logger.getLogger("RoomGame [" + room.getData().getName() + "][" + room.getData().getId() + "][" + this.type + "]");
        this.room = room;
    }

    @Override
    public void run() {
        try {
            if (timer == 0) {
                onGameStarts();
            }

            tick();

            if (timer >= gameLength) {
                onGameEnds();
                room.getGame().stop();
            }

            timer++;
        } catch (Exception e) {
            log.error("Error during game tick", e);
        }
    }

    public void stop() {
        if (this.active && this.future != null) {
            this.future.cancel(false);

            this.active = false;
            this.gameLength = 0;
            this.timer = 0;
        }
    }

    public void startTimer(int amount) {
        if (this.active && this.future != null) {
            this.future.cancel(false);
        }

        this.future = CometThreadManager.getInstance().executePeriodic(this, 0, 1, TimeUnit.SECONDS);

        this.gameLength = amount;
        this.active = true;

        log.debug("Game active for " + amount + " seconds");
    }

    protected GameComponent getGameComponent() {
        return this.room.getGame();
    }

    public abstract void tick();

    public abstract void onGameEnds();

    public abstract void onGameStarts();

    public GameType getType() {
        return this.type;
    }

    public Logger getLog() {
        return this.log;
    }
}
