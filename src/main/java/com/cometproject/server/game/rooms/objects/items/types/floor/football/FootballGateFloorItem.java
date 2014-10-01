package com.cometproject.server.game.rooms.objects.items.types.floor.football;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

import java.util.Arrays;

public class FootballGateFloorItem extends RoomItemFloor {
    private String maleFigure = "";
    private String femaleFigure = "";

    public FootballGateFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        final String[] splittedData = data.split(";");

        if(splittedData.length != 1 && splittedData.length != 2) {
            return;
        }

        this.maleFigure = splittedData[0].replace(";", "");
        this.femaleFigure = splittedData.length > 1 ? splittedData[1].replace(";", "") : "";
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        if(!(entity instanceof PlayerEntity))
            return;

        PlayerEntity playerEntity = ((PlayerEntity) entity);

        String newFigure = "";

        for(String playerFigurePart : Arrays.asList(playerEntity.getFigure().split("\\."))) {
            if(!playerFigurePart.startsWith("ch") && !playerFigurePart.startsWith("lg"))
                newFigure += playerFigurePart + ".";
        }

        String newFigureParts = "";

        switch(playerEntity.getGender()) {
            case "M":
                if(this.maleFigure.equals("")) return;
                newFigureParts = this.maleFigure;
                break;

            case "F":
                if(this.femaleFigure.equals("")) return;
                newFigureParts = this.femaleFigure;
                break;
        }

        for(String newFigurePart : Arrays.asList(newFigureParts.split("\\."))) {
            if(newFigurePart.startsWith("hd"))
                newFigureParts = newFigureParts.replace(newFigurePart, "");
        }

        if(newFigureParts.equals("")) return;

        playerEntity.getPlayer().getData().setFigure(newFigure + newFigureParts);
        playerEntity.getPlayer().poof();

        playerEntity.getPlayer().getData().save();
    }

    public void setFigure(String gender, String figure) {
        if(gender.equals("M")) {
            this.maleFigure = figure.split(";")[0];
            return;
        }

        this.femaleFigure = figure.split(";")[1];
    }

    public void saveFigures() {
        this.setExtraData(maleFigure + ";" + femaleFigure);
        this.saveData();
    }
}
