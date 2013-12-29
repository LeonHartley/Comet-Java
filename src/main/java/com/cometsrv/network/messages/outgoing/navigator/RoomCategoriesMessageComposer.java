package com.cometsrv.network.messages.outgoing.navigator;

import com.cometsrv.game.navigator.types.Category;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;
import javolution.util.FastList;

public class RoomCategoriesMessageComposer {
    public static Composer compose(FastList<Category> categories, int rank) {
        Composer msg = new Composer(Composers.RoomCategoriesMessageComposer);

        msg.writeInt(categories.size());

        for(Category cat : categories) {
            msg.writeInt(cat.getId());
            msg.writeString(cat.getTitle());
            msg.writeBoolean(cat.getRank() <= rank);
        }

        return msg;
    }
}
