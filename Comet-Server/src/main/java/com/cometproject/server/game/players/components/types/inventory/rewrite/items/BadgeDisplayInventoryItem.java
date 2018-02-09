package com.cometproject.server.game.players.components.types.inventory.rewrite.items;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.components.types.inventory.rewrite.InventoryItem;

public class BadgeDisplayInventoryItem extends InventoryItem {
    public BadgeDisplayInventoryItem(long id, int virtualId, String data, FurnitureDefinition furnitureDefinition, LimitedEditionItem limitedEditionItem) {
        super(id, virtualId, data, furnitureDefinition, limitedEditionItem);
    }

    @Override
    public boolean composeData(IComposer msg) {
        super.composeData(msg);

        msg.writeInt(4);

        String badge;
        String name = "";
        String date = "";

        if(this.getExtraData().contains("~")) {
            String[] data = this.getExtraData().split("~");

            badge = data[0];
            name = data[1];
            date = data[2];
        } else {
            badge = this.getExtraData();
        }

        msg.writeString("0");
        msg.writeString(badge);
        msg.writeString(name); // creator
        msg.writeString(date); // date

        return true;
    }
}
