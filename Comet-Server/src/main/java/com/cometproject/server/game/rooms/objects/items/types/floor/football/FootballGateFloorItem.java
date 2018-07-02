package com.cometproject.server.game.rooms.objects.items.types.floor.football;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

import java.util.Arrays;


public class FootballGateFloorItem extends RoomItemFloor {
    public FootballGateFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        if (this.getItemData().getData().equals("0")) {
            this.getItemData().setData("hd-99999-99999.ch-3030-63.lg-275-1408;hd-99999-99999.ch-3030-63.lg-275-1408");
            this.saveData();
        }
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (!(entity instanceof PlayerEntity))
            return;

        PlayerEntity playerEntity = ((PlayerEntity) entity);

        String newFigure = "";

        for (String playerFigurePart : playerEntity.getFigure().split("\\.")) {
            if (!playerFigurePart.startsWith("ch") && !playerFigurePart.startsWith("lg"))
                newFigure += playerFigurePart + ".";
        }

        String newFigureParts = this.getFigure(playerEntity.getGender());

        for (String newFigurePart : newFigureParts.split("\\.")) {
            if (newFigurePart.startsWith("hd"))
                newFigureParts = newFigureParts.replace(newFigurePart, "");
        }

        if (newFigureParts.equals("")) return;

        playerEntity.getPlayer().getData().setFigure(newFigure + newFigureParts);
        playerEntity.getPlayer().poof();

        playerEntity.getPlayer().getData().save();
    }

    public void setFigure(String gender, String figure) {
        switch (gender.toUpperCase()) {
            case "M":
                this.getItemData().setData(figure + "," + this.getFigure("F"));
                break;

            case "F":
                this.getItemData().setData(this.getFigure("M") + "," + figure);
                break;
        }
    }

    public String getFigure(String gender) {
        if (this.getItemData().getData().length() < 2) {
            return "hd-99999-99999.ch-3030-63.lg-275-1408";
        }

        String[] figureData = this.getItemData().getData().split(",");
        String figure;

        if (gender.toUpperCase().equals("M")) {
            figure = figureData[0];
        } else {
            if (figureData.length != 2) {
                figure = "";
            } else {
                figure = figureData[1];
            }
        }

        return figure.isEmpty() ? "hd-99999-99999.ch-3030-63.lg-275-1408" : figure.replace(",", "");
    }
}
