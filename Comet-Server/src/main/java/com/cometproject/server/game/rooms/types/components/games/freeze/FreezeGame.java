package com.cometproject.server.game.rooms.types.components.games.freeze;

import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai.BanzaiTimerFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze.FreezeTileFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze.FreezeTimerFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import com.cometproject.server.network.messages.outgoing.room.freeze.UpdateFreezeLivesMessageComposer;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FreezeGame extends RoomGame {

    private final Map<Integer, Integer> lives = Maps.newConcurrentMap();

    public FreezeGame(Room room) {
        super(room, GameType.FREEZE);
    }

    @Override
    public void tick() {
//        for(PlayerEntity playerEntity : this.room.getGame().getPlayers()) {
//            playerEntity.getRoom().getEntities().broadcastMessage(new UpdateFreezeLivesMessageComposer(playerEntity.getId(), 3));
//        }

        for (RoomItemFloor item : room.getItems().getByClass(FreezeTimerFloorItem.class)) {
            item.setExtraData((gameLength - timer) + "");
            item.sendUpdate();
        }
    }

    @Override
    public void onGameStarts() {
        // Everyone starts with 40 points & 3 lives.
        for(PlayerEntity playerEntity : this.room.getGame().getPlayers()) {
            this.getGameComponent().increaseScore(playerEntity.getGameTeam(), 40);
            playerEntity.getRoom().getEntities().broadcastMessage(new UpdateFreezeLivesMessageComposer(playerEntity.getId(), 3));
        }
    }

    public int getLives(int playerId) {
        return this.lives.get(playerId);
    }

    public void loseLife(int playerId) {
        int lives = this.getLives(playerId);

        this.lives.replace(playerId, lives, lives - 1);
    }

    public void gainLife(int playerId) {
        int lives = this.getLives(playerId);

        this.lives.replace(playerId, lives, lives + 1);
    }

    @Override
    public void onGameEnds() {
        // reset all scores to 0
        this.getGameComponent().resetScores(true);
    }
}
