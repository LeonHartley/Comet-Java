package com.cometproject.server.game.rooms.types.components.games;

import com.cometproject.server.game.rooms.types.Room;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class RoomGame implements Runnable {
    // TODO: recode this to use comet's thread pool

    private Map<GameTeam, List<Integer>> teams;
    private GameType type;
    protected int timer;
    protected int gameLength;
    protected boolean active;
    protected boolean finished = false;
    protected Room room;

    private Thread thread;
    private Logger log;

    public RoomGame(Room room, GameType gameType) {
        this.log = Logger.getLogger("RoomGame [" + room.getData().getName() + "][" + room.getData().getId() + "]");
        this.teams = new FastMap<>();
        this.type = gameType;
        this.room = room;

        for(GameTeam team : GameTeam.values()) {
            this.teams.put(team, new ArrayList<Integer>());
        }
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

            active = false;
            gameEnds();

        } catch(Exception e) {
            log.error("Error during game tick", e);
        }
    }

    public void dispose() {
        this.teams.clear();
    }

    public void stop() {
        if(this.active && thread != null) {
            this.thread.interrupt();
            this.active = false;
            this.gameLength = 0;
            this.timer = 0;
        }
    }

    public void startTimer(int amount) {
        if(this.active && thread != null) {
            this.thread.interrupt();
        }

        this.thread = new Thread(this);
        this.thread.start();

        this.gameLength = amount;
        this.active = true;

        log.debug("Game active for " + amount + " seconds");
    }

    public boolean isTeamed(int id) {
        return this.getTeam(id) != GameTeam.NONE;
    }

    public void removeFromTeam(GameTeam team, int id) {
        if(this.teams.get(team).contains(id))
            this.teams.get(team).remove(id);
    }

    public GameTeam getTeam(int userId) {
        for(Map.Entry<GameTeam, List<Integer>> entry : this.getTeams().entrySet()) {
            if(entry.getValue().contains(userId)) {
                return entry.getKey();
            }
        }

        return GameTeam.NONE;
    }

    public abstract void tick();
    public abstract void gameEnds();
    public abstract void gameStarts();

    public GameType getType() {
        return this.type;
    }

    public Map<GameTeam, List<Integer>> getTeams() {
        return teams;
    }

    public Logger getLog() {
        return this.log;
    }
}
