package com.cometproject.api.game.rooms.objects.data;

import com.cometproject.api.networking.messages.IComposer;

public class StringArrayItemData extends ItemData {
    private final String[] strings;

    public StringArrayItemData(String[] strings) {
        this.strings = strings;
    }

    @Override
    public void compose(IComposer composer) {
        composer.writeInt(2);
        composer.writeInt(strings.length);

        for (String s : strings) {
            composer.writeString(s);
        }
    }
}
