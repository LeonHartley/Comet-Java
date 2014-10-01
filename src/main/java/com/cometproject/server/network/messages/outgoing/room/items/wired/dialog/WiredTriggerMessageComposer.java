package com.cometproject.server.network.messages.outgoing.room.items.wired.dialog;

import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class WiredTriggerMessageComposer {
    public static Composer compose(WiredTriggerItem wiredTrigger) {

        List<WiredActionItem> incompatibleActions = wiredTrigger.getIncompatibleActions();

        Composer msg = new Composer(Composers.WiredTriggerMessageComposer);

        msg.writeBoolean(false); // advanced
        msg.writeInt(WiredUtil.MAX_FURNI_SELECTION);

        msg.writeInt(wiredTrigger.getWiredData().getSelectedIds().size());

        for (Integer itemId : wiredTrigger.getWiredData().getSelectedIds()) {
            msg.writeInt(itemId);
        }

        msg.writeInt(wiredTrigger.getDefinition().getSpriteId());
        msg.writeInt(wiredTrigger.getId());

        msg.writeString(wiredTrigger.getWiredData().getText());

        msg.writeInt(wiredTrigger.getWiredData().getParams().size());

        for(int param : wiredTrigger.getWiredData().getParams().values()) {
            msg.writeInt(param);
        }

        msg.writeInt(wiredTrigger.getWiredData().getSelectionType());
        msg.writeInt(wiredTrigger.getInterface());

        msg.writeInt(incompatibleActions.size());

        for(WiredActionItem incompatibleAction : incompatibleActions) {
            msg.writeInt(incompatibleAction.getDefinition().getSpriteId());
        }

        msg.writeString(""); //no idea

        return msg;
    }
}
