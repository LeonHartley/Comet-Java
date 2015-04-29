package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

import java.util.List;


public class RoomCategoriesMessageComposer extends MessageComposer {
    private final List<Category> categories;
    private final int rank;

    public RoomCategoriesMessageComposer(final List<Category> categories, final int rank) {
        this.categories = categories;
        this.rank = rank;
    }

    @Override
    public short getId() {
        return Composers.FlatCategoriesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(categories.size());

        for (Category cat : categories) {
            msg.writeInt(cat.getId());
            msg.writeString(cat.getTitle());
            msg.writeBoolean(cat.getRank() <= rank);
            msg.writeBoolean(false);
            msg.writeString("NONE");
            msg.writeString("");
            msg.writeBoolean(false);
        }
    }
}
