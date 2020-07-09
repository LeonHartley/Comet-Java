package com.cometproject.server.game.rooms.types.components.games.banzai;

import com.cometproject.api.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai.BanzaiTileFloorItem;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import com.cometproject.server.game.rooms.types.components.games.RoomGameLogic;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;

public class BanzaiGame extends RoomGameLogic {
    private int startBanzaiTileCount = 0;
    private int banzaiTileCount = 0;

    @Override
    public void tick(RoomGame game) {
        if (this.startBanzaiTileCount != 0 && this.banzaiTileCount == 0) {
            // Stop the game!
            game.setTimer(game.getGameLength());
        }

        for (PlayerEntity playerEntity : game.getRoom().getEntities().getPlayerEntities()) {
            if (playerEntity.getGameTeam() != GameTeam.NONE && playerEntity.getGameType() == GameType.BANZAI) {
                if (playerEntity.getBanzaiPlayerAchievement() >= 60) {
                    playerEntity.getPlayer().getAchievements().progressAchievement(AchievementType.BB_PLAYER, 1);
                    playerEntity.setBanzaiPlayerAchievement(0);
                } else {
                    playerEntity.incrementBanzaiPlayerAchievement();
                }
            }
        }
    }

    @Override
    public void onGameStarts(RoomGame game) {
        this.banzaiTileCount = 0;

        for (RoomItemFloor item : game.getRoom().getItems().getByClass(BanzaiTileFloorItem.class)) {
            this.banzaiTileCount++;
            ((BanzaiTileFloorItem) item).onGameStarts();
        }

        this.startBanzaiTileCount = this.banzaiTileCount;

        this.updateScoreboard(game, null);
    }

    @Override
    public void onGameEnds(RoomGame game) {
        GameTeam winningTeam = game.winningTeam();

        for (RoomItemFloor item : game.getRoom().getItems().getByClass(BanzaiTileFloorItem.class)) {
            if (item instanceof BanzaiTileFloorItem) {
                if (((BanzaiTileFloorItem) item).getTeam() == winningTeam && winningTeam != GameTeam.NONE) {
                    ((BanzaiTileFloorItem) item).flash();
                } else {
                    ((BanzaiTileFloorItem) item).onGameEnds();
                }
            }
        }

        for (PlayerEntity playerEntity : game.getRoom().getEntities().getPlayerEntities()) {
            if (playerEntity.getGameTeam().equals(winningTeam) && winningTeam != GameTeam.NONE) {
                playerEntity.getPlayer().getAchievements().progressAchievement(AchievementType.BB_WINNER, 1);
                game.getRoom().getEntities().broadcastMessage(new ActionMessageComposer(playerEntity.getId(), 1)); // wave o/
            }
        }

        game.getGameComponent().resetScores();
    }

    public void increaseScore(RoomGame game, GameTeam team, int amount) {
        game.getGameComponent().increaseScore(team, amount);
        this.updateScoreboard(game, team);
    }

    public void updateScoreboard(RoomGame roomGame, GameTeam team) {
        for (RoomItemFloor scoreboard : roomGame.getGameComponent().getRoom().getItems().getByInteraction("%_score")) {
            if (team == null || scoreboard.getDefinition().getInteraction().toUpperCase().startsWith(team.name())) {
                scoreboard.getItemData().setData(team == null ? "0" : roomGame.getScore(team) + "");
                scoreboard.sendUpdate();
            }
        }
    }

    public void addTile() {
        this.banzaiTileCount += 1;
        this.startBanzaiTileCount += 1;
    }

    public void removeTile() {
        this.banzaiTileCount -= 1;
        this.startBanzaiTileCount -= 1;
    }

    public void decreaseTileCount() {
        this.banzaiTileCount--;
    }
}
