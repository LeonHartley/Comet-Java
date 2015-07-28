package com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.data.ScoreboardItemData;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.JsonFactory;
import com.google.common.collect.Lists;

import java.util.List;

public class HighscoreClassicFloorItem extends RoomItemFloor {

    private final ScoreboardItemData itemData;

    public HighscoreClassicFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if(data.startsWith("{")) {
            this.itemData = JsonFactory.getInstance().fromJson(data, ScoreboardItemData.class);
        } else {
            this.itemData = new ScoreboardItemData(1, 0, Lists.newArrayList());
        }
    }

    @Override
    public String getDataObject() {
        return JsonFactory.getInstance().toJson(this.itemData);
    }

    public void addEntry(List<String> users, int score) {
        this.itemData.addEntry(users, score);
        this.sendUpdate();
    }

    public ScoreboardItemData getScoreData() {
        return itemData;
    }

    public void composeHighscoreData(IComposer msg) {
        msg.writeInt(6);

        msg.writeString("1");
        msg.writeInt(this.getScoreData().getScoreType());
        msg.writeInt(this.getScoreData().getClearType());

        msg.writeInt(this.getScoreData().getEntries().size());

        for(ScoreboardItemData.HighscoreEntry entry : this.getScoreData().getEntries()) {
            msg.writeInt(entry.getScore());
            msg.writeInt(entry.getUsers().size());

            for(String name : entry.getUsers()) {
                msg.writeString(name);
            }
        }
    }
}
