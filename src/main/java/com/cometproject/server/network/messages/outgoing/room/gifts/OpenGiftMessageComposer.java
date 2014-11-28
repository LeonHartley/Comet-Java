package com.cometproject.server.network.messages.outgoing.room.gifts;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class OpenGiftMessageComposer {
    public static Composer compose(int presentId, String type, GiftData giftData, ItemDefinition itemDefinition) {
        Composer msg = new Composer(Composers.OpenGiftMessageComposer);

        msg.writeString(itemDefinition.getType());
        msg.writeInt(itemDefinition.getSpriteId());
        msg.writeString(itemDefinition.getPublicName());
        msg.writeInt(presentId);
        msg.writeString(type);
        msg.writeBoolean(true);
        msg.writeString(giftData.getExtraData());

        return msg;
    }
}
