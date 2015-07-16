package com.cometproject.server.network.messages.outgoing.room.gifts;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class OpenGiftMessageComposer extends MessageComposer {
    private final int presentId;
    private final String type;
    private final GiftData giftData;
    private final ItemDefinition itemDefinition;

    public OpenGiftMessageComposer(final int presentId, final String type, final GiftData giftData, final ItemDefinition itemDefinition) {
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
        msg.writeBoolean(true);
        msg.writeString(giftData.getExtraData());
    }
}
