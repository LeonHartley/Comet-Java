package com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.data.ScoreboardItemData;
import com.cometproject.server.game.rooms.types.Room;

import java.util.List;

public class HighscoreMostWinsFloorItem extends HighscoreFloorItem {
    public HighscoreMostWinsFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);
    }

    @Override
    public void onTeamWins(List<String> usernames, int score) {
        this.addEntry(usernames, 1, true, true);
    }

    @Override
    public int getScoreType() {
        return 1;
    }
}
