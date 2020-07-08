package com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.types.Room;

import java.util.List;

public class HighscoreClassicFloorItem extends HighscoreFloorItem {
    public HighscoreClassicFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);
    }

    @Override
    public void onTeamWins(List<String> users, int score) {
        this.addEntry(users, score);
    }

    @Override
    public int getScoreType() {
        return 2;
    }
}
