package com.cometproject.server.network.messages.outgoing.room.gifts;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.GiftData;
import com.cometproject.api.game.furniture.types.ItemType;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class OpenGiftMessageComposer extends MessageComposer {
    private final int presentId;
    private final String type;
    private final GiftData giftData;
    private final FurnitureDefinition itemDefinition;

    public OpenGiftMessageComposer(final int presentId, final String type, final GiftData giftData, final FurnitureDefinition itemDefinition) {
        this.presentId = presentId;
        this.type = type;
        this.giftData = giftData;
        this.itemDefinition = itemDefinition;
    }

    @Override
    public short getId() {
        return Composers.OpenGiftMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(itemDefinition.getType());
        msg.writeInt(itemDefinition.getSpriteId());
        msg.writeString(itemDefinition.getPublicName());
        msg.writeInt(presentId);
        msg.writeString(type);
        msg.writeBoolean(itemDefinition.getItemType() == ItemType.FLOOR);
        msg.writeString(giftData.getExtraData());
    }
}
