package com.cometproject.server.composers.help.guides;

import com.cometproject.api.game.moderation.guides.IHelpRequest;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GuideSessionAttachedMessageComposer extends MessageComposer {
    private final IHelpRequest helpRequest;

    private final boolean isGuide;

    public GuideSessionAttachedMessageComposer(final IHelpRequest helpRequest, final boolean isGuide) {
        this.helpRequest = helpRequest;
        this.isGuide = isGuide;
    }

    @Override
    public short getId() {
        return Composers.GuideSessionAttachedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(this.isGuide);
        msg.writeInt(1);//type
        msg.writeString(helpRequest.getMessage());
        msg.writeInt(60);//avg waiting time
    }
}
