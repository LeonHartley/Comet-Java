package com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.data.ScoreboardItemData;
import com.cometproject.server.game.rooms.types.Room;
import com.google.common.collect.Lists;

import java.util.*;

public class HighscoreClassicFloorItem extends RoomItemFloor {

    private final ScoreboardItemData itemData;
    private boolean state;

    public HighscoreClassicFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        final String data = roomItemData.getData();

        if (roomItemData.getData().startsWith("1{") || roomItemData.getData().startsWith("0{")) {
            this.state = data.startsWith("1");
            this.itemData = JsonUtil.getInstance().fromJson(data.substring(1), ScoreboardItemData.class);
        } else {
            this.state = false;
            this.itemData = new ScoreboardItemData(1, 0, Lists.newArrayList(), new LinkedHashMap<>());
        }
    }

    @Override
    public void composeItemData(IComposer msg) {
        msg.writeInt(0);
        this.composeHighscoreData(msg);
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
        return (this.state ? "1" : "0") + JsonUtil.getInstance().toJson(this.itemData);
    }

    public void addEntry(List<String> users, int score) {
        this.itemData.addEntry(users, score);
        this.sendUpdate();

        this.saveData();
    }

    public void addPoint(String identifier, int score) {
        this.itemData.addPoint(identifier, score);

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

        msg.writeInt(this.getScoreData().getPoints().size() > 50 ? 50 : this.getScoreData().getPoints().size());

        int x = 0;

        List<Map.Entry<String, Integer>> list = new ArrayList<>(this.getScoreData().getPoints().entrySet());

        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        for (Map.Entry<String, Integer> entry : list) {
            x++;

            if (x > 50) break;

            msg.writeInt(entry.getValue());
            msg.writeInt(1);

            //for (String name : entry.getUsers()) {
            msg.writeString(entry.getKey());
            //}
        }
    }
}
