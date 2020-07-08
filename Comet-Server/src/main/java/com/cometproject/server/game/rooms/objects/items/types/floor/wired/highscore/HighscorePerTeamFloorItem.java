package com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.data.ScoreboardItemData;
import com.cometproject.server.game.rooms.types.Room;

import java.util.List;

public class HighscorePerTeamFloorItem extends HighscoreFloorItem {
    public HighscorePerTeamFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);
    }

    @Override
    public void onTeamWins(List<String> usernames, int score) {
        this.addEntry(usernames, score);
    }

    @Override
    public int getScoreType() {
        return 0;
    }
}