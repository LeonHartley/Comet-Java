package com.cometproject.server.composers.catalog.groups;

import com.cometproject.api.game.groups.IGroupItemService;
import com.cometproject.api.game.groups.items.IGroupBadgeItem;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class GroupElementsMessageComposer extends MessageComposer {
    private final IGroupItemService groupItemService;

    public GroupElementsMessageComposer(IGroupItemService groupItemService) {
        this.groupItemService = groupItemService;
    }

    @Override
    public short getId() {
        return Composers.BadgeEditorPartsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.groupItemService.getBases().size());

        for (IGroupBadgeItem base : this.groupItemService.getBases()) {
            msg.writeInt(base.getId());
            msg.writeString(base.getFirstValue());
            msg.writeString(base.getSecondValue());
        }

        msg.writeInt(this.groupItemService.getSymbols().size());

        for (IGroupBadgeItem symbol : this.groupItemService.getSymbols()) {
            msg.writeInt(symbol.getId());
            msg.writeString(symbol.getFirstValue());
            msg.writeString(symbol.getSecondValue());
        }

        msg.writeInt(this.groupItemService.getBaseColours().size());

        for (IGroupBadgeItem colour : this.groupItemService.getBaseColours()) {
            msg.writeInt(colour.getId());
            msg.writeString(colour.getFirstValue());
        }

        msg.writeInt(this.groupItemService.getSymbolColours().size());

        for (IGroupBadgeItem colour : this.groupItemService.getSymbolColours().values()) {
            msg.writeInt(colour.getId());
            msg.writeString(colour.getFirstValue());
        }

        msg.writeInt(this.groupItemService.getBackgroundColours().size());

        for (IGroupBadgeItem colour : this.groupItemService.getBackgroundColours().values()) {
            msg.writeInt(colour.getId());
            msg.writeString(colour.getFirstValue());
        }
    }
}
