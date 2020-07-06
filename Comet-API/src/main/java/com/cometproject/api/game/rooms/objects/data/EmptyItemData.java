package com.cometproject.api.game.rooms.objects.data;

import com.cometproject.api.networking.messages.IComposer;

public class EmptyItemData extends ItemData  {
    @Override
    public void compose(IComposer composer) {
        composer.writeInt(4);
    }
}
