package com.cometproject.game.rooms.items;

import com.cometproject.game.items.interactions.InteractionQueueItem;

public interface InteractableRoomItem {
    public void queueInteraction(InteractionQueueItem interaction);

    public boolean hasInteraction();
    public InteractionQueueItem getNextInteraction();

    public boolean toggleInteract(boolean state);
}
