package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WiredActionGiveScore extends WiredActionItem {
    private final static int PARAM_SCORE = 0;
    private final static int PARAM_PER_GAME = 1;

    private final Map<Integer, AtomicInteger> awardedPlayers = Maps.newConcurrentMap();

    public WiredActionGiveScore(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        if (this.getWiredData().getParams().size() < 2) {
            this.getWiredData().getParams().clear();

            this.getWiredData().getParams().put(PARAM_SCORE, 1);
            this.getWiredData().getParams().put(PARAM_PER_GAME, 1);
        }
    }

    @Override
    public void onLoad() {
        this.getRoom().getGame().subscribe(this);
    }

    @Override
    public void onPlaced() {
        this.onLoad();
    }

    @Override
    public void onPickup() {
        super.onPickup();

        this.getRoom().getGame().unsubscribe(this);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 6;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (!(event.entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity playerEntity = ((PlayerEntity) event.entity);
        if (!this.awardedPlayers.containsKey(playerEntity.getPlayerId())) {
            this.awardedPlayers.put(playerEntity.getPlayerId(), new AtomicInteger(1));
        } else {
            if (this.awardedPlayers.get(playerEntity.getPlayerId()).getAndIncrement() > this.timesPerGame()) {
                return;
            }
        }

        this.getRoom().getGame().increaseScore(playerEntity, this.getScore());
    }

    @Override
    public void onGameStarts(RoomGame roomGame) {
        this.awardedPlayers.clear();
    }

    public int getScore() {
        return this.getWiredData().getParams().get(PARAM_SCORE);
    }

    public int timesPerGame() {
        return this.getWiredData().getParams().get(PARAM_PER_GAME);
    }
}
