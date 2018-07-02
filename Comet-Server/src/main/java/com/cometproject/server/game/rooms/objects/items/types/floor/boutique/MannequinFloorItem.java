package com.cometproject.server.game.rooms.objects.items.types.floor.boutique;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

import java.util.Arrays;


public class MannequinFloorItem extends RoomItemFloor {
    private String name = "New Mannequin";
    private String figure = "ch-210-62.lg-270-62";
    private String gender = "m";

    public MannequinFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        if (!this.getItemData().getData().isEmpty()) {
            String[] splitData = this.getItemData().getData().split(";#;");
            if (splitData.length != 3) return;

            this.name = splitData[0];
            this.figure = splitData[1];
            this.gender = splitData[2];

            String[] figureParts = this.figure.split("\\.");
            StringBuilder finalFigure = new StringBuilder();

            for (String figurePart : figureParts) {
                if (!figurePart.contains("hr") && !figurePart.contains("hd") && !figurePart.contains("he") && !figurePart.contains("ha")) {
                    finalFigure.append(figurePart).append(".");
                }
            }

            this.figure = finalFigure.substring(0, finalFigure.length() - 1);
        }
    }

    public void composeItemData(IComposer msg) {
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(3);

        msg.writeString("GENDER");
        msg.writeString(this.getGender());
        msg.writeString("FIGURE");
        msg.writeString(this.getFigure());
        msg.writeString("OUTFIT_NAME");
        msg.writeString(this.getName());
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (isWiredTrigger || !(entity instanceof PlayerEntity))
            return isWiredTrigger;

        PlayerEntity playerEntity = (PlayerEntity) entity;

        if (this.name == null || this.gender == null || this.figure == null) return false;

        if (!this.gender.equals(playerEntity.getGender())) return false;

        String newFigure = "";

        for (String playerFigurePart : playerEntity.getFigure().split("\\.")) {
            if (!playerFigurePart.startsWith("ch") && !playerFigurePart.startsWith("lg"))
                newFigure += playerFigurePart + ".";
        }

        String newFigureParts = "";

        switch (playerEntity.getGender().toUpperCase()) {
            case "M":
                if (this.figure.equals("")) return false;
                newFigureParts = this.figure;
                break;

            case "F":
                if (this.figure.equals("")) return false;
                newFigureParts = this.figure;
                break;
        }

        for (String newFigurePart : newFigureParts.split("\\.")) {
            if (newFigurePart.startsWith("hd"))
                newFigureParts = newFigureParts.replace(newFigurePart, "");
        }

        if (newFigureParts.equals("")) return false;

        final String figure = newFigure + newFigureParts;

        if (figure.length() > 512)
            return false;

        playerEntity.getPlayer().getData().setFigure(figure);
        playerEntity.getPlayer().getData().setGender(this.gender);

        playerEntity.getPlayer().getData().save();
        playerEntity.getPlayer().poof();

        return true;
    }

    @Override
    public String getDataObject() {
        return this.name + ";#;" + this.figure + ";#;" + this.gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

