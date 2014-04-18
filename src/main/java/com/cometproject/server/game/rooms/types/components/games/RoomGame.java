package com.cometproject.server.game.rooms.types.components.games;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.tasks.CometTask;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class RoomGame implements CometTask {
    // TODO: recode this to use comet's thread pool

    private Map<GameTeam, List<Integer>> teams;
    private GameType type;
    protected int timer;
    protected int gameLength;
    protected boolean active;
    protected boolean finished = false;
    protected Room room;

    private ScheduledFuture future;
    private Logger log;

    public RoomGame(Room room, GameType gameType) {
        this.log = Logger.getLogger("RoomGame [" + room.getData().getName() + "][" + room.getData().getId() + "][" + this.type + "");
        this.teams = new FastMap<>();
        this.type = gameType;
        this.room = room;

        for (GameTeam team : GameTeam.values()) {
            this.teams.put(team, new FastList<Integer>());
        }
    }

    @Override
    public void run() {
        try {
            if(timer == 0) {
                gameStarts();
            }

            tick();

            if(timer >= gameLength) {
                gameEnds();
                room.getGame().stop();
            }

            timer++;
        } catch (Exception e) {
            log.error("Error during game tick", e);
        }
    }

    public void dispose() {
        this.teams.clear();
    }

    public void stop() {
        if (this.active && this.future != null) {
            this.future.cancel(false);

            this.active = false;
            this.gameLength = 0;
            this.timer = 0;

            this.dispose();
        }
    }

    public void startTimer(int amount) {
        if (this.active && this.future != null) {
            this.future.cancel(false);
        }

        this.future = Comet.getServer().getThreadManagement().executePeriodic(this, 0, 1, TimeUnit.SECONDS);

        this.gameLength = amount;
        this.active = true;

        log.debug("Game active for " + amount + " seconds");
    }

    public boolean isTeamed(int id) {
        return this.getTeam(id) != GameTeam.NONE;
    }

    public void removeFromTeam(GameTeam team, Integer id) {
        if (this.teams.get(team).contains(id))
            this.teams.get(team).remove(id);
    }

    public GameTeam getTeam(int userId) {
        for (Map.Entry<GameTeam, List<Integer>> entry : this.getTeams().entrySet()) {
            if (entry.getValue().contains(userId)) {
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
