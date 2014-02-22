package com.cometproject.network.messages.outgoing.catalog;

import com.cometproject.game.GameEngine;
import com.cometproject.game.catalog.types.CatalogPage;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

import java.util.List;

public class CataIndexMessageComposer {
    public static Composer compose(int rank) {
        List<CatalogPage> pages = GameEngine.getCatalog().getPagesForRank(rank);

        Composer msg = new Composer(Composers.CataIndexMessageComposer);

        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(-1);
        msg.writeString("root");
        msg.writeString("");

        msg.writeInt(count(-1, pages));
        for(CatalogPage page : pages) {
            if(page.getParentId() != -1) {
                continue;
            }

            msg.writeBoolean(true);
            msg.writeInt(page.getColour());
            msg.writeInt(page.getIcon());
            msg.writeInt(page.getId());
            msg.writeString(page.getCaption().toLowerCase().replace(" ", "_"));
            msg.writeString(page.getCaption());

            msg.writeInt(count(page.getId(), pages));

            for(CatalogPage child : pages) {
                if(child.getParentId() != page.getId()) {
                    continue;
                }

                msg.writeBoolean(true);
                msg.writeInt(child.getColour());
                msg.writeInt(child.getIcon());
                msg.writeInt(child.getId());
                msg.writeString(child.getCaption().toLowerCase().replace(" ", "_"));
                msg.writeString(child.getCaption());
                msg.writeInt(0);
            }
        }

        msg.writeBoolean(true);

        return msg;
    }

    private static int count(int index, List<CatalogPage> pages) {
        int i = 0;

        for(CatalogPage page : pages) {
            if(page.getParentId() == index) {
                i++;
            }
        }

        return i;
    }
}
