package com.cometproject.api.game.rooms.objects.data;

import com.cometproject.api.networking.messages.IComposer;

public class LegacyItemData extends ItemData {
    private final String data;

    public LegacyItemData(String data) {
        this.data = data;
    }

    @Override
    public void compose(IComposer composer) {
        composer.writeInt(0);
        composer.writeString(data);
    }
}
