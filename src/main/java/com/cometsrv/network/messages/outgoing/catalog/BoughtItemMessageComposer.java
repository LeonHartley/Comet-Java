package com.cometsrv.network.messages.outgoing.catalog;

import com.cometsrv.config.CometSettings;
import com.cometsrv.game.catalog.types.CatalogItem;
import com.cometsrv.game.items.types.ItemDefinition;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class BoughtItemMessageComposer {
    public static Composer compose(CatalogItem item, ItemDefinition def) {
        Composer msg = new Composer(Composers.BoughtItemMessageComposer);

        msg.writeInt(item.getId());
        msg.writeString(def.getItemName());
        msg.writeInt(item.getCostCredits());
        msg.writeInt(item.getCostActivityPoints());
        msg.writeInt(0);
        msg.writeBoolean(true);
        msg.writeInt(1);
        msg.writeString(def.getType());
        msg.writeInt(def.getSpriteId());
        msg.writeString("");
        msg.writeInt(1);
        msg.writeInt(0);
        msg.writeString("");
        msg.writeInt(1);

        return msg;
    }

    public static Composer compose() {
        Composer msg = new Composer(Composers.BoughtItemMessageComposer);

        msg.writeInt(6165);
        msg.writeString("CREATE_GUILD");
        msg.writeInt(CometSettings.groupCost);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(2);
        msg.writeBoolean(false);

        return msg;
    }
}
