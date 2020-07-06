package com.cometproject.api.game.rooms.objects.data;

import com.cometproject.api.networking.messages.IComposer;

public class IntArrayItemData extends ItemData {
    private final int[] ints;

    public IntArrayItemData(int[] ints) {
        this.ints = ints;
    }

    @Override
    public void compose(IComposer composer) {
        composer.writeInt(5);
        composer.writeInt(ints.length);

        for (int i : ints) {
            composer.writeInt(i);
        }
    }
}
