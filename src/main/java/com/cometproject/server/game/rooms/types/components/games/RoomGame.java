package com.cometproject.server.game.rooms.types.components.games;

import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class RoomGame implements Runnable {
    // TODO: recode this to use comet's thread pool

    private Map<Integer, GameTeam> teams;
    private GameType type;
    protected int timer;
    protected int gameLength;
    private boolean started;
    protected Room room;

    private Thread thread;
    private Logger log;

    public RoomGame(Room room, GameType gameType) {
        this.log = Logger.getLogger("RoomGame [" + room.getData().getName() + "][" + room.getData().getId() + "]");
        this.teams = new FastMap<>();
        this.type = gameType;
        this.room = room;
    }

    @Override
    public void run() {
        try {
            gameStarts();

            while(timer <= gameLength) {
                tick();

                timer++;
                TimeUnit.SECONDS.sleep(1);
            }

            gameEnds();
        } catch(Exception e) {
            log.error("Error during game tick", e);
        }
    }

    public void dispose() {
        this.teams.clear();
    }

    public void stop() {
        if(this.started && thread != null) {
            this.thread.interrupt();
            this.started = false;
            this.gameLength = 0;
            this.timer = 0;
        }
    }

    public void startTimer(int amount) {
        if(this.started && thread != null) {
            this.thread.interrupt();
        }

        this.thread = new Thread(this);
        this.thread.start();

        this.gameLength = amount;
        this.started = true;

        log.debug("Game started for " + amount + " seconds");
    }

    public boolean isTeamed(int id) {
        return this.teams.containsKey(id);
    }

    public void removeFromTeam(int id) {
        this.teams.remove(id);
    }

    public abstract void tick();
    public abstract void gameEnds();
    public abstract void gameStarts();

    public GameType getType() {
        return this.type;
    }

    public int getTimer() {
        return this.timer;
    }

    public void increaseTimer(int i) {
        this.timer += i;
    }

    public void decreaseTimer(int i) {
        this.timer -= i;
    }

    public Map<Integer, GameTeam> getTeams() {
        return teams;
    }

    public Logger getLog() {
        return this.log;
    }
}
