package com.cometproject.server.network.messages.outgoing.catalog.groups;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.items.types.*;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class GroupElementsMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.GroupElementsMessageComposer);

        msg.writeInt(CometManager.getGroups().getGroupItems().getBases().size());

        for (GroupBase base : CometManager.getGroups().getGroupItems().getBases()) {
            msg.writeInt(base.id);
            msg.writeString(base.valueA);
            msg.writeString(base.valueB);
        }

        msg.writeInt(CometManager.getGroups().getGroupItems().getSymbols().size());

        for (GroupSymbol symbol : CometManager.getGroups().getGroupItems().getSymbols()) {
            msg.writeInt(symbol.id);
            msg.writeString(symbol.valueA);
            msg.writeString(symbol.valueB);
        }

        msg.writeInt(CometManager.getGroups().getGroupItems().getBaseColours().size());

        for (GroupBaseColour colour : CometManager.getGroups().getGroupItems().getBaseColours()) {
            msg.writeInt(colour.id);
            msg.writeString(colour.colour);
        }

        msg.writeInt(CometManager.getGroups().getGroupItems().getSymbolColours().size());

        for (GroupSymbolColour colour : CometManager.getGroups().getGroupItems().getSymbolColours().values()) {
            msg.writeInt(colour.id);
            msg.writeString(colour.colour);
        }

        msg.writeInt(CometManager.getGroups().getGroupItems().getBackgroundColours().size());

        for (GroupBackgroundColour colour : CometManager.getGroups().getGroupItems().getBackgroundColours().values()) {
            msg.writeInt(colour.id);
            msg.writeString(colour.colour);
        }

        return msg;
    }
}
