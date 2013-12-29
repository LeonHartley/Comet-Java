package com.cometsrv.game.wired;

import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.wired.effects.SayMessageEffect;
import com.cometsrv.game.wired.triggers.EnterRoomTrigger;
import com.cometsrv.game.wired.triggers.OnSayTrigger;
import com.cometsrv.game.wired.types.TriggerType;
import com.cometsrv.game.wired.types.WiredCondition;
import com.cometsrv.game.wired.types.WiredEffect;
import com.cometsrv.game.wired.types.WiredTrigger;
import javolution.util.FastMap;

public class WiredManager {
    private FastMap<String, WiredTrigger> triggers;
    private FastMap<String, WiredCondition> conditions;
    private FastMap<String, WiredEffect> effects;

    public WiredManager() {
        this.triggers = new FastMap<>();
        this.conditions = new FastMap<>();
        this.effects = new FastMap<>();

        this.triggers.put("wf_trg_onsay", new OnSayTrigger());
        this.triggers.put("wf_trg_enterroom", new EnterRoomTrigger());

        this.effects.put("wf_act_saymsg", new SayMessageEffect());
    }

    public boolean isWiredTrigger(FloorItem item) {
        return (item.getDefinition().getInteraction().startsWith("wf_trg"));
    }

    public boolean isWiredEffect(FloorItem item) {
        return (item.getDefinition().getInteraction().startsWith("wf_act"));
    }

    public boolean isWiredCondition(FloorItem item) {
        return (item.getDefinition().getInteraction().startsWith("wf_cnd"));
    }

    public boolean isWiredItem(FloorItem item) {
        return (isWiredTrigger(item) || isWiredEffect(item) || isWiredCondition(item));
    }

    public String getString(TriggerType type) {
        if(type == TriggerType.ON_SAY) {
            return "wf_trg_onsay";
        } else if(type == TriggerType.ENTER_ROOM) {
            return "wf_trg_enterroom";
        }

        return "wf_trg_unknown";
    }

    public WiredTrigger getTrigger(String interaction) {
        return this.triggers.get(interaction);
    }

    public WiredCondition getCondition(String interaction) {
        return this.conditions.get(interaction);
    }

    public WiredEffect getEffect(String interaction) {
        return this.effects.get(interaction);
    }
}
