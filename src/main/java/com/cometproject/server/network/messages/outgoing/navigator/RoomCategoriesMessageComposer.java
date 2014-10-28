package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class RoomCategoriesMessageComposer {
    public static Composer compose(List<Category> categories, int rank) {
        Composer msg = new Composer(Composers.FlatCategoriesMessageComposer);

        msg.writeInt(categories.size());

        for (Category cat : categories) {
            msg.writeInt(cat.getId());
            msg.writeString(cat.getTitle());
            msg.writeBoolean(cat.getRank() <= rank);
        }

        return msg;
    }
}
