package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.Set;


public class EffectsInventoryMessageComposer extends MessageComposer {

    private final Set<Integer> effects;
    private final int currentEffect;

    public EffectsInventoryMessageComposer(Set<Integer> effects, int currentEffect) {
        this.effects = effects;
        this.currentEffect = currentEffect;
    }

    @Override
    public short getId() {
        return Composers.AvatarEffectsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.effects.size());

        for (int effect : this.effects) {
            msg.writeInt(effect);
            msg.writeInt(0);
            msg.writeInt(-1);//duration
            msg.writeInt(-1);
            msg.writeInt(currentEffect == effect ? 1 : -1);
            msg.writeBoolean(true);//perm
        }
    }
}
