package com.cometproject.server.network.messages.outgoing.room.pets;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class PetPackageCompleteMessageComposer extends MessageComposer {

    private final int floorItem;
    private final int resultCode;
    private final String name;

    public PetPackageCompleteMessageComposer(int floorItem, int resultCode, String name) {
        this.floorItem = floorItem;
        this.resultCode = resultCode;
        this.name = name;
    }

    @Override
    public short getId() {
        return Composers.PetPackageOpenedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.floorItem);
        msg.writeInt(this.resultCode);
        msg.writeString(this.name);
    }
}
