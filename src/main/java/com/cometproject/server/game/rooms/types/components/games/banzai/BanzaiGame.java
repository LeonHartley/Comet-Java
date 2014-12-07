package com.cometproject.server.game.rooms.types.components.games.banzai;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.banzai.BanzaiTileFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerGameEnds;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerGameStarts;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import javolution.util.FastMap;

import java.util.Map;

public class BanzaiGame extends RoomGame {
    private Map<GameTeam, Integer> scores;
    private int startBanzaiTileCount = 0;
    private int banzaiTileCount = 0;

    public BanzaiGame(Room room) {
        super(room, GameType.BANZAI);

        this.scores = new FastMap<GameTeam, Integer>() {{
            add(GameTeam.BLUE, 0);
            add(GameTeam.YELLOW, 0);
            add(GameTeam.RED, 0);
            add(GameTeam.GREEN, 0);
        }};
    }

    @Override
    public void tick() {
        if(this.startBanzaiTileCount != 0 && this.banzaiTileCount == 0) {
            // Stop the game!
            this.timer = this.gameLength;
        }

        for (RoomItemFloor item : room.getItems().getByInteraction("bb_timer")) {
            item.setExtraData((gameLength - timer) + "");
            item.sendUpdate();
        }
    }

    @Override
    public void gameStarts() {
        WiredTriggerGameStarts.executeTriggers(this.room);

        this.banzaiTileCount = 0;

        for (RoomItemFloor item : this.room.getItems().getByInteraction("bb_patch")) {
            this.banzaiTileCount++;
            ((BanzaiTileFloorItem) item).onGameStarts();
        }

        this.startBanzaiTileCount = this.banzaiTileCount;

        this.updateScoreboard(null);
    }

    @Override
    public void gameEnds() {
        GameTeam winningTeam = this.winningTeam();

        for (RoomItemFloor item : this.room.getItems().getByInteraction("bb_patch")) {
            if(item instanceof BanzaiTileFloorItem) {
                if(((BanzaiTileFloorItem) item).getTeam() == winningTeam && winningTeam != GameTeam.NONE) {
                    ((BanzaiTileFloorItem) item).flash();
                }
            }
        }

        for (GenericEntity entity : this.room.getEntities().getAllEntities().values()) {
            if (entity.getEntityType().equals(RoomEntityType.PLAYER)) {
                PlayerEntity playerEntity = (PlayerEntity) entity;

                if (this.getGameComponent().getTeam(playerEntity.getPlayerId()).equals(winningTeam) && winningTeam != GameTeam.NONE) {
                    this.room.getEntities().broadcastMessage(ActionMessageComposer.compose(playerEntity.getId(), 1)); // wave o/
                }
            }
        }

        this.scores.clear();
        WiredTriggerGameEnds.executeTriggers(this.room);
    }

    public void increaseScore(GameTeam team, int amount) {
        this.scores.replace(team, this.getScore(team) + amount);
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
        if(this.scores.containsKey(team)) {
            return this.scores.get(team);
        }

        return 0;
    }

    public GameTeam winningTeam() {
        Map.Entry<GameTeam, Integer> winningTeam = null;

        for (Map.Entry<GameTeam, Integer> score : this.scores.entrySet()) {
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
