package com.cometproject.server.composers.catalog.pets;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class ValidatePetNameMessageComposer extends MessageComposer {

    private final int errorCode;
    private final String data;

    public ValidatePetNameMessageComposer(final int errorCode, final String data) {
        this.errorCode = errorCode;
        this.data = data;
    }

    @Override
    public short getId() {
        return Composers.CheckPetNameMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(errorCode);
        msg.writeString(data);
    }
}
