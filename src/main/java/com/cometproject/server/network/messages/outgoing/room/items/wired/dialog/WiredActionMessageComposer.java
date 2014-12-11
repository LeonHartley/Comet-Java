package com.cometproject.server.network.messages.outgoing.room.items.wired.dialog;

import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class WiredActionMessageComposer {
    public static Composer compose(WiredActionItem wiredAction) {

        List<WiredTriggerItem> incompatibleTriggers = wiredAction.getIncompatibleTriggers();

        Composer msg = new Composer(Composers.WiredEffectMessageComposer);

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

        for (int param : wiredAction.getWiredData().getParams().values()) {
            msg.writeInt(param);
        }

        msg.writeInt(wiredAction.getWiredData().getSelectionType());
        msg.writeInt(wiredAction.getInterface());
        msg.writeInt(wiredAction.getWiredData().getDelay());

        msg.writeInt(incompatibleTriggers.size());

        for (WiredTriggerItem incompatibleTrigger : incompatibleTriggers) {
            msg.writeInt(incompatibleTrigger.getDefinition().getSpriteId());
        }

        msg.writeString(""); //no idea

        return msg;
    }
}
