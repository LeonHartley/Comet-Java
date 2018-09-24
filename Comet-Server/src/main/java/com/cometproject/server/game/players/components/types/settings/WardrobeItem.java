package com.cometproject.server.game.players.components.types.settings;

import com.cometproject.api.game.players.data.types.IWardrobeItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WardrobeItem implements IWardrobeItem {
    private int slot;
    private String gender;
    private String figure;

    public WardrobeItem(int slot, String gender, String figure) {
        this.slot = slot;
        this.gender = gender;
        this.figure = figure;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    @Override
    public JsonElement toJson() {
        final JsonObject coreObject = new JsonObject();

        coreObject.addProperty("slot", slot);
        coreObject.addProperty("gender", gender);
        coreObject.addProperty("figure", figure);

        return coreObject;
    }
}
