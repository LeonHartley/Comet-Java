package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;


public class UpdateAvatarAspectMessageComposer extends MessageComposer {
    private final String figure;
    private final String gender;

    public UpdateAvatarAspectMessageComposer(final String figure, final String gender) {
        this.figure = figure;
        this.gender = gender;
    }

    @Override
    public short getId() {
        return Composers.UpdateAvatarAspectMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(figure);
        msg.writeString(gender);
    }
}
