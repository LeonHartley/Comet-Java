package com.cometproject.server.network.messages.outgoing.user.wardrobe;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.components.types.settings.WardrobeItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

import java.util.List;


public class WardrobeMessageComposer extends MessageComposer {
    private final List<WardrobeItem> wardrobe;

    public WardrobeMessageComposer(final List<WardrobeItem> wardrobe) {
        this.wardrobe = wardrobe;
    }

    @Override
    public short getId() {
        return Composers.LoadWardrobeMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(1);
        msg.writeInt(wardrobe.size());

        for (WardrobeItem item : wardrobe) {
            msg.writeInt(item.getSlot());
            msg.writeString(item.getFigure());

            if(item.getGender() != null) {
                msg.writeString(item.getGender().toUpperCase());
            } else {
                msg.writeString("M");
            }
        }
    }
}
