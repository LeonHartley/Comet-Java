package com.cometproject.server.game.rooms.types.components.games.banzai;

import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.banzai.BanzaiTileFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.banzai.BanzaiTimerFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore.HighscoreClassicFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerGameEnds;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerGameStarts;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;


public class BanzaiGame extends RoomGame {
    private int startBanzaiTileCount = 0;
    private int banzaiTileCount = 0;

    public BanzaiGame(Room room) {
        super(room, GameType.BANZAI);
    }

    @Override
    public void tick() {
        if (this.startBanzaiTileCount != 0 && this.banzaiTileCount == 0) {
            // Stop the game!
            this.timer = this.gameLength;
        }

        for (RoomItemFloor item : room.getItems().getByClass(BanzaiTimerFloorItem.class)) {
            item.setExtraData((gameLength - timer) + "");
            item.sendUpdate();
        }

        for (RoomEntity entity : this.room.getEntities().getAllEntities().values()) {
            if (entity.getEntityType().equals(RoomEntityType.PLAYER)) {
                PlayerEntity playerEntity = (PlayerEntity) entity;

                if (this.getGameComponent().getTeam(playerEntity.getPlayerId()) != GameTeam.NONE) {
                    if (playerEntity.getBanzaiPlayerAchievement() >= 60) {
                        playerEntity.getPlayer().getAchievements().progressAchievement(AchievementType.BB_PLAYER, 1);
                        playerEntity.setBanzaiPlayerAchievement(0);
                    } else {
                        playerEntity.incrementBanzaiPlayerAchievement();
                    }
                }
            }
        }

    }

    @Override
    public void onGameStarts() {
        WiredTriggerGameStarts.executeTriggers(this.room);

        this.banzaiTileCount = 0;

        for (RoomItemFloor item : this.room.getItems().getByClass(BanzaiTileFloorItem.class)) {
            this.banzaiTileCount++;
            ((BanzaiTileFloorItem) item).onGameStarts();
        }

        this.startBanzaiTileCount = this.banzaiTileCount;

        this.updateScoreboard(null);
    }

    @Override
    public void onGameEnds() {
        GameTeam winningTeam = this.winningTeam();

        for (RoomItemFloor item : this.room.getItems().getByClass(BanzaiTileFloorItem.class)) {
            if (item instanceof BanzaiTileFloorItem) {
                if (((BanzaiTileFloorItem) item).getTeam() == winningTeam && winningTeam != GameTeam.NONE) {
                    ((BanzaiTileFloorItem) item).flash();
                } else {
                    ((BanzaiTileFloorItem) item).onGameEnds();
                }
            }
        }

        final List<RoomItemFloor> scoreboards = this.room.getItems().getByClass(HighscoreClassicFloorItem.class);

        if (scoreboards.size() != 0) {
            List<Integer> winningPlayers = this.room.getGame().getTeams().get(this.winningTeam());
            List<String> winningPlayerUsernames = Lists.newArrayList();

            for (int playerId : winningPlayers) {
                winningPlayerUsernames.add(this.room.getEntities().getEntityByPlayerId(playerId).getUsername());
            }

            if (winningPlayerUsernames.size() != 0) {
                for (RoomItemFloor scoreboard : scoreboards) {
                    ((HighscoreClassicFloorItem) scoreboard).addEntry(winningPlayerUsernames, this.getScore(this.winningTeam()));
                }
            }
        }

        for (RoomEntity entity : this.room.getEntities().getAllEntities().values()) {
            if (entity.getEntityType().equals(RoomEntityType.PLAYER)) {
                PlayerEntity playerEntity = (PlayerEntity) entity;

                if (this.getGameComponent().getTeam(playerEntity.getPlayerId()).equals(winningTeam) && winningTeam != GameTeam.NONE) {
                    playerEntity.getPlayer().getAchievements().progressAchievement(AchievementType.BB_WINNER, 1);
                    this.room.getEntities().broadcastMessage(new ActionMessageComposer(playerEntity.getId(), 1)); // wave o/
                }
            }
        }

        this.getGameComponent().resetScores();
        WiredTriggerGameEnds.executeTriggers(this.room);
    }

    public void increaseScore(GameTeam team, int amount) {
        this.getGameComponent().increaseScore(team, amount);
        this.updateScoreboard(team);
    }

    public void updateScoreboard(GameTeam team) {
        for (RoomItemFloor scoreboard : this.getGameComponent().getRoom().getItems().getByInteraction("%_score")) {
            if (team == null || scoreboard.getDefinition().getInteraction().toUpperCase().startsWith(team.name())) {
                scoreboard.setExtraData(team == null ? "0" : this.getScore(team) + "");
                scoreboard.sendUpdate();
            }
        }
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

    public void decreaseTileCount() {
        this.banzaiTileCount--;
    }
}
