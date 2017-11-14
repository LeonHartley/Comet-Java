package com.cometproject.server.composers.camera;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class PhotoPreviewMessageComposer extends MessageComposer {

    private final String fileName;

    public PhotoPreviewMessageComposer(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    public short getId() {
        return Composers.PhotoPreviewMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.fileName);
    }
}
