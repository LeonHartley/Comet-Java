package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.game.catalog.types.ICatalogPage;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.game.catalog.ICatalogService;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.types.ItemDefinition;
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
        final List<ICatalogPage> pages = CatalogManager.getInstance().getPagesForRank(this.playerRank);
        final List<ICatalogPage> pagesTwo = CatalogManager.getInstance().getPagesForRank(this.playerRank);
        final List<ICatalogPage> subPages = CatalogManager.getInstance().getPagesForRank(this.playerRank);

        Collections.sort(subPages, Comparator.comparing(ICatalogPage::getCaption));

        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(-1);
        msg.writeString("root");
        msg.writeString("");
        msg.writeInt(0);
        msg.writeInt(this.count(-1, pages));

        for (final ICatalogPage page : pages.stream().filter(x -> x.getParentId() == -1).collect(Collectors.toList())) {
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

            for (final ICatalogPage child : pagesTwo.stream().filter(x -> x.getParentId() == page.getId()).collect(Collectors.toList())) {
                if (child.getParentId() != page.getId()) {
                    continue;
                }

                msg.writeBoolean(true);
                msg.writeInt(child.getIcon());
                msg.writeInt(child.isEnabled() ? child.getId() : -1);
                msg.writeString(child.getLinkName().equals("undefined") ? child.getCaption().toLowerCase().replaceAll("[^A-Za-z0-9]", "").replace(" ", "_") : child.getLinkName());
                msg.writeString(child.getCaption());
                msg.writeInt(child.getOfferSize());

                for (ICatalogItem item : child.getItems().values()) {
                    if (item.getItemId().equals("-1")) continue;

                    ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId());

                    if (itemDefinition != null) {
                        int offerId = itemDefinition.getOfferId();

                        if (offerId != -1) {
                            msg.writeInt(offerId);

                        }
                    }
                }

                msg.writeInt(this.count(child.getId(), pagesTwo));

                for (final ICatalogPage childTwo : subPages.stream().filter(x -> x.getParentId() == child.getId()).collect(Collectors.toList())) {
                    if (childTwo.getParentId() != child.getId()) continue;

                    msg.writeBoolean(true);
                    msg.writeInt(childTwo.getIcon());
                    msg.writeInt(childTwo.isEnabled() ? childTwo.getId() : -1);
                    msg.writeString(childTwo.getLinkName().equals("undefined") ? childTwo.getCaption().toLowerCase().replaceAll("[^A-Za-z0-9]", "").replace(" ", "_") : childTwo.getLinkName());
                    msg.writeString(childTwo.getCaption());
                    msg.writeInt(childTwo.getOfferSize());

                    for (ICatalogItem item : childTwo.getItems().values()) {
                        if (item.getItemId().equals("-1")) continue;

                        ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId());

                        if (itemDefinition != null && itemDefinition.getOfferId() != -1 && itemDefinition.getOfferId() != 0) {
                            msg.writeInt(itemDefinition.getOfferId());
                        }
                    }

                    msg.writeInt(0);
                }
            }
        }

        msg.writeBoolean(false);
        msg.writeString("NORMAL");
    }

    private int count(final int index, final List<ICatalogPage> pages) {
        int i = 0;

        for (final ICatalogPage page : pages) {
            if (page.getParentId() == index) {
                ++i;
            }

        }
        return i;
    }
}
