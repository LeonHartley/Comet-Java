package com.cometproject.server.network.messages.outgoing.room.items.wired.dialog;

import com.cometproject.server.game.rooms.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class WiredConditionMessageComposer {
    public static Composer compose(WiredConditionItem wiredAction) {

        Composer msg = new Composer(Composers.WiredConditionMessageComposer);

        msg.writeBoolean(false); // advanced
        msg.writeInt(WiredUtil.MAX_FURNI_SELECTION);

        msg.writeInt(wiredAction.getWiredData().getSelectedIds().size());

        for (Integer itemId : wiredAction.getWiredData().getSelectedIds()) {
            msg.writeInt(itemId);
        }

        msg.writeInt(wiredAction.getDefinition().getSpriteId());
        msg.writeInt(wiredAction.getId());

        msg.writeString(wiredAction.getWiredData().getText());

        msg.writeInt(wiredAction.getWiredData().getParams().size());

        for(int param : wiredAction.getWiredData().getParams().values()) {
            msg.writeInt(param);
        }

        msg.writeInt(wiredAction.getWiredData().getSelectionType());
        msg.writeInt(wiredAction.getInterface());
        msg.writeInt(0); // conflicts

        return msg;
    }
}
