package com.cometsrv.network.messages.outgoing.catalog.groups;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.groups.GroupManager;
import com.cometsrv.game.groups.types.items.*;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class GroupElementsMessageComposer {
    private static Composer comp;
    public static Composer compose() {
        if(comp != null)
            return comp;

        Composer msg = new Composer(Composers.GroupElementsMessageComposer);

        GroupManager g = GameEngine.getGroups();

        msg.writeInt(g.getBases().size());

        for(GroupBase base : g.getBases()) {
            msg.writeInt(base.id);
            msg.writeString(base.valueA);
            msg.writeString(base.valueB);
        }

        msg.writeInt(g.getSymbols().size());

        for(GroupSymbol symbol : g.getSymbols()) {
            msg.writeInt(symbol.id);
            msg.writeString(symbol.valueA);
            msg.writeString(symbol.valueB);
        }

        msg.writeInt(g.getBaseColours().size());

        for(GroupBaseColour colour : g.getBaseColours()) {
            msg.writeInt(colour.id);
            msg.writeString(colour.colour);
        }

        msg.writeInt(g.getSymbolColours().size());

        for(GroupSymbolColour colour : g.getSymbolColours().values()) {
            msg.writeInt(colour.id);
            msg.writeString(colour.colour);
        }

        msg.writeInt(g.getBackgroundColours().size());

        for(GroupBackgroundColour colour : g.getBackgroundColours().values()) {
            msg.writeInt(colour.id);
            msg.writeString(colour.colour);
        }

        comp = msg;
        return msg;
    }
}
