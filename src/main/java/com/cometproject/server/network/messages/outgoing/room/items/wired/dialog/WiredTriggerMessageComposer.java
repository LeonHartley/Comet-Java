package com.cometproject.server.network.messages.outgoing.room.items.wired.dialog;

import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class WiredTriggerMessageComposer extends MessageComposer {
    private final List<WiredActionItem> incompatibleActions;
    private final WiredTriggerItem wiredTrigger;

    public WiredTriggerMessageComposer(final WiredTriggerItem wiredTriggerItem) {
        this.wiredTrigger = wiredTriggerItem;
        this.incompatibleActions = wiredTriggerItem.getIncompatibleActions();
    }

    @Override
    public short getId() {
        return Composers.WiredTriggerMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
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

        for (int param : wiredTrigger.getWiredData().getParams().values()) {
            msg.writeInt(param);
        }

        msg.writeInt(wiredTrigger.getWiredData().getSelectionType());
        msg.writeInt(wiredTrigger.getInterface());

        msg.writeInt(incompatibleActions.size());

        for (WiredActionItem incompatibleAction : incompatibleActions) {
            msg.writeInt(incompatibleAction.getDefinition().getSpriteId());
        }

        msg.writeString(""); //no idea
    }

    @Override
    public void dispose() {
        this.incompatibleActions.clear();
    }
}
