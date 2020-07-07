package com.cometproject.api.game.rooms.objects.data;

import com.cometproject.api.networking.messages.IComposer;

public class CrackableItemData extends ItemData {
    private final String state;
    private final int hits;
    private final int target;

    public CrackableItemData(String state, int hits, int target) {
        this.state = state;
        this.hits = hits;
        this.target = target;
    }

    @Override
    public void compose(IComposer composer) {
        composer.writeInt(7);
        composer.writeString(state);
        composer.writeInt(hits);
        composer.writeInt(target);
    }
}
