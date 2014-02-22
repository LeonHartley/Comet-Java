package com.cometproject.game.players.components.types;

import com.cometproject.game.GameEngine;
import com.cometproject.game.items.types.ItemDefinition;
import com.cometproject.network.messages.types.Composer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryItem {
    private int id;
    private int baseId;
    private String extraData;

    public InventoryItem(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.baseId = data.getInt("base_item");
        this.extraData = data.getString("extra_data");
    }

    public InventoryItem(int id, int baseId, String extraData) {
        this.id = id;
        this.baseId = baseId;
        this.extraData = extraData;
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

        if(this.getDefinition().getType().equals("s")) {
            msg.writeInt(0);
        }
    }

    public int getId() {
        return this.id;
    }

    public ItemDefinition getDefinition() {
        return GameEngine.getItems().getDefintion(this.getBaseId());
    }

    public int getBaseId() {
        return this.baseId;
    }

    public String getExtraData() {
        return this.extraData;
    }
}
