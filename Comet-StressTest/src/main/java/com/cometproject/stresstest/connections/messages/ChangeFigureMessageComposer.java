package com.cometproject.stresstest.connections.messages;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Events;

public class ChangeFigureMessageComposer extends MessageComposer {
    private String figure;

    public ChangeFigureMessageComposer(String figure) {
        this.figure = figure;
    }

    @Override
    public short getId() {
        return Events.UpdateFigureDataMessageEvent;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString("m");
        msg.writeString(this.figure);
    }
}
