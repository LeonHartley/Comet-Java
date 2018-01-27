package com.cometproject.server.composers.catalog;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class BoughtItemMessageComposer extends MessageComposer {
    private final ICatalogItem catalogItem;
    private final FurnitureDefinition itemDefinition;

    private final boolean isGroup;

    private BoughtItemMessageComposer(final ICatalogItem catalogItem, final FurnitureDefinition itemDefinition, final boolean isGroup) {
        this.catalogItem = catalogItem;
        this.itemDefinition = itemDefinition;
        this.isGroup = isGroup;
    }

    public BoughtItemMessageComposer(final ICatalogItem catalogItem, final FurnitureDefinition itemDefinition) {
        this(catalogItem, itemDefinition, false);
    }

    public BoughtItemMessageComposer(final PurchaseType type) {
        this(null, null, type == PurchaseType.GROUP);
    }

    public enum PurchaseType {
        GROUP,
        BADGE
    }

    @Override
    public short getId() {
        return Composers.PurchaseOKMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        if (this.catalogItem != null && this.itemDefinition != null) {
            msg.writeInt(this.catalogItem.getId());
            msg.writeString(this.itemDefinition.getItemName());
            msg.writeBoolean(false);
            msg.writeInt(this.catalogItem.getCostCredits());
            msg.writeInt(this.catalogItem.getCostActivityPoints());
            msg.writeInt(0);
            msg.writeBoolean(false);
            msg.writeInt(1);
            msg.writeString(this.itemDefinition.getType());
            msg.writeInt(this.itemDefinition.getSpriteId());
            msg.writeString("");
            msg.writeInt(1);
            msg.writeString("");
            msg.writeInt(1);

            return;
        }

        if (this.isGroup) {
            msg.writeInt(6165);
            msg.writeString("CREATE_GUILD");
            msg.writeBoolean(false);
            msg.writeInt(CometSettings.groupCost);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeBoolean(true);
            msg.writeInt(0);
            msg.writeInt(2);
            msg.writeBoolean(false);
        } else {
            msg.writeInt(0);
            msg.writeString("");
            msg.writeBoolean(false);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeBoolean(true);
            msg.writeInt(1);
            msg.writeString("s");
            msg.writeInt(0);
            msg.writeString("");
            msg.writeInt(1);
            msg.writeInt(0);
            msg.writeString("");
            msg.writeInt(1);
        }
    }
}
