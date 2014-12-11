package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class CataPageMessageComposer {
    public static Composer compose(CatalogPage page) {
        Composer msg = new Composer(Composers.CatalogPageMessageComposer);
        msg.writeInt(page.getId());

        msg.writeString("NORMAL"); // builders club or not

        msg.writeString(page.getTemplate());

        msg.writeInt(page.getImages().size());

        for (String image : page.getImages()) {
            msg.writeString(image);
        }

        msg.writeInt(page.getTexts().size());

        for (String text : page.getTexts()) {
            msg.writeString(text);
        }

        if (!page.getTemplate().equals("frontpage") && !page.getTemplate().equals("club_buy")) {
            msg.writeInt(page.getItems().size());

            for (CatalogItem item : page.getItems().values()) {
                item.compose(msg);
            }
        } else {
            msg.writeInt(0);
        }

        msg.writeInt(0);

        msg.writeBoolean(false);
        msg.writeBoolean(false);

        return msg;
    }
}
