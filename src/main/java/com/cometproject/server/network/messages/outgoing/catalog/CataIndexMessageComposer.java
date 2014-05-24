package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import javolution.util.FastMap;

import java.util.List;

public class CataIndexMessageComposer {
    private static FastMap<Integer, Composer> cataIndexCache = new FastMap<>();

    public static Composer compose(int rank) {
        if (cataIndexCache.containsKey(rank)) {
            return cataIndexCache.get(rank).duplicate();
        }

        List<CatalogPage> pages = CometManager.getCatalog().getPagesForRank(rank);

        Composer msg = new Composer(Composers.CataIndexMessageComposer);

        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(-1);
        msg.writeString("root");
        msg.writeString("");
        msg.writeInt(0);

        msg.writeInt(count(-1, pages));
        for (CatalogPage page : pages) {
            if (page.getParentId() != -1) {
                continue;
            }

            msg.writeBoolean(true);
            //msg.writeInt(page.getColour());
            msg.writeInt(page.getIcon());
            msg.writeInt(page.getId());
            msg.writeString(page.getCaption().toLowerCase().replace(" ", "_"));
            msg.writeString(page.getCaption());
            msg.writeInt(0);

            msg.writeInt(count(page.getId(), pages));

            for (CatalogPage child : pages) {
                if (child.getParentId() != page.getId()) {
                    continue;
                }

                msg.writeBoolean(true);
                //msg.writeInt(child.getColour());
                msg.writeInt(child.getIcon());
                msg.writeInt(child.getId());
                msg.writeString(child.getCaption().toLowerCase().replace(" ", "_"));
                msg.writeString(child.getCaption());
                msg.writeInt(0);
                msg.writeInt(0); //??
            }
        }

        msg.writeBoolean(false);
        msg.writeString("NORMAL");

        cataIndexCache.add(rank, msg);
        return msg;
    }

    private static int count(int index, List<CatalogPage> pages) {
        int i = 0;

        for (CatalogPage page : pages) {
            if (page.getParentId() == index) {
                i++;
            }
        }

        return i;
    }

    public static void clearCataIndexCache() {
        cataIndexCache.clear();
    }
}
