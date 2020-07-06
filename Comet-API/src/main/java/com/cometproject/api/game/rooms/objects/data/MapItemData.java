package com.cometproject.api.game.rooms.objects.data;

import com.cometproject.api.networking.messages.IComposer;

import java.util.HashMap;

public class MapItemData extends ItemData {
    private final HashMap<String, String> data;

    public MapItemData(HashMap<String, String> data) {
        this.data = data;
    }

    @Override
    public void compose(IComposer composer) {
        composer.writeInt(1);
        composer.writeInt(data.size());

        data.forEach((key, val) -> {
            composer.writeString(key);
            composer.writeString(val);
        });
    }
}
