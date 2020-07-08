package com.cometproject.server.game.rooms.types.components.games;

import com.cometproject.api.game.utilities.RandomUtil;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons.WiredAddonBlob;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore.HighscoreFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.GameComponent;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public abstract class RoomGame implements CometTask {
    protected int timer;
    protected int gameLength;
    protected boolean active = false;
    protected boolean finished = false;
    protected Room room;
    private GameType type;
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
                this.active = true;
                final List<WiredAddonBlob> blobs = room.getItems().getByClass(WiredAddonBlob.class);
                Collections.shuffle(blobs);

                for (WiredAddonBlob blob : blobs) {
                    blob.onGameStarted();
                }

                onGameStarts();
            }

            try {
                if (this.getGameComponent().getBlobCounter().get() < 2) {
                    if (RandomUtil.getRandomBool(0.1)) {
                        final List<WiredAddonBlob> blobs = room.getItems().getByClass(WiredAddonBlob.class);
                        Collections.shuffle(blobs);

                        for (WiredAddonBlob blob : blobs) {
                            blob.onGameStarted();
                        }
                    }
                }

                tick();
            } catch (Exception e) {
                log.error("Failed to process game tick", e);
            }

            if (timer >= gameLength) {
                gameEnds();
                room.getGame().stop();
                this.stop();
            }

            timer++;
        } catch (Exception e) {
            log.error("Error during game process", e);
        }
    }

    public void stop() {
        for (WiredAddonBlob blob : room.getItems().getByClass(WiredAddonBlob.class)) {
            blob.hideBlob();
        }

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

    public void gameEnds() {
        final List<HighscoreFloorItem> scoreboards = this.room.getItems().getByClass(HighscoreFloorItem.class);

        if (scoreboards.size() != 0) {
            List<Integer> winningPlayers = this.room.getGame().getTeams().get(this.winningTeam());
            List<String> winningPlayerUsernames = Lists.newArrayList();
            final int score = this.getScore(this.winningTeam());

            if (score > 0) {
                for (int playerId : winningPlayers) {
                    winningPlayerUsernames.add(this.room.getEntities().getEntityByPlayerId(playerId).getUsername());
                }

                if (winningPlayerUsernames.size() != 0) {
                    for (HighscoreFloorItem scoreboard : scoreboards) {
                        scoreboard.onTeamWins(winningPlayerUsernames, score);
                    }
                }
            }
        }

        this.onGameEnds();
    }

    public int getScore(GameTeam team) {
        return this.getGameComponent().getScore(team);
    }

    public GameTeam winningTeam() {
        Map.Entry<GameTeam, Integer> winningTeam = null;

        for (Map.Entry<GameTeam, Integer> score : this.getGameComponent().getScores().entrySet()) {
            if (winningTeam == null || winningTeam.getValue() < score.getValue()) {
                winningTeam = score;
            }
        }

        return winningTeam != null ? winningTeam.getKey() : GameTeam.NONE;
    }

    public abstract void onGameEnds();

    public abstract void onGameStarts();

    public GameType getType() {
        return this.type;
    }

    public Logger getLog() {
        return this.log;
    }

    public boolean isActive() {
        return active;
    }
}
