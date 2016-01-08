package com.cometproject.server.network.messages.outgoing.catalog.groups;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.items.types.*;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class GroupElementsMessageComposer extends MessageComposer {
    public GroupElementsMessageComposer() {

    }

    @Override
    public short getId() {
        return Composers.BadgeEditorPartsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(GroupManager.getInstance().getGroupItems().getBases().size());

        for (GroupBase base : GroupManager.getInstance().getGroupItems().getBases()) {
            msg.writeInt(base.getId());
            msg.writeString(base.getValueA());
            msg.writeString(base.getValueB());
        }

        msg.writeInt(GroupManager.getInstance().getGroupItems().getSymbols().size());

        for (GroupSymbol symbol : GroupManager.getInstance().getGroupItems().getSymbols()) {
            msg.writeInt(symbol.getId());
            msg.writeString(symbol.getValueA());
            msg.writeString(symbol.getValueB());
        }

        msg.writeInt(GroupManager.getInstance().getGroupItems().getBaseColours().size());

        for (GroupBaseColour colour : GroupManager.getInstance().getGroupItems().getBaseColours()) {
            msg.writeInt(colour.getId());
            msg.writeString(colour.getColour());
        }

        msg.writeInt(GroupManager.getInstance().getGroupItems().getSymbolColours().size());

        for (GroupSymbolColour colour : GroupManager.getInstance().getGroupItems().getSymbolColours().values()) {
            msg.writeInt(colour.getId());
            msg.writeString(colour.getColour());
        }

        msg.writeInt(GroupManager.getInstance().getGroupItems().getBackgroundColours().size());

        for (GroupBackgroundColour colour : GroupManager.getInstance().getGroupItems().getBackgroundColours().values()) {
            msg.writeInt(colour.getId());
            msg.writeString(colour.getColour());
        }
    }
}
