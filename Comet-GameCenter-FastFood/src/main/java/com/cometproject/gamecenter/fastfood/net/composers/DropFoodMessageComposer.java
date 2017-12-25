package com.cometproject.gamecenter.fastfood.net.composers;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.gamecenter.fastfood.objects.FoodPlate;
import com.cometproject.server.protocol.messages.MessageComposer;

public class DropFoodMessageComposer extends MessageComposer {

    private final FoodPlate foodPlate;

    public DropFoodMessageComposer(FoodPlate foodPlate) {
        this.foodPlate = foodPlate;
    }

    @Override
    public short getId() {
        return 4;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.foodPlate.getObjectId());
        msg.writeInt(this.foodPlate.getPlayerId());
        msg.writeString(Float.toString(this.foodPlate.getLocation())); // locY
        msg.writeString(Float.toString(this.foodPlate.getSpeed()));//speed
        msg.writeInt(this.foodPlate.getState());// state
        msg.writeBoolean(false);// isFalseStarted
    }
}