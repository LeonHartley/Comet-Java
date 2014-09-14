package com.cometproject.server.network.messages.outgoing.room.items.wired;

import com.cometproject.server.game.rooms.items.types.floor.wired.actions.WiredActionItem;
import com.cometproject.server.game.wired.WiredStaticConfig;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class WiredActionMessageComposer {
    public static Composer compose(WiredActionItem wiredAction) {

        Composer msg = new Composer(Composers.WiredActionMessageComposer);

        msg.writeBoolean(false); // advanced
        msg.writeInt(WiredStaticConfig.MAX_FURNI_SELECTION);

        msg.writeInt(wiredAction.getWiredData().getSelectedIds().size());

        for (Integer itemId : wiredAction.getWiredData().getSelectedIds()) {
            msg.writeInt(itemId);
        }

        msg.writeInt(wiredAction.getDefinition().getSpriteId());
        msg.writeInt(wiredAction.getId());

        msg.writeString(wiredAction.getWiredData().getText());

        msg.writeInt(wiredAction.getWiredData().getParams().length);

        for(int param : wiredAction.getWiredData().getParams()) {
            msg.writeInt(param);
        }

        msg.writeInt(wiredAction.getWiredData().getSelectionType());
        msg.writeInt(wiredAction.getInterface());
        msg.writeInt(wiredAction.getWiredData().getDelay());
        msg.writeInt(0); // conflicts
        msg.writeString(""); //no idea

        return msg;
    }
}
