package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogIndexMessageComposer extends MessageComposer {
    private final int playerRank;

    public CatalogIndexMessageComposer(final int playerRank) {
        this.playerRank = playerRank;
    }

    @Override
    public short getId() {
        return Composers.CatalogIndexMessageComposer;
    }

    @Override
    public void compose(final IComposer msg) {
        final List<CatalogPage> pages = CatalogManager.getInstance().getPagesForRank(this.playerRank);
        final List<CatalogPage> pagesTwo = CatalogManager.getInstance().getPagesForRank(this.playerRank);
        final List<CatalogPage> subPages = CatalogManager.getInstance().getPagesForRank(this.playerRank);

        Collections.sort(subPages, new Comparator<CatalogPage>() {
            @Override
            public int compare(final CatalogPage o1, final CatalogPage o2) {
                return o1.getCaption().compareTo(o2.getCaption());
            }
        });

        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(-1);
        msg.writeString("root");
        msg.writeString("");
        msg.writeInt(0);
        msg.writeInt(this.count(-1, pages));

        for (final CatalogPage page : pages.stream().filter(x -> x.getParentId() == -1).collect(Collectors.toList())) {
            if (page.getParentId() != -1) {
                continue;
            }

            msg.writeBoolean(true);
            msg.writeInt(page.getIcon());
            msg.writeInt(page.isEnabled() ? page.getId() : -1);
            msg.writeString(page.getLinkName().equals("undefined") ? page.getCaption().toLowerCase().replaceAll("[^A-Za-z0-9]", "").replace(" ", "_") : page.getLinkName());
            msg.writeString(page.getCaption());
            msg.writeInt(0);
            msg.writeInt(this.count(page.getId(), pages));

            for (final CatalogPage child : pagesTwo.stream().filter(x -> x.getParentId() == page.getId()).collect(Collectors.toList())) {
                if (child.getParentId() != page.getId()) {
                    continue;
                }

                msg.writeBoolean(true);
                msg.writeInt(child.getIcon());
                msg.writeInt(child.isEnabled() ? child.getId() : -1);
                msg.writeString(child.getLinkName().equals("undefined") ? child.getCaption().toLowerCase().replaceAll("[^A-Za-z0-9]", "").replace(" ", "_") : child.getLinkName());
                msg.writeString(child.getCaption());
                msg.writeInt(child.getOfferSize());

                for (CatalogItem item : child.getItems().values()) {
                    if(item.getItemId().equals("-1")) continue;

                    ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId());

                    if (itemDefinition != null) {
                        int offerId = itemDefinition.getOfferId();

                        if (offerId != -1) {
                            msg.writeInt(offerId);

                        }
                    }
                }
                
                msg.writeInt(this.count(child.getId(), pagesTwo));
                
                for (final CatalogPage childTwo : subPages.stream().filter(x -> x.getParentId() == child.getId()).collect(Collectors.toList())) {
                    if (childTwo.getParentId() != child.getId()) {
                        continue;
                    }
                    msg.writeBoolean(true);
                    msg.writeInt(childTwo.getIcon());
                    msg.writeInt(childTwo.isEnabled() ? childTwo.getId() : -1);
                    msg.writeString(childTwo.getLinkName().equals("undefined") ? childTwo.getCaption().toLowerCase().replaceAll("[^A-Za-z0-9]", "").replace(" ", "_") : childTwo.getLinkName());
                    msg.writeString(childTwo.getCaption());
                    msg.writeInt(childTwo.getOfferSize());

                    for (CatalogItem item : childTwo.getItems().values()) {
                        if(item.getItemId().equals("-1")) continue;

                        ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId());

                        if (itemDefinition != null && itemDefinition.getOfferId() != -1 && itemDefinition.getOfferId() != 0) {
                            msg.writeInt(itemDefinition.getOfferId());
                        }
                    }
                    msg.writeInt(0);
                }
            }
        }

        msg.writeBoolean(true);
        msg.writeString("NORMAL");
    }

    private int count(final int index, final List<CatalogPage> pages) {
        int i = 0;

        for (final CatalogPage page : pages) {
            if (page.getParentId() == index) {
                ++i;
            }

        }
        return i;
    }
}
