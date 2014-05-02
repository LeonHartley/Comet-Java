package com.cometproject.server.network.messages.outgoing.catalog.groups;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class GroupElementsMessageComposer {
    private static Composer comp;

    public static Composer compose() {
        if (comp != null)
            return comp;

        Composer msg = new Composer(Composers.GroupElementsMessageComposer);

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);

        /*
        GroupManager g = GameEngine.getGroups();

        msg.writeInt(g.getBases().size());

        for (GroupBase base : g.getBases()) {
            msg.writeInt(base.id);
            msg.writeString(base.valueA);
            msg.writeString(base.valueB);
        }

        msg.writeInt(g.getSymbols().size());

        for (GroupSymbol symbol : g.getSymbols()) {
            msg.writeInt(symbol.id);
            msg.writeString(symbol.valueA);
            msg.writeString(symbol.valueB);
        }

        msg.writeInt(g.getBaseColours().size());

        for (GroupBaseColour colour : g.getBaseColours()) {
            msg.writeInt(colour.id);
            msg.writeString(colour.colour);
        }

        msg.writeInt(g.getSymbolColours().size());

        for (GroupSymbolColour colour : g.getSymbolColours().values()) {
            msg.writeInt(colour.id);
            msg.writeString(colour.colour);
        }

        msg.writeInt(g.getBackgroundColours().size());

        for (GroupBackgroundColour colour : g.getBackgroundColours().values()) {
            msg.writeInt(colour.id);
            msg.writeString(colour.colour);
        }
*/
        comp = msg;
        return msg;
    }
}
