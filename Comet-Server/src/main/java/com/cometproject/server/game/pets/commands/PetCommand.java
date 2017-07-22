package com.cometproject.server.game.pets.commands;

import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;

public abstract class PetCommand {
    public abstract boolean execute(PlayerEntity executor, PetEntity entity);

    public abstract int getRequiredLevel();

    public abstract boolean requiresOwner();

    public int experienceGain() {
        return 0;
    }
}
