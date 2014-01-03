package com.cometsrv.game.rooms.items;

import com.cometsrv.game.items.interactions.InteractionQueueItem;

public interface InteractableRoomItem {
    public void queueInteraction(InteractionQueueItem interaction);

    public boolean hasInteraction();
    public InteractionQueueItem getNextInteraction();

    public boolean toggleInteract(boolean state);
}
