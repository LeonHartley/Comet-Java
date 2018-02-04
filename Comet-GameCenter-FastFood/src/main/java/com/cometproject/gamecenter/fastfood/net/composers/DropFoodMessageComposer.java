package com.cometproject.gamecenter.fastfood.net.composers;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.gamecenter.fastfood.objects.FoodPlate;
import com.cometproject.server.protocol.messages.MessageComposer;

public class DropFoodMessageComposer extends MessageComposer {
    private final int objectId;
    private final FoodPlate foodPlate;

    public DropFoodMessageComposer(int objectId, FoodPlate foodPlate) {
        this.objectId = objectId;
        this.foodPlate = foodPlate;
    }

    @Override
    public short getId() {
        return 4;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(objectId);

        msg.writeInt(this.foodPlate.getPlayerId());
        msg.writeString(Float.toString(this.foodPlate.getLocation())); // locY
        msg.writeString(Float.toString(this.foodPlate.getSpeed()));//speed
        msg.writeInt(this.foodPlate.getState());// state
        msg.writeBoolean(false);// isFalseStarted
    }
}