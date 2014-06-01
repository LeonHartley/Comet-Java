package com.cometproject.server.game.players.components.types;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.network.messages.types.Composer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryItem {
    private int id;
    private int baseId;
    private String extraData;
    private GiftData giftData;

    public InventoryItem(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.baseId = data.getInt("base_item");
        this.extraData = data.getString("extra_data");
    }

    public InventoryItem(int id, int baseId, String extraData, GiftData giftData) {
        this.init(id, baseId, extraData, giftData);
    }

    public InventoryItem(int id, int baseId, String extraData) {
        this.init(id, baseId, extraData, null);
    }

    private void init(int id, int baseId, String extraData, GiftData giftData) {
        this.id = id;
        this.baseId = baseId;
        this.extraData = extraData;
        this.giftData = giftData;
    }

    public void serializeTrade(Composer msg) {
        msg.writeInt(this.id);
        msg.writeString(this.getDefinition().getType().toLowerCase());
        msg.writeInt(this.id);
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeInt(0);
        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeString("");
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);

        if (this.getDefinition().getType().equals("s")) {
            msg.writeInt(0);
        }
    }

    public int getId() {
        return this.id;
    }

    public ItemDefinition getDefinition() {
        return CometManager.getItems().getDefintionNullable(this.getBaseId());
    }

    public int getBaseId() {
        return this.baseId;
    }

    public String getExtraData() {
        return this.extraData;
    }

    public GiftData getGiftData() {
        return giftData;
    }
}
