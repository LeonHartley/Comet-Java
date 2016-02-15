package com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.data.ScoreboardItemData;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.JsonFactory;
import com.google.common.collect.Lists;

import java.util.List;

public class HighscoreClassicFloorItem extends RoomItemFloor {

    private boolean state;
    private final ScoreboardItemData itemData;

    public HighscoreClassicFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if(data.startsWith("1{") || data.startsWith("0{")) {
            this.state = data.startsWith("1");
            this.itemData = JsonFactory.getInstance().fromJson(data.substring(1), ScoreboardItemData.class);
        } else {
            this.state = false;
            this.itemData = new ScoreboardItemData(1, 0, Lists.newArrayList());
        }
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!(entity instanceof PlayerEntity)) {
                return false;
            }

            PlayerEntity pEntity = (PlayerEntity) entity;

            if (!pEntity.getRoom().getRights().hasRights(pEntity.getPlayerId())
                    && !pEntity.getPlayer().getPermissions().getRank().roomFullControl()) {
                return false;
            }
        }

        this.state = !this.state;

        this.sendUpdate();
        this.saveData();
        return true;
    }

    @Override
    public String getDataObject() {
        return (this.state ? "1" : "0") + JsonFactory.getInstance().toJson(this.itemData);
    }

    public void addEntry(List<String> users, int score) {
        this.itemData.addEntry(users, score);
        this.sendUpdate();

        this.saveData();
    }

    public ScoreboardItemData getScoreData() {
        return itemData;
    }

    public void composeHighscoreData(IComposer msg) {
        msg.writeInt(6);

        msg.writeString(this.state ? "1" : "0");
        msg.writeInt(this.getScoreData().getScoreType());
        msg.writeInt(this.getScoreData().getClearType());

        msg.writeInt(this.getScoreData().getEntries().size() > 50 ? 50 : this.getScoreData().getEntries().size());

        int x = 0;

        for(ScoreboardItemData.HighscoreEntry entry : this.getScoreData().getEntries()) {
            x++;

            if(x > 50) break;

            msg.writeInt(entry.getScore());
            msg.writeInt(entry.getUsers().size());

            for(String name : entry.getUsers()) {
                msg.writeString(name);
            }
        }
    }
}
