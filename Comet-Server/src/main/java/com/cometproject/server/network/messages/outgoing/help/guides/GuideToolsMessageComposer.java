package com.cometproject.server.network.messages.outgoing.help.guides;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.guides.GuideManager;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class GuideToolsMessageComposer extends MessageComposer {

    private final boolean onDuty;

    public GuideToolsMessageComposer(final boolean onDuty) {
        this.onDuty = onDuty;
    }

    @Override
    public short getId() {
        return Composers.GuideToolsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(this.onDuty);
        msg.writeInt(0);
        msg.writeInt(GuideManager.getInstance().getActiveGuideCount());//active guides
        msg.writeInt(GuideManager.getInstance().getActiveGuardianCount());//active guardians
    }
}
