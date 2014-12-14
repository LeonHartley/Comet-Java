package com.cometproject.server.network.messages.outgoing.catalog.data;

import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class GiftWrappingConfigurationMessageComposer {
    private static final int[] giftColours = {
            0, 1, 2, 3, 4, 5, 6, 8
    };

    private static final int[] giftDecorations = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    };

    public static Composer compose() {
        Composer msg = new Composer(Composers.GiftWrappingConfigurationMessageComposer);

        msg.writeBoolean(true);//?
        msg.writeInt(1);//?
        msg.writeInt(CatalogManager.getInstance().getGiftBoxesNew().size());

        for (int spriteId : CatalogManager.getInstance().getGiftBoxesNew()) {
            msg.writeInt(spriteId);
        }

        msg.writeInt(giftColours.length);

        for (int giftColour : giftColours) {
            msg.writeInt(giftColour);
        }

        msg.writeInt(giftDecorations.length);

        for (int giftDecoration : giftDecorations) {
            msg.writeInt(giftDecoration);
        }

        msg.writeInt(CatalogManager.getInstance().getGiftBoxesOld().size());

        for (int spriteId : CatalogManager.getInstance().getGiftBoxesOld()) {
            msg.writeInt(spriteId);
        }

        return msg;
    }
}
