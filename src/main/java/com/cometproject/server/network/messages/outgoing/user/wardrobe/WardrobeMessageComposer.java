package com.cometproject.server.network.messages.outgoing.user.wardrobe;

import com.cometproject.server.game.players.components.types.settings.WardrobeItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class WardrobeMessageComposer {
    public static Composer compose(List<WardrobeItem> wardrobe) {
        Composer msg = new Composer(Composers.LoadWardrobeMessageComposer);

        msg.writeInt(1);
        msg.writeInt(wardrobe.size());

        for (WardrobeItem item : wardrobe) {
            msg.writeInt(item.getSlot());
            msg.writeString(item.getFigure());
            msg.writeString(item.getGender().toUpperCase());
        }

        return msg;
    }
}
