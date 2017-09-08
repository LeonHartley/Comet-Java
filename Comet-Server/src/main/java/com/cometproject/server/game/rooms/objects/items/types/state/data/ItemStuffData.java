package com.cometproject.server.game.rooms.objects.items.types.state.data;

import com.cometproject.api.networking.messages.IComposer;

public interface ItemStuffData {
    int getType();

    void compose(IComposer msg);
}
