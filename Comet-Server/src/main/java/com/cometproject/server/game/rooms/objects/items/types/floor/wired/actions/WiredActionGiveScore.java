package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore.HighscoreClassicFloorItem;
import com.cometproject.server.game.rooms.types.Room;

import java.util.List;


public class WiredActionGiveScore extends WiredActionItem {
    private final static int PARAM_SCORE = 0;
    private final static int PARAM_PER_GAME = 1;

    public WiredActionGiveScore(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        if (this.getWiredData().getParams().size() < 2) {
            this.getWiredData().getParams().clear();

            this.getWiredData().getParams().put(PARAM_SCORE, 1);
            this.getWiredData().getParams().put(PARAM_PER_GAME, 1);
        }
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

        final List<HighscoreClassicFloorItem> scoreboards = getRoom().getItems().getByClass(HighscoreClassicFloorItem.class);

        for (HighscoreClassicFloorItem scoreboard : scoreboards) {
            scoreboard.addPoint(playerEntity.getUsername(), 1);
        }
    }

    public int getScore() {
        return this.getWiredData().getParams().get(PARAM_SCORE);
    }
}
