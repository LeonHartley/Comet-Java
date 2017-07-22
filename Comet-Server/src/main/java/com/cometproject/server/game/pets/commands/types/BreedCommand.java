package com.cometproject.server.game.pets.commands.types;

import com.cometproject.server.game.pets.commands.PetCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.entities.types.ai.pets.PetAI;

public class BreedCommand extends PetCommand {
    @Override
    public boolean execute(PlayerEntity executor, PetEntity entity) {
        final PetAI ai = (PetAI) entity.getAI();

        ai.beginBreeding();
        return true;
    }

    @Override
    public int getRequiredLevel() {
        return 0;
    }

    @Override
    public boolean requiresOwner() {
        return true;
    }
}
